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

package de.decidr.ui.view.help;

import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

/**
 * This component is a part of the integrated manual
 * and contains information related to workflow modeling
 *
 * @author Geoffrey-Alexeih Heinze
 */
public class GlossaryHelpComponent extends VerticalLayout{

	/**
	 * Serial version uid
	 */
	private static final long serialVersionUID = 1L;
	private Label glossLabel = null;
	
	public GlossaryHelpComponent(){
        setMargin(false, true, true, true);
        
        glossLabel = new Label("<b>What is an activity?</b><br/>"
        		+"An activity is an interaction with the outside world that is executed by the workflow engine, such as sending an e-mail or asking the user to fill out a web form.<br/><br/>"
        		+"<b>Who is a default tenant?</b><br/>"
        		+"The default tenant is a special tenant that every registered user has access to. The super admin is responsible for administrating the default tenant and building its workflow models.<br/><br/>"
        		+"<b>What is a Modeling Tool?</b><br/>"
        		+"Modeling Tool is integrated a tool integrated into the DecidR website that can be used to edit workflow models graphically.<br/><br/>"
        		+"<b>Who is a tenant?</b><br/>"
        		+"Tenants are client organizations using the DecidR application.<br/><br/>"
        		+"<b>Who is a tenant member?</b><br/>"
        		+"Every user who is using DecidR can be a member of one or more tenants. All users are implicitly members of the default tenant. In order to join a tenant, users must first receive an invitation form the respective tenant admin.<br/><br/>"
        		+"<b>What is a Web Service?</b><br/>"
        		+"A Web Service is a software system designed to support interoperable machine-to-machine interaction over a network. Other systems can interact with the Web Service over a network and request for special services provided by the web service.<br/><br/>"
        		+"<b>What is a work item?</b><br/>"
        		+"A work item is a unit of work that must be performed by the user, such as filling out a web form.<br/><br/>"
        		+"<b>What is work list?</b><br/>"
        		+"A work list is a list which contains a number of work items.<br/><br/>"
        		+"<b>What is a workflow?</b><br/>"
        		+"A workflow is a depiction of a sequence of operations, declared as work of a person, work of a simple or complex mechanism, work of a group of persons, work of an organization of staff, or machines.<br/><br/>"
        		+"<b>What is a workflow instance configuration?</b><br/>"
        		+"Workflow instance configurations can assign values to variables in a workflow model. This includes the roles and actors that are defied by a workflow model, which are always left blank during the modeling phase. The workflow configuration makes sure that actual users are assigned to take a certain role in a workflow.<br/><br/>"
        		+"<b>What is a workflow model?</b><br/>"
        		+"Workflow models are representations of workflows of workflows, described by the DecidR workflow definition language (DWDL). Tenant admins can use a graphical editor to create and modify workflow models.<br/><br/>"
        		+"<b>Who is a workflow participant?</b><br/>"
        		+"A workflow participant is a user who takes part in a workflow. He works on human tasks. The workflow admin isn’t automatically a workflow participant, as he may choose not to assign himself to any roles in a workflow instance.",
        		Label.CONTENT_XHTML);
        
        this.addComponent(glossLabel);
        
		
	}
	
}
