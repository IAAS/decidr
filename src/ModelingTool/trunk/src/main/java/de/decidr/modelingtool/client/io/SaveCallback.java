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

package de.decidr.modelingtool.client.io;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.InvocationException;

/**
 * TODO: add comment
 *
 * @author Modood Alvi
 * @version 0.1
 */
public class SaveCallback implements AsyncCallback<String>{
    
    @Override
    public void onSuccess(String result) {
        
    }
    
    @Override
    public void onFailure(java.lang.Throwable caught){
        try {
            throw caught;
        } catch (IncompatibleRemoteServiceException e) {
            // this client is not compatible with the server; cleanup and
            // refresh the
            // browser
        } catch (InvocationException e) {
            // the call didn't complete cleanly
        } catch (Throwable e) {
            // last resort -- a very unexpected exception
        }
    }
    
    

}
