package home.assignment.jfrog.arifactory.rest.client;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import home.assignment.jfrog.arifactory.rest.client.domain.AQLResult;
import home.assignment.jfrog.arifactory.rest.client.domain.ApiResponse;
import home.assignment.jfrog.arifactory.rest.client.domain.Result;

/**
 * @author Ben Harosh
 * @since 08/10/18
 */
public class ArtifactoryClient 
{
	private static Logger logger = Logger.getLogger(ArtifactoryClient.class.getSimpleName());
	private final String USER_PASS_DECODED = "admin:Zw6c1QdHxHsJ";
	private final String HOST = "35.225.53.253";
	private final String ARTIFACTORY_SEARCH_AQL = "http://" + HOST + "/artifactory/api/search/aql";

	public static void main( String[] args ) throws JsonParseException, JsonMappingException, IOException, InterruptedException
	{
		ArtifactoryClient client = new ArtifactoryClient();
		String repositoryName = "jcenter-cache";
		ApiResponse response = client.api(repositoryName);

		if (response != null) {
			logger.info(response.toString());
		} else {
			logger.severe("Error when fetching request body");
		}

	}

	@SuppressWarnings("unchecked")
	private ApiResponse api(String mavenRepositoryName) throws JsonParseException, JsonMappingException, IOException, InterruptedException {

		RestTemplate restClient = new RestTemplate(); 	
		String requestBody = Util.readReuestBodyFromFile("aql/get_and_sort.txt");
		
		if (requestBody == null) {
			logger.severe("problem with reading AQL body from file.");
			return null;
		}

		requestBody = requestBody.replace("REPO_NAME", mavenRepositoryName);
		ResponseEntity<AQLResult> postResponse = (ResponseEntity<AQLResult>)Util.post(restClient, ARTIFACTORY_SEARCH_AQL, requestBody, MediaType.APPLICATION_JSON, AQLResult.class, USER_PASS_DECODED);
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

}
