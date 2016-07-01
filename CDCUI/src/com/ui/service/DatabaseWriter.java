package com.ui.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.item.ItemWriter;

import com.ui.model.Customer;



/**
 * Implements a Spring Batch ItemWriter
 * 
 * @author marcelbirkner
 * 
 */
public class DatabaseWriter implements ItemWriter<Customer> {
	
	private static final Log LOG = LogFactory.getLog( DatabaseWriter.class );
	
	@Override
	public void write(List<? extends Customer> items) throws Exception {
		LOG.info("writing data to database");
		for (Customer customer : items) {
			LOG.info("-> Customer " + customer.getCustomerId() + " :: " + customer);
		}
	}
}
