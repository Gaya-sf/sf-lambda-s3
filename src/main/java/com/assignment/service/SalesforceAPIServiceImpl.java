package com.assignment.service;

import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assignment.model.SalesforceOauthAthenticationResult;
import com.assignment.utils.OauthUtils;
import com.assignment.utils.RestAPIUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service responsible for querying the CaseDetails data from Salesforce
 * @author GS
 *
 */
@Service
public class SalesforceAPIServiceImpl implements SalesforceAPIService {

	@Autowired
	private OauthUtils sfLoginUtil;

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public Map<String, Object> retrieveCaseDetails(String uri) throws Exception {
		// login to salesforce and retrive the access token
		SalesforceOauthAthenticationResult authResult = sfLoginUtil.loginToSalesforce();
		URIBuilder builder = new URIBuilder(authResult.getInstanceUrl());
		builder.setPath(uri);
		// Add authentication header
		HttpGet get = new HttpGet(builder.build());
		get.setHeader("Authorization", "Bearer " + authResult.getAccessToken());
		// Execute the get api
		HttpResponse httpResponse = httpClient.execute(get);
		// verify the response is valid, otherwise it will throw an exception
		RestAPIUtils.checkResponseIsValid(httpResponse);
		// covert this to map
		Map<String, Object> result = objectMapper.readValue(httpResponse.getEntity().getContent(),
				new TypeReference<Map<String, Object>>() {
				});
		return result;

	}

}
