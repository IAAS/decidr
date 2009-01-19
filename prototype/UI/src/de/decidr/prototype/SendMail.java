package de.decidr.prototype;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.xml.namespace.QName;

import java.net.*;
import java.util.*;
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
		
		
		String ns1 = "";
		try 
		{ 
			ns1 = request.getParameter("ns1"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		String ns2 = "";
		try 
		{ 
			ns2 = request.getParameter("ns2"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		String paylo = "";
		try 
		{ 
			paylo = request.getParameter("paylo"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		String actio = "";
		try 
		{ 
			actio = request.getParameter("actio"); 
		}catch (Exception e) 
		{ 
		    e.printStackTrace(); 
		}
		
		
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
			//EndpointReference targetEPR = new EndpointReference("http://iaassrv7.informatik.uni-stuttgart.de:8080/axis2/services/eMailWS");
			EndpointReference targetEPR = new EndpointReference("http://iaassrv7.informatik.uni-stuttgart.de:8080/ode/processes/mailws");
			
			try {
				/*SOAPFactory sfac = OMAbstractFactory.getSOAP12Factory();
				SOAPEnvelope env = sfac.getDefaultEnvelope();
				
				//OMFactory fac = OMAbstractFactory.getOMFactory();
				OMNamespace ns = sfac.createOMNamespace("http://decidr.org/mailws", "mail");
				 

				OMElement subjElem = sfac.createOMElement("subject",ns);
				subjElem.setText(mailSubject);
				OMElement textElem = sfac.createOMElement("message",ns);
				textElem.setText(mailText);
				OMElement toElem = sfac.createOMElement("recipient",ns);
				toElem.setText(mailTo);
				OMElement fromElem = sfac.createOMElement("sender",ns);
				fromElem.setText("");
				
				env.getBody().addChild(subjElem);
				env.getBody().addChild(textElem);
				env.getBody().addChild(toElem);
				env.getBody().addChild(fromElem);
				 
				MessageContext mc = new MessageContext();
				mc.setEnvelope(env);
				
				Options options = new Options();
				options.setTo(targetEPR);
				options.setSoapVersionURI(SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);
				options.setAction("urn:eMailWSProcessRequest");
				ServiceClient client = new ServiceClient();
				client.setOptions(options);
				OperationClient reclient = client.createClient(ServiceClient.ANON_OUT_IN_OP);
				reclient.addMessageContext(mc);
				reclient.execute(true);
				MessageContext resp = reclient.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE);
				SOAPBody body = resp.getEnvelope().getBody();
				//Blocking invocation
				OMElement result = body.getFirstElement().getFirstChildWithName(new QName("http://service.soapwithattachments.sample/xsd","return"));
//client.sendReceive(payload);
				success = Boolean.parseBoolean(result.getFirstElement().getText());*/
				
/*  Directly ysing the WS				OMFactory fac = OMAbstractFactory.getOMFactory();
				OMNamespace ns = fac.createOMNamespace("http://decidr.org/mailws", "ns");
				OMElement payload = fac.createOMElement("sendEmail", ns);
				 

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
				options.setAction("urn:sendEmail");
				client.setOptions(options);
				 //Blocking invocation
				OMElement result = client.sendReceive(payload);
				success = Boolean.parseBoolean(result.getFirstElement().getText());*/
				
				OMFactory fac = OMAbstractFactory.getOMFactory();
				//OMNamespace ns = fac.createOMNamespace("http://decidr.org/mailws/process", "mail");
				//OMElement payload = fac.createOMElement("process", ns);
				OMNamespace ns = fac.createOMNamespace(ns1, ns2);
				OMElement payload = fac.createOMElement(paylo, ns);
				 

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
				//options.setAction("urn:process");
				options.setAction(actio);
				client.setOptions(options);
				 //Blocking invocation
				OMElement result = client.sendReceive(payload);
				ErrorMsg = result.getFirstElement().getText();
				success = Boolean.parseBoolean(ErrorMsg);
		
			
			} catch (AxisFault axisFault) {
				System.out.println("fault");
				axisFault.printStackTrace();
				ErrorMsg = axisFault.getMessage(); 
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
