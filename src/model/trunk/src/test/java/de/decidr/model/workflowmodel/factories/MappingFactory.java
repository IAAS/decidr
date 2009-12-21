/*
 * The DecidR Development Team licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package de.decidr.model.workflowmodel.factories;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;

import de.decidr.model.webservices.EmailInterface;
import de.decidr.model.webservices.HumanTaskInterface;
import de.decidr.model.workflowmodel.dwdl.transformation.BPELConstants;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.dwdl.transformation.PropertyConstants;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.webservices.ObjectFactory;
import de.decidr.model.workflowmodel.webservices.PLTInfo;
import de.decidr.model.workflowmodel.webservices.Properties;
import de.decidr.model.workflowmodel.bpel.varprop.Property;
import de.decidr.model.workflowmodel.bpel.varprop.PropertyAlias;
import de.decidr.model.workflowmodel.bpel.varprop.Query;
import de.decidr.model.workflowmodel.webservices.PropertyAliases;
import de.decidr.model.workflowmodel.webservices.WebserviceMapping;

/**
 * This class creates the HumanTask and the Email mapping
 * 
 * @author Modood Alvi
 */
public class MappingFactory {

    public static WebserviceMapping getEmailMapping() {

        WebserviceMapping email = new WebserviceMapping();

        // setting email mappings
        email.setActivity(BPELConstants.Email.NAME);
        email.setPortType(EmailInterface.PORT_TYPE_NAME);
        email.setOperation("sendEmail");
        email.setBinding(EmailInterface.BINDING_NAME);
        PLTInfo partnerlinktype = new PLTInfo();
        partnerlinktype.setName(BPELConstants.Email.NAME + "PLT");
        partnerlinktype.setPartnerRole(BPELConstants.Email.NAME + "Provider");
        email.setPartnerLinkType(partnerlinktype);
        email.setService(EmailInterface.SERVICE_NAME);
        email.setServicePort(EmailInterface.PORT_NAME);

        QName createEmailRequest = new QName(EmailInterface.TARGET_NAMESPACE,
                "sendEmailRequest");

        Property to = new Property();
        to.setName(PropertyConstants.Email.TO);
        to.setType(new QName(Constants.DECIDRTYPES_NAMESPACE, "tRole"));

        Property cc = new Property();
        cc.setName(PropertyConstants.Email.CC);
        cc.setType(new QName(Constants.DECIDRTYPES_NAMESPACE, "tRole"));

        Property bcc = new Property();
        bcc.setName(PropertyConstants.Email.BCC);
        bcc.setType(new QName(Constants.DECIDRTYPES_NAMESPACE, "tRole"));

        Property fromName = new Property();
        fromName.setName(PropertyConstants.Email.FROM);
        fromName
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property fromAddress = new Property();
        fromAddress.setName(PropertyConstants.Email.FROMADRESS);
        fromAddress.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property subject = new Property();
        subject.setName(PropertyConstants.Email.SUBJECT);
        subject
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property headers = new Property();
        headers.setName(PropertyConstants.Email.HEADERS);
        headers.setType(new QName(Constants.DECIDRWSTYPES_NAMESPACE,
                "tStringMapping"));

        Property bodyTXT = new Property();
        bodyTXT.setName(PropertyConstants.Email.MESSAGE);
        bodyTXT
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property bodyHTML = new Property();
        bodyHTML.setName(PropertyConstants.Email.MESSAGEHTML);
        bodyHTML
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property attachments = new Property();
        attachments.setName(PropertyConstants.Email.ATTACHEMENT);
        attachments.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "long"));

        PropertyAlias to_Alias = new PropertyAlias();
        to_Alias.setPropertyName(new QName(EmailInterface.TARGET_NAMESPACE, to
                .getName()));
        to_Alias.setMessageType(createEmailRequest);
        to_Alias.setPart("parameters");
        Query to_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:to//dwsdl:roleUser/dwsdl:role");
        to_Alias.setQuery(to_Query);

        PropertyAlias cc_Alias = new PropertyAlias();
        cc_Alias.setPropertyName(new QName(EmailInterface.TARGET_NAMESPACE, cc
                .getName()));
        cc_Alias.setMessageType(createEmailRequest);
        to_Alias.setPart("parameters");
        Query cc_Query = new Query();
        cc_Query.getContent().add("/tns:sendEmail/tns:cc//dwsdl:roleUser/dwsdl:role");
        cc_Alias.setQuery(cc_Query);

        PropertyAlias bcc_Alias = new PropertyAlias();
        bcc_Alias.setPropertyName(new QName(EmailInterface.TARGET_NAMESPACE,
                bcc.getName()));
        bcc_Alias.setMessageType(createEmailRequest);
        bcc_Alias.setPart("parameters");
        Query bcc_Query = new Query();
        bcc_Query.getContent().add("/tns:sendEmail/tns:bcc//dwsdl:roleUser/dwsdl:role");
        bcc_Alias.setQuery(bcc_Query);

        PropertyAlias fromName_Alias = new PropertyAlias();
        fromName_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, fromName.getName()));
        fromName_Alias.setMessageType(createEmailRequest);
        fromName_Alias.setPart("parameters");
        Query fromName_Query = new Query();
        fromName_Query.getContent().add("/tns:sendEmail/tns:fromName");
        fromName_Alias.setQuery(fromName_Query);

        PropertyAlias fromAddress_Alias = new PropertyAlias();
        fromAddress_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, fromAddress.getName()));
        fromAddress_Alias.setMessageType(createEmailRequest);
        fromAddress_Alias.setPart("parameters");
        Query fromAddress_Query = new Query();
        fromAddress_Query.getContent().add("/tns:sendEmail/tns:fromAddress");
        fromAddress_Alias.setQuery(fromAddress_Query);

        PropertyAlias subject_Alias = new PropertyAlias();
        subject_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, subject.getName()));
        subject_Alias.setMessageType(createEmailRequest);
        subject_Alias.setPart("parameters");
        Query subject_Query = new Query();
        subject_Query.getContent().add("/tns:sendEmail/tns:subject");
        subject_Alias.setQuery(subject_Query);

        PropertyAlias headers_Alias = new PropertyAlias();
        headers_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, headers.getName()));
        headers_Alias.setMessageType(createEmailRequest);
        headers_Alias.setPart("parameters");
        Query headers_Query = new Query();
        headers_Query.getContent().add("/tns:sendEmail/tns:headers//decidr:item");
        headers_Alias.setQuery(headers_Query);

        PropertyAlias bodyTXT_Alias = new PropertyAlias();
        bodyTXT_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, bodyTXT.getName()));
        bodyTXT_Alias.setMessageType(createEmailRequest);
        bodyTXT_Alias.setPart("parameters");
        Query bodyTXT_Query = new Query();
        bodyTXT_Query.getContent().add("/tns:sendEmail/tns:bodyTXT");
        bodyTXT_Alias.setQuery(bodyTXT_Query);

        PropertyAlias bodyHTML_Alias = new PropertyAlias();
        bodyHTML_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, bodyHTML.getName()));
        bodyHTML_Alias.setMessageType(createEmailRequest);
        bodyHTML_Alias.setPart("parameters");
        Query bodyHTML_Query = new Query();
        bodyHTML_Query.getContent().add("/tns:sendEmail/tns:bodyHTML");
        bodyHTML_Alias.setQuery(bodyHTML_Query);

        PropertyAlias attachments_Alias = new PropertyAlias();
        attachments_Alias.setPropertyName(new QName(
                EmailInterface.TARGET_NAMESPACE, attachments.getName()));
        attachments_Alias.setMessageType(createEmailRequest);
        attachments_Alias.setPart("parameters");
        Query attachments_Query = new Query();
        attachments_Query.getContent().add("/tns:sendEmail/tns:attachments//decidr:id");
        attachments_Alias.setQuery(attachments_Query);

        Properties emailProperties = new Properties();
        emailProperties.getProperty().add(to);
        emailProperties.getProperty().add(cc);
        emailProperties.getProperty().add(bcc);
        emailProperties.getProperty().add(fromName);
        emailProperties.getProperty().add(fromAddress);
        emailProperties.getProperty().add(subject);
        emailProperties.getProperty().add(headers);
        emailProperties.getProperty().add(bodyTXT);
        emailProperties.getProperty().add(bodyHTML);
        emailProperties.getProperty().add(attachments);

        PropertyAliases emailPropertyAliases = new PropertyAliases();
        emailPropertyAliases.getPropertyAlias().add(to_Alias);
        emailPropertyAliases.getPropertyAlias().add(cc_Alias);
        emailPropertyAliases.getPropertyAlias().add(bcc_Alias);
        emailPropertyAliases.getPropertyAlias().add(fromName_Alias);
        emailPropertyAliases.getPropertyAlias().add(fromAddress_Alias);
        emailPropertyAliases.getPropertyAlias().add(subject_Alias);
        emailPropertyAliases.getPropertyAlias().add(headers_Alias);
        emailPropertyAliases.getPropertyAlias().add(bodyTXT_Alias);
        emailPropertyAliases.getPropertyAlias().add(bodyHTML_Alias);
        emailPropertyAliases.getPropertyAlias().add(attachments_Alias);

        email.getProperties().add(emailProperties);
        email.getPropertyAliases().add(emailPropertyAliases);

        return email;
    }

    public static byte[] getEmailMappingBytes() throws JAXBException {
        WebserviceMapping mapping = getEmailMapping();
        return TransformUtil.mappingToBytes(mapping);
    }

    public static WebserviceMapping getHumanTaskMapping() {
        ObjectFactory factory = new ObjectFactory();
        WebserviceMapping humanTask = factory.createWebserviceMapping();

        // setting HumanTask mapping
        humanTask.setActivity(BPELConstants.Humantask.NAME);
        humanTask.setPortType(HumanTaskInterface.PORT_TYPE_NAME);
        humanTask.setOperation("createTask");
        humanTask.setBinding(HumanTaskInterface.BINDING_NAME);
        PLTInfo partnerlinktype = new PLTInfo();
        partnerlinktype.setName(BPELConstants.Humantask.NAME+"PLT");
        partnerlinktype.setMyRole(BPELConstants.Humantask.NAME+"Client");
        partnerlinktype.setPartnerRole(BPELConstants.Humantask.NAME+"Provider");
        humanTask.setPartnerLinkType(partnerlinktype);
        humanTask.setService(HumanTaskInterface.SERVICE_NAME);
        humanTask.setServicePort(HumanTaskInterface.PORT_NAME);
        
        QName createTaskRequest = new QName(
                HumanTaskInterface.TARGET_NAMESPACE, "createTaskRequest");

        Property wfmID = new Property();
        wfmID.setName(PropertyConstants.Humantask.WFMID);
        wfmID.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"));

        Property processID = new Property();
        processID.setName(PropertyConstants.Humantask.TASKDATA);
        processID.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property userID = new Property();
        userID.setName(PropertyConstants.Humantask.USER);
        userID.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"));

        Property taskName = new Property();
        taskName.setName(PropertyConstants.Humantask.NAME);
        taskName
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property userNotification = new Property();
        userNotification.setName(PropertyConstants.Humantask.USERNOTIFICATION);
        userNotification.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "boolean"));

        Property description = new Property();
        description.setName(PropertyConstants.Humantask.DESCRIPTION);
        description.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property taskData = new Property();
        taskData.setName(PropertyConstants.Humantask.TASKDATA);
        taskData.setType(new QName(Constants.HUMANTASK_NAMESPACE,
                "tHumanTaskData"));

        Property taskID = new Property();
        taskID.setName(PropertyConstants.Humantask.TASKID);
        taskID.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"));

        
        
        PropertyAlias wfmID_Alias = new PropertyAlias();
        wfmID_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, wfmID.getName()));
        wfmID_Alias.setMessageType(createTaskRequest);
        wfmID_Alias.setPart("parameters");
        Query wfmID_Query = new Query();
        wfmID_Query.getContent().add("/wfmID");
        wfmID_Alias.setQuery(wfmID_Query);


        PropertyAlias processID_Alias = new PropertyAlias();
        processID_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, processID.getName()));
        processID_Alias.setMessageType(createTaskRequest);
        processID_Alias.setPart("parameters");
        Query processID_Query = new Query();
        processID_Query.getContent().add("/processID");
        processID_Alias.setQuery(processID_Query);
        
        PropertyAlias userID_Alias = new PropertyAlias();
        userID_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, userID.getName()));
        userID_Alias.setMessageType(createTaskRequest);
        userID_Alias.setPart("parameters");
        Query userID_Query = new Query();
        userID_Query.getContent().add("/userID");
        userID_Alias.setQuery(userID_Query);

        PropertyAlias taskName_Alias = new PropertyAlias();
        taskName_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, taskName.getName()));
        taskName_Alias.setMessageType(createTaskRequest);
        taskName_Alias.setPart("parameters");
        Query taskName_Query = new Query();
        taskName_Query.getContent().add("/taskName");
        taskName_Alias.setQuery(taskName_Query);

        PropertyAlias userNotification_Alias = new PropertyAlias();
        userNotification_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, userNotification
                .getName()));
        userNotification_Alias.setMessageType(createTaskRequest);
        userNotification_Alias.setPart("parameters");
        Query userNotification_Query = new Query();
        userNotification_Query.getContent().add("/userNotification");
        userNotification_Alias.setQuery(userNotification_Query);

        PropertyAlias description_Alias = new PropertyAlias();
        description_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, description.getName()));
        description_Alias.setMessageType(createTaskRequest);
        description_Alias.setPart("parameters");
        Query description_Query = new Query();
        description_Query.getContent().add("/description");
        description_Alias.setQuery(description_Query);
        
        PropertyAlias taskData_Alias = new PropertyAlias();
        taskData_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, taskData.getName()));
        taskData_Alias.setMessageType(createTaskRequest);
        taskData_Alias.setPart("parameters");
        Query taskData_Query = new Query();
        taskData_Query.getContent().add("/taskData");
        taskData_Alias.setQuery(taskData_Query);

        PropertyAlias taskID_Alias = new PropertyAlias();
        taskID_Alias.setPropertyName(new QName(HumanTaskInterface.TARGET_NAMESPACE, taskID.getName()));
        taskID_Alias.setMessageType(new QName(
                HumanTaskInterface.TARGET_NAMESPACE, "createTaskResponse"));
        taskID_Alias.setPart("parameters");
        Query taskID_Query = new Query();
        taskID_Query.getContent().add("/taskID");
        taskID_Alias.setQuery(taskID_Query);

        Properties humanTaskProperties = factory.createProperties();
        humanTaskProperties.getProperty().add(wfmID);
        humanTaskProperties.getProperty().add(processID);
        humanTaskProperties.getProperty().add(userID);
        humanTaskProperties.getProperty().add(taskName);
        humanTaskProperties.getProperty().add(userNotification);
        humanTaskProperties.getProperty().add(description);
        humanTaskProperties.getProperty().add(taskData);
        humanTaskProperties.getProperty().add(taskID);

        PropertyAliases humanTaskPropertyAliases = factory
                .createPropertyAliases();
        humanTaskPropertyAliases.getPropertyAlias().add(wfmID_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(processID_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(userID_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(taskName_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(userNotification_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(description_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(taskData_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(taskID_Alias);

        humanTask.getProperties().add(humanTaskProperties);
        humanTask.getPropertyAliases().add(humanTaskPropertyAliases);

        return humanTask;
    }

    public static byte[] getHumanTaskMappingBytes() throws JAXBException {
        WebserviceMapping mapping = getHumanTaskMapping();
        return TransformUtil.mappingToBytes(mapping);
    }

    public static void main(String main[]) throws JAXBException {
        String emailMappingString = new String(getEmailMappingBytes());
        System.out.println(emailMappingString);
        String humantaskMappingString = new String(getHumanTaskMappingBytes());
        System.out.println(humantaskMappingString);
    }
}