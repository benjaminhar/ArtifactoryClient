package com.ben.example.artifactory.downloads;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;

import com.ben.example.artifactory.downloads.domain.Record;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author Ben Harosh
 * @since 08/10/18
 */
public class ArtifactoryClient {
	private static Logger logger = Logger.getLogger(ArtifactoryClient.class.getSimpleName());

	public
	void main(String[] args) throws JsonParseException, JsonMappingException, IOException, InterruptedException {
		DownloadsCountController controller = new DownloadsCountController();
		AppConfig config = Util.readAppConfig();

		
		ResponseEntity<List<Record>> response = controller.findMostDownloadedArtifacts(config.getTargetHost(), config.getTargetRepo(),
				config.getPaginationlimit());

		if (response != null) {
			logger.info("Http Status Code: " + response.getStatusCode().toString());
			for(Record record : response.getBody()) {
				logger.info(record.toString());
			}
		} else {
			logger.severe("Error when fetching request body");
		}
	}
}
