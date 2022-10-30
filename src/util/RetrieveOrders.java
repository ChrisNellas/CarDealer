package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.Order;
import model.Type;

public class RetrieveOrders implements Runnable{
	private DBConnector con;
	private Set<Order> orderSet;

	public RetrieveOrders(Set<Order> orders){
		con = new DBConnector();
		orderSet = orders;
	}
	
	@Override
	public void run() {
		con.sendQuery("SELECT * FROM orders");	
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					//get the fuel type of each ordered car
					String givenType = rs.getString(7);
					Type type = null;
					switch(givenType) {
					case "DIESEL"-> type=Type.DIESEL;
					case "PETROL"-> type=Type.PETROL;
					case "ELECTRIC"-> type=Type.ELECTRIC;
					case "HYBRID"-> type=Type.HYBRID;
					case "HIDROGEN"-> type=Type.HYDROGEN;
					case "PLUG_IN_HYBRID"-> type=Type.PLUG_IN_HYBRID;
					case "LPG" -> type=Type.LPG;
					}
					//get the date of each order
					String date = rs.getString(11);
					LocalDate localDate = LocalDate.parse(date);
					
					orderSet.add(new Order(rs.getDouble(2), rs.getLong(3), 
							rs.getLong(4), rs.getString(10).strip(), rs.getLong(5), 
							type, rs.getLong(6), rs.getLong(8), localDate));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
