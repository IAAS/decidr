/**
 * 
 */
package de.decidr.seminar.webservice;

import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import de.decidr.seminar.tenantmgr.TenantManager;
import de.decidr.seminar.tenantmgr.TenantManagerFactory;

/**
 * @author RR
 * 
 */
@WebService(name = "TenantService", targetNamespace = "http://decidr.de/seminarWS")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class TenantManagerService {

	public static TenantManager manager;

	@WebMethod(exclude = true)
	public synchronized TenantManager getManager() {
		if (manager == null)
			manager = TenantManagerFactory.getTenantManager();
		return manager;
	}

	@WebMethod(operationName = "addTenant")
	public void addTenant(@WebParam(name = "name") String name)
			throws Exception {
		getManager().addTenant(name);
	}

	@WebMethod(operationName = "login")
	@WebResult(name = "sessionID")
	public String login(@WebParam(name = "name") String name) throws Exception {
		return getManager().login(name);
	}

	@WebMethod(operationName = "logout")
	public void logout(@WebParam(name = "sessionID") String sid)
			throws Exception {
		getManager().logout(sid);
	}

	@WebMethod(operationName = "addField")
	public void addCustomField(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName,
			@WebParam(name = "value") String value) throws Exception {
		getManager().addCustomField(sid, fieldName, value);
	}

	@WebMethod(operationName = "setField")
	public void setValue(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName,
			@WebParam(name = "value") String value) throws Exception {
		getManager().setValue(sid, fieldName, value);
	}

	@WebMethod(operationName = "setFields")
	public void setValues(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldMap") Map<String, String> fields)
			throws Exception {
		for (String field : fields.keySet()) {
			getManager().setValue(sid, field, fields.get(field));
		}
	}

	@WebMethod(operationName = "getField")
	@WebResult(name = "fieldValue")
	public String getValue(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName) throws Exception {
		return getManager().getValue(sid, fieldName);
	}

	@WebMethod(operationName = "getTenantFields")
	@WebResult(name = "fieldMap")
	public Map<String, String> getValues(
			@WebParam(name = "sessionID") String sid) throws Exception {
		return getManager().getValues(sid);
	}
}
