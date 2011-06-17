/*
 * STriple.java
 *
 * Copyright (C) ECS University of Southampton, 2011
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package uk.soton.service.mediation;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 * The Class STriple implements a flexible RDF Triple that implements a replacement feature.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class STriple {
	
	/**
	 * The field s is the subject of the triple.
	 */
	private Node s;
	
	/**
	 * The field p is the property of the triple.
	 */
	private Node p;
	
	/**
	 * The field o is the object of the triple.
	 */
	private Node o;

	/**
	 * Sets the property field.
	 *
	 * @param p the property node
	 * @return the triple itself
	 */
	public STriple setP(Node p) {
		this.p = p;
		return this;
	}

	/**
	 * Gets the property field.
	 *
	 * @return the p field
	 */
	public Node getP() {
		return p;
	}

	/**
	 * Sets the subject field.
	 *
	 * @param s the subject node
	 * @return the triple itself
	 */
	public STriple setS(Node s) {
		this.s = s;
		return this;
	}

	/**
	 * Gets the subject field.
	 *
	 * @return the s field
	 */
	public Node getS() {
		return s;
	}

	/**
	 * Sets the object field.
	 *
	 * @param o the object field
	 * @return the triple itself
	 */
	public STriple setO(Node o) {
		this.o = o;
		return this;
	}

	/**
	 * Gets the object field.
	 *
	 * @return the o field
	 */
	public Node getO() {
		return o;
	}
	
	/**
	 * Replace the <b>old</b> node with the <b>n</b> one.
	 *
	 * @param old the old node
	 * @param n the new node
	 * @return the triple itself
	 */
	public STriple replace(Node old, Node n){
		if (old == null || n == null) return this;
		if (this.s != null && this.s.sameValueAs(old)) this.setS(n);
		if (this.p != null && this.p.sameValueAs(old)) this.setP(n);
		if (this.o != null && this.o.sameValueAs(old)) this.setO(n);
		return this;	
	}
	
	/**
	 * Return <b>this</b> as a Jena Triple.
	 *
	 * @return the triple
	 */
	public Triple asTriple(){
		return new Triple(this.s , this.p , this.o);
	}

	/**
	 * Instantiates a new STriple with the input s,p, and o.
	 *
	 * @param s the subject
	 * @param p the property
	 * @param o the object
	 */
	public STriple(Node s , Node p , Node o ){
		this.s = s;
		this.p = p;
		this.o = o;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return String.format("< %s , %s , %s>", s,p,o);
	}
}
