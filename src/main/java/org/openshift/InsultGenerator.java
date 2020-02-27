package org.openshift;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.log4j.Logger;

public class InsultGenerator {
	private Logger log = Logger.getLogger(InsultGenerator.class);

	public String generateInsult() {
		String vowels = "AEIOU";
		String article = "an";
		String theInsult = "";
		log.info("Inside the method");
		try {

			String databaseURL = "jdbc:postgresql://";
			databaseURL += System.getenv("POSTGRESQL_SERVICE_HOST");
			databaseURL += "/" + System.getenv("POSTGRESQL_DATABASE");
			String username = System.getenv("POSTGRESQL_USER");
			String password = System.getenv("PGPASSWORD");
			log.info("username "+username);
			log.info("password "+password);
			log.info("databaseURL "+databaseURL);
			Connection connection = DriverManager.getConnection(databaseURL, username, password);
			log.info("connection "+(connection==null));

			if (connection != null) {
				log.info("connection not null ");
				String SQL = "select a.string AS first, b.string AS second, c.string AS noun from short_adjective a , long_adjective b, noun c ORDER BY random() limit 1";
				Statement stmt = connection.createStatement();
				log.info("Runnning the SQL "+SQL);
				ResultSet rs = stmt.executeQuery(SQL); 
				while (rs.next()) {
					if (vowels.indexOf(rs.getString("first").charAt(0)) == -1) {
						article = "a";
					}
					theInsult = String.format("Thou art %s %s %s %s!", article, rs.getString("first"),
							rs.getString("second"), rs.getString("noun"));
					log.info("Inside the loop "+rs.getString("first"));
				}
				rs.close();
				connection.close();
				log.info("Closing the connection");
			}
		} catch (Exception e) {
			log.info(e.fillInStackTrace()); 
			return "Database connection problem!"; 
		}
		return theInsult;
	}
}