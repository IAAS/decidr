

# Introduction #

The purpose of this wiki page is to collect useful information about the Apache Synapse ESB in the context of the DecidR project. Instead of repeating what is already mentioned in the seminar composition, this page deals with solutions to real-world problems that have occurred during the project.

## Assumptions ##
  * You have read the seminar composition which can be found here: http://code.google.com/p/decidr/source/browse/trunk/seminars/dh/texlipse/seminar.pdf
  * You have a basic understanding of the ESB concept and web services.
  * You have a basic understanding of the Apache Synapse configuration language.

# Deployment on the Amazon EC2 #

_TODO_

# Changing the WSDL port address location in a proxied web service #
A common task is to import WSDL from an existing web service into Synapse and create a gateway proxy for that web service. In this case, Synapse will modify the port address of the proxied web service to point to the machine that Synapse is running on.

For example, we create a simple proxy for our e-mail web service:
```
 <proxy name="MailProxy" transports="http,https">
        <target>
            <endpoint>
                <!-- This is where the web service is actually running -->
                <address uri="http://far-away-host:9000/soap/MailService"/>
            </endpoint>
            <outSequence>
                <send/>
            </outSequence>
        </target>
        <publishWSDL uri="http://far-away-host:9000/soap/MailService?wsdl"/>
    </proxy>
```

Assuming that Synapse is running on the machine with the hostname "iaassrv7", the resulting WSDL as propagated by Synapse will look like this:

```
...

<wsdl:service name="MailProxy">

<wsdl:port name="MailProxyHttpSoap11Endpoint" binding="ns:MailProxySoap11Binding">
<soap:address location="http://iaassrv7:8280/soap/MailProxy.MailProxyHttpSoap11Endpoint"/>
</wsdl:port>

...
```

As you can see, whatever address location was declared by the original WSDL has been replaced with the hostname of the machine that Synapse is running on. This is necessary if messages sent to the proxied web service need to be processed (e.g. logged) by Synapse before they reach their ultimate receiver.
However, what if _http://iaassrv7:8280_ is not the address that the client can use or wishes to use in order to communicate with the ESB? The automatically determined address may not even be reachable from any other computer since "localhost" is used in case Synapse cannot determine a hostname by asking the OS. So what if we want the address location to point to _http://iaassrv7.iaas.informatik.uni-stuttgart.de:8280_?

The solution is **not**, as indicated by the Synapse documentation, to change the SynapseHostName system property. To reach our goal, we have to modify _axis2.xml_. In the http/s transport receiver sections, uncomment the _WSDLEPRPrefix_ parameter and insert your correct custom URL.

```
    <!-- ================================================= -->
    <!-- Transport Ins -->
    <!-- ================================================= -->
    <!-- the non blocking http transport based on HttpCore + NIO extensions -->
    <transportReceiver name="http" class="org.apache.synapse.transport.nhttp.HttpCoreNIOListener">
    	<parameter name="port">8280</parameter>
    	<parameter name="non-blocking">true</parameter>
        <!--parameter name="bind-address" locked="false">hostname or IP address</parameter-->
        <parameter name="WSDLEPRPrefix" locked="false">http://iaassrv7.iaas.informatik.uni-stuttgart.de:8280</parameter>
    </transportReceiver>
```

That's it. After restarting Synapse you can verify your changes in the WSDL of your service proxies.

# Getting XSD imports to work #
I was writing a more detailed article including a description of the problem, but my browser ate it. So instead here's a concise description of what you need to do if you want to import schema definitions in your WSDL file:

  1. If you're using the Synapse version that comes with the decidr install package, you're done, but you may want to read on to see what had to be changed in order to get Synapse to work.
  1. Get the XML Schema Library version 1.4.3 and use it to replace the library that comes bundled with Synapse. There is a bug in 1.4.2 that throws nasty `XmlSchemaException`s.
  1. Do NOT use the Axis-generated WSDL URL in your service proxy definitions. Doing so will cause Synapse to look for a **local** file named "MyService?xsd=someSchema":

```
Bad example:
<proxy>
 ...
<publishWSDL uri="http://localhost:8080/axis2/services/MyService?wsdl"/>
</proxy>
```

> Instead, copy all your required WSDLs and XSDs to the Synapse repository and create "local registry entries" that point to the copied files. Make sure that the "location" attribute of the "resource" node is equal to the schemaLocation that is given in the WSDL file:

```
Good example:
    <localEntry key="wsdl-email" src="file:repository/wsdl/Email.wsdl"/>
    <localEntry key="xsd-decidrwstypes" src="file:repository/wsdl/DecidrWSTypes.xsd"/>
    <localEntry key="xsd-decidrtypes" src="file:repository/wsdl/DecidrTypes.xsd"/>

    <proxy name="EmailProxy" transports="http,https" faultSequence="defaultFaultHandler">
        <target>
            <endpoint>
                <address uri="http://localhost:8080/axis2/services/Email.EmailSOAP11"/>
            </endpoint>
            <outSequence>
                <log level="full"/>  
                <send/>
            </outSequence>
        </target>
        <publishWSDL key="wsdl-email">
            <resource location="DecidrWSTypes.xsd" key="xsd-decidrwstypes"/>
            <resource location="DecidrTypes.xsd" key="xsd-decidrtypes"/>
        </publishWSDL>
    </proxy>

```

The corresponding WSDL containing the import:
```
...
			<xsd:import schemaLocation="DecidrWSTypes.xsd"
				namespace="http://decidr.de/schema/DecidrWSTypes" />
			<xsd:import schemaLocation="DecidrTypes.xsd"
				namespace="http://decidr.de/schema/DecidrTypes" />
...

```

If the gods are mercifuly, Synapse should now start up without throwing exceptions in your face.