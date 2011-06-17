/*
 * RewritingRule.java
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

import java.util.List;
import uk.soton.service.mediation.FunctionalDependency;
import com.hp.hpl.jena.graph.Triple;
import java.util.ArrayList;

/**
 * The RewritingRule class implements a rule for rewriting RDF patterns
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class RewritingRule {


	/**
	 * The field LHS is the Left Hand Side Triple to match in the rule.
	 */
	private Triple LHS;

	/**
	 * The field RHS is the Right Hand Side Triples that replace the matched LHS
	 */
	private List<Triple> RHS;
	
	/**
	 * The field FD is the list of functional dependencies present among the free variables in LHS + RHS
	 */
	private List<FunctionalDependency> FD;

	/**
	 * The setLHS method set the LHS field
	 * @param lhs the Left Hand Side of the rule to set
	 */
	public RewritingRule setLHS(Triple lhs) {
		LHS = lhs;
		return this;
	}

	/**
	 * Getter method for the rewriting rule's Left Hand Side
	 * @return the Left Hand Side of the rule
	 */
	public Triple getLHS() {
		return this.LHS;
	}

	/**
	 * The setRHS method set the RHS field
	 * @param rhs the Right Hand Side of the rule to set
	 */
	public RewritingRule setRHS(List<Triple> rhs) {
		RHS = rhs;
		return this;
	}

	/**
	 * Getter method for the rewriting rule's Right Hand Side
	 * @return the Right Hand Side of the rule
	 */
	public List<Triple> getRHS() {
		return RHS;
	}

	/**
	 * The setFD method set the FD field
	 * @param fd the functional dependencies of the rule
	 */
	public RewritingRule setFD(List<FunctionalDependency> fd) {
		FD = fd;
		return this;
	}

	/**
	 * Getter method for the rewriting rule's Functional Dependencies
	 * @return the FD functional dependencies of the rule
	 */
	public List<FunctionalDependency> getFD() {
    if (FD == null) return new ArrayList<FunctionalDependency>();
		return FD;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString() 
	 */
	@Override
	public String toString(){
		String result = "[ <"+this.LHS.toString() + "> -> ";
		for (Triple rhs : this.RHS){
			result += "<" + rhs.toString() + ">,";
		}
		result = result.substring(0, result.length()-1) + " / " + this.FD.toString();
		return result;
	}
	
}
