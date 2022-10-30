package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.CarBrand;
import model.DBConnector;

public class RetrieveBrands implements Runnable{

	DBConnector con;
	Set<CarBrand> brands;
	
	public RetrieveBrands(Set<CarBrand> brandsSet) {
		this.brands = brandsSet;
		con = new DBConnector();
	}

	@Override
	public void run() {
		con.sendQuery("SELECT * FROM brands");
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					brands.add(new CarBrand(rs.getLong("brand_id"),rs.getString("name").strip(),rs.getString("logo").strip()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}
