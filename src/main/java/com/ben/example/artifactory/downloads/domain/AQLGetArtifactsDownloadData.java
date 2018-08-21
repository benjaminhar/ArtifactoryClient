package com.ben.example.artifactory.downloads.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"results",
	"range"
})
public class AQLGetArtifactsDownloadData {

	@JsonProperty("results")
	private List<ArtifactsDownloadCountResult> results = null;
	@JsonProperty("range")
	private ArtifactsDownloadRange range;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("results")
	public List<ArtifactsDownloadCountResult> getResults() {
		return results;
	}

	@JsonProperty("results")
	public void setResults(List<ArtifactsDownloadCountResult> results) {
		this.results = results;
	}

	@JsonProperty("range")
	public ArtifactsDownloadRange getRange() {
		return range;
	}

	@JsonProperty("range")
	public void setRange(ArtifactsDownloadRange range) {
		this.range = range;
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