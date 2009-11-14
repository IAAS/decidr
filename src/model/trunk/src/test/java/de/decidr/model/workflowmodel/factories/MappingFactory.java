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

import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;
import de.decidr.model.workflowmodel.webservices.ObjectFactory;
import de.decidr.model.workflowmodel.webservices.Properties;
import de.decidr.model.workflowmodel.bpel.varprop.Property;
import de.decidr.model.workflowmodel.bpel.varprop.PropertyAlias;
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
        email.setPortType("EmailPT");
        email.setOperation("sendEmail");
        email.setBinding("EmailSOAP");
        email.setPartnerLinkTyp("EmailPLT");
        email.setService("Email");
        email.setServicePort("EmailSOAP");
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

        Property subject = new Property();
        subject.setName("subject");
        subject
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property message = new Property();
        message.setName("message");
        message
                .setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property attachement = new Property();
        attachement.setName("attachement");
        attachement.setType(new QName("http://decidr.de/schema/DecidrTypes",
                "tIDList"));

        PropertyAlias to_Alias = new PropertyAlias();
        to_Alias.setPropertyName(new QName(to.getName()));
        to_Alias.setMessageType(new QName("sendEmailRequest"));
        to_Alias.setType(to.getType());

        PropertyAlias cc_Alias = new PropertyAlias();
        cc_Alias.setPropertyName(new QName(cc.getName()));
        cc_Alias.setMessageType(new QName("sendEmailRequest"));
        cc_Alias.setType(cc.getType());

        PropertyAlias bcc_Alias = new PropertyAlias();
        bcc_Alias.setPropertyName(new QName(bcc.getName()));
        bcc_Alias.setMessageType(new QName("sendEmailRequest"));
        bcc_Alias.setType(bcc.getType());

        PropertyAlias subject_Alias = new PropertyAlias();
        subject_Alias.setPropertyName(new QName(subject.getName()));
        subject_Alias.setMessageType(new QName("sendEmailRequest"));
        subject_Alias.setType(subject.getType());

        PropertyAlias message_Alias = new PropertyAlias();
        message_Alias.setPropertyName(new QName(message.getName()));
        message_Alias.setMessageType(new QName("sendEmailRequest"));
        message_Alias.setType(message.getType());

        PropertyAlias attachement_Alias = new PropertyAlias();
        attachement_Alias.setPropertyName(new QName(attachement.getName()));
        attachement_Alias.setMessageType(new QName("sendEmailRequest"));
        attachement_Alias.setType(attachement.getType());

        Properties emailProperties = new Properties();
        emailProperties.getProperty().add(to);
        emailProperties.getProperty().add(cc);
        emailProperties.getProperty().add(bcc);
        emailProperties.getProperty().add(subject);
        emailProperties.getProperty().add(message);
        emailProperties.getProperty().add(attachement);

        PropertyAliases emailPropertyAliases = new PropertyAliases();
        emailPropertyAliases.getPropertyAlias().add(to_Alias);
        emailPropertyAliases.getPropertyAlias().add(cc_Alias);
        emailPropertyAliases.getPropertyAlias().add(bcc_Alias);
        emailPropertyAliases.getPropertyAlias().add(subject_Alias);
        emailPropertyAliases.getPropertyAlias().add(message_Alias);
        emailPropertyAliases.getPropertyAlias().add(attachement_Alias);

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
        humanTask.setPortType("HumanTaskPT");
        humanTask.setOperation("createTask");
        humanTask.setBinding("HumanTaskSOAP");
        humanTask.setPartnerLinkTyp("HumanTaskPLT");
        humanTask.setService("HumanTask");
        humanTask.setServicePort("HumanTaskSOAP");

        Property wfmID = new Property();
        wfmID.setName("wfmID");
        wfmID.setType(new QName("http://decidr.de/schema/DecidrTypes", "tID"));

        Property user = new Property();
        user.setName("user");
        user.setType(new QName("http://decidr.de/schema/DecidrTypes", "tID"));

        Property name = new Property();
        name.setName("name");
        name.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI, "string"));

        Property description = new Property();
        description.setName("description");
        description.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property userNotification = new Property();
        userNotification.setName("userNotification");
        userNotification.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "boolean"));

        Property taskDescription = new Property();
        taskDescription.setName("taskDescription");
        taskDescription.setType(new QName(XMLConstants.W3C_XML_SCHEMA_NS_URI,
                "string"));

        Property taskResult = new Property();
        taskResult.setName("taskResult");
        taskResult.setType(new QName("nochtnichtdefiniert"));

        PropertyAlias wfmID_Alias = new PropertyAlias();
        wfmID_Alias.setPropertyName(new QName(wfmID.getName()));
        wfmID_Alias.setMessageType(new QName("createTaskRequest"));
        wfmID_Alias.setType(wfmID.getType());

        PropertyAlias user_Alias = new PropertyAlias();
        user_Alias.setPropertyName(new QName(user.getName()));
        user_Alias.setMessageType(new QName("createTaskRequest"));
        user_Alias.setType(wfmID.getType());

        PropertyAlias name_Alias = new PropertyAlias();
        name_Alias.setPropertyName(new QName(name.getName()));
        name_Alias.setMessageType(new QName("createTaskRequest"));
        name_Alias.setType(name.getType());

        PropertyAlias description_Alias = new PropertyAlias();
        description_Alias.setPropertyName(new QName(description.getName()));
        description_Alias.setMessageType(new QName("createTaskRequest"));
        description_Alias.setType(description.getType());

        PropertyAlias userNotification_Alias = new PropertyAlias();
        userNotification_Alias.setPropertyName(new QName(user.getName()));
        userNotification_Alias.setMessageType(new QName("createTaskRequest"));
        userNotification_Alias.setType(userNotification.getType());

        PropertyAlias taskDescription_Alias = new PropertyAlias();
        taskDescription_Alias.setPropertyName(new QName(taskDescription
                .getName()));
        taskDescription_Alias.setMessageType(new QName("createTaskRequest"));
        taskDescription_Alias.setType(taskDescription.getType());

        PropertyAlias taskResult_Alias = new PropertyAlias();
        taskResult_Alias.setPropertyName(new QName(taskResult.getName()));
        taskResult_Alias.setMessageType(new QName("createTaskRequest"));
        taskResult_Alias.setType(taskResult.getType());

        Properties humanTaskProperties = factory.createProperties();
        humanTaskProperties.getProperty().add(wfmID);
        humanTaskProperties.getProperty().add(user);
        humanTaskProperties.getProperty().add(name);
        humanTaskProperties.getProperty().add(description);
        humanTaskProperties.getProperty().add(userNotification);
        humanTaskProperties.getProperty().add(taskDescription);
        humanTaskProperties.getProperty().add(taskResult);

        PropertyAliases humanTaskPropertyAliases = factory
                .createPropertyAliases();
        humanTaskPropertyAliases.getPropertyAlias().add(wfmID_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(user_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(name_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(description_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(userNotification_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(taskDescription_Alias);
        humanTaskPropertyAliases.getPropertyAlias().add(taskResult_Alias);

        humanTask.getProperties().add(humanTaskProperties);
        humanTask.getPropertyAliases().add(humanTaskPropertyAliases);

        return humanTask;

    }

    public static byte[] getHumanTaskMappingBytes() throws JAXBException {
        WebserviceMapping mapping = getHumanTaskMapping();
        return TransformUtil.mappingToBytes(mapping);
    }
}