package util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

import model.DBConnector;
import model.Role;
import model.User;

public class RetrieveUsers implements Runnable{

	DBConnector con;
	Set<User> users;
	
	public RetrieveUsers(Set<User> UserSet) {
		this.users = UserSet;
		con = new DBConnector();
	}

	@Override
	public void run() {
		con.sendQuery("SELECT * FROM users");
		Optional<ResultSet> optRs = con.getResults();
		if(optRs.isPresent()) {
			ResultSet rs = optRs.get();
			try {
				while(rs.next()) {
					Role role = null;
					switch(rs.getString("role")) {
					case "ADMIN"-> role = Role.ADMIN;
					case "USER"-> role = Role.USER;
					}
					users.add(new User(rs.getLong("id"), rs.getString("name").strip(), rs.getString("username").strip(), rs.getString("password").strip(), role));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}	
	}	
}