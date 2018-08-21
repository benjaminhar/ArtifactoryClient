package com.ben.example.artifactory.downloads.domain;

import java.util.List;

public interface Response {
	
	boolean addRecord(Record record);
	
	List<Record> getRecords();
	
	String toString();

}
