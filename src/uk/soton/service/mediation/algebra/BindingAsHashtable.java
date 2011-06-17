/*
 * BindingAsHashtable.java
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
package uk.soton.service.mediation.algebra;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.engine.binding.Binding;


/**
 * The Class BindingAsHashtable implements the Jena ARQ <a href="http://www.openjena.org/ARQ/javadoc/com/hp/hpl/jena/sparql/engine/binding/Binding.html">Binding</a> interface
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class BindingAsHashtable implements Binding{

	/**
	 * The field inner is the internal Hashtable that stores the variable values.
	 */
	private Hashtable<Node,Node> inner;
	
	private BindingAsHashtable(){}
	
	/**
	 * The getBinding creates a new BindingAsHashtable with the input Hashtable
	 * @param table Hashtable with the variables bindings
	 * @return new instance of BindingAsHashtable class 
	 * 
	 */
	static public BindingAsHashtable getBinding(Hashtable<Node,Node> table){
		BindingAsHashtable result = new BindingAsHashtable();
		result.inner = table;
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#add(com.hp.hpl.jena.sparql.core.Var, com.hp.hpl.jena.graph.Node)
	 */
	@Override
	public void add(Var var, Node node) {
		this.inner.put((Node) var, node); 		
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#addAll(com.hp.hpl.jena.sparql.engine.binding.Binding)
	 * TODO: to implement
	 */
	@Override
	public void addAll(Binding key) {
		/*for (Var var : key.vars()){
			this.inner.put(var, key.get(var));
		}*/		
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#contains(com.hp.hpl.jena.sparql.core.Var)
	 */
	@Override
	public boolean contains(Var var) {
		return this.inner.contains(var);
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#get(com.hp.hpl.jena.sparql.core.Var)
	 */
	@Override
	public Node get(Var var) {
		return this.inner.get(var);
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#getParent()
	 */
	@Override
	public Binding getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.inner.isEmpty();
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#size()
	 */
	@Override
	public int size() {
		return this.inner.size();
	}

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.engine.binding.Binding#vars()
	 */
	@Override
	public Iterator<Var> vars() {
		ArrayList<Var> result = new ArrayList<Var>();
		for (Node var : this.inner.keySet()){
			result.add((Var) var);
		}
		return result.iterator();
	}

}
