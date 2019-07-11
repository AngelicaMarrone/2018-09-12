package it.polito.tdp.poweroutages.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.poweroutages.db.PowerOutagesDAO;

public class Model {
	
	
private PowerOutagesDAO dao;

	

	//scelta valore mappa

	 private Map<Integer,Nerc> idMap;

	

	//scelta tipo valori lista

	private List<Nerc> vertex;

	

	//scelta tra uno dei due edges

	private List<Adiacenza> edges;

	

	//scelta tipo vertici e tipo archi

	private Graph<Nerc, DefaultWeightedEdge> graph;


	private Nerc source;
	private Nerc target;

	

	public Model() {

		

		//inserire tipo dao

		dao  = new PowerOutagesDAO();

		//inserire tipo values

		idMap = new HashMap<Integer,Nerc>();

	}

	

	public List<Nerc> creaGrafo() {

		

		//scelta tipo vertici e archi

		graph = new SimpleWeightedGraph<Nerc,DefaultWeightedEdge>(DefaultWeightedEdge.class);

		

		//scelta tipo valori lista

		vertex = new ArrayList<Nerc>(dao.loadAllNercs(idMap));

		Graphs.addAllVertices(graph,vertex);

		

		edges = new ArrayList<Adiacenza>(dao.getEdges());

		

		for(Adiacenza a : edges) {

			

			//CASO BASE POTRESTI DOVER AGGIUNGERE CONTROLLI

		 source = idMap.get(a.getId1());

		 target = idMap.get(a.getId2());

			double peso = a.getPeso();

			Graphs.addEdge(graph,source,target,peso);

			System.out.println("AGGIUNTO ARCO TRA: "+source.toString()+" e "+target.toString());

			

		}

		
		
		List<Adiacenza> relations= dao.getAdiacenze();
		
		relations.removeAll(edges);
		
		for (Adiacenza a: relations)
		{
			 source = idMap.get(a.getId1());

			 target = idMap.get(a.getId2());

				double peso = 0;

				Graphs.addEdge(graph,source,target,peso);

				System.out.println("AGGIUNTO ARCO TRA: "+source.toString()+" e "+target.toString());
			
		}
		

		System.out.println("#vertici: "+graph.vertexSet().size());

		System.out.println("#archi: "+graph.edgeSet().size());

		
		return vertex;
	}



	public String visualizzaVicini(Nerc n) {
		
		List<Nerc> vicini= Graphs.neighborListOf(this.graph, n);
		List<ViciniPeso> vicpeso= new ArrayList<>();
		
		
		
		for (Nerc v: vicini)
		{
			
			DefaultWeightedEdge e= graph.getEdge(n, v);
			int peso= (int) graph.getEdgeWeight(e);
			
			vicpeso.add(new ViciniPeso(v,peso));
			
		}
		
		Collections.sort(vicpeso);
		
	if (vicpeso.size()!=0)	
		{String ris= "La lista dei vicini è: \n";
		for (ViciniPeso vp: vicpeso)
		{
			ris+= "- "+ vp.getN().getValue() + " " + vp.getPeso() + "\n";
		}
			
	return ris;}
	
	String ris= "Non ci sono vicini\n";
	
	return ris ;
		
	}

}
