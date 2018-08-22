package com.ben.example.artifactory.downloads;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.ben.example.artifactory.downloads.domain.AQLGetArtifactsDownloadData;
import com.ben.example.artifactory.downloads.domain.AQLGetArtifactsResult;
import com.ben.example.artifactory.downloads.domain.ArtifactsDownloadCountResult;
import com.ben.example.artifactory.downloads.domain.Record;
import com.ben.example.artifactory.downloads.domain.Result;

public class Util {

	private static Logger logger = Logger.getLogger(Util.class.getSimpleName());

	private static AppConfig appConfig = null;

	private static RestTemplate restClient = new RestTemplate();

	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." + "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\."
			+ "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

	private static final String REPOSITORY_NAME_PATTERN = "[a-zA-Z0-9_\\.-]";

	public static String getFileContent(String resourceFilePath) throws FileNotFoundException {
		File file = getFile(resourceFilePath);

		if (file == null) {
			logger.severe("File with path " + resourceFilePath + " wasn't found. Please check the file path.");
			throw new FileNotFoundException(
					"File with path " + resourceFilePath + " wasn't found. Please check the file path.");
		}

		String fileContent = null;

		try (Scanner sc = new Scanner(file)) {
			if (sc != null) {
				sc.useDelimiter("\\Z");
				fileContent = sc.next();
			}
		} catch (FileNotFoundException e) {
			logger.severe("File with path " + resourceFilePath + " wasn't found. Please check the file path.");
			throw e;
		}

		return fileContent;
	}

	/**
	 * Creates a segment String to be used in the Json request body
	 * 
	 * @param artifactName
	 * @param artifactPath
	 * @param index
	 * @param pagingLimit
	 * @return string in json format to represent the input artifact in the
	 *         query
	 */
	public static String generateQuerySegmentForArtifact(final String artifactName, final String artifactPath,
			int index, int pagingLimit) {
		String queryPartTemplate = "{   \"$and\":[   {\"name\":{\"$eq\":\"" + artifactName + "\"}},"
				+ "   {\"path\":{\"$eq\":\"" + artifactPath + "\"}} ] },";
		if (index == pagingLimit) {
			return queryPartTemplate.substring(0, queryPartTemplate.length() - 1);
		} else {
			return queryPartTemplate;
		}
	}

	/**
	 * Search for the top 2 downloaded artifacts on artifactory maven repository
	 * using the following steps:
	 * <p>
	 * 1. Calling the artifactory AQL Rest api to receive names and paths of the
	 * artifacts matching the search criteria using pagination.</br>
	 * 2. Processing the data collected on precious step to create a query in
	 * order to receive downloads history data on these artifacts.</br>
	 * 3. Calling artifactory AQL Rest api to receive downloads information on
	 * the artifacts and then sorting the input.</br>
	 * 4. After sorting the input it will pick the top 2 downloads for the
	 * current page and will compare them against the previous top 2 to get a
	 * global top 2
	 * 
	 * @param restClient
	 * @param endpoint
	 * @param mavenRepositoryName
	 * @param paginationLimit
	 * @return Top 2 downloaded jars on a maven repository
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("unchecked")
	public static ResponseEntity<List<Record>> getMostDownloadedArtifacts(String endpoint,
			final String mavenRepositoryName, int paginationLimit) throws FileNotFoundException {

		String getArtifactsByPageRequestTemplate = Util.getFileContent("aql/get-artifacts-by-page.txt");
		String getDownloadsCountRequestTemplate = Util.getFileContent("aql/get-downloads-count.txt");

		if (getArtifactsByPageRequestTemplate == null || getDownloadsCountRequestTemplate == null) {
			logger.severe("problem with reading request body from file.");
			return null;
		}

		int offset = 0, iterations = 0;
		boolean isPaginationFinished = false;

		ArtifactsDownloadCountResult artifactMostDownloaded = null;
		ArtifactsDownloadCountResult artifactRunnerUpMostDoanloaded = null;

		while (!isPaginationFinished) {
			String getArtifactsByPageWithValues = StringUtils.replaceEach(getArtifactsByPageRequestTemplate,
					new String[] { "REPO_NAME", "PAGE_SIZE", "OFFSET" }, new String[] { mavenRepositoryName,
							String.valueOf(paginationLimit), String.valueOf(offset + iterations * paginationLimit) });

			ResponseEntity<AQLGetArtifactsResult> postResponse = (ResponseEntity<AQLGetArtifactsResult>) Util.post(
					restClient, endpoint, getArtifactsByPageWithValues, MediaType.APPLICATION_JSON,
					AQLGetArtifactsResult.class, readAppConfig().getBasicAuthorization());
			List<Result> results = postResponse.getBody().getResults();
			if (results.size() == 0) {
				break;
			}

			int total = postResponse.getBody().getRange().getTotal();
			int limit = (paginationLimit == total) ? paginationLimit : total;
			int resultIndex = 1;

			StringBuilder queryArtifactList = new StringBuilder();

			for (Result result : results) {
				String artifactName = result.getName();
				String artifactPath = result.getPath();
				String queryFromArtifact = Util.generateQuerySegmentForArtifact(artifactName, artifactPath, resultIndex,
						limit);
				queryArtifactList.append(queryFromArtifact);
				resultIndex++;
			}

			String getDownloadsCountRequestWithData = getDownloadsCountRequestTemplate.replace("ARTIFACTLIST",
					queryArtifactList.toString());

			ResponseEntity<AQLGetArtifactsDownloadData> downloadsResponse = (ResponseEntity<AQLGetArtifactsDownloadData>) Util
					.post(restClient, endpoint, getDownloadsCountRequestWithData, MediaType.APPLICATION_JSON,
							AQLGetArtifactsDownloadData.class, readAppConfig().getBasicAuthorization());
			List<ArtifactsDownloadCountResult> artifactsDownloadCountResults = downloadsResponse.getBody().getResults();
			artifactsDownloadCountResults.sort(
					(a1, a2) -> (a2.getStats().get(0).getDownloads()).compareTo((a1.getStats().get(0).getDownloads())));

			// first pagination
			if (artifactMostDownloaded == null && artifactsDownloadCountResults.size() > 0) {
				artifactMostDownloaded = artifactsDownloadCountResults.get(0);
				if (artifactsDownloadCountResults.size() > 1) {
					artifactRunnerUpMostDoanloaded = artifactsDownloadCountResults.get(1);
				}
				// second pagination
			} else if (artifactsDownloadCountResults.size() > 0) {
				List<ArtifactsDownloadCountResult> mostDownloaded = Util.pickGlobalMostDownloadedArtifacts(
						(artifactsDownloadCountResults.size() > 0) ? artifactsDownloadCountResults.get(0) : null,
						(artifactsDownloadCountResults.size() > 1) ? artifactsDownloadCountResults.get(1) : null,
						artifactMostDownloaded, artifactRunnerUpMostDoanloaded);

				artifactMostDownloaded = mostDownloaded.get(0);
				artifactRunnerUpMostDoanloaded = mostDownloaded.get(1);
			}

			if (total < paginationLimit) {
				isPaginationFinished = true;
			}

			iterations++;
		}

		List<Record> records = new ArrayList<>();
		Record record1 = (artifactMostDownloaded != null)
				? new Record(
						mavenRepositoryName.concat("/").concat(artifactMostDownloaded.getPath()).concat("/")
								.concat(artifactMostDownloaded.getName()),
						artifactMostDownloaded.getStats().get(0).getDownloads())
				: null;
		Record record2 = (artifactRunnerUpMostDoanloaded != null) ? new Record(
				mavenRepositoryName.concat("/").concat(artifactRunnerUpMostDoanloaded.getPath()).concat("/")
						.concat(artifactRunnerUpMostDoanloaded.getName()),
				artifactRunnerUpMostDoanloaded.getStats().get(0).getDownloads()) : null;

		Collections.addAll(records, record1, record2);

		return new ResponseEntity<>(records.stream().filter(Objects::nonNull).collect(Collectors.toList()),
				HttpStatus.OK);
	}

	/**
	 * Generating a POST request using Spring's RestTemplate client
	 * 
	 * @param restClient
	 * @param url
	 * @param body
	 * @param acceptType
	 * @param clazz
	 * @param userToken
	 * @return the response as entity
	 */
	public static ResponseEntity<?> post(RestTemplate restClient, String url, String body, MediaType acceptType,
			Class<?> clazz, String userToken) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Collections.singletonList(acceptType));
		headers.set("Authorization", "Basic " + userToken);
		HttpEntity<String> entity;

		if (body != null) {
			entity = new HttpEntity<String>(body, headers);
		} else {
			entity = new HttpEntity<String>(headers);
		}

		return restClient.exchange(url, HttpMethod.POST, entity, clazz);
	}

	public static AppConfig readAppConfig() throws FileNotFoundException {
		if (appConfig != null) {
			return appConfig;
		}

		AppConfig appConfigLocal = new AppConfig();

		try {
			String text = getFileContent("config/appConfig.json");
			JSONObject jsonObject = new JSONObject(text);

			appConfigLocal.setTargetHost((String) jsonObject.get("targetHost"));
			appConfigLocal.setTargetPort((String) jsonObject.get("targetPort"));
			appConfigLocal.setPath((String) jsonObject.get("searchPath"));
			appConfigLocal.setTargetRepo((String) jsonObject.get("targetRepo"));
			appConfigLocal.setUser((String) jsonObject.get("user"));
			appConfigLocal.setToken((String) jsonObject.get("token"));
			appConfigLocal.setPaginationlimit(Integer.valueOf((String) jsonObject.get("paginationLimit")));

		} catch (JSONException e) {
			logger.severe(e.getMessage());
		}

		return appConfig = appConfigLocal;
	}

	public static List<ArtifactsDownloadCountResult> pickGlobalMostDownloadedArtifacts(
			final ArtifactsDownloadCountResult candidate1, final ArtifactsDownloadCountResult candidate2,
			final ArtifactsDownloadCountResult topDownloaded, final ArtifactsDownloadCountResult runnerup) {
		List<ArtifactsDownloadCountResult> candidatesList = new ArrayList<>();
		Collections.addAll(candidatesList, topDownloaded, runnerup, candidate1, candidate2);

		List<ArtifactsDownloadCountResult> response = candidatesList.stream().filter(Objects::nonNull).sorted(
				(a1, a2) -> (a2.getStats().get(0).getDownloads()).compareTo((a1.getStats().get(0).getDownloads())))
				.limit(2).collect(Collectors.toList());

		return response;
	}

	public static boolean validateIPAddress(String address) {
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(address);
		return matcher.matches();
	}

	public static boolean validateRepositoryName(String repoName) {
		Pattern pattern = Pattern.compile(REPOSITORY_NAME_PATTERN);
		Matcher matcher = pattern.matcher(repoName);
		return matcher.matches();
	}

	private static File getFile(String resourceFilePath) {
		ClassLoader classLoader = Util.class.getClassLoader();
		URL url = classLoader.getResource(resourceFilePath);
		File file = (url != null) ? new File(url.getFile()) : null;

		return file;
	}

}
