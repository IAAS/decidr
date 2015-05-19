# Step-by-Step Guide #

## Setup ##
  1. Configure Eclipse as described in EclipseConfiguration.
  1. Open the preferences window. (Window -> Preferences)
  1. Set up and configure the "Server Runtime Environments".
  1. Download `axis2-1.4.1-bin.zip` from the `mailws` project in SVN and unzip it to a directory of your choice.
  1. Open and configure the "Axis2 Preferences" and the "Server and Runtime" found in the "Web Services" category:
    * In "Axis2 Preferences", set "Axis2 Runtime location" to the directory unzipped in step 4.
    * In "Server and Runtime", set "Server" to "Tomcat v5.5 Server" and "Web service runtime" to "Apache Axis2"

## Create a Web Service using Axis2 ##
  1. Create a new "Dynamic Web Project".
    * Modify the preselected configuration by selecting the Axis2 facet
  1. Create the Web Service (or at least some class annotated with `@WebService`).
  1. Open "File -> New -> Other..." and select "Web Services -> Web Service".
  1. Choose "Bottom up Java bean Web Service" and select the class containing your web service.
  1. Make sure the links in "Configuration" say "Server: Tomcat v5.5 Server", "Web service runtime: Apache Axis2" and the name of the "Dynamic Web Project" containing the web service in "Service project".
  1. Click on "Next >".
  1. Select the appropriate option and click on "Next >".
  1. Click on "Start server" and then on "Finish".

# Important Notes #
  * If you don't create a "Dynamic Web Project", the Axis2 integration will fail, even though the user interface suggests this possibility.
  * NEVER EVER use the internal server configuration eclipse offers! The axis2 this uses is buggy as hell and has cost me > half a week of trouble-shooting!