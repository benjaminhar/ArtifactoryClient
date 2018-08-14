package home.assignment.jfrog.arifactory.rest.client.domain;

import java.util.Base64;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Task implements Callable<ResponseEntity<?>> {

    private RestTemplate restClient;
    private String token;
    private String url;
    private String body;
    private MediaType acceptType;
    private Class<?> clazz;
    private HttpMethod httpMethod;
    
    private Logger logger;
	private CountDownLatch latch;

    public Task(RestTemplate restTemplaterestClient, String token, String url, String body, MediaType acceptType, Class<?> clazz, HttpMethod method, CountDownLatch  latch) {
    	// encode in Base64 the user/password for the http header
    	this.restClient = restTemplaterestClient;
    	this.url = url;
    	this.body = body;
    	this.acceptType = acceptType;
    	this.clazz = clazz;
    	this.token = token;
    	this.httpMethod = method;
    	this.latch = latch;
    	this.logger = Logger.getLogger(Task.class.getSimpleName());
    }

    public ResponseEntity<? extends ArtifactInfo> call() throws Exception {

    	String user_uassword_header = new String(Base64.getEncoder().encode(token.getBytes()));
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
     	
     	ResponseEntity<ArtifactInfo> artifactInfoEntity =  (ResponseEntity<ArtifactInfo>)restClient.exchange(url, httpMethod, entity, clazz);
     	latch.countDown();
     	
     	return artifactInfoEntity;
    }
}
