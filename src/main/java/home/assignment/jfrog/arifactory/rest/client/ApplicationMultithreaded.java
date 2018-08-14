package home.assignment.jfrog.arifactory.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import home.assignment.jfrog.arifactory.rest.client.domain.AQLResult;
import home.assignment.jfrog.arifactory.rest.client.domain.ArtifactInfo;
import home.assignment.jfrog.arifactory.rest.client.domain.Result;
import home.assignment.jfrog.arifactory.rest.client.domain.Task;

/**
 * Hello world!
 *
 */
public class ApplicationMultithreaded 
{	

	Logger logger = Logger.getLogger(ArtifactoryClient.class.getSimpleName());
	private final String USER_PASS_DECODED = "admin:Zw6c1QdHxHsJ";
	private final String HOST = "35.225.53.253";
	private final String ARTIFACTORY_SEARCH_AQL = "http://" + HOST + "/artifactory/api/search/aql";
	private final String ARTIFACTORY_STORAGE_BASE = "http://" + HOST + "/artifactory/api/storage/";
	private final String MAVEN_REPOSITORY_NAME = "jcenter-cache";
	
	private Map<String, Integer> downloadCountMap = new HashMap<>();
	private ExecutorService executor = Executors.newFixedThreadPool(100);
	
    public /* static */ void main( String[] args ) throws JsonParseException, JsonMappingException, IOException, InterruptedException
    {
    	ApplicationMultithreaded client = new ApplicationMultithreaded();
    	client.run();		
			
    }
    
    private void run() throws JsonParseException, JsonMappingException, IOException, InterruptedException {
    	
    	RestTemplate restClient = new RestTemplate();
    	
    /** 	String body = "items.find( { "
    	+ 			" \"repo\":{\"$eq\":\"jcenter-cache\" }"
        +           "}	)"; **/
    	
    	String requestBody = readReuestBodyFromFile("aql/get_artifacts.txt");
  
    	requestBody.replace("REPO_NAME", MAVEN_REPOSITORY_NAME);
    	ResponseEntity<AQLResult> postResponse = (ResponseEntity<AQLResult>)post(restClient, requestBody, MediaType.APPLICATION_JSON, AQLResult.class);
    	// restClient.exchange(ARTIFACTORY_SEARCH_AQL, HttpMethod.POST, entity, AQLResult.class);
    			
    	List<Result> results = postResponse.getBody().getResults();
    	int totalRecords = postResponse.getBody().getRange().getTotal();
    	CountDownLatch latch = new CountDownLatch(totalRecords);

    	results.forEach(result -> {
      		String repo = result.getRepo();
      		String path = result.getPath();
    		String name = result.getName();
    		String fetchURL = repo.concat("/").concat(path).concat("/").concat(name).concat("?stats");
    		String storageArtifactUrl = ARTIFACTORY_STORAGE_BASE + fetchURL;
    		Task task = new Task(restClient, USER_PASS_DECODED, storageArtifactUrl, requestBody, MediaType.APPLICATION_JSON, ArtifactInfo.class, HttpMethod.GET, latch);
    		Future<ResponseEntity<?>> artifactInfo = (Future<ResponseEntity<?>>)executor.submit(task);
    		try {
				downloadCountMap.put(name, ((ResponseEntity<ArtifactInfo>)artifactInfo.get()).getBody().getDownloadCount());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	});
    		
    		try {
    		  latch.await();
    		} catch (InterruptedException E) {
    		   // handle
    		}
            
    		System.out.println("downloadCountMap size = " + downloadCountMap.size());
    	/* for (Result result : results) {
    		String repo = result.getRepo();
    		String path = result.getPath();
    		String name = result.getName();
    		String fetchURL = repo.concat("/").concat(path).concat("/").concat(name).concat("?stats");

    		String storageArtifactUrl = ARTIFACTORY_STORAGE_BASE + fetchURL;
    		logger.info("storageArtifactUrl = " + storageArtifactUrl);
    		
    		ResponseEntity<ArtifactInfo> getResponse = (ResponseEntity<ArtifactInfo>)get(restClient, storageArtifactUrl, null, MediaType.APPLICATION_JSON, ArtifactInfo.class);
    		int downloadCount = getResponse.getBody().getDownloadCount();
    		logger.info("downloadCount for name = " + downloadCount);  
    		
    		downloadCountMap.put(name, downloadCount);
    	} */
    		
    		executor.shutdown();
    		executor.awaitTermination(60, TimeUnit.SECONDS);
    	
    	

    }
    
    private ResponseEntity<?> post(RestTemplate restClient, String body, MediaType acceptType, Class<?> clazz) {
    	// encode in Base64 the user/password for the http header
    	String user_uassword_header = new String(Base64.getEncoder().encode(USER_PASS_DECODED.getBytes()));
    	logger.info("encodedBytes " + user_uassword_header);    	
    	
    	// set headers
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Collections.singletonList(acceptType));
     	headers.set("Authorization", "Basic " + user_uassword_header);
     	HttpEntity<String> entity;
     	
     	if (body != null) {
     		entity = new HttpEntity<String>(body, headers);
     	} else {
     		entity = new HttpEntity<String>(headers);
     	}
     	
     	return restClient.exchange(ARTIFACTORY_SEARCH_AQL, HttpMethod.POST, entity, clazz);
    }
    
    private ResponseEntity<?> get(RestTemplate restClient, String Url, String body, MediaType acceptType, Class<?> clazz) {
    	// encode in Base64 the user/password for the http header
    	String user_uassword_header = new String(Base64.getEncoder().encode(USER_PASS_DECODED.getBytes()));
    	logger.info("encodedBytes " + user_uassword_header);    	
    	
    	// set headers
    	HttpHeaders headers = new HttpHeaders();
    	headers.setAccept(Collections.singletonList(acceptType));
     	headers.set("Authorization", "Basic " + user_uassword_header);
     	HttpEntity<String> entity;
     	
     	if (body != null) {
     		entity = new HttpEntity<String>(body, headers);
     	} else {
     		entity = new HttpEntity<String>(headers);
     	}
     	
     	return restClient.exchange(Url, HttpMethod.GET, entity, clazz);
    }
    
    private String readReuestBodyFromFile(String filePath) {
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(filePath).getFile());
        Scanner sc = null;
		try {
			sc = new Scanner(file);
		     
	        // we just need to use \\Z as delimiter
	        sc.useDelimiter("\\Z");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info("File with path " + filePath +  " wasn't found. Please check file path.");
			if (sc != null) 
				sc.close();	
			return null;
		} 

        return sc.next();
    }
}
