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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.wsdl.Definition;
import javax.wsdl.WSDLException;
import javax.wsdl.xml.WSDLWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMText;
import org.apache.axiom.om.util.Base64;
import org.apache.axis2.AxisFault;
import org.apache.ode.axis2.service.ServiceClientUtil;
import org.apache.ode.utils.Namespaces;

import java.io.InputStream;
import java.util.Iterator;

import com.ibm.wsdl.xml.WSDLWriterImpl;
import de.decidr.model.DecidrGlobals;
import de.decidr.model.entities.DeployedWorkflowModel;
import de.decidr.model.entities.KnownWebService;
import de.decidr.model.entities.Server;
import de.decidr.model.entities.ServerLoadView;
import de.decidr.model.workflowmodel.bpel.Process;
import de.decidr.model.workflowmodel.dd.TDeployment;
import de.decidr.model.workflowmodel.dwdl.translator.Translator;
import de.decidr.model.workflowmodel.dwdl.validation.IProblem;
import de.decidr.model.workflowmodel.dwdl.validation.Validator;

/**
 * This class provides an interface for other components to access the
 * functionality to deploy a workflow instance.
 * 
 * @author Modood Alvi
 * @version 0.1
 */
public class DeployerImpl implements Deployer {

    private List<ServerLoadView> prefferedServers = null;
    private List<Long> serverList = new ArrayList<Long>();
    private Validator validator = null;
    private List<IProblem> problems = null;
    private Translator translator = null;
    private FileDeployer fileDeployer = null;
    private DeploymentResult result = null;
    private ServiceClientUtil _client;
    private Object _factory;

    /*
     * (non-Javadoc)
     * 
     * @see de.decidr.model.workflowmodel.deployment.Deployer#deploy(byte[],
     * java.lang.String, java.util.List,
     * de.decidr.model.workflowmodel.deployment.DeploymentStrategy)
     */
    @Override
    public DeploymentResult deploy(byte[] dwdl, List<KnownWebService> knownWebservices, String tenantName,
            List<ServerLoadView> serverStatistics, DeploymentStrategy strategy)
            throws DWDLValidationException, ODESelectorException, IOException,
            JAXBException, WSDLException {
        try {
            deploy("SampleProcess");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

//        validator = new Validator();
//        problems = validator.validate(dwdl);
//        if (!problems.isEmpty()) {
//            throw new DWDLValidationException(problems);
//        }
//        prefferedServers = strategy.selectServer(serverStatistics);
//        if (prefferedServers.isEmpty()) {
//            throw new ODESelectorException(serverStatistics);
//        }
//        translator = new Translator();
//        translator.load(dwdl, tenantName, knownWebservices);
//        Process bpel = translator.getBPEL();
//        byte[] soap = translator.getSOAP(); 
//        Definition wsdl = null;
//        TDeployment dd = null;
//        fileDeployer = new FileDeployer();
//        for (ServerLoadView server : prefferedServers){
//            wsdl = translator.getWSDL(server.getLocation(), tenantName);
//            dd = translator.getDD(bpel);
//            byte[] zipFile = getDeploymentBundle(tenantName, bpel, wsdl, dd);
//            fileDeployer.deploy(zipFile, server.getLocation());
//            serverList.add(server.getId());
//        }
        result = new DeploymentResultImpl();
        result.setDoplementDate(DecidrGlobals.getTime().getTime());
        byte[] soap = "soap test".getBytes();
        serverList.add(serverStatistics.get(0).getId());
        result.setSOAPTemplate(soap);
        result.setServers(serverList);

        return result;
    }
    
    private void setUp(){
        _factory = OMAbstractFactory.getOMFactory();
        _client = new ServiceClientUtil();
    }
    
    public String deploy(String packageName) throws Exception {
        
        setUp();
        
        // Use the factory to create three elements
        OMNamespace depns = ((OMFactory) _factory).createOMNamespace(Namespaces.ODE_DEPLOYAPI_NS, "deployapi");
        OMElement root = ((OMFactory) _factory).createOMElement("deploy", depns);
        OMElement namePart = ((OMFactory) _factory).createOMElement("name", null);
        namePart.setText(packageName);
        OMElement zipPart = ((OMFactory) _factory).createOMElement("package", null);
        OMElement zipElmt = ((OMFactory) _factory).createOMElement("zip", depns);

        // Add the zip to deploy
        //InputStream is = this.getClass().getClassLoader().getResourceAsStream("DynPartner.zip");
        InputStream is = new FileInputStream("SampleProcess.zip");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int len;
        while((len = is.read(buffer)) >= 0) {
            outputStream.write(buffer, 0, len);
        }
        String base64Enc = Base64.encode(outputStream.toByteArray());
        OMText zipContent = ((OMFactory) _factory).createOMText(base64Enc, "application/zip", true);
        root.addChild(namePart);
        root.addChild(zipPart);
        zipPart.addChild(zipElmt);
        zipElmt.addChild(zipContent);

        // Deploy
        OMElement result = sendToDeployment(root);

        String pakage = null;
        Iterator<OMElement> iter = result.getFirstElement().getChildElements();
        while (iter.hasNext()) {
            OMElement e = iter.next();
            if (e.getLocalName().equals("name")) {
                pakage = e.getText();
            }
        }
        return pakage;
    }

    
    private OMElement sendToDeployment(OMElement msg) throws AxisFault {
        return _client.send(msg, "http://ec2-174-129-24-232.compute-1.amazonaws.com:8080/ode/processes/DeploymentService");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.decidr.model.workflowmodel.deployment.Deployer#undeploy(de.decidr.
     * model.entities.DeployedWorkflowModel, de.decidr.model.entities.Server)
     */
    @Override
    public void undeploy(DeployedWorkflowModel dwfm, Server server)
            throws Exception {
    }
    
    private byte[] getDeploymentBundle(String name, Process bpel,
            Definition wsdl, TDeployment dd) throws JAXBException,
            WSDLException, IOException {
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
