# Maven and Hudson Configuration #

## Maven ##
**Eclipse Update Site:**
http://m2eclipse.sonatype.org/update/

## Hudson ##
**Eclipse Update Site:**
http://hudson-eclipse.googlecode.com/svn/trunk/hudson-update/

## Maven Repository Browser ##
**Maven Repository Browser:**
http://www.mvnbrowser.com/index.html

Here you can browse the maven central repository to search for artifacts.


## Maven Repository Manager ##
**Artifactory:**
http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory

The Repository Manager is used for uploading external jars which aren't available in the central maven repository.

**User:** Your forename
**Pwd:** projectname

To use the repository manager you have to modify the project's pom.xml and the settings.xml.

```
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
http://maven.apache.org/xsd/settings-1.0.0.xsd">                       


<servers>
    <server>
      <id>iaassrv7-releases</id>
      <username>artifactory username</username>
      <password>artifactory password</password>
    </server>
  </servers>

<profiles>
      <profile>
            <id>dev</id>
            <repositories>
                  <repository>
                     <id>iaassrv7-releases</id>
                     <name>iaassrv7-releases</name>
                     <url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/Decidr</url>
                  </repository>
                  <repository>
                     <id>iaassrv7-releases</id>
                     <name>iaassrv7-releases</name>
                     <url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/ibiblio</url>
                  </repository>
            </repositories>   
      </profile>
</profiles>
</settings>
```

Create a settings.xml in _homefolder/.m2/_-folder and put the above code into it. To use the repository add this to your pom.xml:

```
<distributionManagement>
    <repository>
        <id>iaassrv7-releases</id>
        <name>iaassrv7-releases</name>
        <url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/Decidr</url>
    </repository>
    <snapshotRepository>
        <id>iaassrv7-snapshots</id>
        <name>iaassrv7-snapshots</name>
        <url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/Decidr</url>
    </snapshotRepository>
</distributionManagement>
  <repositories>
    <repository>
        <id>iaassrv7-releases</id>
        <name>iaassrv7-releases</name>
        <url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/Decidr</url>
    </repository>
    <repository>
    	<id>iaassrv7-releases</id>
    	<name>iaassrv7-releases</name>
    	<url>http://iaassrv7.informatik.uni-stuttgart.de:8081/artifactory/ibiblio</url>
	</repository>
</repositories>
```

Now you can add any dependencies you want.


# Details for Maven configuration #

For some reason there might be problems installing the m2eclipse plugin. There could several reasons for your installation to fail. So the best way is to check every project folder of the maven update site step by step to see if it causes problems. Continue until you have reached the last project folder.


# Details for Hudson Configuration #
If you have downloaded the Hudson plugin for eclipse follow this tutorial to show the hudson projects in eclipse's hudson console:

  1. In eclipse go to Window -> Preferences -> Hudson
  1. In the text field _Hudson base url_ type in the hudson web page:
> > http://iaassrv7.informatik.uni-stuttgart.de:8080/hudson
  1. You can check the URL by clicking the _Check url_ button next to the text field. But you have to be connected to the university network via VPN.
  1. Check the _Use authentication_ checkbox and type in your login and password for the Hudson web page. If you don't have a login yet, go to Hudson and register.
  1. The following three checkboxes can be checked but it isn't necessary.