/*
 * EntityTranslationServiceImpl.java
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

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.apache.log4j.Logger;

import uk.soton.service.mediation.FunctionalDependency;
import uk.soton.service.mediation.Alignment.MatchingResult;
import uk.soton.service.mediation.algebra.OpAssignGenerator;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.graph.query.Expression.Variable;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.op.OpAssign;
import com.hp.hpl.jena.sparql.core.Var;
import com.hp.hpl.jena.sparql.expr.E_Add;
import com.hp.hpl.jena.sparql.expr.Expr;
import com.hp.hpl.jena.sparql.expr.ExprNode;
import com.hp.hpl.jena.sparql.expr.ExprVar;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase;

/**
 * The Class EntityTranslationServiceImpl implements the EntityTranslationService interface using Jena API as an RDF framework.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class EntityTranslationServiceImpl implements EntityTranslationService {


	private List<OpAssignGenerator> lets = new ArrayList<OpAssignGenerator>();
	
	 
	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.EntityTranslationService#getTranslatedTriples(uk.soton.service.mediation.Alignment, java.util.List)
	 */
	@Override
	public BGPTranslationResult getTranslatedTriples(Alignment a, List<Triple> source) {
		ArrayList<Triple>  result = new ArrayList<Triple>();		
		Hashtable<Node,Node> bind = new Hashtable<Node,Node>();
		
		for (Triple t : source) {
			MatchingResult mr = a.matchLHS(t);
			if (mr != null) {
				for (Triple target : mr.getRhs()) {
					target = instantiateFunction(target, mr , a);
					target = instantiatePattern(target, mr.getBinding());
					result.add(target);
					bind.putAll(mr.getBinding());
				}
			} else
				result.add(t);
		}
		return new BGPTranslationResult(result, bind,lets);
	}

	/*
	 * Utility functions to operate the translation
	 */

	/**
	 * Instantiate a template pattern with the given binding.
	 *
	 * @param t  template triple from an rhs
	 * @param binding binding from a match operation
	 * @return instantiated triple
	 */
	public Triple instantiatePattern(Triple t,
			Hashtable<Node, Node> binding) {
		Triple result = null;
		Node subject = t.getMatchSubject();
		Node predicate = t.getMatchPredicate();
		Node object = t.getMatchObject();
		if (binding.containsKey(subject))			subject = binding.get(subject);
		if (binding.containsKey(predicate))			predicate = binding.get(predicate);
		if (binding.containsKey(object))			object = binding.get(object);
		if (subject.isBlank())						subject = Utility.getVarFromBlank(subject);
		if (object.isBlank())						object = Utility.getVarFromBlank(object);
		result = new Triple(subject, predicate, object);
		return result;
	}

	/**
	 * instantiateFunction instantiate the input triple with the input variable binding and alignment.
	 *
	 * @param target triple to instantiate
	 * @param mr variables binding to use
	 * @return instantiated triple
	 */
	public Triple instantiateFunction(Triple target, MatchingResult mr, Alignment a) {
		Hashtable<Node, Node> binding = mr.getBinding();
		Node[] vars = new Node[] { target.getSubject(), target.getObject() };
		FunctionalDependency fd;
		ArrayList<Node> unbounded = new ArrayList<Node>();
		
		for (Node v : vars) {
			
			if (v.isVariable()) {
				fd = mr.getFunctionalDependency(v);
				if (fd != null) {
					List<NodeValue> params = new ArrayList<NodeValue>();
					NodeValue nvp, nvr;
					Boolean inst = true;

					for (Node p : fd.getParam()) {
						Node ground = null;
						if (p.isVariable() && binding.containsKey(p)) {
							ground = binding.get(p);
						}
						if (ground != null && ground.isVariable()) {
							inst = false;
							unbounded.add(p);
							params.add(NodeValue.makeNode(p));
						} else if (ground != null){
							nvp = NodeValue.makeNode(ground);
							params.add(nvp);
						} else {
							//Create the value for value not present in the bindings
							params.add(NodeValue.makeNode(p));
						}
					}
					if (inst){
						//Free variables - generate binding Ops
						nvr = fd.getFunc().eval(params);
					    binding.put(v, nvr.asNode());
					} else {
						//Create the OpAssign bindings for unbounded vars
						instanciateBindings(unbounded,  mr);
					}
				}
			}
		}
		return target;
	}

	/**
	 * The instanciateBindings method create the data for generate the OpAssign operations for assign correct values when unbounded vars appears
	 * @param unbounded a list of vars without ground values
	 * @param mr the mapping result instance
	 * 
	 */
	private void instanciateBindings(List<Node> unbounded, MatchingResult mr) {
		for (Node tobind : unbounded){
			FunctionalDependency fd = mr.getFunctionalDependency(tobind);
			List<Node> par = fd.getParam();
			tobind = mr.getBinding().get(tobind);
			List<Expr> exp = new ArrayList<Expr>();
			for (Node nv : par){
				exp.add(NodeValue.makeNode(nv));
			}
			lets.add(new OpAssignGenerator((Var) tobind, fd.getFunc().copy(exp)));
		}		
	}
}
