/*
 * STripleList.java
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

import java.util.ArrayList;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 * The Class STripleList extends the ArrayList<STriple> class to implement a replace method.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class STripleList extends ArrayList<STriple> {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1200405447771620096L;

	/**
	 * Sets the subject node for all the STriple.
	 *
	 * @param s the subject node
	 * @return the STriple list
	 */
	public STripleList setS(Node s){
		for (STriple st : this){
			st.setS(s);
		}
		return this;
	}
	
	/**
	 * Replace the <b>old</b> node with the <b>n</b> one.
	 *
	 * @param old the old
	 * @param n the n
	 * @return the STriple list
	 */
	public STripleList replace(Node old, Node n){
		if (n == null) return this;
		for (STriple st : this){
			st.replace(old, n);
		}
		return this;
	}
	
	/**
	 * Return <b>this</b> as an ArrayList<Triple>.
	 * @see java.util.ArrayList
	 * @return <b>this</b> instance as an ArrayList of Jena Triple instances
	 */
	public ArrayList<Triple> asTripleList(){
		 ArrayList<Triple> al = new ArrayList<Triple>();
		 for (STriple st : this){
				al.add(st.asTriple());
		 }
		 return al;
	}
}
