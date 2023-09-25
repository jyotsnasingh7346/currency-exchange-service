package com.in28minutes.microservices.currencyexchangeservice;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CurrencyExchangeController {

	/*
	 * To have MULTIPLE INSTANCES of the same application,
	 * we can launch up the same application on different ports.
	 * In run configurations, duplicate a new one and give it vm arguments as :
	 * -Dserver.port=8001 (this new instance will run on port 8001)
	 * Make sure to name all the instances properly, so its easier to differentiate between them.
	 * Check in Talend API on the uri: http://localhost:8001/currency-exchange/from/USD/to/INR
	 * */
	
	@Autowired
	private Environment environment;
	
	@GetMapping("/currency-exchange/from/{from}/to/{to}")
	public CurrencyExchange retrieveExchangeValue(@PathVariable String from, @PathVariable String to) {
		CurrencyExchange currencyExchange = new CurrencyExchange(1000L, from, to, BigDecimal.valueOf(50));
		String port = environment.getProperty("local.server.port");
		currencyExchange.setEnvironment(port);
		return currencyExchange;
	}
	
}
