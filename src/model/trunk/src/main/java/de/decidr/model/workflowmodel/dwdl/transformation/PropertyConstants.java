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

package de.decidr.model.workflowmodel.dwdl.transformation;

/**
 * A class that holds all properties used in HumanTask web service and Email web
 * service.
 * 
 * @author Modood Alvi
 */
// MA: You've used that outdated definition of the email an HT WS' again. Please
// update both them and the contents of this file!
public final class PropertyConstants {

    public static class Email {

        /**
         * This property accepts a rolet.
         */
        public static final String TO = "to";

        /**
         * This property accepts an actor.
         */
        public static final String TOACTOR = "toActor";

        /**
         * This property accepts a role
         */
        public static final String CC = "cc";

        /**
         * This property accepts a role
         */
        public static final String BCC = "bcc";

        /**
         * This property accepts simple string name
         */
        public static final String FROM = "fromName";

        /**
         * This property accepts simple string address
         */
        public static final String FROMADRESS = "fromAddress";

        /**
         * The subject of the email
         */
        public static final String SUBJECT = "subject";

        /**
         * The headers of the email
         */
        public static final String HEADERS = "headers";

        /**
         * The message of the email
         */
        public static final String MESSAGE = "message";

        /**
         * The message of the email as HTML
         */
        public static final String MESSAGEHTML = "messageHTML";

        /**
         * This property accepts a file ID
         */
        public static final String ATTACHEMENT = "attachment";
    }

    public static class Humantask {
        /**
         * The ID of the workflow. Usually this property refers to a variable
         * which by default holds the id of the workflow.
         */
        public static final String WFMID = "wfmID";

        /**
         * The current process ID set by Apache ODE
         */
        public static final String PROCESSID = "processID";

        /**
         * The user who has been assigned to this task. This property usually
         * refers to an actor.
         */
        public static final String USER = "user";

        /**
         * The name of the workitem.
         */
        public static final String NAME = "name";

        /**
         * Description of the workitem. Usually this property contains meta
         * inforamtion for users how to carry out the workitem.
         */
        public static final String DESCRIPTION = "description";

        /**
         * This property accepts only „yes“ or „no“. In first case, the user
         * will be informed via email that a new workitem is assigned to him.
         */
        public static final String USERNOTIFICATION = "userNotification";

        /**
         * This property contains the task ID
         */
        public static final String TASKID = "taskID";

        /**
         * This property is set through the parameter element of an invoke node.
         */
        public static final String TASKDESCRIPTION = "taskDescription";

        /**
         * This property contains the result data of a human task. It has to be
         * assigned to a variable from type 'form'. See Section 2 Variable types
         * and names.
         */
        public static final String TASKRESULT = "taskResult";

        /**
         * The actual HumanTask definition as XML
         */
        public static final String TASKDATA = "taskData";
    }

    public static class FaultHandler {

        /**
         * This property sets the message, which will be sent to the recipient.
         */
        public static final String MESSAGE = "message";
    }

    public static class Recipient {

        /**
         * The name of the recipient. This property is optional.
         */
        public static final String NAME = "name";

        /**
         * This property accepts an actor, a role or an email addresses. The
         * email address can be set with the propertyValue element.
         */
        public static final String TO = "to";
    }

    public static class Notification {

        /**
         * The message sent when the workflow completes successfully.
         */
        public static final String MESSAGE = "message";

        /**
         * This property accepts an actor, a role or an email addresses. The
         * email address can be set with the propertyValue element.
         */
        public static final String TO = "to";
    }
}
