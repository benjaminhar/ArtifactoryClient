package com.ben.example.artifactory.downloads.domain;

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
"start_pos",
"end_pos",
"total"
})
public class ArtifactsDownloadRange {

@JsonProperty("start_pos")
private Integer startPos;
@JsonProperty("end_pos")
private Integer endPos;
@JsonProperty("total")
private Integer total;
@JsonIgnore
private Map<String, Object> additionalProperties = new HashMap<String, Object>();

@JsonProperty("start_pos")
public Integer getStartPos() {
return startPos;
}

@JsonProperty("start_pos")
public void setStartPos(Integer startPos) {
this.startPos = startPos;
}

@JsonProperty("end_pos")
public Integer getEndPos() {
return endPos;
}

@JsonProperty("end_pos")
public void setEndPos(Integer endPos) {
this.endPos = endPos;
}

@JsonProperty("total")
public Integer getTotal() {
return total;
}

@JsonProperty("total")
public void setTotal(Integer total) {
this.total = total;
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