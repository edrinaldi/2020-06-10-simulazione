package it.polito.tdp.imdb.model;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	
	public Model() {
		this.dao = new ImdbDAO();
	}

	public Object getAllGeneri() {
		// TODO Auto-generated method stub
		return this.dao.getAllGeneri();
	}

}
