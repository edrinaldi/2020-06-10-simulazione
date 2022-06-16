package it.polito.tdp.imdb.model;

public class Event implements Comparable<Event> {
	public enum EventType {
		INTERVISTA,
		PAUSA,
	}
	
	private int time;
	private EventType type;
	private Actor attore;
	
	public Event(int time, EventType type, Actor attore) {
		super();
		this.type = type;
		this.time = time;
		this.attore = attore;
	}
	
	public EventType getType() {
		return type;
	}
	public int getTime() {
		return time;
	}
	public Actor getAttore() {
		return attore;
	}
	
	@Override
	public int compareTo(Event o) {
		// TODO Auto-generated method stub
		return this.time-o.time;
	}
}
