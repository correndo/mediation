package uk.soton.service.mediation.algebra;

import java.util.List;

import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.NodeValue;

/**
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 * The Function interface defines a function template that can generate ground values and expressions to embed in query.
 */
public interface Function {

	/**
	 * The eval method takes as input an ordered list of NodeValue parameters and apply the function over them (ground value)
	 * @param args ordered set of parameters
	 * @return result value
	 * 
	 */
	NodeValue eval(List<NodeValue> args);
	
	/**
	 * The copy method takes as input an ordered list of Expr instances and reconstruct the function with those (function to run)
	 * @param args
	 * @return an expression, copy of the wrapped one with the input expressions as arguments
	 * 
	 */
	Expr copy(List<Expr> args);
	
}
