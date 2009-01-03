import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.axis2.*;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

/**
 * Servlet implementation class DemoServlet
 */
@SuppressWarnings("serial")
public class DemoServlet extends HttpServlet {
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String value1 = "0";
		String value2 = "0";
		String output = "an error occured";
		
		//get the two parameters
		value1 = request.getParameter("val1");
		value2 = request.getParameter("val2");
		
		//the location of the webservice
		EndpointReference targetEPR = new EndpointReference("http://localhost:8080/axis2/services/DemoWebservice");
		try {
			OMFactory fac = OMAbstractFactory.getOMFactory();
			// namespace of the webservice = package in reversed order
			OMNamespace ns = fac.createOMNamespace("http://seminar.decidr.de", "ns");
			// using the name of the function
			OMElement payload = fac.createOMElement("intAddition", ns);
			 
			//adding the values of the functions' parameters
			OMElement subjElem = fac.createOMElement("value1",null,payload);
			subjElem.setText(value1);
			OMElement textElem = fac.createOMElement("value2",null,payload);
			textElem.setText(value2);
			
			//creating a new query
			Options options = new Options();
			ServiceClient client = new ServiceClient();
			//where the webservice is
			options.setTo(targetEPR);
			//what the webservice should do
			options.setAction("urn:intAddition");
			// setup the query
			client.setOptions(options);
			 //send a blocking invocation and get the answer
			OMElement result = client.sendReceive(payload);
			//get the result
			output = result.getFirstElement().getText();
		
		} catch (AxisFault axisFault) {
			System.out.println("fault");
			axisFault.printStackTrace();
		}
		
		//add some values for the jsp
		request.setAttribute("additionVal1", value1);
		request.setAttribute("additionVal2", value2);
		request.setAttribute("additionResult", output);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/index.jsp");
		
		if (dispatcher != null) 
		{
			//give back control to the jsp
			dispatcher.forward(request,response);
		}
	}

}
