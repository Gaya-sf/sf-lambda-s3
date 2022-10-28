package com.assignment.utils;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.assignment.model.SalesforceOauthAthenticationResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public final class OauthUtils {

	private static final String TOKEN_URL = "https://login.salesforce.com/services/oauth2/token";

	@Value("${salesforce.username}")
	private String sfUserName;

	@Value("${salesforce.password}")
	private String sfPassword;

	@Value("${salesforce.consumer-secret}")
	private String sfConsumerSecret;

	@Value("${salesforce.consumer-key}")
	private String sfConsumerKey;

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private ObjectMapper objectMapper;

	/**
	 * Retrieves the oauthtoken from the salesforce with credentials
	 * @return
	 * @throws Exception
	 */
	public SalesforceOauthAthenticationResult loginToSalesforce() throws Exception {
		log.debug("Invoking salesforce API to retrieve OauthToken");
		List<NameValuePair> loginParams = new ArrayList<>();
		loginParams.add(new BasicNameValuePair("client_id", sfConsumerKey));
		loginParams.add(new BasicNameValuePair("client_secret", sfConsumerSecret));
		loginParams.add(new BasicNameValuePair("grant_type", "password"));
		loginParams.add(new BasicNameValuePair("username", sfUserName));
		loginParams.add(new BasicNameValuePair("password", sfPassword));
		HttpPost post = new HttpPost(TOKEN_URL);
		post.setEntity(new UrlEncodedFormEntity(loginParams));
		HttpResponse httpResponse = httpClient.execute(post);
		SalesforceOauthAthenticationResult salesforceLoginResult = objectMapper
				.readValue(httpResponse.getEntity().getContent(), SalesforceOauthAthenticationResult.class);
		log.debug("retrieved SalesforceOauthAthenticationResult");
		return salesforceLoginResult;
	}

}
