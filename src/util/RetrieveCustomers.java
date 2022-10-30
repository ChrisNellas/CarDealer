package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.Customer;
import model.DBConnector;

public class RetrieveCustomers implements Runnable{

	DBConnector con;
	Set<Customer> customers;
	
	public RetrieveCustomers(Set<Customer> customerSet) {
		this.customers = customerSet;
		con = new DBConnector();
	}

	@Override
	public void run() {
		con.sendQuery("SELECT * FROM customer");
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					customers.add(new Customer(rs.getLong("id"), rs.getString("name").strip(), rs.getString("address").strip(), rs.getDouble("dept")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}
