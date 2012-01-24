package uk.soton.service.mediation.algebra;

import java.util.List;

import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprFunction2;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.expr.ExprFunction1; 
import com.hp.hpl.jena.sparql.function.FunctionBase2;

/**
 * The FunctionWrapper class wraps an instance of ExprFunction or FunctionBase and implement the Function interface over them.
 * @see com.hp.hpl.jena.sparql.expr.ExprFunction2
 * @see com.hp.hpl.jena.sparql.expr.ExprFunction1
 * @see com.hp.hpl.jena.sparql.function.FunctionBase2
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class FunctionWrapper implements Function {

	protected Object inner;
	
	private FunctionWrapper(Object i){
		this.inner = i;
	}
	
	/**
	 * The $ method is a constructor wrapper
	 * @param i object to wrap
	 * @return the instance
	 * 
	 */
	public static FunctionWrapper $(Object i){
		return new FunctionWrapper(i);
	}
	
	@Override
	public NodeValue eval(List<NodeValue> args) {
		if ((this.inner instanceof ExprFunction1) && (args.size() == 1)){
			return ((ExprFunction1)this.inner).eval(args.get(0));
		} else if ((this.inner instanceof ExprFunction2) && (args.size() == 2)){
			return ((ExprFunction2)this.inner).eval(args.get(0),args.get(1));
		} else if ((this.inner instanceof ExprFunction2) && (args.size() == 2)){
			return ((FunctionBase2)this.inner).exec(args.get(0),args.get(1));
		} else return null;
	}

	@Override
	public Expr copy(List<Expr> args) {
		if ((this.inner instanceof ExprFunction1) && (args.size() == 1)){
			return ((ExprFunction1)this.inner).copy(args.get(0));
		} else if ((this.inner instanceof ExprFunction2) && (args.size() == 2)){
			return ((ExprFunction2)this.inner).copy(args.get(0),args.get(1));
		} else return null;
	}
	
	@Override
	public String toString(){
		return this.inner.getClass().toString();
	}

}
