package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.Gearbox;
import model.GearboxType;

public class RetrieveGearboxes implements Runnable{

	DBConnector con;
	Set<Gearbox> gearboxes;
	Long mVId, eId, brandId;
	
	//retrieves all gearboxes that is available with a specific model version and engine
	public RetrieveGearboxes(Set<Gearbox> gearboxes, Long modVerId, Long engId) {
		this.gearboxes = gearboxes;
		con = new DBConnector();
		mVId = modVerId;
		eId = engId;
	}
	
	//retrieves all gearboxes that a brand is using
	public RetrieveGearboxes(Set<Gearbox> gearboxes,Long brandId) {
		this.gearboxes = gearboxes;
		con = new DBConnector();
		this.brandId = brandId;
	}

	@Override
	public void run() {
		if(brandId==null) {
			con.sendQuery("""
					SELECT distinct g.* 
					FROM engine_provided_with_gearbox ePWG join gearbox g on gearbox_id=g.id 
					where model_version_id = %s and engine_id = %s
					""".formatted(mVId, eId));	
		} else {
			con.sendQuery("""
					select gb.* 
					from brands br join car_model cM on br.brand_id=cM.brand_id 
						join model_version mV on cM.model_id=mV.model_id 
					    join engine_provided_with_gearbox ePWG on mV.version_id=ePWG.model_version_id 
					    join gearbox gb on ePWG.gearbox_id=gb.id
					where br.brand_id = %s
					""".formatted(brandId));
			
		}
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					gearboxes.add(new Gearbox(rs.getLong("id"),rs.getString("name").strip(), 
							rs.getString("type").strip().equals("MANUAL")?GearboxType.MANUAL:GearboxType.AUTOMATIC, rs.getInt("gears")));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}
