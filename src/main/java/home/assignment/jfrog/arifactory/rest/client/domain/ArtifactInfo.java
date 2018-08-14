package home.assignment.jfrog.arifactory.rest.client.domain;


import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"uri",
	"downloadCount",
	"lastDownloaded",
	"lastDownloadedBy",
	"remoteDownloadCount",
	"remoteLastDownloaded"
})
public class ArtifactInfo {

	@JsonProperty("uri")
	private String uri;
	@JsonProperty("downloadCount")
	private Integer downloadCount;
	@JsonProperty("lastDownloaded")
	private Long lastDownloaded;
	@JsonProperty("lastDownloadedBy")
	private String lastDownloadedBy;
	@JsonProperty("remoteDownloadCount")
	private Integer remoteDownloadCount;
	@JsonProperty("remoteLastDownloaded")
	private Integer remoteLastDownloaded;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("uri")
	public String getUri() {
		return uri;
	}

	@JsonProperty("uri")
	public void setUri(String uri) {
		this.uri = uri;
	}

	@JsonProperty("downloadCount")
	public Integer getDownloadCount() {
		return downloadCount;
	}

	@JsonProperty("downloadCount")
	public void setDownloadCount(Integer downloadCount) {
		this.downloadCount = downloadCount;
	}

	@JsonProperty("lastDownloaded")
	public Long getLastDownloaded() {
		return lastDownloaded;
	}

	@JsonProperty("lastDownloaded")
	public void setLastDownloaded(Long lastDownloaded) {
		this.lastDownloaded = lastDownloaded;
	}

	@JsonProperty("lastDownloadedBy")
	public String getLastDownloadedBy() {
		return lastDownloadedBy;
	}

	@JsonProperty("lastDownloadedBy")
	public void setLastDownloadedBy(String lastDownloadedBy) {
		this.lastDownloadedBy = lastDownloadedBy;
	}

	@JsonProperty("remoteDownloadCount")
	public Integer getRemoteDownloadCount() {
		return remoteDownloadCount;
	}

	@JsonProperty("remoteDownloadCount")
	public void setRemoteDownloadCount(Integer remoteDownloadCount) {
		this.remoteDownloadCount = remoteDownloadCount;
	}

	@JsonProperty("remoteLastDownloaded")
	public Integer getRemoteLastDownloaded() {
		return remoteLastDownloaded;
	}

	@JsonProperty("remoteLastDownloaded")
	public void setRemoteLastDownloaded(Integer remoteLastDownloaded) {
		this.remoteLastDownloaded = remoteLastDownloaded;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
