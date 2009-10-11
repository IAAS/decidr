/*
 * The Decidr Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package de.decidr.prototype;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.WebParam.Mode;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * This class provides the web service wrapper to
 * <code>{@link MailBackend}</code>.
 * 
 * @author RR
 */
@WebService(serviceName = "eMailWS", name = "eMailWSPT", targetNamespace = "http://decidr.org/mailws", wsdlLocation = "mailws.wsdl")
@SOAPBinding(style = Style.RPC)
public class MailService {

    private static Logger log = Logger.getLogger(MailService.class);
    static {
        log.removeAllAppenders();
        BasicConfigurator.configure();
    }

    /**
     * Provides the means to send an Email.
     * 
     * @param subject
     *            The subject the Email should have.
     * @param body
     *            The Email's main content.
     * @param tos
     *            The recipients of the Email.
     * @param from
     *            The sender of the Email. May be overwritten by the mail
     *            transport agent.
     * @return <code>true</code>, if the Email was successfully received by the
     *         MTA, otherwise <code>false</code>.
     */
    @WebMethod(operationName = "sendEmail")
    public boolean sendEmail(
            @WebParam(name = "subject", mode = Mode.IN) String subject,
            @WebParam(name = "message", mode = Mode.IN) String body,
            @WebParam(name = "recipient", mode = Mode.IN) String tos,
            @WebParam(name = "sender", mode = Mode.IN) String from) {

        log.setLevel(Level.DEBUG);
        log.error("Parameters:\n\tTo: " + tos + "\n\tFrom: " + from
                + "\n\tSubject: " + subject);

        try {
            // construct the mail
            log.error("construction mail...");
            MailBackend mail = new MailBackend(tos,
                    "decidr.iaas@googlemail.com", subject);
            log.error("setting body...");
            mail.setBodyText(body);
            log.error("setting hostname...");
            mail.setHostname("smtp.googlemail.com");
            log.error("tell mail to use ssl-based transport...");
            mail.useTLS(true);
            log.error("setting authentification info...");
            mail.setAuthInfo("decidr.iaas@googlemail.com", "DecidR0809");
            mail.setXMailer("DecidR Prototype");

            // send the message
            log.error("sending message...");
            mail.sendMessage();
        } catch (Exception e) {
            log.error("Error occurred while sending message!", e);
            return false;
        }
        log.error("Successfully sent!");
        return true;
    }
}
