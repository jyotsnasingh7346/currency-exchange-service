package com.in28minutes.microservices.currencyexchangeservice;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrencyExchangeRepository extends JpaRepository<CurrencyExchange, Long> {
	
	// Implementation of every method here is provided by SpringDataJpa
	// SpringDataJpa will convert the method to an sql query where we can query the table using from & to
	CurrencyExchange findByFromAndTo(String from, String to);

}
