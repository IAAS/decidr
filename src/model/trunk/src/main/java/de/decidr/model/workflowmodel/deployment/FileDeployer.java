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
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.ibm.wsdl.xml.WSDLWriterImpl;
import de.decidr.model.workflowmodel.bpel.TProcess;
import de.decidr.model.workflowmodel.dd.TDeployment;

/**
 * This class provides the functionality to deploy the files of a workflow model
 * process on selected servers
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class FileDeployer {

    /**
     * This function expects a list of the server addresses on which the files
     * should be deployed, the name of the process which represents the name of
     * the folder in the ODE and has to be unique for each workflow model, as
     * well as the BPEL, WSDL and Deployment Descriptor as strings. If a process
     * is deployed in Apache ODE, it automatically receives a version number,
     * which is returned by the function deploy. If the function is called, a
     * folder with the passed name is created locally on the server. The passed
     * BPEL, WSDL and Deployment Descriptor are written to files in this folder.
     * This folder is compressed as a zip file. By invoking the Deployment Web
     * service of the Apache ODEs on the given servers the zip file is deployed
     * on all these servers. The returned ODE versions of the processes are
     * collected, after all servers are handled the highest one is returned.
     * After deploying the files on all servers, all files that have been
     * created locally are deleted.
     * 
     * 
     * @param serverList
     *            List of server addresses
     * @param name
     *            Name of the process
     * @param bpel
     *            BPEL representation of the process
     * @param wsdl
     *            WSDL representation of the process
     * @param dd
     *            The Deployment Descriptor
     * @throws JAXBException
     * @throws WSDLException
     * @throws IOException
     * @throws IOException
     */
    public void deploy(List<Long> serverList, String name, TProcess bpel,
            Definition wsdl, TDeployment dd) throws JAXBException,
            WSDLException, IOException {

        byte[] zipFile = getDeploymentBundle(name, bpel, wsdl, dd);
        System.out.println(zipFile[0]);

    }

    private byte[] getDeploymentBundle(String name, TProcess bpel,
            Definition wsdl, TDeployment dd) throws JAXBException,
            WSDLException, IOException {
        JAXBContext bpelCntxt = JAXBContext.newInstance(TProcess.class);
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
        zip_out_stream.close();

        return zipOut.toByteArray();
    }

}
