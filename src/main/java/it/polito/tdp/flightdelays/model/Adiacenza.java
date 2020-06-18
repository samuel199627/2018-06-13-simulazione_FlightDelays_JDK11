package it.polito.tdp.flightdelays.model;
//package com.javadocmd.simplelatlng;

//import com.javadocmd.simplelatlng.util.LatLngConfig;
//import com.javadocmd.simplelatlng.util.LengthUnit;


public class Adiacenza implements Comparable<Adiacenza>{
	
	Airport p;
	Airport a;
	int totVoli;
	int totDelay;
	Double peso;
	
	public Adiacenza(Airport p, Airport a, int totVoli, int totDelay) {
		super();
		this.p = p;
		this.a = a;
		this.totVoli = totVoli;
		this.totDelay = totDelay;
		peso=0.0;
	}

	public Airport getP() {
		return p;
	}

	public void setP(Airport p) {
		this.p = p;
	}

	public Airport getA() {
		return a;
	}

	public void setA(Airport a) {
		this.a = a;
	}

	public int getTotVoli() {
		return totVoli;
	}

	public void setTotVoli(int totVoli) {
		this.totVoli = totVoli;
	}

	public int getTotDelay() {
		return totDelay;
	}

	public void setTotDelay(int totDelay) {
		this.totDelay = totDelay;
	}
	
	public double calcolaDistanza() {
	    double earthRadius = 3958.75; // miles (or 6371.0 kilometers)
	    double dLat = Math.toRadians(a.getLatitude()-p.getLatitude());
	    double dLng = Math.toRadians(a.getLongitude()-p.getLongitude());
	    double sindLat = Math.sin(dLat / 2);
	    double sindLng = Math.sin(dLng / 2);
	    double val = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
	            * Math.cos(Math.toRadians(p.getLatitude())) * Math.cos(Math.toRadians(a.getLatitude()));
	    double c = 2 * Math.atan2(Math.sqrt(val), Math.sqrt(1-val));
	    double dist = earthRadius * c;

	    return dist;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(double peso) {
		this.peso = peso;
	}

	@Override
	public int compareTo(Adiacenza o) {
		// TODO Auto-generated method stub
		return o.getPeso().compareTo(this.getPeso());
	}
	
	
	
	

}
