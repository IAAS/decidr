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

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceBInterface")
@WebServiceClient(name = "DummyServiceA", targetNamespace = "http://decidr.de/test/encryption", wsdlLocation = "http://127.0.0.1:8080/encryptionTest/services/DummyServiceAService?wsdl")
public class DummyServiceB extends Service implements DummyServiceBInterface {

	public DummyServiceB(URL wsdlDocumentLocation, QName serviceName) {
		super(wsdlDocumentLocation, serviceName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.test.encryption.DummyServiceBInterface#printNcallB(java.lang
	 * .String)
	 */
	@Oneway
	public void printNcallB(@WebParam(name = "message") String msg,
			@WebParam(name = "counter") int counter) {
		System.out.println("B Called with:\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			wait(30000);
		} catch (InterruptedException e) {
		}
		getDummyServiceAInterfacePort().printNcallA(msg, counter - 1);
	}

	@WebEndpoint(name = "DummyServiceAInterfacePort")
	public DummyServiceAInterface getDummyServiceAInterfacePort() {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceAInterfacePort"), DummyServiceAInterface.class);
	}

	@WebEndpoint(name = "DummyServiceAInterfacePort")
	public DummyServiceAInterface getDummyServiceAInterfacePort(
			WebServiceFeature... features) {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceAInterfacePort"), DummyServiceAInterface.class,
				features);
	}
}
