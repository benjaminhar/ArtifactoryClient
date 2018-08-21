package com.ben.example.artifactory.downloads.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;	

public class ApiResponse extends ResponseEntity<List<Record>> {

	
	public ApiResponse() {
		super(new ArrayList<>(),  HttpStatus.OK);
	}

	public ApiResponse(List<Record> records) {
		this(records, HttpStatus.OK);
	}
	
	public ApiResponse(List<Record> records, HttpStatus badRequest) {
		super(records, HttpStatus.OK);
	}

	
	public List<Record> getRecords() {
		return getBody();	
	}
	
	@Override
	public String toString() {
		StringBuilder results = new StringBuilder("API Response: \n");
		if (getBody().size() == 0) {
			results.append("{}");
		}
		for(Record record : getBody()) {
			results.append("Artifact Name := " + record.getArtifactName() + "\n"
					+ "Downloads Count := " + record.getDownloadsCount() + "\n\n");
		}
		return results.toString();
	}
}
