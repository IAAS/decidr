package de.decidr.prototype;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.axis2.*;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.axiom.*;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAP12Constants;
import org.apache.axiom.soap.SOAPBody;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
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
		boolean success = false;
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
			 // epr to the url the webservice 
						EndpointReference targetEPR = new EndpointReference("http://iaassrv7.informatik.uni-stuttgart.de:8080/ode/processes/mailws");
			
			try {
				SOAPFactory sfac = OMAbstractFactory.getSOAP12Factory();
				SOAPEnvelope env = sfac.getDefaultEnvelope();
				
				OMNamespace ns = sfac.createOMNamespace("http://decidr.org/mailws", "ns");
				 
				OMElement processElem = sfac.createOMElement("eMailWSProcessRequest",ns);
				
				OMElement subjElem = sfac.createOMElement("subject",ns);
				subjElem.setText(mailSubject);
				OMElement textElem = sfac.createOMElement("message",ns);
				textElem.setText(mailText);
				OMElement toElem = sfac.createOMElement("recipient",ns);
				toElem.setText(mailTo);
				OMElement fromElem = sfac.createOMElement("sender",ns);
				fromElem.setText("");
				
				processElem.addChild(subjElem);
				processElem.addChild(textElem);
				processElem.addChild(toElem);
				processElem.addChild(fromElem);
				env.getBody().addChild(processElem);
				 
				MessageContext mc = new MessageContext();
				mc.setEnvelope(env);
				
				Options options = new Options();
				options.setTo(targetEPR);
				options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
				options.setAction("urn:process");
				ServiceClient client = new ServiceClient();
				client.setOptions(options);
				OperationClient reclient = client.createClient(ServiceClient.ANON_OUT_IN_OP);
				reclient.addMessageContext(mc);
				reclient.execute(true);
				MessageContext resp = reclient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
				SOAPBody body = resp.getEnvelope().getBody();
				//Blocking invocation
				OMElement result = body.getFirstElement();
				success = Boolean.parseBoolean(result.getFirstElement().getText());
				
			
			} catch (AxisFault axisFault) {
				ErrorMsg = axisFault.getMessage() + "<br/>";
				for (StackTraceElement fault: axisFault.getStackTrace())
				{
					ErrorMsg = ErrorMsg + "<br/>" + fault.toString();
				}
			}
	

			
			RequestDispatcher dispatcher;
			
			if(success){
				request.setAttribute("mailResult", String.valueOf(success));
				dispatcher = request.getRequestDispatcher("/mail_success.jsp");
			}else{
				request.setAttribute("mailResult", String.valueOf(success)+"<br/>"+ErrorMsg);
				dispatcher = request.getRequestDispatcher("/mail_failure.jsp");
			}
			
			if (dispatcher != null) 
			{
				dispatcher.forward(request,response);
			}
			
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
