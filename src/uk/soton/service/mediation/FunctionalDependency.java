/*
 * FunctionalDependency.java
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
import com.hp.hpl.jena.sparql.function.Function;
import java.util.List;

/**
 * 
 * The Class FunctionalDependency describes a functional dependency between variables.<br/>
 * var = func(parameters)
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class FunctionalDependency {
  /** The var. */
  private Node var;
  
  /** The SPARQL function instance (may be null). */
  private Function func;
  
  /** The function URI */
  private String funcURI;
  
  /** The list of parameters. */
  private List<Node> param;

  public FunctionalDependency(Node var, Function func, List<Node> param) {
    this.var = var;
    this.func = func;
    this.param = param;
  }

  public FunctionalDependency(Node var, Function func, List<Node> param, String furi) {
    this(var, func, param);
    this.funcURI = furi;
  }

  //Getters
  /**
   * Gets the variable.
   *
   * @return the variable
   */
  public Node getVar() {
    return var;
  }

  /**
   * Gets the function URI.
   * @return the function URI to represent in the RDF document
   */
  public String getFuncURI() {
    return this.funcURI;
  }

  /**
   * Gets the function instance.
   *
   * @return the function instance to call when ground values are present.
   */
  public Function getFunc() {
    return func;
  }

  /**
   * Gets the parameters of the functional dependency as a List of Nodes.
   *
   * @return the list of parameters
   */
  public List<Node> getParam() {
    return param;
  }
  
  /**
   * Sets the list of parameters
   * @param parameters the list of parameters
   */
  public FunctionalDependency setParam(List<Node> parameters){
	  this.param = parameters;
	  return this;
  }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString
	 */
  @Override
  public String toString() {
    String result = var.getClass().toString() + ":" + var.getName() + " = " + this.func.getClass().toString() + "(";
    for (Node p : this.param) {
      result += nr(p) + ",";
    }
    return result.substring(0, result.length() - 1) + ')';
  }

  /**
   * Utility function to retrieve a viable string representation of a node
   * @param n the Node to represent
   * @return the String representation
   */
  private String nr(Node n) {
    if (n.isVariable()) {
      return "var:" + n.getName();
    }
    if (n.isLiteral()) {
      return "lit:" + n.getLiteralValue().toString();
    }
    if (n.isURI()) {
      return "uri:" + n.getLocalName();
    }
    return "";
  }
  
}
