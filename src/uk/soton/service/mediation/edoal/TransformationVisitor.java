/*
 * TransformationVisitor.java
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

import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Visitable;

import uk.soton.service.mediation.FunctionalDependency;
import uk.soton.service.mediation.RewritingRule;
import uk.soton.service.mediation.STriple;

import com.hp.hpl.jena.graph.Node;

import fr.inrialpes.exmo.align.impl.edoal.Apply;
import fr.inrialpes.exmo.align.impl.edoal.ClassConstruction;
import fr.inrialpes.exmo.align.impl.edoal.ClassDomainRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassExpression;
import fr.inrialpes.exmo.align.impl.edoal.ClassId;
import fr.inrialpes.exmo.align.impl.edoal.ClassOccurenceRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassTypeRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassValueRestriction;
import fr.inrialpes.exmo.align.impl.edoal.Datatype;
import fr.inrialpes.exmo.align.impl.edoal.Expression;
import fr.inrialpes.exmo.align.impl.edoal.InstanceExpression;
import fr.inrialpes.exmo.align.impl.edoal.InstanceId;
import fr.inrialpes.exmo.align.impl.edoal.PathExpression;
import fr.inrialpes.exmo.align.impl.edoal.PropertyConstruction;
import fr.inrialpes.exmo.align.impl.edoal.PropertyDomainRestriction;
import fr.inrialpes.exmo.align.impl.edoal.PropertyExpression;
import fr.inrialpes.exmo.align.impl.edoal.PropertyId;
import fr.inrialpes.exmo.align.impl.edoal.PropertyRestriction;
import fr.inrialpes.exmo.align.impl.edoal.PropertyTypeRestriction;
import fr.inrialpes.exmo.align.impl.edoal.PropertyValueRestriction;
import fr.inrialpes.exmo.align.impl.edoal.RelationCoDomainRestriction;
import fr.inrialpes.exmo.align.impl.edoal.RelationConstruction;
import fr.inrialpes.exmo.align.impl.edoal.RelationDomainRestriction;
import fr.inrialpes.exmo.align.impl.edoal.RelationExpression;
import fr.inrialpes.exmo.align.impl.edoal.RelationId;
import fr.inrialpes.exmo.align.impl.edoal.RelationRestriction;
import fr.inrialpes.exmo.align.impl.edoal.Transformation;
import fr.inrialpes.exmo.align.impl.edoal.Value;
import fr.inrialpes.exmo.align.impl.edoal.ValueExpression;
import fr.inrialpes.exmo.align.impl.edoal.EDOALVisitor;

/**
 * The TransformationVisitor class implements the AlignmentVisitor interface and produces RewritingRule instances.
 * 
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class TransformationVisitor implements EDOALVisitor, RewritingRuleGenerator{

	/**
	 * The field lmr is the intermediate mediation result for the LHS
	 */
	private MediationResult lmr;
	
	/**
	 * The field rmr is the intermediate mediation result for the RHS
	 */
	private MediationResult rmr;
	
	/**
	 * The field rr is the resulting RewritingRule
	 */
	private RewritingRule rr;
	
	/**
	 * The field forward is true if the rule generated will go into the forward alignment, false otherwise
	 */
	private boolean forward = true;
	
    private static FDInverter inverter = FDInverterImpl.getDefaultInstance();

	
	public TransformationVisitor(){
		this.lmr = new MediationResult();
		this.rmr = new MediationResult();
		this.rr = new RewritingRule();
	}
	/*
	@Override
	public MediationResult getMediationResult() {
		return this.mr;
	}*/

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.align.AlignmentVisitor#init(java.util.Properties)
	 */
	/*@Override
	public void init(Properties arg0) {
		// NOOP		
	}*/

	/* (non-Javadoc)
	 * @see org.semanticweb.owl.align.AlignmentVisitor#visit(org.semanticweb.owl.align.Visitable)
	 */
	/*@Override
	public void visit(Visitable t) throws AlignmentException {
		if (t instanceof Transformation) this.visit((Transformation)t);
		else Logger.getAnonymousLogger().log(Level.WARNING,	"Class not handled yet:" + t.getClass());
	}*/
	
	/**
	 * The visit method visit the Transformation structure and generate the RewritingRule that implements the Transformation.
	 * @param t the Transformation element to visit
	 */
	public void visit(Transformation t) throws AlignmentException{
		Node s = Utility.getNewVar();
		Node o = Utility.getNewVar();
		ValueExpression from , to;
		from = t.getObject1();
		to = t.getObject2();
		if (from instanceof Apply){
			this.forward = true;
		} else {
			this.forward = false;
			ValueExpression temp = from;
			from = to;
			to = temp;
		}
		PropertyVisitor vv = PropertyVisitor.$().setMediationResult(rmr).setO(o).setS(s); //RHS : (s <?> o)
		vv.visit(to); //rmr = (s obj1:p o)
		// <-------->
		ValueVisitor vf = ValueVisitor.$().setValue(o);
		vf.visit(from); // o = f(x1,x2,..,xn)
		reifyFD(vf.getFD() , s); //(s x1 v1) AND y = f(v1,x2,..,xn) for each xi (actually only one property is allowed right now because of the limitation of the rules
		if (vf.getFD() != null && lmr.getPatterns().size() > 0 && rmr.getPatterns().size() > 0 ){
			this.lmr.getFD().add(vf.getFD());
			//Add invert function
			FunctionalDependency i = inverter.invert(vf.getFD());
			if (i != null){
				this.lmr.getFD().add(i);
			}
			this.rr.setLHS(lmr.getPatterns().asTripleList().get(0)).setRHS(this.rmr.getPatterns().asTripleList()).setFD(lmr.getFD());
		} else {
			this.rr = null;
		}
	}
	
	/**
	 * The reifyFD method scan the list of parameters of a FunctionalDependency and replaces every property found
	 * with a variable that is bounded to an object whose predicate is the property itself.
	 * e.g. ?y = f(p) --> <s , p , ?v> + ?y=f(?v)
	 * @param fd the FunctionalDependency
	 * @param s the subject to use when reifying 
	 * 
	 */
	private void reifyFD(FunctionalDependency fd , Node s){
		ArrayList<Node> np = new ArrayList<Node>();
		if (fd != null){
			for (Node p : fd.getParam()){
				if (p.isURI()){
					Node o;
					o = Utility.getNewVar();//Node.createAnon(AnonId.create(getSymb()));
					this.lmr.getPatterns().add(new STriple(s, p, o));
					np.add(o);
				} else np.add(p);
			}	
			fd.setParam(np);
		}
	}
	
	/**
	 * The $ method is a constructor shortcut
	 * @return a new instance of the class
	 * 
	 */
	public static TransformationVisitor $(){
		return new TransformationVisitor();
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.edoal.RewritingRuleGenerator#getRewritingRule()
	 */
	@Override
	public RewritingRule getRewritingRule() {
		return this.rr;
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.edoal.RewritingRuleGenerator#isForward()
	 */
	@Override
	public boolean isForward() {
		return this.forward;
	}

	@Override
	public void visit(PathExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Expression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassId o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassConstruction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassTypeRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassDomainRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassValueRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ClassOccurenceRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyId o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyConstruction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyDomainRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyTypeRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(PropertyValueRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationId o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationConstruction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationDomainRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(RelationCoDomainRestriction o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InstanceExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(InstanceId o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(ValueExpression o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Value o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Apply o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void visit(Datatype o) throws AlignmentException {
		// TODO Auto-generated method stub
		
	}

}
