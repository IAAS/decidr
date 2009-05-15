/**
 * HumanTaskMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
package de.decidr.webservices.humantask;

/**
 * HumanTaskMessageReceiverInOut message receiver
 */

public class HumanTaskMessageReceiverInOut extends
		org.apache.axis2.receivers.AbstractInOutMessageReceiver {

	@Override
	public void invokeBusinessLogic(
			org.apache.axis2.context.MessageContext msgContext,
			org.apache.axis2.context.MessageContext newMsgContext)
			throws org.apache.axis2.AxisFault {

		try {

			// get the implementation class for the Web Service
			Object obj = getTheImplementationObject(msgContext);

			HumanTaskSkeletonInterface skel = (HumanTaskSkeletonInterface) obj;
			// Out Envelop
			org.apache.axiom.soap.SOAPEnvelope envelope = null;
			// Find the axisOperation that has been set by the Dispatch phase.
			org.apache.axis2.description.AxisOperation op = msgContext
					.getOperationContext().getAxisOperation();
			if (op == null) {
				throw new org.apache.axis2.AxisFault(
						"Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
			}

			java.lang.String methodName;
			if ((op.getName() != null)
					&& ((methodName = org.apache.axis2.util.JavaUtils
							.xmlNameToJava(op.getName().getLocalPart())) != null)) {

				if ("removeTask".equals(methodName)) {

					de.decidr.webservices.humantask.RemoveTaskResponse removeTaskResponse9 = null;
					de.decidr.webservices.humantask.RemoveTask wrappedParam = (de.decidr.webservices.humantask.RemoveTask) fromOM(
							msgContext.getEnvelope().getBody()
									.getFirstElement(),
							de.decidr.webservices.humantask.RemoveTask.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					removeTaskResponse9 =

					skel.removeTask(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext),
							removeTaskResponse9, false);
				} else

				if ("taskCompleted".equals(methodName)) {

					de.decidr.webservices.humantask.TaskCompletedResponse taskCompletedResponse11 = null;
					de.decidr.webservices.humantask.TaskCompleted wrappedParam = (de.decidr.webservices.humantask.TaskCompleted) fromOM(
							msgContext.getEnvelope().getBody()
									.getFirstElement(),
							de.decidr.webservices.humantask.TaskCompleted.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					taskCompletedResponse11 =

					skel.taskCompleted(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext),
							taskCompletedResponse11, false);
				} else

				if ("createTask".equals(methodName)) {

					de.decidr.webservices.humantask.CreateTaskResponse createTaskResponse13 = null;
					de.decidr.webservices.humantask.CreateTask wrappedParam = (de.decidr.webservices.humantask.CreateTask) fromOM(
							msgContext.getEnvelope().getBody()
									.getFirstElement(),
							de.decidr.webservices.humantask.CreateTask.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					createTaskResponse13 =

					skel.createTask(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext),
							createTaskResponse13, false);
				} else

				if ("removeTasks".equals(methodName)) {

					de.decidr.webservices.humantask.RemoveTasksResponse removeTasksResponse15 = null;
					de.decidr.webservices.humantask.RemoveTasks wrappedParam = (de.decidr.webservices.humantask.RemoveTasks) fromOM(
							msgContext.getEnvelope().getBody()
									.getFirstElement(),
							de.decidr.webservices.humantask.RemoveTasks.class,
							getEnvelopeNamespaces(msgContext.getEnvelope()));

					removeTasksResponse15 =

					skel.removeTasks(wrappedParam);

					envelope = toEnvelope(getSOAPFactory(msgContext),
							removeTasksResponse15, false);

				} else {
					throw new java.lang.RuntimeException("method not found");
				}

				newMsgContext.setEnvelope(envelope);
			}
		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	//
	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.RemoveTask param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(
					de.decidr.webservices.humantask.RemoveTask.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.RemoveTaskResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							de.decidr.webservices.humantask.RemoveTaskResponse.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.TaskCompleted param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(
					de.decidr.webservices.humantask.TaskCompleted.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.TaskCompletedResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							de.decidr.webservices.humantask.TaskCompletedResponse.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.CreateTask param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(
					de.decidr.webservices.humantask.CreateTask.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.CreateTaskResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							de.decidr.webservices.humantask.CreateTaskResponse.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.RemoveTasks param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param.getOMElement(
					de.decidr.webservices.humantask.RemoveTasks.MY_QNAME,
					org.apache.axiom.om.OMAbstractFactory.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.om.OMElement toOM(
			de.decidr.webservices.humantask.RemoveTasksResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {

		try {
			return param
					.getOMElement(
							de.decidr.webservices.humantask.RemoveTasksResponse.MY_QNAME,
							org.apache.axiom.om.OMAbstractFactory
									.getOMFactory());
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}

	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			de.decidr.webservices.humantask.RemoveTaskResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();

			emptyEnvelope
					.getBody()
					.addChild(
							param
									.getOMElement(
											de.decidr.webservices.humantask.RemoveTaskResponse.MY_QNAME,
											factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	private de.decidr.webservices.humantask.RemoveTaskResponse wrapremoveTask() {
		de.decidr.webservices.humantask.RemoveTaskResponse wrappedElement = new de.decidr.webservices.humantask.RemoveTaskResponse();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			de.decidr.webservices.humantask.TaskCompletedResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();

			emptyEnvelope
					.getBody()
					.addChild(
							param
									.getOMElement(
											de.decidr.webservices.humantask.TaskCompletedResponse.MY_QNAME,
											factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	private de.decidr.webservices.humantask.TaskCompletedResponse wraptaskCompleted() {
		de.decidr.webservices.humantask.TaskCompletedResponse wrappedElement = new de.decidr.webservices.humantask.TaskCompletedResponse();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			de.decidr.webservices.humantask.CreateTaskResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();

			emptyEnvelope
					.getBody()
					.addChild(
							param
									.getOMElement(
											de.decidr.webservices.humantask.CreateTaskResponse.MY_QNAME,
											factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	private de.decidr.webservices.humantask.CreateTaskResponse wrapcreateTask() {
		de.decidr.webservices.humantask.CreateTaskResponse wrappedElement = new de.decidr.webservices.humantask.CreateTaskResponse();
		return wrappedElement;
	}

	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory,
			de.decidr.webservices.humantask.RemoveTasksResponse param,
			boolean optimizeContent) throws org.apache.axis2.AxisFault {
		try {
			org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory
					.getDefaultEnvelope();

			emptyEnvelope
					.getBody()
					.addChild(
							param
									.getOMElement(
											de.decidr.webservices.humantask.RemoveTasksResponse.MY_QNAME,
											factory));

			return emptyEnvelope;
		} catch (org.apache.axis2.databinding.ADBException e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
	}

	private de.decidr.webservices.humantask.RemoveTasksResponse wrapremoveTasks() {
		de.decidr.webservices.humantask.RemoveTasksResponse wrappedElement = new de.decidr.webservices.humantask.RemoveTasksResponse();
		return wrappedElement;
	}

	/**
	 * get the default envelope
	 */
	private org.apache.axiom.soap.SOAPEnvelope toEnvelope(
			org.apache.axiom.soap.SOAPFactory factory) {
		return factory.getDefaultEnvelope();
	}

	private java.lang.Object fromOM(org.apache.axiom.om.OMElement param,
			java.lang.Class type, java.util.Map extraNamespaces)
			throws org.apache.axis2.AxisFault {

		try {

			if (de.decidr.webservices.humantask.RemoveTask.class.equals(type)) {

				return de.decidr.webservices.humantask.RemoveTask.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.RemoveTaskResponse.class
					.equals(type)) {

				return de.decidr.webservices.humantask.RemoveTaskResponse.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.TaskCompleted.class
					.equals(type)) {

				return de.decidr.webservices.humantask.TaskCompleted.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.TaskCompletedResponse.class
					.equals(type)) {

				return de.decidr.webservices.humantask.TaskCompletedResponse.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.CreateTask.class.equals(type)) {

				return de.decidr.webservices.humantask.CreateTask.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.CreateTaskResponse.class
					.equals(type)) {

				return de.decidr.webservices.humantask.CreateTaskResponse.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.RemoveTasks.class.equals(type)) {

				return de.decidr.webservices.humantask.RemoveTasks.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

			if (de.decidr.webservices.humantask.RemoveTasksResponse.class
					.equals(type)) {

				return de.decidr.webservices.humantask.RemoveTasksResponse.Factory
						.parse(param.getXMLStreamReaderWithoutCaching());

			}

		} catch (java.lang.Exception e) {
			throw org.apache.axis2.AxisFault.makeFault(e);
		}
		return null;
	}

	/**
	 * A utility method that copies the namepaces from the SOAPEnvelope
	 */
	private java.util.Map getEnvelopeNamespaces(
			org.apache.axiom.soap.SOAPEnvelope env) {
		java.util.Map returnMap = new java.util.HashMap();
		java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
		while (namespaceIterator.hasNext()) {
			org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator
					.next();
			returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
		}
		return returnMap;
	}

	private org.apache.axis2.AxisFault createAxisFault(java.lang.Exception e) {
		org.apache.axis2.AxisFault f;
		Throwable cause = e.getCause();
		if (cause != null) {
			f = new org.apache.axis2.AxisFault(e.getMessage(), cause);
		} else {
			f = new org.apache.axis2.AxisFault(e.getMessage());
		}

		return f;
	}

}// end of class
