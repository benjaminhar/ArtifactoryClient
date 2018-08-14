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
import home.assignment.jfrog.arifactory.rest.client.domain.ApiResponse;
import home.assignment.jfrog.arifactory.rest.client.domain.Result;

/**
 * Hello world!
 *
 */
public class ArtifactoryClient 
{
	private static Logger logger = Logger.getLogger(ArtifactoryClient.class.getSimpleName());
	private final String USER_PASS_DECODED = "admin:Zw6c1QdHxHsJ";
	private final String HOST = "35.225.53.253";
	private final String ARTIFACTORY_SEARCH_AQL = "http://" + HOST + "/artifactory/api/search/aql";
	// private final String ARTIFACTORY_STORAGE_BASE = "http://" + HOST + "/artifactory/api/storage/";

	public static void main( String[] args ) throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		ArtifactoryClient client = new ArtifactoryClient();
		String repositoryName = "jcenter-cache";
		ApiResponse response = client.api(repositoryName);

		logger.info(response.toString());

	}

	@SuppressWarnings("unchecked")
	private ApiResponse api(String mavenRepositoryName) throws JsonParseException, JsonMappingException, IOException, InterruptedException {

		RestTemplate restClient = new RestTemplate(); 	
		String requestBody = readReuestBodyFromFile("aql/get_and_sort.txt");

		requestBody = requestBody.replace("REPO_NAME", mavenRepositoryName);
		ResponseEntity<AQLResult> postResponse = (ResponseEntity<AQLResult>)post(restClient, requestBody, MediaType.APPLICATION_JSON, AQLResult.class);
		// restClient.exchange(ARTIFACTORY_SEARCH_AQL, HttpMethod.POST, entity, AQLResult.class);

		List<Result> results = postResponse.getBody().getResults();

		ApiResponse response = new ApiResponse();
		results.forEach(result -> {
			String repo = result.getRepo();
			String path = result.getPath();
			String name = result.getName();
			String artifactPath = repo.concat("/").concat(path).concat("/").concat(name);
			Long downloadsCount = result.getStats().get(0).getDownloads();
			response.addRecord(artifactPath, downloadsCount);
		});

		return response;

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

	//@SuppressWarnings({ "unused" })
	private String readReuestBodyFromFile(String filePath) {
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource(filePath).getFile());
		Scanner sc = null;
		try {
			sc = new Scanner(file);

			// we just need to use \\Z as delimiter
			sc.useDelimiter("\\Z");
		} catch (FileNotFoundException e) {
			logger.info("File with path " + filePath +  " wasn't found. Please check file path.");

		} 

		String fileContent = null;
		if (sc != null) {
			fileContent = sc.next();
			sc.close();
		}
		
		return fileContent;
	}
}
