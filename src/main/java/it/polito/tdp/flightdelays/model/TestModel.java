package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Model m = new Model();
		
		LocalDateTime a=LocalDateTime.of(2015, 01, 01, 9, 3);
		LocalDateTime b=LocalDateTime.of(2015, 01, 01, 10, 3);
		System.out.println(""+b.compareTo(a));
	}

}
