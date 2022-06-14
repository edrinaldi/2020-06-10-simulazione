/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimili"
    private Button btnSimili; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimulazione"
    private Button btnSimulazione; // Value injected by FXMLLoader

    @FXML // fx:id="boxGenere"
    private ComboBox<String> boxGenere; // Value injected by FXMLLoader

    @FXML // fx:id="boxAttore"
    private ComboBox<Actor> boxAttore; // Value injected by FXMLLoader

    @FXML // fx:id="txtGiorni"
    private TextField txtGiorni; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doAttoriSimili(ActionEvent event) {
    	// pulisco l'area di testo
    	this.txtResult.clear();
    	
    	// verifico il grafo
    	if(!this.model.grafoCreato()) {
    		// stampo errore ed esco
    		this.txtResult.setText("Errore: devi prima creare il grafo!");
    		return;
    	}
    	
    	// verifico l'attore
    	Actor a = this.boxAttore.getValue();
    	if(a == null) {
    		// stampo errore ed esco
    		this.txtResult.setText("Errore: devi prima selezionare un attore!");
    		return;
    	}
    	
    	// trovo gli attori simili
    	List<Actor> simili = this.model.trovaAttoriSimili(a);
    	
    	// stampo il risultato
    	this.txtResult.setText("ATTORI SIMILI A: " + a.toString() + "\n");
    	for(Actor s : simili) {
        	this.txtResult.appendText(s.toString() + "\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	// pulisco l'area di testo
    	this.txtResult.clear();
    	
    	// verifico il genere
    	String genre = this.boxGenere.getValue();
    	if(genre == null) {
    		// stampo errore ed esco
    		this.txtResult.setText("Errore: devi prima selezionare un genere!");
    		return;
    	}
    	
    	// creo il grafo e stampo il messaggio
    	String msg = this.model.creaGrafo(genre);
    	this.txtResult.setText(msg);
    	
    	// pulisco e riempio la tendina degli attori
    	this.boxAttore.getItems().clear();
    	this.boxAttore.getItems().addAll(this.model.getVertici());
    }

    @FXML
    void doSimulazione(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimili != null : "fx:id=\"btnSimili\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimulazione != null : "fx:id=\"btnSimulazione\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGenere != null : "fx:id=\"boxGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAttore != null : "fx:id=\"boxAttore\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtGiorni != null : "fx:id=\"txtGiorni\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxGenere.getItems().addAll(this.model.getAllGeneri());
    }
}
