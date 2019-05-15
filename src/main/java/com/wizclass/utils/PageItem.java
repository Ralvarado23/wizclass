package com.wizclass.utils;

/**
 * This class presents each of the items used in a pagination.
 * @author Raul Alvarado
 *
 */
public class PageItem {
	private int numero;
	private boolean actual;
	
	public PageItem(int numero, boolean actual) { 
		this.numero = numero;
		this.actual = actual;
	}

	public int getNumero() {
		return numero;
	}

	public boolean isActual() {
		return actual;
	}

}