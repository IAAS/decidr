package de.decidr.test.encryption;

import java.net.URL;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceAInterface")
@WebServiceClient(name = "DummyServiceB", targetNamespace = "http://decidr.de/test/encryption", wsdlLocation = "http://127.0.0.1:8080/encryptionTest/services/DummyServiceBService?wsdl")
public class DummyServiceA extends Service implements DummyServiceAInterface {

	public DummyServiceA(URL wsdlDocumentLocation, QName serviceName) {
		super(wsdlDocumentLocation, serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.test.encryption.DummyServiceAInterface#printNcallA(java.lang
	 * .String)
	 */
	@Oneway
	public void printNcallA(@WebParam(name = "message") String msg,
			@WebParam(name = "counter") int counter) {
		System.out.println("A Called with:\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			wait(30000);
		} catch (InterruptedException e) {
		}
		getDummyServiceBInterfacePort().printNcallB(msg, counter - 1);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.test.encryption.DummyServiceAInterface#inject(java.lang.String)
	 */
	@Oneway
	public void inject() {
		getDummyServiceBInterfacePort().printNcallB("testmsg", 10);
	}

	@WebEndpoint(name = "DummyServiceBInterfacePort")
	public DummyServiceBInterface getDummyServiceBInterfacePort() {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceBInterfacePort"), DummyServiceBInterface.class);
	}

	@WebEndpoint(name = "DummyServiceBInterfacePort")
	public DummyServiceBInterface getDummyServiceBInterfacePort(
			WebServiceFeature... features) {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceBInterfacePort"), DummyServiceBInterface.class,
				features);
	}
}
