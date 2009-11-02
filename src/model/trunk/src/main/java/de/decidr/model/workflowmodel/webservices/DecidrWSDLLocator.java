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

package de.decidr.model.workflowmodel.webservices;

import javax.wsdl.xml.WSDLLocator;

import org.xml.sax.InputSource;

/**
 * This class implements the <code>WSDLLocator</code> interface to locate all
 * relevant schema elements in the Decidr web service WSDLs
 * 
 * @author Modood Alvi
 */
public class DecidrWSDLLocator implements WSDLLocator {

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.xml.WSDLLocator#close()
     */
    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.xml.WSDLLocator#getBaseInputSource()
     */
    @Override
    public InputSource getBaseInputSource() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.xml.WSDLLocator#getBaseURI()
     */
    @Override
    public String getBaseURI() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.xml.WSDLLocator#getImportInputSource(java.lang.String,
     * java.lang.String)
     */
    @Override
    public InputSource getImportInputSource(String parentLocation,
            String importLocation) {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.wsdl.xml.WSDLLocator#getLatestImportURI()
     */
    @Override
    public String getLatestImportURI() {
        // TODO Auto-generated method stub
        return null;
    }

}
