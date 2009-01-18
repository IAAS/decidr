package de.decidr.seminar.consoleui;

import java.util.Map;
import java.util.Set;

import de.decidr.seminar.tenantmgr.TenantManager;
import de.decidr.seminar.tenantmgr.TenantManagerFactory;

public class ConsoleUIMain {
	
	static String sid = "";
	static String tenantname = "";
	
	public static void main(String[] args) {
		java.io.BufferedReader in = new java.io.BufferedReader(
				new java.io.InputStreamReader(System.in));
		try {
			TenantManager tmgr = TenantManagerFactory.getTenantManager();
		//String sid = "";
		
		while (true) {
			try {
				System.out.print("\n" + tenantname + ">");
				String[] query = in.readLine().split(" ");
				
				if (query[0].equals("exit")) {
					System.exit(0);
				}
				
				Object rtn;
				rtn = call(tmgr, query);
				
				if (rtn != null) {
					printOut(rtn);
				}
				
			} catch (Exception e) {
				System.err.println(e.getMessage());
				//e.printStackTrace();
			}
		}
		
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
	}
	
	private static Object call(TenantManager tmgr, String[] query) throws Exception {
		String m = query[0].toLowerCase();
		if (m.equals("addtenant")) {
			tmgr.addTenant(query[1]);
			return null;
		} else if (m.equals("login")) {
			sid = tmgr.login(query[1]);
			tenantname = query[1];
			return "Logged in as " + query[1] + "."; 
		} else if (m.equals("logout")) {
			tmgr.logout(sid);
			sid = "";
			tenantname = "";
			return "Logout successful.";
		} else if (m.equals("addcustomfield")) {
			tmgr.addCustomField(sid, query[1], query[2]);
			return null;
		} else if (m.equals("getvalue")) {
			return tmgr.getValue(sid, query[1]);
		} else if (m.equals("setvalue")) {
			tmgr.setValue(sid, query[1], query[2]);
			return null;
		} else if (m.equals("getvalues")) {
			return tmgr.getValues(sid);
		} else {
			return "Invald command.";
		}
	}
	
	private static void printOut(Object val) {
		if (val instanceof String) {
			System.out.println((String)val);
		} else if (val instanceof Map) {
			Map<String, String> map = (Map<String, String>)val;
			Set<String> keys = map.keySet();
			
			for (String key: keys) {
				System.out.println(key + ": " + map.get(key));
			}
		}
	}
}
