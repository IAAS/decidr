package de.decidr.prototype;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.net.*;
import java.util.*;
import org.apache.axis2.*;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axiom.*;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
/**
 * Servlet implementation class SendMail
 */
public class SendMail extends HttpServlet {
	private static final long serialVersionUID = 1L;

    
	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	/**
	 * @see Servlet#destroy()
	 */
	public void destroy() {
		super.destroy();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String mailTo = "";
		try 
		{ 
		    mailTo = request.getParameter("mailTo"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		
		String mailSubject = "";
		try 
		{ 
		    mailSubject = request.getParameter("mailSubject"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		
		String mailText = "";
		try 
		{ 
		    mailText = request.getParameter("mailText"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		
		String ErrorMsg = "";
		
		if (mailTo == "" || mailSubject == "") ErrorMsg = "<br/><font style=\"color: #F00;\">Error: Invalid/Missing data</font>";
		
		if (ErrorMsg == "")
		{
			String output = "";
			 // epr to the url the webservice 
			EndpointReference targetEPR = new EndpointReference("http://localhost:8080/axis2/services/DecidrMailDemo");
			try {
				OMFactory fac = OMAbstractFactory.getOMFactory();
				OMNamespace ns = fac.createOMNamespace("http://prototype.decidr.de", "ns");
				OMElement payload = fac.createOMElement("sendMail", ns);
				 

				OMElement subjElem = fac.createOMElement("subject",null,payload);
				subjElem.setText(mailSubject);
				OMElement textElem = fac.createOMElement("message",null,payload);
				textElem.setText(mailText);
				OMElement toElem = fac.createOMElement("recipient",null,payload);
				toElem.setText(mailTo);
				OMElement fromElem = fac.createOMElement("sender",null,payload);
				fromElem.setText("");
				
				 
				Options options = new Options();
				ServiceClient client = new ServiceClient();
				options.setTo(targetEPR);
				options.setAction("urn:sendMail");
				client.setOptions(options);
				 //Blocking invocation
				OMElement result = client.sendReceive(payload);
				output = result.toString();
			
			} catch (AxisFault axisFault) {
				System.out.println("fault");
				axisFault.printStackTrace();
			}
			
			//if(output.equalsIgnoreCase("Mail sent to: " + mailTo))
			//{
				request.setAttribute("mailResult", output);
				RequestDispatcher dispatcher = request.getRequestDispatcher("/mail_success.jsp");
				if (dispatcher != null) dispatcher.forward(request,response);
			//}else
			//{
			//	request.setAttribute("mailResult", output);
			//	RequestDispatcher dispatcher = request.getRequestDispatcher("/mail_failure.jsp");
			//	if (dispatcher != null) dispatcher.forward(request,response);
			//}
			
		}else
		{
			request.setAttribute("mailResult", ErrorMsg);
			request.setAttribute("mailTo", mailTo);
			request.setAttribute("mailSubject", mailSubject);
			request.setAttribute("mailText", mailText);
			RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
			if (dispatcher != null) dispatcher.forward(request,response);
		}
		
	}

}
