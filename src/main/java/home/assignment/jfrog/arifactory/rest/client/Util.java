package home.assignment.jfrog.arifactory.rest.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Base64;
import java.util.Collections;
import java.util.Scanner;
import java.util.logging.Logger;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


public class Util {

	private static Logger logger = Logger.getLogger(Util.class.getSimpleName());

	public static String readReuestBodyFromFile(String resourceFilePath) {
		ClassLoader classLoader = Util.class.getClassLoader();
		URL url = classLoader.getResource(resourceFilePath);
		File file = null;

		file = (url != null) ? new File(url.getFile()) : null;

		if (file == null) {
			logger.severe("File with path " + resourceFilePath +  " wasn't found. Please check the file path.");
			return null;
		}
		
		String fileContent = null;

		try (Scanner sc = new Scanner(file) ) {
			if (sc != null) {
				sc.useDelimiter("\\Z");
				fileContent = sc.next();
			} 
		} catch (FileNotFoundException e) {
			logger.severe("File with path " + resourceFilePath +  " wasn't found. Please check the file path.");
			return null;
		} 	

		return fileContent;
	}
	
	public static ResponseEntity<?> post(RestTemplate restClient, String url, String body, MediaType acceptType, Class<?> clazz, String userToken) {
		// encode in Base64 the user/password for the http header
		String user_uassword_header = new String(Base64.getEncoder().encode(userToken.getBytes()));
		logger.info("encodedBytes " + user_uassword_header);    	

		// set headers
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(acceptType));
		headers.set("Authorization", "Basic " + user_uassword_header);
		HttpEntity<String> entity;

		// body
		if (body != null) {
			entity = new HttpEntity<String>(body, headers);
		} else {
			entity = new HttpEntity<String>(headers);
		}

		return restClient.exchange(url, HttpMethod.POST, entity, clazz);
	}
}
