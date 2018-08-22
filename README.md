An <a href="https://jfrog.com/artifactory/" rel="nofollow">Artifactory</a> client that finds the most downloaded artifacts on a Maven repository.

The client is packaged as a Rest web service that will return the most downloaded artifacts on a Maven repository. 
Behind the scenes the web service has a rest client that calls Artifactory's Rest apis to find download statistics on stored Maven artifacts. The client is using the <a href="https://www.jfrog.com/confluence/display/RTF/Artifactory+Query+Language" rel="nofollow">Artifactory AQL</a> to extract the necessary data.

This is a Java project which is using Maven for packaging and Spring Boot to expose the web services and also quering Artifactory. The project can be packaged with Maven as a war file using Maven's 'mvn clean package' command.

Using the most downloaded web service:

Web Service URL:</br>
http://{<b>host</b>}:{<b>port</b>}/{<b>contextPath</b>}/mostdownloaded?targetHost={<b>targetHost</b>}&mavenRepositoryName={<b>mavenRepositoryName</b>}&limit={<b>limit</b>}

Where:</br>
<b>host</b> - is the host where we deploy our solution.</br>
<b>port</b> - the container port, normally port 8080.</br>
<b>contextPath</b> - the web application's context path. Will be usually the war name or it's alias name. For example, on Tomcat the context path can be defined on the server.xml file.</br>
<b>targetHost</b> - (optional query param) the target Artifactory instance ip address on which we query.</br>
<b>mavenRepositoryName</b> - (optional query param) the target Maven repository name on the given Artifactory instace that we want to query.</br>
<b>limit</b> - (optional query param) pagination limit to match the limit when submitting a call to the Artifactory instance. The defualt value is 1000.</br></br>
The query params targetHost, mavenRepositoryName and limit are optional and their default values can be configured at the <b>src/main/resources/config/AppConfig.json</b> application configuration file.
