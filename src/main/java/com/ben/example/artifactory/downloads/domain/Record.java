package com.ben.example.artifactory.downloads.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Record {
	private String artifactName;
	private Long downloadsCount;
	
	public String toString() {
		return "Artifact Name := " + this.getArtifactName() + "\n"
				+ "Downloads Count := " + this.getDownloadsCount() + "\n";
	}
	
}