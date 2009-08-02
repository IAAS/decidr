package de.decidr.model.workflowmodel.dwdl.translator;


/**
 * @author Modood Alvi
 * @version 0.1
 */
public class HumanTaskWebservice extends DecidrWebservice {
    
    public final String NAME = "HumanTask";
    public final String NAMESPACE = "http://decidr.de/webservices/HumanTask";
    public final String LOCATION = "HumanTask.wsdl";
    public final String PARTNERLINK = "HumanTaskPL";
    public final String PARTNERLINKTYPE = "HumanTaskPLT";
    public final String REQUEST_MESSAGE = "createTaskRequest";
    public final String RESPONSE_MESSAGE = "createTaskResponse";
    public final String OPERATION = "createTask";
    
}