package model;

public class Customer {
	private final String customerId;
	private final String name;
	private final String email;
	private final String phone;

	public Customer(String id, String name, String email, String phone) {
		this.customerId = id;
		this.name = name;
		this.email = email;
		this.phone = phone;
	}

	public String getCustomerId() {
		return customerId;
	}
	public String getName() {
		return name; 
	}
	public String getEmail() { 
		return email;
	}
	public String getPhone() { 
		return phone; 
	}
}
