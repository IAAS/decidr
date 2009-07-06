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

package de.decidr.modelingtool.client.model.variable;

import de.decidr.modelingtool.client.ModelingTool;

/**
 * TODO: add comment
 * 
 * @author Jonas Schlaak
 */
public enum VariableType {

	STRING(ModelingTool.messages.typeString()), INTEGER(ModelingTool.messages
			.typeInteger()), FLOAT(ModelingTool.messages.typeFloat()), BOOLEAN(
			ModelingTool.messages.typeBoolean()), FILE(ModelingTool.messages
			.typeFile()), DATE(ModelingTool.messages.typeDate()), ROLE(
			ModelingTool.messages.typeRole()), FORM(ModelingTool.messages
			.typeForm());

	private final String name;

	private VariableType(String name) {
		this.name = name;
	}

	public String getLocalName() {
		return name;
	}

	public static VariableType getTypeFromLocalName(String localName) {
		VariableType result = null;
		for (VariableType t : VariableType.values()) {
			if (localName == t.getLocalName())
				result = t;
		}
		return result;
	}
}