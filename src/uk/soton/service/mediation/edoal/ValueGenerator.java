/*
 * ValueGenerator.java
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

import uk.soton.service.mediation.FunctionalDependency;
import uk.soton.service.mediation.RewritingRule;

/**
 * The interface ValueGenerator is the common interface to all the classes that generates <a href="http://openjena.org/javadoc/com/hp/hpl/jena/graph/Node.html">Node</a> instances.
 * 
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 * @see com.hp.hpl.jena.graph.Node
 */
public interface ValueGenerator {

	/**
	 * The getValue method returns the Node value
	 * @return jena Node value node
	 */
	public Node getValue();
	
	/**
	 * The getFD method returns the FunctionalDependency that bind the value returned
	 * @return a FunctionalDependency instance for the returned value.
	 * 
	 */
	public FunctionalDependency getFD();
	
}
