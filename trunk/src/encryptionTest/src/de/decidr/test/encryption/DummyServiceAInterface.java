package de.decidr.test.encryption;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "DummyServiceAPT", targetNamespace = "http://decidr.de/test/encryption", serviceName="DummyServiceA", portName="DummyServiceAPort")
public interface DummyServiceAInterface {

	@WebMethod
	@Oneway
	public abstract void printNcallA(@WebParam(name = "message") String msg,
			@WebParam(name = "counter") int counter);

	@WebMethod
	@Oneway
	public abstract void inject();
}