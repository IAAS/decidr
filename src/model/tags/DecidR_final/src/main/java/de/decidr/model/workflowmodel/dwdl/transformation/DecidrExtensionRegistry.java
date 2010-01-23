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

import javax.wsdl.Definition;
import javax.xml.namespace.QName;

import com.ibm.wsdl.extensions.PopulatedExtensionRegistry;

import de.decidr.model.workflowmodel.bpel.partnerlinktype.PartnerLinkType;
import de.decidr.model.workflowmodel.bpel.varprop.Property;
import de.decidr.model.workflowmodel.bpel.varprop.PropertyAlias;
import de.decidr.model.workflowmodel.webservices.PartnerLinkTypeSerializer;
import de.decidr.model.workflowmodel.webservices.PropertyAliasSerializer;
import de.decidr.model.workflowmodel.webservices.PropertySerializer;

/**
 * This class extends the {@link PopulatedExtensionRegistry} class and adds
 * {@link PartnerLinkTypeSerializer}, {@link PropertySerializer} and
 * {@link PropertyAliasSerializer}
 * 
 * @author Modood Alvi
 */
public class DecidrExtensionRegistry extends PopulatedExtensionRegistry {

    private static final long serialVersionUID = 1L;

    public DecidrExtensionRegistry() {
        super();
        PartnerLinkTypeSerializer partnerlinkSerializer = new PartnerLinkTypeSerializer();
        PropertySerializer propertySerializer = new PropertySerializer();
        PropertyAliasSerializer propertyAliasSerializer = new PropertyAliasSerializer();
        registerSerializer(Definition.class, new QName(
                Constants.PARTNERLINKTYPE_NAMESPACE, "partnerLinkType"),
                partnerlinkSerializer);
        registerDeserializer(Definition.class, new QName(
                Constants.PARTNERLINKTYPE_NAMESPACE, "partnerLinkType"),
                partnerlinkSerializer);
        mapExtensionTypes(Definition.class, new QName(
                Constants.PARTNERLINKTYPE_NAMESPACE, "partnerLinkType"),
                PartnerLinkType.class);
        registerSerializer(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "property"), propertySerializer);
        registerDeserializer(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "property"), propertySerializer);
        mapExtensionTypes(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "property"), Property.class);
        registerSerializer(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "propertyAlias"),
                propertyAliasSerializer);
        registerDeserializer(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "propertyAlias"),
                propertyAliasSerializer);
        mapExtensionTypes(Definition.class, new QName(
                Constants.VARPROP_NAMESPACE, "propertyAlias"),
                PropertyAlias.class);
    }

}
