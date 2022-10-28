package com.assignment.utils;

import org.apache.http.HttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestAPIUtils {

	/**
	 * Checks whether the http response is valid (i.e. with status code 200)
	 * @param httpResponse
	 * @throws Exception
	 */
	public static void checkResponseIsValid(HttpResponse httpResponse) throws Exception {
		// validate that the response is 200
		if (httpResponse.getStatusLine().getStatusCode() < 200 || httpResponse.getStatusLine().getStatusCode() > 299) {
			log.error("response with errors received");
			throw new Exception(httpResponse.getStatusLine().getReasonPhrase());
		}
	}

}
