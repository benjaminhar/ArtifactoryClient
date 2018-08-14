package home.assignment.jfrog.arifactory.rest.client.domain;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse implements IResponse {

	private List<Record> records;

	public ApiResponse() {
		super();
		records = new ArrayList<Record>();
	}

	public ApiResponse(List<Record> records) {
		super();
		this.records = records;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String result = "API Response: \n";
		for(Record record : records) {
			result = result + "Artifact Name := " + record.getArtifactName() + "\n"
					+ "Downloads Count := " + record.getDownloadsCount() + "\n\n";
		}
		return result;
	}

	public void addRecord(String name, Long downloadsCount) {
		records.add(new Record(name, downloadsCount));
	}
}
