package com.ui.service;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import com.ui.model.Customer;


/**
 * Implements a Spring Batch ItemReader
 * 
 * @author marcelbirkner
 * 
 */
public class FeedReader implements ItemReader<Customer> {
	
	private static final Log LOG = LogFactory.getLog(FeedReader.class);
	
	private Random random;
	
	/*
	 * Is called before an item is read
	 */
	@BeforeStep
	public void setUp() {
		random = new Random();
	}

	@Override
	public Customer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		LOG.info("reading data from feed ...");
		
		int nextInt = random.nextInt(100);
		if( nextInt > 95) {
			// Stop Batch Processing
			LOG.info("No more data to read :: nextInt=" + nextInt );
			return null;
		}
		
		return new Customer(nextInt, "John", "Doe");
	}
}
