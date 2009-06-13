/*
 * The DecidR Development Team licenses this file to you under
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
package de.decidr.webservices.email;

import org.apache.log4j.Logger;

import de.decidr.model.exceptions.TransactionException;
import de.decidr.model.logging.DefaultLogger;
import de.decidr.model.soap.exceptions.IoExceptionWrapper;
import de.decidr.model.soap.exceptions.MalformedURLExceptionWrapper;
import de.decidr.model.soap.exceptions.MessagingExceptionWrapper;
import de.decidr.model.soap.types.AbstractUserList;
import de.decidr.model.soap.types.IDList;
import de.decidr.model.soap.types.StringMap;

/**
 * This class was generated by Apache CXF 2.1.3 Fri Jun 12 16:46:57 CEST 2009
 * Generated source version: 2.1.3
 */
@javax.jws.WebService(serviceName = "Email", portName = "EmailSOAP", targetNamespace = "http://decidr.de/webservices/Email", wsdlLocation = "file:Email.wsdl", endpointInterface = "de.decidr.webservices.email.EmailPT")
public class EmailService implements EmailInterface {

    private static final Logger log = DefaultLogger
            .getLogger(EmailService.class);

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.webservices.email.EmailPT#sendEmail(de.decidr.webservices.email
     * .TAbstractUserList to ,)de.decidr.webservices.email.TAbstractUserList cc
     * ,)de.decidr.webservices.email.TAbstractUserList bcc ,)java.lang.String
     * fromName ,)java.lang.String fromAddress ,)java.lang.String subject
     * ,)de.decidr.webservices.email.TStringMap headers ,)java.lang.String
     * bodyTXT ,)java.lang.String bodyHTML ,)de.decidr.webservices.email.TIDList
     * attachments )*
     */
    public void sendEmail(AbstractUserList to, AbstractUserList cc,
            AbstractUserList bcc, String fromName, String fromAddress,
            String subject, StringMap headers, String bodyTXT, String bodyHTML,
            IDList attachments) throws MessagingExceptionWrapper,
            MalformedURLExceptionWrapper, TransactionException,
            IoExceptionWrapper {
        log.trace("Executing operation sendEmail");
        System.out.println(to);
        System.out.println(cc);
        System.out.println(bcc);
        System.out.println(fromName);
        System.out.println(fromAddress);
        System.out.println(subject);
        System.out.println(headers);
        System.out.println(bodyTXT);
        System.out.println(bodyHTML);
        System.out.println(attachments);
        try {
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
        // throw new MessagingException("messagingException...");
        // throw new MalformedURLException("malformedURLException...");
        // throw new TransactionException("transactionException...");
        // throw new IoException("ioException...");
    }
}
