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

/**
 * Contains custom templates that must be used when 
 * reverse engineering the DecidR database schema. 
 * The following changes have been made to the standard template:
 * <ul>
 * <li>Set dynamic-insert and dynamic-update to true for all generated entities.</li>
 * <li>Put all column names in backticks so they will be put into dialect-specific quotes.</li>
 * </ul>
 */
package de.decidr.model.reveng.templates;

