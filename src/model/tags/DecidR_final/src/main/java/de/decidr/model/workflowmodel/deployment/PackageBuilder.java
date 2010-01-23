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

package de.decidr.model.workflowmodel.deployment;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.xml.bind.JAXBException;

import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.transformation.Constants;
import de.decidr.model.workflowmodel.dwdl.transformation.TransformUtil;

/**
 * This class builds the package, i.e. the zip file needed for deployment
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class PackageBuilder {

    private final String DECIDRTYPES_URI = Constants.DOCUMENT_BASE_DIRECTORY
            + Constants.DECIDRTYPES_LOCATION;
    private final String DECIDRWSTYPES_URI = Constants.DOCUMENT_BASE_DIRECTORY
            + Constants.DECIDRWSTYPES_LOCATION;
    private final String DECIDRPROCESSTYPES_URI = Constants.DOCUMENT_BASE_DIRECTORY
            + Constants.DECIDRPROCESSTYPES_LOCATION;

    public byte[] getPackage(String name, Process bpel, Definition wsdl,
            TDeployment dd, List<KnownWebService> knownWebservices)
            throws JAXBException, WSDLException, IOException {

        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();

        String bpelFilename = name + ".bpel";
        String wsdlFilename = name + ".wsdl";
        String ddFilename = "deploy.xml";

        ZipOutputStream zip_out_stream = new ZipOutputStream(zipOut);
        zip_out_stream.putNextEntry(new ZipEntry(bpelFilename));
        zip_out_stream.write(TransformUtil.bpelToBytes(bpel));
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(wsdlFilename));
        zip_out_stream.write(TransformUtil.definitionToBytes(wsdl));
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(ddFilename));
        zip_out_stream.write(TransformUtil.ddToBytes(dd));
        zip_out_stream.closeEntry();
        zip_out_stream
                .putNextEntry(new ZipEntry(Constants.DECIDRTYPES_LOCATION));
        zip_out_stream.write(loadSchema(DECIDRTYPES_URI));
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(
                Constants.DECIDRWSTYPES_LOCATION));
        zip_out_stream.write(loadSchema(DECIDRWSTYPES_URI));
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(
                Constants.DECIDRPROCESSTYPES_LOCATION));
        zip_out_stream.write(loadSchema(DECIDRPROCESSTYPES_URI));
        zip_out_stream.closeEntry();
        Map<KnownWebService, Definition> definitions = retrieveWSDLs(knownWebservices);
        for (KnownWebService webservice : knownWebservices) {
            zip_out_stream.putNextEntry(new ZipEntry(definitions
                    .get(webservice).getQName().getLocalPart()
                    + ".wsdl"));
            zip_out_stream.write(DecidrGlobals.getWebServiceWsdl(webservice
                    .getName()));
            zip_out_stream.closeEntry();
        }
        zip_out_stream.close();

        return zipOut.toByteArray();

    }

    private byte[] loadSchema(String location) throws IOException {
        InputStream in = PackageBuilder.class.getResourceAsStream(location);
        byte[] data = new byte[in.available()];
        in.read(data, 0, in.available());

        return data;
    }

    private Map<KnownWebService, Definition> retrieveWSDLs(
            List<KnownWebService> webservices) throws WSDLException {
        Map<KnownWebService, Definition> definitions = new HashMap<KnownWebService, Definition>();
        for (KnownWebService webservice : webservices) {
            Definition webserviceDefinition = TransformUtil
                    .bytesToDefinition(DecidrGlobals
                            .getWebServiceWsdl(webservice.getName()));
            definitions.put(webservice, webserviceDefinition);
        }
        return definitions;
    }

}
