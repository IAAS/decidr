package de.decidr.test.encryption;

import java.net.MalformedURLException;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceAInterface", name = "DummyServiceAPT", targetNamespace = "http://decidr.de/test/encryption", serviceName = "DummyServiceA", portName = "DummyServiceAPort")
public class DummyServiceA implements DummyServiceAInterface {
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
		System.out.println("A Called with:\ncounter: " + counter
				+ "\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			Thread.sleep(30000);
		} catch (Exception e) {
		}
		try {
			new DummyClientB().getDummyServiceBInterfacePort().printNcallB(msg,
					counter - 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.decidr.test.encryption.DummyServiceAInterface#inject(java.lang.String)
	 */
	@Oneway
	public void inject(@WebParam(name = "counter") int counter) {
		try {
			new DummyClientB().getDummyServiceBInterfacePort().printNcallB(
					"testmsg", counter);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
