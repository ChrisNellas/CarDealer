package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class DBConnector {
	
	private final String USERNAME = "";
	private final String PASSWORD = "";
	private final String DB_NAME = "jdbc:mysql://127.0.0.1:3306/cars_db";
	private final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
	
	private Connection con;
	private ResultSet rs;
	private PreparedStatement pStmt;
	
	public void sendQuery(String query) {
		try {	
			Class.forName(DB_DRIVER);	 
			con = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
			synchronized (con) {
				pStmt = con.prepareStatement(query);
				rs = pStmt.executeQuery();	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Optional<ResultSet> getResults() {
		return Optional.ofNullable(rs);
	}
	
	public void updateDatabase(String query) {
		try {	
			Class.forName(DB_DRIVER);	 
			con = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
			synchronized (con) {
				pStmt = con.prepareStatement(query);
				pStmt.executeUpdate();	
			}
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
//	public void doManualyCommittedTransaction(List<String> queries) {
//		try {
//			Class.forName(DB_DRIVER);
//			con = DriverManager.getConnection(DB_NAME, USERNAME, PASSWORD);
//			con.setAutoCommit(false);
//			synchronized(con) {
//				
//			}
//		} catch (ClassNotFoundException e) {
//			e.printStackTrace();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}	 
//		
//	}
}
