package com.assignment.service;

import java.util.Map;

public interface SalesforceAPIService {

	Map<String, Object> retrieveCaseDetails(String uri) throws Exception;

}
