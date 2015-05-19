# HowTo Install Apache Tomcat 5.5.27, Set Up Apache Axis2 and Deploy a Web Service Locally #

## Apache Tomcat 5.5.27 Installation ##

  1. Download [Apache Tomcat 5.5.27](http://tomcat.apache.org/download-55.cgi#5.5.27) (download core distribution)
  1. Unzip/Install Tomcat to a directory of your choice. I will call this directory `$TOMCAT_HOME` from here on.
    * On Linux and Mac OS X, make sure that the shell-scripts in `$TOMCAT_HOME/bin/` are executable if you didn't use the .tar.gz distribution. To do this, you can execute `chmod a+x $TOMCAT_HOME/bin/*.sh`.
  1. Execute `$TOMCAT_HOME/bin/startup.sh` or `$TOMCAT_HOME/bin/startup.bat` respectively.
  1. Chech the logfiles in `$TOMCAT_HOME/logs` to make sure that no problems occured during startup.
  1. Execute `$TOMCAT_HOME/bin/shutdown.sh` or `$TOMCAT_HOME/bin/shutdown.bat` respectively.

## Apache Axis2 Deployment and Configuration ##

### Deployment ###

  1. Download [Apache Axis2 1.4.1](http://ws.apache.org/axis2/download/1_4_1/download.cgi) (download WAR distribution)
  1. Unzip `axis2.war` and copy it to `$TOMCAT_HOME/webapps`.
  1. Execute `$TOMCAT_HOME/bin/startup.sh` or `$TOMCAT_HOME/bin/startup.bat` respectively.
  1. Chech the logfiles in `$TOMCAT_HOME/logs` to make sure that no problems occured during startup.
  1. Execute `$TOMCAT_HOME/bin/shutdown.sh` or `$TOMCAT_HOME/bin/shutdown.bat` respectively.
  1. There should now be a `$TOMCAT_HOME/webapps/axis2`. I will call this directory `$AXIS_HOME` from here on.

### Configuration ###

  1. Open `$AXIS_HOME/WEB-INF/conf/axis2.xml` in your favourite text or XML editor.
    * Alternatively, you can start Tomcat and navigate to http://127.0.0.1:8080/axis2/axis2-admin/. The default username and password are `admin` and `axis2`.<br><b><i>WARNING:</i></b> Any changes made here will be lost when the server is shut down/restarted!<br>
</li></ul><ol><li>Change <code>&lt;parameter name="hotdeployment"&gt;false&lt;/parameter&gt;</code> to <code>&lt;parameter name="hotdeployment"&gt;true&lt;/parameter&gt;</code>.<br>
</li><li>Change <code>&lt;parameter name="hotupdate"&gt;false&lt;/parameter&gt;</code> to <code>&lt;parameter name="hotupdate"&gt;true&lt;/parameter&gt;</code>.<br>
</li><li>You will now be able to deploy and update Web Services while Axis2 is running.<br>
</li><li>You may want to change the <code>userName</code> and <code>password</code> parameters.</li></ol>

<h2>Deploying a Sample Web Service ##

  1. You may use your own Web Service for this or the [sample Web Service](http://code.google.com/p/decidr/source/browse/trunk/docs/misc/mailws.jar?r=548) provided.
  1. Simply change the `.jar` to `.aar` (e.g. the sample will become `mailws.aar`).
  1. Copy the `.aar` file to `$AXIS_HOME/WEB-INF/services/`.
  1. Start Axis2, if it isn't already running.
  1. Navigate to http://127.0.0.1:8080/axis2/services/listServices. Your Web Service should appear on this page.