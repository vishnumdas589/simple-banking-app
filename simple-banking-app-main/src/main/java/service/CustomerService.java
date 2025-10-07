package service;

import model.Customer;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
	private final Map<String, Customer> customers = new HashMap<>();

	public void createCustomer(Customer c) {
		customers.put(c.getCustomerId(), c);
	}

	public Customer getCustomer(String id) {
		return customers.get(id);
	}
}
