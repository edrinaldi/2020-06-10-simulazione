package it.polito.tdp.imdb.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Model m = new Model();
		String msg = m.creaGrafo("Horror");
		System.out.println(msg);
		
		Actor aTest = m.getVertici().get(0);
		List<Actor> simili = m.trovaAttoriSimili(aTest);
		System.out.println(simili);
	}

}
