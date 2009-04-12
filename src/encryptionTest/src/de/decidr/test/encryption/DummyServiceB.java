package de.decidr.test.encryption;

import java.net.MalformedURLException;

import javax.jws.Oneway;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(endpointInterface = "de.decidr.test.encryption.DummyServiceBInterface")
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
		System.out.println("B Called with:\n<start>\n" + msg + "\n<end>");
		if (counter < 1)
			return;
		try {
			wait(30000);
		} catch (InterruptedException e) {
		}
		try {
			new DummyClientA().getDummyServiceAInterfacePort().printNcallA(msg,
					counter - 1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
}
