package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Simulator sim;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		this.dao.listAllActors(this.idMap);
	}
	
	public String creaGrafo(String genre) {
		// creo la struttura del grafo
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(this.grafo, this.dao.getActorsByGenre(genre, idMap));
		
		// aggiungo gli archi
		for(Adiacenza a : this.dao.getArchi(genre)) {
			Graphs.addEdge(this.grafo, this.idMap.get(a.getA1()), this.idMap.get(a.getA2()), a.getPeso());
		}
		
		// ritorno il messaggio di conferma
		return String.format("Grafo creato!\n# vertici: %d\n# archi: %d", this.grafo.vertexSet().size(),
				this.grafo.edgeSet().size());
	}
	
	public List<Actor> trovaAttoriSimili(Actor a) {
		ConnectivityInspector<Actor, DefaultWeightedEdge> connIn = 
				new ConnectivityInspector<>(this.grafo);
		List<Actor> simili = new ArrayList<>(connIn.connectedSetOf(a));
		simili.remove(a);
		Collections.sort(simili);
		return simili;
	}
	
	public void simula(int n) {
		this.sim = new Simulator(this.grafo);
		this.sim.init(n);
		this.sim.run();
	}

	public List<String> getAllGeneri() {
		// TODO Auto-generated method stub
		return this.dao.getAllGeneri();
	}

	public List<Actor> getVertici() {
		// TODO Auto-generated method stub
		List<Actor> vertici = new ArrayList<Actor>(this.grafo.vertexSet());
		Collections.sort(vertici);
		return vertici;
	}
	
	public boolean grafoCreato() {
		return (this.grafo != null);
	}

	public List<Actor> getInvervistati() {
		// TODO Auto-generated method stub
		return this.sim.getIntervistati();
	}

	public int getnPause() {
		// TODO Auto-generated method stub
		return this.sim.getnPause();
	}

}
