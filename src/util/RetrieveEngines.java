package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.DBConnector;
import model.Engine;
import model.Type;

/* To be fixed
 *  
 * the first constructor is used in order to make orders (EnginecomboBox inside CreateNewCarOrderController)
 * 	needs the line 101
 * the third constructor is used in order to present the available Engines for a specific model_version
 * 	needs the line 100
 * 
 * i have to make them work using the same line 
 * 
 * remember to delete 'availEngines = new HashSet<>();' from the 3rd constructor
 * 
*/
public class RetrieveEngines implements Runnable{

	DBConnector con;
	Set<Engine> availEngines, manufacturerEngines;
	Type type;
	Long mVId, brandId;
	
	ObservableList<Engine> list;
	
	
	public RetrieveEngines(Set<Engine> enginesSet, Long modelVersionId) {
		this.availEngines = enginesSet;
		con = new DBConnector();
		mVId = modelVersionId;
		list = FXCollections.observableArrayList();
	}
	
	public RetrieveEngines(Set<Engine> AvailEngineSet, Long modelVersionId, Set<Engine> manufacturerEngineSet, Long brandId) {
		this.availEngines = AvailEngineSet;
		this.manufacturerEngines = manufacturerEngineSet;
		con = new DBConnector();
		mVId = modelVersionId;
		this.brandId = brandId;
	}
	
	
	public RetrieveEngines(ObservableList<Engine> AvailEngineSet, Long modelVersionId, Set<Engine> manufacturerEngineSet, Long brandId) {
		this.list = AvailEngineSet;
		this.manufacturerEngines = manufacturerEngineSet;
		con = new DBConnector();
		mVId = modelVersionId;
		this.brandId = brandId;
		
//		delete it 
		availEngines = new HashSet<>();
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
//					temporary solution 
					list.add(new Engine(rs.getLong("id"), rs.getString("name").strip(), rs.getInt("power"), type)); //availEngines | i had list.add......??
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
