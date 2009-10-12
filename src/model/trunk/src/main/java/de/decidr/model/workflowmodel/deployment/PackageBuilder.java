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

import java.io.ByteArrayInputStream;
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
import javax.wsdl.xml.WSDLReader;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.xml.sax.InputSource;

import com.ibm.wsdl.xml.WSDLReaderImpl;
import com.ibm.wsdl.xml.WSDLWriterImpl;

import de.decidr.model.entities.KnownWebService;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;

/**
 * This class builds the package, i.e. the zip file needed for deployment
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class PackageBuilder {
    
    private Class<PackageBuilder> resourceClass = PackageBuilder.class;
    private final String DECIDRTYPES_LOCATION = "resources/xsd/DecidrProcessTypes.xsd";
    private final String DECIDRTYPES_NAME = "DecidrProcessTypes";

    public byte[] getPackage(String name, Process bpel, Definition wsdl,
            TDeployment dd, List<KnownWebService> knownWebservices)
            throws JAXBException, WSDLException, IOException {
        
        JAXBContext bpelCntxt = JAXBContext.newInstance(Process.class);
        JAXBContext ddCntxt = JAXBContext.newInstance(TDeployment.class);
        Marshaller bpelMarshaller = bpelCntxt.createMarshaller();
        Marshaller ddMarshaller = ddCntxt.createMarshaller();

        ByteArrayOutputStream bpelOut = new ByteArrayOutputStream();
        ByteArrayOutputStream ddOut = new ByteArrayOutputStream();
        ByteArrayOutputStream wsdlOut = new ByteArrayOutputStream();

        ByteArrayOutputStream zipOut = new ByteArrayOutputStream();

        WSDLWriter wsdlWriter = new WSDLWriterImpl();

        String bpelFilename = name + ".bpel";
        String wsdlFilename = name + ".wsdl";
        String ddFilename = "deploy.xml";
        String decidrTypesFilename = DECIDRTYPES_NAME + ".xsd";

        bpelMarshaller.marshal(bpel, bpelOut);
        ddMarshaller.marshal(dd, ddOut);
        wsdlWriter.writeWSDL(wsdl, wsdlOut);

        ZipOutputStream zip_out_stream = new ZipOutputStream(zipOut);
        zip_out_stream.putNextEntry(new ZipEntry(bpelFilename));
        zip_out_stream.write(bpelOut.toByteArray());
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(wsdlFilename));
        zip_out_stream.write(wsdlOut.toByteArray());
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(ddFilename));
        zip_out_stream.write(ddOut.toByteArray());
        zip_out_stream.closeEntry();
        zip_out_stream.putNextEntry(new ZipEntry(decidrTypesFilename));
        zip_out_stream.write(getTypes());
        zip_out_stream.closeEntry();
        Map<KnownWebService, Definition> definitions = retrieveWSDLs(knownWebservices);
        for (KnownWebService webservice : knownWebservices) {
            zip_out_stream.putNextEntry(new ZipEntry(definitions
                    .get(webservice).getQName().getLocalPart()
                    + "wsdl"));
            zip_out_stream.write(webservice.getWsdl());
            zip_out_stream.closeEntry();
        }
        zip_out_stream.close();

        return zipOut.toByteArray();

    }

    private Map<KnownWebService, Definition> retrieveWSDLs(
            List<KnownWebService> webservices) throws WSDLException {
        Map<KnownWebService, Definition> definitions = new HashMap<KnownWebService, Definition>();
        WSDLReader wsdlReader = new WSDLReaderImpl();
        ByteArrayInputStream inStream = null;
        InputSource in = null;
        for (KnownWebService webservice : webservices) {
            inStream = new ByteArrayInputStream(webservice.getWsdl());
            in = new InputSource(inStream);
            Definition webserviceDefinition = wsdlReader.readWSDL(null, in);
            definitions.put(webservice, webserviceDefinition);
        }
        return definitions;
    }
    
    private byte[] getTypes() throws IOException {
        InputStream in = resourceClass.getResourceAsStream(DECIDRTYPES_LOCATION);
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[512];
        int readBytes;
        while ((readBytes = in.read(buffer)) > 0) {
            out.write(buffer, 0, readBytes);
        }
        byte[] byteData = out.toByteArray();

        in.close();
        out.close();
     
        return byteData;
    }

}
