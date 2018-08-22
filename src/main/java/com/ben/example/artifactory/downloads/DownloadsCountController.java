package com.ben.example.artifactory.downloads;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Positive;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ben.example.artifactory.downloads.domain.Record;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * @author benjaminhar
 */

@RestController
@RequestMapping("/")
public class DownloadsCountController {

	/**
	 * Finds the top 2 downloaded artifacts on an Artifactory instance on a
	 * given repository
	 * 
	 * @param targetHost
	 * @param mavenRepositoryName
	 * @param limit
	 * @return response entity with the most downloaed artifacts on a maven repository
	 */
	@RequestMapping("/mostdownloaded")
	public ResponseEntity<List<Record>> findMostDownloadedArtifacts(
			@RequestParam(value = "targetHost", defaultValue = "35.202.171.89") String targetHost,
			@RequestParam(value = "mavenRepositoryName", defaultValue = "jcenter-cache") String mavenRepositoryName,
			@Positive @RequestParam(value = "limit", defaultValue = "1000") Integer limit)
			throws FileNotFoundException {

		String endpoint = "http://" + targetHost + Util.readAppConfig().getPath();
		
		if (limit <=0 || limit > Integer.MAX_VALUE || !Util.validateIPAddress(targetHost)) 
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

		ResponseEntity<List<Record>> response = Util.getMostDownloadedArtifacts(endpoint, mavenRepositoryName, limit);

		return response;
	}

}
