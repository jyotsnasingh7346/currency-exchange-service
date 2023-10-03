package com.in28minutes.microservices.currencyexchangeservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;

@RestController
public class CircuitBreakerController {

	private Logger logger = LoggerFactory.getLogger(CircuitBreakerController.class);
	
	/*
	 * Using @Retry is great if we have small number of requests that might fail.
	 * 
	 * Using @CircuitBreaker is for when there are wayy too many requests that keep failing, 
	 * and we dont have enough time to process a response from the fallbackMethod.
	 * 
	 * What it does is, it stores the fallbackMethod response after a few failures
	 * and keeps on sending that response without actually calling the fallbackMethod.
	 * So, it breaks the failing Circuit & directly returns the response back.
	 * 
	 * Using @RateLimiter, we can specify how many requests we want to allow in how many seconds.
	 * eg: in 10s, only allow 2 calls to the sample-api
	 */
	
	/*
	 * 3 different states of a @CircuitBreaker : 
	 * 
	 * 1. CLOSED 	: The M.S that is down keeps on getting called, always, on each attempt.
	 * 2. OPEN 		: The @CircuitBreaker does not call the M.S that is down, it directly returns the fallback Response.
	 * 3. HALF-OPEN : The @CircuitBreaker send only a % of requests to the M.S 
	 * 					and returns the hardcoded fallback Response for rest of the calls.
	 * 
	 * The @CircuitBreaker switches into different states depending on failure rate and its threshold.
	 * Refer -> https://resilience4j.readme.io/docs/circuitbreaker
	 */
	
	@GetMapping("/sample-api")
//	@Retry(name = "sample-api", fallbackMethod = "hardCodedResponse")
//	@CircuitBreaker(name = "default", fallbackMethod = "hardCodedResponse")
//	@RateLimiter(name = "default", fallbackMethod = "hardCodedResponse")	// we dont need the failure logic to test @RateLimiter
	@Bulkhead(name = "sample-api")
	public String sampleApi() {
		// we want to make this api fail, to test the @Retry of Circuit Breaker
		// lets write a code for that
		logger.info("----------Sample API call received");

		ResponseEntity<String> forEntity = new RestTemplate().getForEntity("http://localhost:8080/some-dummy-url",
				String.class);
		
		// check in logs for the logger & see is the call was retried
		return forEntity.getBody();
	}
	
	public String hardCodedResponse(Exception ex) {
		return "fallBackResponse because the original MS throws an exception even after 5 attempts";
	}
}
