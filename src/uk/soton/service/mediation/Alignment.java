/*
 * Alignment.java
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

import java.util.Hashtable;
import java.util.List;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/** 
 * The Interface Alignment defines the basic information contained in an ontology alignment based on RDF graph rewriting rules.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public interface Alignment {
	
	/**
	 * The Enum Relation encodes the different kind of alignments.
	 */
	public enum Relation {/** The EQuivalence relation. */
EQ};
	
	/**
	 * Gets the rewriting patterns in an Hashtable, the key is the Left Hand Side triple.
	 *
	 * @return an Hashtable with all the patterns contained in the alignment.
	 */
	public Hashtable<Triple,List<Triple>> getPatterns();
	
  /**
	 * Gets the functional dependencies in an Hashtable, the key is the Left Hand Side triple..
	 *
	 * @return an Hashtable with all the functional dependencies contained in the alignment.
	 */
	public Hashtable<Triple,Hashtable<Node, FunctionalDependency>> getFunctionalDependencies();
	
	/**
	 * Adds the rewriting rule to <b>this</b> alignment.
	 *
	 * @param rule the rewriting rule
	 */
	 public void addRewritingRule(RewritingRule rule);
  
	
	/**
	 * Sets the source ontology URIs.
	 *
	 * @param sourceOntologyURIs list of URIs of source ontologies managed by the alignment
	 * to set.
	 */
	public void setSourceOntologyURIs(List<String> sourceOntologyURIs);
	
	/**
	 * Gets the source ontology URIs.
	 *
	 * @return list of URIs for the source ontologies managed by the alignment.
	 */
	public List<String> getSourceOntologyURIs(); 
	
	/**
	 * Sets the target ontology URIs.
	 *
	 * @param targetOntologyURIs list of URIs of target ontologies managed by the alignment
	 * to set.
	 */
	public void setTargetOntologyURIs(List<String> targetOntologyURIs);
	
	/**
	 * Gets the target ontology URIs.
	 *
	 * @return list of URIs for the target ontologies managed by the alignment.
	 */
	public List<String> getTargetOntologyURIs(); 
	
	/**
	 * Sets the target dataset URIs.
	 *
	 * @param targetDatasetURIs list of URIs of target datasets managed by the alignment
	 * to set.
	 */
	public void setTargetDatasetURIs(List<String> targetDatasetURIs);
	
	/**
	 * Gets the target dataset URIs.
	 *
	 * @return list of URIs for the target datasets managed by the alignment.
	 */
	public List<String> getTargetDatasetURIs();
	
	/**
	 * Match the input lhs to the managed alignments.
	 *
	 * @param t Triple to match against the LHS of all the patterns contained
	 * in the alignment.
	 * @return null if any of the LHS matched, a matching result otherwise
	 */
	public MatchingResult matchLHS(Triple t);
	
		
	/**
	 * The Class MatchingResult represent the result of a matching between a BGP triple and this ontology alignment.
     * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
	 */
	public class MatchingResult{
		
		/** The rhs. */
		private List<Triple> rhs;		
		
		/** The binding. */
		private Hashtable<Node,Node> binding;
		
		/** The fdependencies. */
		private Hashtable<Node,FunctionalDependency> fdependencies;
		
		/**
		 * Instantiates a new matching result.
		 *
		 * @param rhs the Right Hand Side
		 * @param binding the binding between variables
		 * @param fdependencies the functional dependencies
		 */
		public MatchingResult(List<Triple> rhs, Hashtable<Node,Node> binding, Hashtable<Node,FunctionalDependency> fdependencies){
			this.rhs = rhs;
			this.binding = binding;
			this.fdependencies = fdependencies;
		}
		//Getters
		/**
		 * Gets the Right Hand Side.
		 *
		 * @return the Right Hand Side
		 */
		public List<Triple> getRhs() {
			return rhs;
		}
		
		/**
		 * Gets the binding between variables.
		 *
		 * @return the binding
		 */
		public Hashtable<Node, Node> getBinding() {
			return binding;
		}		
		
		/**
		 * Gets the functional dependencies.
		 *
		 * @return the functional dependencies
		 */
		public Hashtable<Node,FunctionalDependency> getFunctionalDependencies(){
			return this.fdependencies;
		}
		
		/**
		 * Gets the functional dependency relevant for a variable.
		 *
		 * @param var the variable
		 * @return its functional dependency
		 */
		public FunctionalDependency getFunctionalDependency(Node var){
			return this.fdependencies.get(var);
		}		
	}
}
