package it.polito.tdp.flightdelays.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import it.polito.tdp.flightdelays.model.Evento.Tipo;

public class Simulatore {
	Model m;
	List<Flight> voli;
	Map<String,Airport> aereoporti;
	List<Airport> aereoportiLista;
	
	int numPasseggeri;
	int numVoli;
	
	Integer[] voliPasseggeri;
	Integer[] ritardoPasseggeri;
	
	public Simulatore(Model model) {
		m=model;
	}
	
	PriorityQueue<Evento> coda;
	public void init() {
		coda=new PriorityQueue();
		voli=new ArrayList<>();
		//voli solo tra gli aereoporti precedentemente creati
		voli=m.dao.loadAllFlights(m.aereoporti);
		numPasseggeri=m.getNumPasseggeri();
		numVoli=m.getNumVoli();
		aereoporti=new HashMap<>();
		aereoporti=m.aereoporti;
		aereoportiLista=new ArrayList<>();
		for(Airport a: aereoporti.values()) {
			aereoportiLista.add(a);
		}
		
		voliPasseggeri=new Integer[numPasseggeri];
		ritardoPasseggeri=new Integer[numPasseggeri];
		
		for(int i=0; i<numPasseggeri; i++) {
			voliPasseggeri[i]=0;
			ritardoPasseggeri[i]=0;
		}
		
		//posiziono in maniera casuale i k passeggeri tra gli aereoporti creando gli eventi di partenza in pratica
		for(int i=0;i<numPasseggeri;i++) {
			double random=Math.random();
			int size=aereoportiLista.size();
			int indice=(int) (random*size);
			Airport partenza=aereoportiLista.get(indice);
			//devo prendere il primo volo che parte da questo aereoporto
			Flight volo=cercaVolo(partenza, null);
			Evento e = new Evento(volo.getArrivalDate(),Tipo.ARRIVO,aereoporti.get(volo.getDestinationAirportId().getId()),i);
			voliPasseggeri[i]++;
			ritardoPasseggeri[i]=ritardoPasseggeri[i]+volo.getArrivalDelay();
			System.out.println("AGGIUNTO EVENTO ARRIVO: "+e.getTime()+" "+e.getAereoporto().getId()+" passeggero "+e.getPasseggero());
			coda.add(e);
		}
		
	}
	
	public Flight cercaVolo(Airport a, LocalDateTime t) {
		if(t==null) {
			//stiamo cercando il primo volo
			for(Flight f:voli) {
				if(f.getOriginAirportId().equals(a)) {
					return f;
				}
			}
		}
		else {
			//dobbiamo cercare un volo successivo alla data di arrivo, cioe' la data che ho passato deve venire prima di quella del volo che prendo
			for(Flight f:voli) {
				if(f.getOriginAirportId().equals(a)&&t.compareTo(f.getScheduledDepartureDate())<0) {
					//System.out.println(""+t.compareTo(f.getScheduledDepartureDate()));
					return f;
				}
			}
		}
		
		
		
		
		return null;
	}
	
	
	
	public void run() {
		while(!coda.isEmpty()) {
			Evento e=coda.poll();
			processEvent(e);
		}
	}
	
	public void processEvent(Evento e) {
		
		switch(e.getTipo()) {
		case ARRIVO:
			//se il passeggero ha ancora voli disponibili e ce ne sono in partenza dall'aereoporto selezionato creo un nuovo evento
			if(voliPasseggeri[e.getPasseggero()]<numVoli) {
				//il passeggero puo' ancora viaggiare e gli aggiungo il nuovo volo se c'e'
				Flight volo=cercaVolo(e.getAereoporto(),e.getTime());
				if(volo!=null) {
					//c'e' un volo inp partenza
					Evento ev = new Evento(volo.getArrivalDate(),Tipo.ARRIVO,aereoporti.get(volo.getDestinationAirportId().getId()),e.getPasseggero());
					voliPasseggeri[e.getPasseggero()]++;
					ritardoPasseggeri[e.getPasseggero()]=ritardoPasseggeri[e.getPasseggero()]+volo.getArrivalDelay();
					System.out.println("AGGIUNTO EVENTO ARRIVO: "+ev.getTime()+" "+ev.getAereoporto().getId()+" passeggero "+ev.getPasseggero());
					coda.add(ev);
				}
				//altrimenti non aggiungo nessun evento e lascio morire tutto cosi'
			}
			break;
		}
		
	}
	

}
