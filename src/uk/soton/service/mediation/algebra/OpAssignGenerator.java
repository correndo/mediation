package uk.soton.service.mediation.algebra;

import java.util.List;

import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpAssign;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.Expr;

/**
 * The OpAssignGenerator class wraps a possible assignment generator and generate the operation with a father Op when requested.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class OpAssignGenerator {

	/**
	 * The field var is the variable to assign
	 */
	private Var var;
	
	/**
	 * The field exp is the Expr to assign
	 */
	private Expr exp;
	
	public OpAssignGenerator(Var v , Expr p){
		this.setVar(v);
		this.setExpr(p);
	}

	/**
	 * The get method return an OpAssign operation with the input parameter as father operation
	 * @param op father Op
	 * @return the OpAssign operation
	 * 
	 */
	public OpAssign get(Op op){
		return (OpAssign) OpAssign.assign(op, this.var , this.exp);
	}
	
	/**
	 * @param var the var to set
	 */
	public void setVar(Var var) {
		this.var = var;
	}

	/**
	 * @return the var
	 */
	public Var getVar() {
		return var;
	}

	/**
	 * @param e the expression to set
	 */
	public void setExpr(Expr e) {
		this.exp = e;
	}

	/**
	 * @return the expression to set
	 */
	public Expr getExpr() {
		return this.exp;
	}
}
