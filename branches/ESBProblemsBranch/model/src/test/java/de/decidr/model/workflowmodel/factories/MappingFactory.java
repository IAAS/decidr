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
        email.setActivity("Decidr-Email");
        email.setPortType(EmailInterface.PORT_TYPE_NAME);
        email.setOperation("sendEmail");
        email.setBinding(EmailInterface.BINDING_NAME);
        PLTInfo partnerlinktype = new PLTInfo();
        partnerlinktype.setName("EmailPLT");
        partnerlinktype.setPartnerRole("EmailProvider");
        email.setPartnerLinkType(partnerlinktype);
        email.setService(EmailInterface.SERVICE_NAME);
        email.setServicePort(EmailInterface.PORT_NAME);

        Property to = new Property();
        to.setName("to");
        to.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tAbstractUserList"));

        Property cc = new Property();
        cc.setName("cc");
        cc.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tAbstractUserList"));

        Property bcc = new Property();
        bcc.setName("bcc");
        bcc.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tAbstractUserList"));

        Property fromName = new Property();
        fromName.setName("fromName");
        fromName
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property fromAddress = new Property();
        fromAddress.setName("fromAddress");
        fromAddress.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property subject = new Property();
        subject.setName("subject");
        subject
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property headers = new Property();
        headers.setName("headers");
        headers.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tStringMap"));

        Property bodyTXT = new Property();
        bodyTXT.setName("message");
        bodyTXT
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property bodyHTML = new Property();
        bodyHTML.setName("bodyHTML");
        bodyHTML
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property attachments = new Property();
        attachments.setName("attachments");
        attachments.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tIDList"));

        Query to_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:to");

        Query cc_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:cc");

        Query bcc_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:bcc");

        Query fromName_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:fromName");

        Query fromAddress_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:fromAddress");

        Query subject_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:subject");

        Query headers_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:headers");

        Query bodyTXT_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:bodyTXT");

        Query bodyHTML_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:bodyHTML");

        Query attachments_Query = new Query();
        to_Query.getContent().add("/tns:sendEmail/tns:attachments");

        QName createEmailRequest = new QName(EmailInterface.TARGET_NAMESPACE,
                "sendEmailRequest");
        PropertyAlias to_Alias = new PropertyAlias();
        to_Alias.setPropertyName(new QName(to.getName()));
        to_Alias.setMessageType(createEmailRequest);
        to_Alias.setType(to.getType());
        to_Alias.setQuery(to_Query);

        PropertyAlias cc_Alias = new PropertyAlias();
        cc_Alias.setPropertyName(new QName(cc.getName()));
        cc_Alias.setMessageType(createEmailRequest);
        cc_Alias.setType(cc.getType());
        cc_Alias.setQuery(cc_Query);

        PropertyAlias bcc_Alias = new PropertyAlias();
        bcc_Alias.setPropertyName(new QName(bcc.getName()));
        bcc_Alias.setMessageType(createEmailRequest);
        bcc_Alias.setType(bcc.getType());
        bcc_Alias.setQuery(bcc_Query);

        PropertyAlias fromName_Alias = new PropertyAlias();
        fromName_Alias.setPropertyName(new QName(fromName.getName()));
        fromName_Alias.setMessageType(createEmailRequest);
        fromName_Alias.setType(fromName.getType());
        fromName_Alias.setQuery(fromName_Query);

        PropertyAlias fromAddress_Alias = new PropertyAlias();
        fromAddress_Alias.setPropertyName(new QName(fromAddress.getName()));
        fromAddress_Alias.setMessageType(createEmailRequest);
        fromAddress_Alias.setType(fromAddress.getType());
        fromAddress_Alias.setQuery(fromAddress_Query);

        PropertyAlias subject_Alias = new PropertyAlias();
        subject_Alias.setPropertyName(new QName(subject.getName()));
        subject_Alias.setMessageType(createEmailRequest);
        subject_Alias.setType(subject.getType());
        subject_Alias.setQuery(subject_Query);

        PropertyAlias headers_Alias = new PropertyAlias();
        headers_Alias.setPropertyName(new QName(headers.getName()));
        headers_Alias.setMessageType(createEmailRequest);
        headers_Alias.setType(headers.getType());
        headers_Alias.setQuery(headers_Query);

        PropertyAlias bodyTXT_Alias = new PropertyAlias();
        bodyTXT_Alias.setPropertyName(new QName(bodyTXT.getName()));
        bodyTXT_Alias.setMessageType(createEmailRequest);
        bodyTXT_Alias.setType(bodyTXT.getType());
        bodyTXT_Alias.setQuery(bodyTXT_Query);

        PropertyAlias bodyHTML_Alias = new PropertyAlias();
        bodyHTML_Alias.setPropertyName(new QName(bodyHTML.getName()));
        bodyHTML_Alias.setMessageType(createEmailRequest);
        bodyHTML_Alias.setType(bodyHTML.getType());
        bodyHTML_Alias.setQuery(bodyHTML_Query);

        PropertyAlias attachments_Alias = new PropertyAlias();
        attachments_Alias.setPropertyName(new QName(attachments.getName()));
        attachments_Alias.setMessageType(createEmailRequest);
        attachments_Alias.setType(attachments.getType());
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
        humanTask.setActivity("Decidr-HumanTask");
        humanTask.setPortType(HumanTaskInterface.PORT_TYPE_NAME);
        humanTask.setOperation("createTask");
        humanTask.setBinding(HumanTaskInterface.BINDING_NAME);
        PLTInfo partnerlinktype = new PLTInfo();
        partnerlinktype.setName("HumanTaskPLT");
        partnerlinktype.setMyRole("HumanTaskClient");
        partnerlinktype.setPartnerRole("HumanTaskProvider");
        humanTask.setPartnerLinkType(partnerlinktype);
        humanTask.setService(HumanTaskInterface.SERVICE_NAME);
        humanTask.setServicePort(HumanTaskInterface.PORT_NAME);

        Property wfmID = new Property();
        wfmID.setName("wfmID");
        wfmID.setType(new QName("http://decidr.de/schema/DecidrTypes", "tID"));

        Property processID = new Property();
        processID.setName("processID");
        processID.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tID"));

        Property userID = new Property();
        userID.setName("userID");
        userID.setType(new QName("http://decidr.de/schema/DecidrTypes", "tID"));

        Property taskName = new Property();
        taskName.setName("taskName");
        taskName
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property userNotification = new Property();
        userNotification.setName("userNotification");
        userNotification.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "boolean"));

        Property description = new Property();
        description.setName("description");
        description.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property taskData = new Property();
        taskData.setName("taskData");
        taskData.setType(new QName("http://decidr.de/schema/humanTask",
                "tHumanTaskData"));

        Property taskID = new Property();
        taskID.setName("taskID");
        taskID.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "long"));

        Query wfmID_Query = new Query();
        wfmID_Query.getContent().add("/tns:createTask/tns:wfmID");

        Query processID_Query = new Query();
        processID_Query.getContent().add("/tns:createTask/tns:processID");

        Query userID_Query = new Query();
        userID_Query.getContent().add("/tns:createTask/tns:userID");

        Query taskName_Query = new Query();
        taskName_Query.getContent().add("/tns:createTask/tns:taskName");

        Query userNotification_Query = new Query();
        userNotification_Query.getContent().add("/tns:createTask/tns:userNotification");

        Query description_Query = new Query();
        description_Query.getContent().add("/tns:createTask/tns:description");

        Query taskData_Query = new Query();
        taskData_Query.getContent().add("/tns:createTask/tns:taskData");

        Query taskID_Query = new Query();
        taskID_Query.getContent().add("/tns:createTask/tns:taskID");

        QName createTaskRequest = new QName(
                HumanTaskInterface.TARGET_NAMESPACE, "createTaskRequest");
        PropertyAlias wfmID_Alias = new PropertyAlias();
        wfmID_Alias.setPropertyName(new QName(wfmID.getName()));
        wfmID_Alias.setMessageType(createTaskRequest);
        wfmID_Alias.setType(wfmID.getType());
        wfmID_Alias.setQuery(wfmID_Query);

        PropertyAlias processID_Alias = new PropertyAlias();
        processID_Alias.setPropertyName(new QName(processID.getName()));
        processID_Alias.setMessageType(createTaskRequest);
        processID_Alias.setType(processID.getType());
        processID_Alias.setQuery(processID_Query);

        PropertyAlias userID_Alias = new PropertyAlias();
        userID_Alias.setPropertyName(new QName(userID.getName()));
        userID_Alias.setMessageType(createTaskRequest);
        userID_Alias.setType(userID.getType());
        userID_Alias.setQuery(userID_Query);

        PropertyAlias taskName_Alias = new PropertyAlias();
        taskName_Alias.setPropertyName(new QName(taskName.getName()));
        taskName_Alias.setMessageType(createTaskRequest);
        taskName_Alias.setType(taskName.getType());
        taskName_Alias.setQuery(taskName_Query);

        PropertyAlias userNotification_Alias = new PropertyAlias();
        userNotification_Alias.setPropertyName(new QName(userNotification
                .getName()));
        userNotification_Alias.setMessageType(createTaskRequest);
        userNotification_Alias.setType(userNotification.getType());
        userNotification_Alias.setQuery(userNotification_Query);

        PropertyAlias description_Alias = new PropertyAlias();
        description_Alias.setPropertyName(new QName(description.getName()));
        description_Alias.setMessageType(createTaskRequest);
        description_Alias.setType(description.getType());
        description_Alias.setQuery(description_Query);

        PropertyAlias taskData_Alias = new PropertyAlias();
        taskData_Alias.setPropertyName(new QName(taskData.getName()));
        taskData_Alias.setMessageType(createTaskRequest);
        taskData_Alias.setType(taskData.getType());
        taskData_Alias.setQuery(taskData_Query);

        PropertyAlias taskID_Alias = new PropertyAlias();
        taskID_Alias.setPropertyName(new QName(taskID.getName()));
        taskID_Alias.setMessageType(new QName(
                HumanTaskInterface.TARGET_NAMESPACE, "createTaskResponse"));
        taskID_Alias.setType(taskID.getType());
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
}