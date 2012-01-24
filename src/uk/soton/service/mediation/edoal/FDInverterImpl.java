package uk.soton.service.mediation.edoal;

import java.util.ArrayList;
import java.util.HashMap;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.E_Add;
import com.hp.hpl.jena.sparql.expr.E_Subtract;
import com.hp.hpl.jena.sparql.expr.E_Multiply;
import com.hp.hpl.jena.sparql.expr.E_Divide;

import uk.soton.service.mediation.FunctionalDependency;
import uk.soton.service.mediation.algebra.FunctionWrapper;
import uk.soton.service.mediation.RDFVocabulary;

public class FDInverterImpl implements FDInverter {

	private HashMap<String,FDInverter> inner = new HashMap<String,FDInverter>();
	
	public FDInverterImpl add(String furi,FDInverter inverter){
		this.inner.put(furi, inverter);
		return this;		
	}
	
	@Override
	public FunctionalDependency invert(FunctionalDependency fd) {
		return inner.get(fd.getFuncURI()).invert(fd);
	}
	
	public static FDInverter getDefaultInstance(){
		FDInverterImpl fdii = new FDInverterImpl();
		String XPATH = "http://www.w3.org/2005/xpath-functions/";
		final String sub = XPATH+"sub";
		final String sum = XPATH+"sum";

		//TODO implement other xsd function inverse
		fdii.add(RDFVocabulary.FN_SUM, new FDInverter() {
			{
			}

			public FunctionalDependency invert(FunctionalDependency fd) {
				if (fd.getParam() != null) {
					Node var = fd.getParam().get(0).isBlank() ? fd.getParam().get(0) : fd.getParam().get(1);
					ArrayList<Node> params = new ArrayList<Node>();
					params.add(fd.getVar());
					params.add(fd.getParam().get(0).isBlank() ? fd.getParam().get(1) : fd.getParam().get(0));
					return new FunctionalDependency(var, FunctionWrapper.$(new E_Subtract(null, null)), params, RDFVocabulary.FN_SUB);
				} else
					return new FunctionalDependency(null, FunctionWrapper.$(new E_Subtract(null, null)), null, RDFVocabulary.FN_SUB);
			}
		}).add(RDFVocabulary.FN_SUB, new FDInverter() {
			{
			}
			//TODO implement position-wise inverting
			public FunctionalDependency invert(final FunctionalDependency fd) {
				if (fd.getParam() != null) {
					if (fd.getParam().get(0).isBlank()){
						Node var = fd.getParam().get(0);
						ArrayList<Node> params = new ArrayList<Node>(){{add(fd.getVar()); add(fd.getParam().get(1));}};
						return new FunctionalDependency(var, FunctionWrapper.$(new E_Add(null, null)), params, RDFVocabulary.FN_SUM);
					} else {
						Node var = fd.getParam().get(1);
						ArrayList<Node> params = new ArrayList<Node>(){{add(fd.getParam().get(0)); add(fd.getVar()); }};
						return new FunctionalDependency(var, FunctionWrapper.$(new E_Subtract(null, null)), params, RDFVocabulary.FN_SUB);
					}
				} else return new FunctionalDependency(null, FunctionWrapper.$(new E_Add(null, null)), null, RDFVocabulary.FN_SUM);
			}
		});
		return fdii;
	}
}
