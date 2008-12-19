/**
 * 
 */
package de.decidr.prototype;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

/**
 * @author RR
 * 
 */
//, wsdlLocation = "mailws.wsdl"
@WebService(serviceName = "eMailWS", name = "eMailWSPT", targetNamespace = "http://decidr.org/mailws")
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
	public boolean sendEmail(@WebParam(name = "subject", mode=Mode.IN) String subject,
			@WebParam(name = "message", mode=Mode.IN) String body,
			@WebParam(name = "recipient", mode=Mode.IN) String tos,
			@WebParam(name = "sender", mode=Mode.IN) String from) {
		try {
			JavaMailBackend mail = new JavaMailBackend(tos, from, subject);
			mail.setBodyText(body);
			
			mail.setHostname("smtp.googlemail.com");
			mail.useTLS(true);
			mail.setAuthInfo("decidr.iaas@googlemail.com", "DecidR0809");
			
			mail.sendMessage();
		} catch (Exception e) {
			// TODO Log failure
			e.printStackTrace();
			return false;
		}
		// TODO Log success
		return true;
	}
}
