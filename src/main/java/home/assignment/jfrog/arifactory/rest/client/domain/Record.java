package home.assignment.jfrog.arifactory.rest.client.domain;

public class Record {
	private String artifactName;
	private Long downloadsCount;
	
	public Record(String artifactName, Long downloadsCount) {
		super();
		this.artifactName = artifactName;
		this.downloadsCount = downloadsCount;
	}

	public String getArtifactName() {
		return artifactName;
	}

	public void setArtifactName(String artifactName) {
		this.artifactName = artifactName;
	}

	public Long getDownloadsCount() {
		return downloadsCount;
	}

	public void setDownloadsCount(Long downloadsCount) {
		this.downloadsCount = downloadsCount;
	}	
	
}