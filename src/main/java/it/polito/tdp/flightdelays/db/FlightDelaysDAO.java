package it.polito.tdp.flightdelays.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.flightdelays.model.Adiacenza;
import it.polito.tdp.flightdelays.model.Airline;
import it.polito.tdp.flightdelays.model.Airport;
import it.polito.tdp.flightdelays.model.Flight;

public class FlightDelaysDAO {

	public Map<String,Airline> loadAllAirlines() {
		String sql = "SELECT id, airline from airlines";
		Map<String,Airline> result = new HashMap<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				result.put(rs.getString("ID"),new Airline(rs.getString("ID"), rs.getString("airline")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public Map<String,Airport> loadAllAirports() {
		String sql = "SELECT id, airport, city, state, country, latitude, longitude FROM airports";
		Map<String,Airport> result = new HashMap<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Airport airport = new Airport(rs.getString("id"), rs.getString("airport"), rs.getString("city"),
						rs.getString("state"), rs.getString("country"), rs.getDouble("latitude"), rs.getDouble("longitude"));
				result.put(airport.getId(),airport);
			}
			
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<Flight> loadAllFlights(Map<String, Airport> aereoporti) {
		String sql = "SELECT id, airline, flight_number, origin_airport_id, destination_airport_id, scheduled_dep_date, arrival_date, departure_delay, arrival_delay, air_time, distance " + 
				" FROM flights " + 
				" order by scheduled_dep_date asc ";
		List<Flight> result = new LinkedList<Flight>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				
				String aereoportoP=rs.getString("origin_airport_id");
				String aereoportoA=rs.getString("destination_airport_id");
				
				if(aereoporti.containsKey(aereoportoP)&&aereoporti.containsKey(aereoportoA)) {
					Flight flight = new Flight(rs.getInt("id"), rs.getString("airline"), rs.getInt("flight_number"),
							aereoporti.get(aereoportoP), aereoporti.get(aereoportoA),
							rs.getTimestamp("scheduled_dep_date").toLocalDateTime(),
							rs.getTimestamp("arrival_date").toLocalDateTime(), rs.getInt("departure_delay"),
							rs.getInt("arrival_delay"), rs.getInt("air_time"), rs.getInt("distance"));
					
					result.add(flight);
				}
				
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<Adiacenza> loadAllAdiacenze(Map<String,Airport> aereoporti, Airline linea) {
		String sql = "select origin_airport_id,destination_airport_id,count(*) as tot_voli, sum(arrival_delay) as tot_ritardo " + 
				"from flights " + 
				"where airline=? " + 
				"group by origin_airport_id,destination_airport_id ";
		List<Adiacenza> result = new LinkedList<>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, linea.getId());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				String aereoportoP=rs.getString("origin_airport_id");
				String aereoportoA=rs.getString("destination_airport_id");	
				
				if(aereoporti.containsKey(aereoportoP)&&aereoporti.containsKey(aereoportoA)) {
					Adiacenza a=new Adiacenza(aereoporti.get(aereoportoP),aereoporti.get(aereoportoA),rs.getInt("tot_voli"),rs.getInt("tot_ritardo"));
					result.add(a);
				}
				
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	
}

