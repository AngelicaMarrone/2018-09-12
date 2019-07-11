package it.polito.tdp.poweroutages.model;

public class ViciniPeso implements Comparable<ViciniPeso>{
	
	private Nerc n;
	private int peso;
	public ViciniPeso(Nerc n, int peso) {
		super();
		this.n = n;
		this.peso = peso;
	}
	public Nerc getN() {
		return n;
	}
	public void setN(Nerc n) {
		this.n = n;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}
	@Override
	public int compareTo(ViciniPeso altro) {
		
		return -(this.peso-altro.getPeso());
	}
	
	

}
