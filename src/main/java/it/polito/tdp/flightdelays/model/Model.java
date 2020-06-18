package it.polito.tdp.flightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.flightdelays.db.FlightDelaysDAO;

public class Model {
	
	SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge> grafo= null;
	FlightDelaysDAO dao;
	Map<String,Airport> aereoporti;
	
	public Model() {
		dao = new FlightDelaysDAO();
	}
	
	public List<Airline> caricaLinee(){
		List<Airline> ritornare=new ArrayList<>();
		for(Airline a: dao.loadAllAirlines().values()) {
			ritornare.add(a);
		}
		ritornare.sort(null);
		return ritornare;
	}
	
	public String creaGrafo(Airline a) {
		String ritornare="GRAFO CREATO CON ";
		//non e' chiaro se i vertici debbano essere tutti gli aereoporti, ma suppongo di si'
		aereoporti=new HashMap<>();
		aereoporti=dao.loadAllAirports();
		
		grafo=new SimpleDirectedWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, aereoporti.values());
		
		//nella lista dei voli ci sono aereoporti con degli identificativi numerici che non sono caricati nella tabella airport e quelli
		//li vado ad escludere perche' non ho informazioni su di loro
		List<Adiacenza> connessioni=new LinkedList<>();
		connessioni=dao.loadAllAdiacenze(aereoporti, a);
		
		//inserisco gli archi
		for(Adiacenza ad: connessioni) {
			double peso=0.0;
			double mediaRitardi=((double) Math.abs(ad.getTotDelay()))/((double) ad.getTotVoli());
			peso=mediaRitardi/(ad.calcolaDistanza());
			System.out.println("DISTANZA: "+ad.calcolaDistanza());
			System.out.println(""+peso);
			if(peso>0.0) {
				Graphs.addEdge(grafo, ad.getP(), ad.getA(), peso);
			}
			ad.setPeso(peso);
			
		}
		
		connessioni.sort(null);
		
		ritornare=ritornare + ""+grafo.vertexSet().size()+" vertici "+grafo.edgeSet().size()+" lati.\n\n";
		
		for(int i=0;i<10;i++) {
			ritornare=ritornare+connessioni.get(i).getP().getId()+" "+connessioni.get(i).getA().getId()+" "+connessioni.get(i).getPeso()+"\n";
		}
		
		return ritornare;
	}
	
	private int numPasseggeri;
	private int numVoli;
	
	
	
	public int getNumPasseggeri() {
		return numPasseggeri;
	}

	

	public int getNumVoli() {
		return numVoli;
	}

	

	public String simulazione(int passeggeri, int voli) {
		String ritornare="SIMULAZIONE";
		numPasseggeri=passeggeri;
		numVoli=voli;
		Simulatore sim=new Simulatore(this);
		sim.init();
		sim.run();
		
		return ritornare;
	}

}
