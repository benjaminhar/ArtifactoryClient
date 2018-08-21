package com.ben.example.artifactory.downloads;

import java.util.Base64;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AppConfig {

	private String targetHost;
	
	private String targetPort;

	private String targetRepo;

	private String user;

	private String token;

	private String path;

	private Integer paginationlimit;

	public String getBasicAuthorization() {
		return new String(Base64.getEncoder().encode((user + ":" + token).getBytes()));
	}

}
