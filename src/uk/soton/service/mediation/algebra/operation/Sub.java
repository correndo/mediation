/*
 * Sub.java
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
package uk.soton.service.mediation.algebra.operation;

import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase2;

/**
 * The Sub class implements a XPath function that makes the difference between two input numbers.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 *
 */
public class Sub extends FunctionBase2 {

	/* (non-Javadoc)
	 * @see com.hp.hpl.jena.sparql.function.FunctionBase2#exec(com.hp.hpl.jena.sparql.expr.NodeValue, com.hp.hpl.jena.sparql.expr.NodeValue)
	 */
	@Override
	public NodeValue exec(NodeValue arg0, NodeValue arg1) {
		float f0 = Float.parseFloat(arg0.asString());
		float f1 = Float.parseFloat(arg1.asString());
		NodeValue result = NodeValue.makeFloat(f0-f1);
		return result;
	}

}
