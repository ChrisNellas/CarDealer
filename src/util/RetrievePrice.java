package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import model.DBConnector;

public class RetrievePrice implements Runnable{

	DBConnector con;
	Long mVId, eId, gbId;
	Double price;
	
	public RetrievePrice(Double price , Long modVerId, Long engId, Long gearboxId) {
		con = new DBConnector();
		this.price = price;
		mVId = modVerId;
		eId = engId;
		gbId = gearboxId; 
	}

	@Override
	public void run() {
		con.sendQuery("""
				SELECT price
				FROM engine_provided_with_gearbox
				where model_version_id = %s and engine_id = %s and gearbox_id = %s
				""".formatted(mVId, eId, gbId));
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					price = rs.getDouble("price");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}
	public double getPrice() {
		return price;
	}
}