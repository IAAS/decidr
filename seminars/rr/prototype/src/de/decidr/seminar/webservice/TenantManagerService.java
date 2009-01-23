/**
 * 
 */
package de.decidr.seminar.webservice;

import java.util.Map;

import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlTransient;

import de.decidr.seminar.tenantmgr.TenantManager;
import de.decidr.seminar.tenantmgr.TenantManagerFactory;

/**
 * @author RR
 * 
 */
// @XmlType(name = "TenantService", namespace = "http://decidr.de/seminarWS")
@WebService(name = "TenantServicePT", serviceName = "TenantService", targetNamespace = "http://decidr.de/seminarWS")
public class TenantManagerService {

	@XmlTransient
	public static TenantManager manager;

	@WebMethod(exclude = true)
	public synchronized TenantManager getManager() {
		if (manager == null)
			manager = TenantManagerFactory.getTenantManager();
		return manager;
	}

	@WebMethod(operationName = "addTenant")
	public void addTenant(@WebParam(name = "name") String name) {
		try {
			getManager().addTenant(name);
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	@WebMethod(operationName = "login")
	@WebResult(name = "sessionID")
	public String login(@WebParam(name = "name") String name) throws Exception {
		return getManager().login(name);
	}

	@Oneway
	@WebMethod(operationName = "logout")
	public void logout(@WebParam(name = "sessionID") String sid) {
		try {
			getManager().logout(sid);
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	@WebMethod(operationName = "addField")
	public void addField(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName,
			@WebParam(name = "value") String value) {
		try {
			getManager().addCustomField(sid, fieldName, value);
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	@WebMethod(operationName = "setField")
	public void setField(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName,
			@WebParam(name = "value") String value) {
		try {
			getManager().setValue(sid, fieldName, value);
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	@WebMethod(operationName = "setFields")
	public void setFields(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldMap") Map<String, String> fields) {
		try {
			for (String field : fields.keySet()) {
				getManager().setValue(sid, field, fields.get(field));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw e;
		}
	}

	@WebMethod(operationName = "getField")
	@WebResult(name = "fieldValue")
	public String getField(@WebParam(name = "sessionID") String sid,
			@WebParam(name = "fieldName") String fieldName) throws Exception {
		return getManager().getValue(sid, fieldName);
	}

	@WebMethod(operationName = "getTenantFields")
	@WebResult(name = "fieldMap")
	public Map<String, String> getTenantFields(
			@WebParam(name = "sessionID") String sid) throws Exception {
		return getManager().getValues(sid);
	}
}