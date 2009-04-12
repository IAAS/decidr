package de.decidr.test.encryption;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "DummyClientB", targetNamespace = "http://decidr.de/test/encryption", wsdlLocation = "http://127.0.0.1:8080/axis2/services/DummyServiceB?wsdl")
public class DummyClientB extends Service {

	public DummyClientB(URL wsdlDocumentLocation, QName serviceName) {
		super(wsdlDocumentLocation, serviceName);
	}

	public DummyClientB() throws MalformedURLException {
		super(
				new URL(
						"http://127.0.0.1:8080/axis2/services/DummyServiceB?wsdl"),
				new QName("http://decidr.de/test/encryption",
						"DummyServiceB"));
	}

	@WebEndpoint(name = "DummyServiceBHttpSoap12Endpoint")
	public DummyServiceBInterface getDummyServiceBInterfacePort() {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceBHttpSoap12Endpoint"), DummyServiceBInterface.class);
	}

	@WebEndpoint(name = "DummyServiceBHttpSoap12Endpoint")
	public DummyServiceBInterface getDummyServiceBInterfacePort(
			WebServiceFeature... features) {
		return super.getPort(new QName("http://decidr.de/test/encryption",
				"DummyServiceBHttpSoap12Endpoint"), DummyServiceBInterface.class, features);
	}
}
