package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;

public class Evento implements Comparable<Evento> {
	
	public enum Tipo{
		PARTENZA,
		ARRIVO
	}
	
	private LocalDateTime time;
	private Tipo tipo;
	private Airport aereoporto;
	private int passeggero;
	
	
	public Evento(LocalDateTime time, Tipo tipo, Airport aereoporto,int passeggero) {
		super();
		this.time = time;
		this.tipo = tipo;
		this.aereoporto = aereoporto;
		this.passeggero=passeggero;
	}

	public LocalDateTime getTime() {
		return time;
	}



	public void setTime(LocalDateTime time) {
		this.time = time;
	}


	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}


	public Airport getAereoporto() {
		return aereoporto;
	}


	public void setAereoporto(Airport aereoporto) {
		aereoporto = aereoporto;
	}

	
	
	public int getPasseggero() {
		return passeggero;
	}

	public void setPasseggero(int passeggero) {
		this.passeggero = passeggero;
	}

	@Override
	public int compareTo(Evento o) {
		// TODO Auto-generated method stub
		return this.getTime().compareTo(o.getTime());
	}
	
	
	

}
