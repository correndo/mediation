/*
 * EntityTranslationService.java
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

import uk.soton.service.mediation.algebra.OpAssignGenerator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

/**
 * The Interface EntityTranslationService defines a service that translate a Basic Graph Pattern in accordance with a  given ontology alignment.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public interface EntityTranslationService {
	
	/**
	 * This method apply a simple transformation for a list of triples.
	 * No function application are devised nor complex structures for lhs patterns.
	 * (i.e. the matching head is just one triple, not a list.) 
	 * @param a Alignment to use for the translation
	 * @param source list of triples to translate
	 * @return list of triples translated
	 */
	public BGPTranslationResult getTranslatedTriples(Alignment a, List<Triple> source);
	
	/**
	 * The Class BGPTranslationResult represents the result of a translation of a BGP.
	 */
	class BGPTranslationResult {
		
		/** The translated bgp. */
		private List<Triple> translatedBGP;
		
		/** The variable bindings. */
		private Hashtable<Node,Node> bindings;
		
		/**
		 * The field lets is the list of assignment statements.
		 */
		private List<OpAssignGenerator> lets;

		/**
		 * Instantiates a new BGP translation result.
		 *
		 * @param nbgp the new Basic Graph Pattern
		 * @param bindings the variable bindings
		 */
		public BGPTranslationResult(List<Triple> nbgp, Hashtable<Node,Node> bindings, List<OpAssignGenerator> l){
			this.translatedBGP = nbgp;
			this.bindings = bindings;
			this.setLets(l);
		}
		
		/**
		 * Gets the translated Basic Graph Pattern.
		 *
		 * @return the translated bgp
		 */
		public List<Triple> getTranslatedBGP() {
			return translatedBGP;
		}
		
		/**
		 * Gets the bindings.
		 *
		 * @return the bindings
		 */
		public Hashtable<Node, Node> getBindings() {
			return bindings;
		}

		/**
		 * @param lets the lets to set
		 */
		public void setLets(List<OpAssignGenerator> lets) {
			this.lets = lets;
		}

		/**
		 * @return the lets
		 */
		public List<OpAssignGenerator> getLets() {
			return lets;
		}
	}

}

