
/**
 * HumanTaskMessageReceiverInOut.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */
        package de.decidr.webservices.humantask;

        /**
        *  HumanTaskMessageReceiverInOut message receiver
        */

        public class HumanTaskMessageReceiverInOut extends org.apache.axis2.receivers.AbstractInOutMessageReceiver{


        public void invokeBusinessLogic(org.apache.axis2.context.MessageContext msgContext, org.apache.axis2.context.MessageContext newMsgContext)
        throws org.apache.axis2.AxisFault{

        try {

        // get the implementation class for the Web Service
        Object obj = getTheImplementationObject(msgContext);

        HumanTaskInterface skel = (HumanTaskInterface)obj;
        //Out Envelop
        org.apache.axiom.soap.SOAPEnvelope envelope = null;
        //Find the axisOperation that has been set by the Dispatch phase.
        org.apache.axis2.description.AxisOperation op = msgContext.getOperationContext().getAxisOperation();
        if (op == null) {
        throw new org.apache.axis2.AxisFault("Operation is not located, if this is doclit style the SOAP-ACTION should specified via the SOAP Action to use the RawXMLProvider");
        }

        java.lang.String methodName;
        if((op.getName() != null) && ((methodName = org.apache.axis2.util.JavaUtils.xmlNameToJava(op.getName().getLocalPart())) != null)){

        

            if("removeTask".equals(methodName)){
                
                de.decidr.webservices.humantask.RemoveTaskResponse removeTaskResponse9 = null;
	                        de.decidr.webservices.humantask.RemoveTask wrappedParam =
                                                             (de.decidr.webservices.humantask.RemoveTask)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    de.decidr.webservices.humantask.RemoveTask.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               removeTaskResponse9 =
                                                   
                                                   
                                                         skel.removeTask(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), removeTaskResponse9, false);
                                    } else 

            if("taskCompleted".equals(methodName)){
                
                de.decidr.webservices.humantask.TaskCompletedResponse taskCompletedResponse11 = null;
	                        de.decidr.webservices.humantask.TaskCompleted wrappedParam =
                                                             (de.decidr.webservices.humantask.TaskCompleted)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    de.decidr.webservices.humantask.TaskCompleted.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               taskCompletedResponse11 =
                                                   
                                                   
                                                         skel.taskCompleted(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), taskCompletedResponse11, false);
                                    } else 

            if("createTask".equals(methodName)){
                
                de.decidr.webservices.humantask.CreateTaskResponse createTaskResponse13 = null;
	                        de.decidr.webservices.humantask.CreateTask wrappedParam =
                                                             (de.decidr.webservices.humantask.CreateTask)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    de.decidr.webservices.humantask.CreateTask.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               createTaskResponse13 =
                                                   
                                                   
                                                         skel.createTask(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), createTaskResponse13, false);
                                    } else 

            if("removeTasks".equals(methodName)){
                
                de.decidr.webservices.humantask.RemoveTasksResponse removeTasksResponse15 = null;
	                        de.decidr.webservices.humantask.RemoveTasks wrappedParam =
                                                             (de.decidr.webservices.humantask.RemoveTasks)fromOM(
                                    msgContext.getEnvelope().getBody().getFirstElement(),
                                    de.decidr.webservices.humantask.RemoveTasks.class,
                                    getEnvelopeNamespaces(msgContext.getEnvelope()));
                                                
                                               removeTasksResponse15 =
                                                   
                                                   
                                                         skel.removeTasks(wrappedParam)
                                                    ;
                                            
                                        envelope = toEnvelope(getSOAPFactory(msgContext), removeTasksResponse15, false);
                                    
            } else {
              throw new java.lang.RuntimeException("method not found");
            }
        

        newMsgContext.setEnvelope(envelope);
        }
        }
        catch (java.lang.Exception e) {
        throw org.apache.axis2.AxisFault.makeFault(e);
        }
        }
        
        //
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_RemoveTask;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_RemoveTaskResponse;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_TaskCompleted;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_TaskCompletedResponse;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_CreateTask;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_CreateTaskResponse;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_RemoveTasks;
            
                private static javax.xml.bind.JAXBContext _de_decidr_webservices_humantask_RemoveTasksResponse;
            

        private static final java.util.HashMap<Class,javax.xml.bind.JAXBContext> classContextMap = new java.util.HashMap<Class,javax.xml.bind.JAXBContext>();

        static {
            javax.xml.bind.JAXBContext jc;
            
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.RemoveTask.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.RemoveTask");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_RemoveTask = jc;
                        classContextMap.put(de.decidr.webservices.humantask.RemoveTask.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.RemoveTaskResponse.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.RemoveTaskResponse");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_RemoveTaskResponse = jc;
                        classContextMap.put(de.decidr.webservices.humantask.RemoveTaskResponse.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.TaskCompleted.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.TaskCompleted");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_TaskCompleted = jc;
                        classContextMap.put(de.decidr.webservices.humantask.TaskCompleted.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.TaskCompletedResponse.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.TaskCompletedResponse");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_TaskCompletedResponse = jc;
                        classContextMap.put(de.decidr.webservices.humantask.TaskCompletedResponse.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.CreateTask.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.CreateTask");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_CreateTask = jc;
                        classContextMap.put(de.decidr.webservices.humantask.CreateTask.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.CreateTaskResponse.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.CreateTaskResponse");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_CreateTaskResponse = jc;
                        classContextMap.put(de.decidr.webservices.humantask.CreateTaskResponse.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.RemoveTasks.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.RemoveTasks");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_RemoveTasks = jc;
                        classContextMap.put(de.decidr.webservices.humantask.RemoveTasks.class, jc);
                    }
                
                    jc = null;
                    try {
                        jc = javax.xml.bind.JAXBContext.newInstance(de.decidr.webservices.humantask.RemoveTasksResponse.class);
                    }
                    catch ( javax.xml.bind.JAXBException ex ) {
                        System.err.println("Unable to create JAXBContext for class: de.decidr.webservices.humantask.RemoveTasksResponse");
                        Runtime.getRuntime().exit(-1);
                    }
                    finally {
                        _de_decidr_webservices_humantask_RemoveTasksResponse = jc;
                        classContextMap.put(de.decidr.webservices.humantask.RemoveTasksResponse.class, jc);
                    }
                
        }

        

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.RemoveTask param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_RemoveTask;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.RemoveTask.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "removeTask");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "removeTask", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.RemoveTask param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.RemoveTaskResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_RemoveTaskResponse;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.RemoveTaskResponse.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "removeTaskResponse");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "removeTaskResponse", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.RemoveTaskResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.TaskCompleted param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_TaskCompleted;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.TaskCompleted.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "taskCompleted");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "taskCompleted", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.TaskCompleted param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.TaskCompletedResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_TaskCompletedResponse;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.TaskCompletedResponse.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "taskCompletedResponse");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "taskCompletedResponse", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.TaskCompletedResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.CreateTask param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_CreateTask;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.CreateTask.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "createTask");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "createTask", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.CreateTask param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.CreateTaskResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_CreateTaskResponse;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.CreateTaskResponse.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "createTaskResponse");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "createTaskResponse", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.CreateTaskResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.RemoveTasks param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_RemoveTasks;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.RemoveTasks.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "removeTasks");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "removeTasks", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.RemoveTasks param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

                private org.apache.axiom.om.OMElement toOM(de.decidr.webservices.humantask.RemoveTasksResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    try {
                        javax.xml.bind.JAXBContext context = _de_decidr_webservices_humantask_RemoveTasksResponse;
                        javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

                        org.apache.axiom.om.OMFactory factory = org.apache.axiom.om.OMAbstractFactory.getOMFactory();

                        JaxbRIDataSource source = new JaxbRIDataSource( de.decidr.webservices.humantask.RemoveTasksResponse.class,
                                                                        param,
                                                                        marshaller,
                                                                        "http://decidr.de/webservices/HumanTask",
                                                                        "removeTasksResponse");
                        org.apache.axiom.om.OMNamespace namespace = factory.createOMNamespace("http://decidr.de/webservices/HumanTask",
                                                                           null);
                        return factory.createOMElement(source, "removeTasksResponse", namespace);
                    } catch (javax.xml.bind.JAXBException bex){
                        throw org.apache.axis2.AxisFault.makeFault(bex);
                    }
                }

                private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory, de.decidr.webservices.humantask.RemoveTasksResponse param, boolean optimizeContent)
                throws org.apache.axis2.AxisFault {
                    org.apache.axiom.soap.SOAPEnvelope envelope = factory.getDefaultEnvelope();
                    envelope.getBody().addChild(toOM(param, optimizeContent));
                    return envelope;
                }

                

        /**
        *  get the default envelope
        */
        private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
            return factory.getDefaultEnvelope();
        }

        private java.lang.Object fromOM (
            org.apache.axiom.om.OMElement param,
            java.lang.Class type,
            java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault{
            try {
                javax.xml.bind.JAXBContext context = classContextMap.get(type);
                javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();

                return unmarshaller.unmarshal(param.getXMLStreamReaderWithoutCaching(), type).getValue();
            } catch (javax.xml.bind.JAXBException bex){
                throw org.apache.axis2.AxisFault.makeFault(bex);
            }
        }

        class JaxbRIDataSource implements org.apache.axiom.om.OMDataSource {
            /**
             * Bound object for output.
             */
            private final Object outObject;

            /**
             * Bound class for output.
             */
            private final Class outClazz;

            /**
             * Marshaller.
             */
            private final javax.xml.bind.Marshaller marshaller;

            /**
             * Namespace
             */
            private String nsuri;

            /**
             * Local name
             */
            private String name;

            /**
             * Constructor from object and marshaller.
             *
             * @param obj
             * @param marshaller
             */
            public JaxbRIDataSource(Class clazz, Object obj, javax.xml.bind.Marshaller marshaller, String nsuri, String name) {
                this.outClazz = clazz;
                this.outObject = obj;
                this.marshaller = marshaller;
                this.nsuri = nsuri;
                this.name = name;
            }

            public void serialize(java.io.OutputStream output, org.apache.axiom.om.OMOutputFormat format) throws javax.xml.stream.XMLStreamException {
                try {
                    marshaller.marshal(new javax.xml.bind.JAXBElement(
                            new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), output);
                } catch (javax.xml.bind.JAXBException e) {
                    throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
                }
            }

            public void serialize(java.io.Writer writer, org.apache.axiom.om.OMOutputFormat format) throws javax.xml.stream.XMLStreamException {
                try {
                    marshaller.marshal(new javax.xml.bind.JAXBElement(
                            new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), writer);
                } catch (javax.xml.bind.JAXBException e) {
                    throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
                }
            }

            public void serialize(javax.xml.stream.XMLStreamWriter xmlWriter) throws javax.xml.stream.XMLStreamException {
                try {
                    marshaller.marshal(new javax.xml.bind.JAXBElement(
                            new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), xmlWriter);
                } catch (javax.xml.bind.JAXBException e) {
                    throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
                }
            }

            public javax.xml.stream.XMLStreamReader getReader() throws javax.xml.stream.XMLStreamException {
                try {
                    javax.xml.bind.JAXBContext context = classContextMap.get(outClazz);
                    org.apache.axiom.om.impl.builder.SAXOMBuilder builder = new org.apache.axiom.om.impl.builder.SAXOMBuilder();
                    javax.xml.bind.Marshaller marshaller = context.createMarshaller();
                    marshaller.marshal(new javax.xml.bind.JAXBElement(
                            new javax.xml.namespace.QName(nsuri, name), outObject.getClass(), outObject), builder);

                    return builder.getRootElement().getXMLStreamReader();
                } catch (javax.xml.bind.JAXBException e) {
                    throw new javax.xml.stream.XMLStreamException("Error in JAXB marshalling", e);
                }
            }
        }
        
    

        /**
        *  A utility method that copies the namepaces from the SOAPEnvelope
        */
        private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env){
        java.util.Map returnMap = new java.util.HashMap();
        java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
        while (namespaceIterator.hasNext()) {
        org.apache.axiom.om.OMNamespace ns = (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
        returnMap.put(ns.getPrefix(),ns.getNamespaceURI());
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

        }//end of class
    