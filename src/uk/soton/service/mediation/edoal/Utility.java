/*
 * Utility.java
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
package uk.soton.service.mediation.edoal;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;

/**
 * The Utility class contains some utility methods
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 *
 */
public class Utility {
	/** The incremental index of the eventual anonymous bnodes. */
	private static int index = 0;

	/**
	 * Gets the symbol for the next anonymous node to increase the readability.
	 * 
	 * @return the symb
	 */
	private static String getSymb() {
		return String.valueOf("var" + index++);
	}
	
	/**
	 * The getNewVar method returns a new variable node
	 * @return new Variable
	 * 
	 */
	public static Node getNewVar(){
		return Node.createAnon(AnonId.create(getSymb()));
	}
}
