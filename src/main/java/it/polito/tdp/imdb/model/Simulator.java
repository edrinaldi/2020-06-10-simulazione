package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.imdb.model.Event.EventType;

public class Simulator {
	// dati in ingresso
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private int n;
	
	// dati in uscita
	private List<Actor> intervistati;
	private int nPause;
	
	// modello del mondo
	private Actor attorePrecedente;
	private List<Actor> daIntervistare;
	
	// coda degli eventi
	private Queue<Event> queue;
	
	/*
	 * costruisce un nuovo simulatore
	 */
	public Simulator(Graph<Actor, DefaultWeightedEdge> grafo) {
		// assegno il grafo ricevuto
		this.grafo = grafo;
	}
	
	/*
	 * inizia una nuova simulazione (con lo stesso simulatore)
	 */
	public void init(int n) {
		this.n = n;
		
		// inizializzo gli output
		this.intervistati = new ArrayList<>();
		this.nPause = 0;

		// inizializzo il mondo
		this.daIntervistare = new ArrayList<>(this.grafo.vertexSet());
		this.attorePrecedente = null;
		
		// inizializzo la coda
		this.queue = new PriorityQueue<>();
		
		// carico la coda per il primo giorno
		Actor attore = this.selezionaCasuale(this.daIntervistare);
		this.queue.add(new Event(1, EventType.INTERVISTA, attore));
		
		// aggiorno il modello del mondo
		this.daIntervistare.remove(attore);
	}

	/*
	 * avvia la simulazione
	 */
	public void run() {
		// estraggo gli eventi dalla coda
		while(!this.queue.isEmpty()) {
			Event e = this.queue.poll();
			
			// processo l'evento
			this.processEvent(e);
		}
	}
	
	/*
	 * processa l'evento
	 */
	private void processEvent(Event e) {
		Actor attore;
		
		// tratto i vari tipi di evento
		switch(e.getType()) {
		case INTERVISTA:
			
			// verifico punto d.
			if(this.attorePrecedente != null &&
					e.getAttore().getGender().compareTo(this.attorePrecedente.getGender()) == 0
					&& Math.random() < 0.9) {
				
				if(e.getTime() < this.n) {
					
					// carico la coda con una pausa
					this.queue.add(new Event(e.getTime()+1, EventType.PAUSA, null));
					
					// aggiorno il modello del mondo
					this.attorePrecedente = null;
					this.nPause++;
				}
				return;
			}
			
			// scelgo il prossimo attore da intervistare
			if(Math.random() < 0.6) {
				
				// scelgo il prossimo attore in modo casuale
				attore = this.selezionaCasuale(this.daIntervistare);	// TODO aggiungere parametro
			}
			else {
				
				// mi faccio consigliare il prossimo attore dall'ultimo intervistato
				attore = this.consigliaAttore(e.getAttore());
			}
			
			if(e.getTime() < this.n) {
				// carico la coda con un'intervista
				this.queue.add(new Event(e.getTime()+1, EventType.INTERVISTA, attore));
				
				// aggiorno il modello del mondo
				this.attorePrecedente = e.getAttore();
				this.daIntervistare.remove(attore);
			}
			break;
		case PAUSA:
			
			// scelgo il prossimo attore in modo casuale
			attore = this.selezionaCasuale(this.daIntervistare);
			
			if(e.getTime() < this.n) {
				
				// carico la coda con un'intervista
				this.queue.add(new Event(e.getTime()+1, EventType.INTERVISTA, attore));
				
				// aggiorno il modello del mondo
				this.attorePrecedente = e.getAttore();
				this.daIntervistare.remove(attore);
			}
			break;
		}
	}
	
	private Actor selezionaCasuale(List<Actor> attori) {
		int caso = (int)(Math.random()*attori.size());
		return attori.get(caso);
	}

	private Actor consigliaAttore(Actor attore) {
		double max = 0;
		List<Actor> consigliati = new ArrayList<Actor>();
		List<Actor> colleghi = Graphs.neighborListOf(this.grafo, attore);
		if(colleghi.size() == 0) {
			
			// seleziono a caso tra gli attori non ancora intervistati e lo restituisco
			return this.selezionaCasuale(daIntervistare);
		}
		
		// trovo il "grado" massimo tra i non intervistati
		for(Actor a : colleghi) {
			if(this.daIntervistare.contains(a)) {
				DefaultWeightedEdge e = this.grafo.getEdge(attore, a);
				double peso = this.grafo.getEdgeWeight(e);
				if(peso > max) {
					max = peso;
				}
			}
		}
		
		// trovo i colleghi di grado massimo ancora da intervistare
		for(Actor a : colleghi) {
			if(this.daIntervistare.contains(a)) {
				DefaultWeightedEdge e = this.grafo.getEdge(attore, a);
				double peso = this.grafo.getEdgeWeight(e);
				if(peso == max) {
					consigliati.add(a);
				}
			}
		}
		
		// seleziono a caso uno di essi e lo restituisco
		return this.selezionaCasuale(consigliati);
	}

	public List<Actor> getIntervistati() {
		// TODO Auto-generated method stub
		for(Actor a : this.grafo.vertexSet()) {
			if(!this.daIntervistare.contains(a)) {
				this.intervistati.add(a);
			}
		}
		return this.intervistati;
	}

	public int getnPause() {
		return this.nPause;
	}
}
