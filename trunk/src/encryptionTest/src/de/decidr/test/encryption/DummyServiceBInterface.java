package de.decidr.test.encryption;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

@WebService(name = "DummyServiceBPT", targetNamespace="http://decidr.de/test/encryption", serviceName="DummyServiceB", portName="DummyServiceBPort")
public interface DummyServiceBInterface {

	@WebMethod
	@Oneway
	public abstract void printNcallB(@WebParam(name = "message") String msg, @WebParam(name = "counter") int counter);

}