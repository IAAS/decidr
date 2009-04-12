package de.decidr.test.encryption;

import java.net.MalformedURLException;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceAInterface")
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
		System.out.println("A Called with:\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			wait(30000);
		} catch (InterruptedException e) {
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
	public void inject() {
		try {
			new DummyClientB().getDummyServiceBInterfacePort().printNcallB(
					"testmsg", 10);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
