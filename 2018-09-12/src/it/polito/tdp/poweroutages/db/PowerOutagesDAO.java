package it.polito.tdp.poweroutages.db;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import it.polito.tdp.poweroutages.model.Adiacenza;
import it.polito.tdp.poweroutages.model.Nerc;

public class PowerOutagesDAO {
	
	public List<Nerc> loadAllNercs(Map<Integer, Nerc> idMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(n);
				idMap.put(n.getId(), n);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	public List<Adiacenza> getEdges() {

		String sql = "SELECT n1.id, n2.id, COUNT(DISTINCT YEAR(p1.date_event_began), MONTH(p1.date_event_began)) as cnt " + 
				"FROM poweroutages p1, poweroutages p2, nerc n1, nerc n2, nercrelations " + 
				"WHERE p1.nerc_id= n1.id AND p2.nerc_id= n2.id AND n1.id= nercrelations.nerc_one AND n2.id= nercrelations.nerc_two " + 
				"AND n1.id!= n2.id " + 
				"AND YEAR(p1.date_event_began)= YEAR(p2.date_event_began) " + 
				"AND MONTH(p1.date_event_began)= MONTH(p2.date_event_began) " + 
				"GROUP BY n1.id, n2.id ";
		List<Adiacenza> adList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Adiacenza a = new Adiacenza(res.getInt("n1.id"), res.getInt("n2.id"), res.getInt("cnt"));
				adList.add(a);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return adList;
	}

	public List<Adiacenza> getAdiacenze() {
		String sql = "SELECT n.nerc_one, n.nerc_two " + 
				"FROM nercrelations n ";
		List<Adiacenza> adList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				
				int peso=0;
				Adiacenza a = new Adiacenza(res.getInt("n.nerc_one"), res.getInt("n.nerc_two"), peso);
				adList.add(a);
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return adList;
	}


}
