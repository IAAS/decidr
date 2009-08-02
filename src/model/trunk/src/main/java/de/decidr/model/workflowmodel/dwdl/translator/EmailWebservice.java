package de.decidr.model.workflowmodel.dwdl.translator;

/**
 * @author Modood Alvi
 * @version 0.1
 */
public class EmailWebservice extends DecidrWebservice{
    
    public final String NAME = "Email";
    public final String NAMESPACE = "http://decidr.de/webservices/Email";
    public final String LOCATION = "Email.wsdl";
    public final String PARTNERLINK = "EmailPL";
    public final String PARTNERLINKTYPE = "EmailPLT";
    public final String REQUEST_MESSAGE = "sendEmailRequest";
    public final String RESPONSE_MESSAGE = "sendEmailResponse";
    public final String OPERATION = "sendEmail";

}
