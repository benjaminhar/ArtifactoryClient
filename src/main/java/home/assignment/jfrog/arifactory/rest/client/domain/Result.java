package home.assignment.jfrog.arifactory.rest.client.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import home.assignment.jfrog.arifactory.rest.client.domain.Stat;



@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
	"repo",
	"path",
	"name",
	"type",
	"size",
	"created",
	"created_by",
	"modified",
	"modified_by",
	"updated",
	"stats"
})
public class Result {

	@JsonProperty("repo")
	private String repo;
	@JsonProperty("path")
	private String path;
	@JsonProperty("name")
	private String name;
	@JsonProperty("type")
	private String type;
	@JsonProperty("size")
	private Integer size;
	@JsonProperty("created")
	private String created;
	@JsonProperty("created_by")
	private String createdBy;
	@JsonProperty("modified")
	private String modified;
	@JsonProperty("modified_by")
	private String modifiedBy;
	@JsonProperty("updated")
	private String updated;
	@JsonProperty("stats")
	private List<Stat> stats = null;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("repo")
	public String getRepo() {
		return repo;
	}

	@JsonProperty("repo")
	public void setRepo(String repo) {
		this.repo = repo;
	}

	@JsonProperty("path")
	public String getPath() {
		return path;
	}

	@JsonProperty("path")
	public void setPath(String path) {
		this.path = path;
	}

	@JsonProperty("name")
	public String getName() {
		return name;
	}

	@JsonProperty("name")
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty("type")
	public String getType() {
		return type;
	}

	@JsonProperty("type")
	public void setType(String type) {
		this.type = type;
	}

	@JsonProperty("size")
	public Integer getSize() {
		return size;
	}

	@JsonProperty("size")
	public void setSize(Integer size) {
		this.size = size;
	}

	@JsonProperty("created")
	public String getCreated() {
		return created;
	}

	@JsonProperty("created")
	public void setCreated(String created) {
		this.created = created;
	}

	@JsonProperty("created_by")
	public String getCreatedBy() {
		return createdBy;
	}

	@JsonProperty("created_by")
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	@JsonProperty("modified")
	public String getModified() {
		return modified;
	}

	@JsonProperty("modified")
	public void setModified(String modified) {
		this.modified = modified;
	}

	@JsonProperty("modified_by")
	public String getModifiedBy() {
		return modifiedBy;
	}

	@JsonProperty("modified_by")
	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	@JsonProperty("updated")
	public String getUpdated() {
		return updated;
	}

	@JsonProperty("updated")
	public void setUpdated(String updated) {
		this.updated = updated;
	}

	@JsonProperty("stats")
	public List<Stat> getStats() {
		return stats;
	}

	@JsonProperty("stats")
	public void setStats(List<Stat> stats) {
		this.stats = stats;
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


