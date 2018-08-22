An Artifactory client that finds the most downloaded artifacts on a Maven repository.

The client is packaged as a Rest web application that will return the most downloaded artifacts on a Maven repository, after calling behind the scenes to Artifactory's Rest apis to find the download statistics on the stored Maven artifacts. The client is using the Artifactory AQL to query the necessary data.

This is a Java project which is using Maven and Spring Boot to package and expose the web services and also quering Artifactory. The project can be packaged with Maven as a war file using Maven's 'mvn clean package' command.

Using the most downloaded web service:

Web Service URL:

http://{host}:{port}/{contextPath}/mostdownloaded?targetHost={targetHost}&mavenRepositoryName={mavenRepositoryName}&limit={limit}

Where:

host - is the host where we deploy our solution.
port - the container port, normally port 8080.
contextPath - the web application's context path. Will be usually the war name or it's alias name. For example, on Tomcat the context path can be defined on the server.xml file.
targetHost - (optional query param) the target Artifactory instance ip on which we query.
mavenRepositoryName - (optional query param) the target Maven repository name on the given Artifactory instace that we want to query.
limit - (optional query param) pagination limit to match the limit when submitting a call to the Artifactory instance. The defualt value is 1000.
The query params targetHost, mavenRepositoryName are optional and their defaults can be configured at the src/main/resources/config/AppConfig.json application configuration file.
