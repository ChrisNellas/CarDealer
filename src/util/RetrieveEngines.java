package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.Engine;
import model.Type;

public class RetrieveEngines implements Runnable{

	DBConnector con;
	Set<Engine> availEngines, manufacturerEngines;
	Type type;
	Long mVId, brandId;
	
	public RetrieveEngines(Set<Engine> enginesSet, Long modelVersionId) {
		this.availEngines = enginesSet;
		con = new DBConnector();
		mVId = modelVersionId;
	}
	
	public RetrieveEngines(Set<Engine> AvailEngineSet, Long modelVersionId, Set<Engine> manufacturerEngineSet, Long brandId) {
		this.availEngines = AvailEngineSet;
		this.manufacturerEngines = manufacturerEngineSet;
		con = new DBConnector();
		mVId = modelVersionId;
		this.brandId = brandId;
	}

	@Override
	public void run() {
		if(this.brandId!=null) { // if i want to get all brand's engines too
			con.sendQuery("""
					select distinct ngn.* 
					from car_model cM join model_version mV on cM.model_id=mV.model_id 
					    join engine_provided_with_gearbox ePWG on mV.version_id=ePWG.model_version_id 
					    join engine ngn on ePWG.engine_id=ngn.id
					where cM.brand_id = %s
					""".formatted(brandId));
			Optional<ResultSet> optRs = con.getResults();
			if(optRs.isPresent()) {
				ResultSet rs = optRs.get();
				try {
					while(rs.next()) {
						type = getEngineType(rs.getString("type"));
						manufacturerEngines.add(new Engine(rs.getLong("id"), rs.getString("name").strip(), rs.getInt("power"), type));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		con.sendQuery("""
				SELECT distinct eng.* 
				FROM engine_provided_with_gearbox ePWG join engine eng on engine_id=eng.id 
				where model_version_id = %s
				""".formatted(mVId));
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					type = getEngineType(rs.getString("type"));
					availEngines.add(new Engine(rs.getLong("id"), rs.getString("name").strip(), rs.getInt("power"), type));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
	
	private Type getEngineType(String typeString) {
		Type type = null;
		switch (typeString) {
			case "HYBRID" -> type= Type.HYBRID;
			case "PETROL" -> type= Type.PETROL;
			case "DIESEL" -> type= Type.DIESEL;
			case "ELECTRIC" -> type= Type.ELECTRIC;
			case "HIDROGEN" -> type= Type.HYDROGEN;
			case "LPG" -> type= Type.LPG;
			case "PLUG_IN_HYBRID" -> type= Type.PLUG_IN_HYBRID;
		}
		return type;
	}
}
