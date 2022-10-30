package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.ModelVersion;

public class RetrieveModelVersions implements Runnable{

	DBConnector con;
	Set<ModelVersion> modelVersions;
	Long modelId;
	
	public RetrieveModelVersions(Set<ModelVersion> modelVersionSet, Long mId) {
		this.modelVersions = modelVersionSet;
		con = new DBConnector();
		modelId = mId;
	}

	@Override
	public void run() {
		con.sendQuery("SELECT * FROM model_version where model_id="+modelId);
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					String text = rs.getString(7);
					text = text.substring(1, text.length()-1);
					String[] subs = text.split(", ");
					Set<String> stringSet = new HashSet<>();
					for(int i=0;i<subs.length;i++) {
						subs[i] = subs[i].substring(1, subs[i].length()-1);
						stringSet.add(subs[i]);
					}
					modelVersions.add(new ModelVersion(rs.getLong("version_id"), rs.getString("version_name").strip(),rs.getString(6)==null?null:rs.getString(6).strip(), rs.getBoolean(5), rs.getBoolean(4), stringSet));
				}
//				modelVersions.forEach(System.out::println);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}