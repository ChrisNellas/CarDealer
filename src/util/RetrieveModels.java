package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.Model;

public class RetrieveModels implements Runnable{

	DBConnector con;
	Set<Model> models;
	Long brandId;
	
	public RetrieveModels(Set<Model> modelSet, Long bId) {
		this.models = modelSet;
		con = new DBConnector();
		brandId = bId;
	}

	@Override
	public void run() {
		con.sendQuery("SELECT * FROM car_model where brand_id="+brandId);
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					models.add(new Model(rs.getLong("model_id"), rs.getString("model_name").strip()));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}
