package com.assignment;

import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.assignment.service.SFCaseDataToS3Processor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class AWSLambdaApplication {
	public static void main(String[] args) {
		SpringApplication.run(AWSLambdaApplication.class, args);
	}

	/**
	 * Lambda function responsible for making the whole process work
	 * @param processor bean injected at runtime
	 * @return
	 */
	@Bean
	public Supplier<String> copySFCaseRecords(SFCaseDataToS3Processor processor) {
		log.info("Starting the Salesforce claims to S3 process");
		return () -> processor.copySFCaseRecords();
	}
}
