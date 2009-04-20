package de.decidr.test.encryption;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "DummyClientA", targetNamespace = "http://decidr.de/test/encryption", wsdlLocation = "http://127.0.0.1:8081/test/encryption/DummyAProxy?wsdl")
public class DummyClientA extends Service {

	public DummyClientA(URL wsdlDocumentLocation, QName serviceName) {
		super(wsdlDocumentLocation, serviceName);
	}

	public DummyClientA() throws MalformedURLException {
		this(new URL("http://127.0.0.1:8081/test/encryption/DummyAProxy?wsdl"),
				new QName("http://decidr.de/test/encryption", "DummyAProxy"));
	}

	@WebEndpoint(name = "DummyServiceAHttpSoap12Endpoint")
	public DummyServiceAInterface getDummyServiceAInterfacePort() {
		// try {
		// new ConfigurationContext
		// ServiceClient client = new ServiceClient();
		// client.engageModule("rampart");
		// OperationClient oClient =
		// client.createClient(ServiceClient.ANON_OUT_ONLY_OP);
		// } catch (AxisFault e) {
		// e.printStackTrace();
		// }
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyAProxyHttpSoap12Endpoint"), DummyServiceAInterface.class);
	}

	@WebEndpoint(name = "DummyServiceAHttpSoap12Endpoint")
	public DummyServiceAInterface getDummyServiceAInterfacePort(
			WebServiceFeature... features) {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyAProxyHttpSoap12Endpoint"), DummyServiceAInterface.class,
				features);
	}
}
