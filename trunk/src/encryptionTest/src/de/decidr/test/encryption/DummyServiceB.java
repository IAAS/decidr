package de.decidr.test.encryption;

import java.net.MalformedURLException;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceBInterface", name = "DummyServiceBPT", targetNamespace = "http://decidr.de/test/encryption", serviceName = "DummyServiceB", portName = "DummyServiceBPort")
public class DummyServiceB implements DummyServiceBInterface {

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
		System.out.println("B Called with:\ncounter: " + counter
				+ "\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			Thread.sleep(30000);
		} catch (Exception e) {
		}
		try {
			new DummyClientA().getDummyServiceAInterfacePort().printNcallA(msg,
					counter - 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
