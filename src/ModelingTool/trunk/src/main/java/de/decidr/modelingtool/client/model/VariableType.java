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

package main.java.de.decidr.modelingtool.client.model;

/**
 * TODO: add comment
 *
 * @author JS
 */
public enum VariableType {

	// TODO: Internationalization
	STRING("String"),
	INTEGER("Integer"),
	FLOAT("Float"),
	BOOLEAN("Boolean"),
	FILE("File"),
	DATE("Date"),
	ROLE("Role");

	private final String name;

	private VariableType(String name) {
		this.name = name;		
	}
	
	public String getName() {
		return name;
	}
}
