/**
 * 
 */
package de.decidr.prototype;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * @author RR
 * 
 */
@WebService(serviceName = "eMailWS", name = "eMailWSPT", targetNamespace = "http://decidr.org/mailws", wsdlLocation="mailws.wsdl")
@SOAPBinding(style = Style.RPC)
public class JavaMailService {
	/**
	 * @param subject
	 * @param body
	 * @param tos
	 * @param from
	 * @return
	 */
	@WebMethod(operationName = "sendEmail")
	public boolean sendEmail(@WebParam(name = "subject") String subject,
			@WebParam(name = "message") String body,
			@WebParam(name = "recipient") String tos,
			@WebParam(name = "sender") String from) {
		try {
			JavaMailBackend mail = new JavaMailBackend(tos, from, subject);
			mail.setBodyText(body);
			mail.setHostname("mx2.informatik.uni-stuttgart.de");
			mail.sendMessage();
		} catch (Exception e) {
			// TODO Log failure
			// e.printStackTrace();
			return false;
		}
		// TODO Log success
		return true;
	}
}
