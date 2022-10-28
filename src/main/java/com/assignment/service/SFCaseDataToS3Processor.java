package com.assignment.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
/**
 * Responsible for keep track of all the work
 * @author GS
 *
 */
@Slf4j
@Component
public class SFCaseDataToS3Processor {

	@Autowired
	private SalesforceAPIService sfAPIService;

	@Autowired
	private StorageService s3StorageService;

	@Autowired
	private ObjectMapper objectMapper;
	// TODO: Couldn't find category column from the assignment, need to check what value it
	// represents from the object
	private static final String QUERY_PATH = "/services/data/v56.0/query/?q=SELECT+Id+CaseNumber+Description+Subject+Origin+Type+from+Case";
	private static final String NEXT_RECORD_URL_KEY = "nextRecordsUrl";

	/**
	 * 
	 * @return success or failure of the entire process (retrieving the case details
	 *         from SF API and saving that into S3 bucket
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String copySFCaseRecords() {
		try {
			// object to hold all the case details data
			List<Object> caseDetails = new ArrayList<>();
			Map<String, Object> result = sfAPIService.retrieveCaseDetails(QUERY_PATH);
			// we need to keep querying the data until NEXT_RECORD_URL_KEY is not present in
			// the response check:
			// https://developer.salesforce.com/docs/atlas.en-us.216.0.api_rest.meta/api_rest/dome_query.htm
			while (result != null && result.get(NEXT_RECORD_URL_KEY) != null) {
				List<Object> records = (List) result.get("records");
				caseDetails.addAll(records);
				// we need to query the salesforce with the url provided by the salesforce from
				// the previous response
				result = sfAPIService.retrieveCaseDetails((String) result.get(NEXT_RECORD_URL_KEY));
			}
			// add the last result as well
			List<Object> records = (List) result.get("records");
			caseDetails.addAll(records);

			String jsonCaseDetails = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(caseDetails);
			String fileName = s3StorageService.uploadFile(jsonCaseDetails);

			return "Process completed successfully, the case details have been saved at S3 location: " + fileName;
		} catch (Exception ex) {
			log.error("error occurred", ex);
			return "Error occurred:" + ex.getMessage() + ", please check the logs for full details";
		}

	}

}
