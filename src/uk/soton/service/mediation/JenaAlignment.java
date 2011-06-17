/*
 * JenaAlignment.java
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
import java.util.logging.Level;
import java.util.logging.Logger;

import uk.soton.service.mediation.algebra.operation.*;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.function.Function;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

/**
 * The Class JenaAlignment implements the Alignment interface using Jena API.
 * At class loading time the resolver is populated with well known functions instances.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class JenaAlignment implements Alignment {

	public static final Logger log = Logger.getAnonymousLogger();
	/** The symb. */
	private static int symb = 0;

	/**
	 * Gets the new string symbol.
	 * 
	 * @return the new symb
	 */
	private static int getNewSymb() {
		return symb++;
	}

	/**
	 * Gets a new Jena variable Node.
	 * 
	 * @return the new var
	 */
	private static Node getNewVar() {
		return Node.createVariable("_" + getNewSymb());
	}
	/** The Jena Model containing the alignments. */
	private Model inner;
	/** The Triple patterns contained in the alignment. */
	private Hashtable<Triple, List<Triple>> patterns;
	/** The functional dependencies contained in the alignment. */
	private Hashtable<Triple, Hashtable<Node, FunctionalDependency>> fdependencies;
	/** The RDF Node root of the alignment document. */
	private Resource root;
	/** The resolver table. */
	private Hashtable<Node, Function> resolver;

	{
		resolver = new Hashtable<Node, Function>();
		resolver.put(Node.createURI(RDFVocabulary.SAMEAS), new SameAs());
		resolver.put(Node.createURI(RDFVocabulary.FN_SUB), new Sub());
		resolver.put(Node.createURI(RDFVocabulary.FN_SUM), new Sum());

	}

	/**
	 * Instantiates a new jena alignment with an empty model.
	 */
	public JenaAlignment() {
		this.inner = ModelFactory.createDefaultModel();
		this.root = this.getA();
		this.inner.add(this.root, this.getP(RDFVocabulary.RDF_TYPE),
				this.getR(RDFVocabulary.ALIGNMENT));
	}

	/**
	 * Instantiates a new jena alignment with a provided RDF model.
	 * 
	 * @param m
	 *            the RDF model to use to build the alignment
	 */
	public JenaAlignment(Model m) {
		this.inner = m;
		this.getPatterns();
	}

	/**
	 * Returns the Model instance
	 * 
	 * @return the Jena Model instance used to represent the alignment
	 */
	public Model getModel() {
		return this.inner;
	}

	/**
	 * Utility function that returns the RDF Node for the input property URI 
	 * @param uri String URI
	 * @return a Jena Property from the inner Model
	 */
	private Property getP(String uri) {
		return this.inner.createProperty(uri);
	}

	/**
	 * Utility function that returns the RDF Node for the input Resource URI 
	 * @param uri String URI
	 * @return a Jena Resource from the inner Model
	 */
	private Resource getR(String uri) {
		return this.inner.createResource(uri);
	}

	/**
	 * Utility function that returns the RDF Anonymous Node for the input anonymous id 
	 * @param id String id
	 * @return a Jena Anonymous Node from the inner Model
	 */
	private Resource getA(String id) {
		return (id == null
				? this.inner.createResource(AnonId.create())
				: this.inner.createResource(AnonId.create(id)));
	}

	/**
	 * Utility function that returns the RDF Anonymous Node for the input anonymous id 
	 * @param id AnonId
	 * @return a Jena Anonymous Node from the inner Model
	 */
	private Resource getA(AnonId id) {
		return (id == null
				? this.inner.createResource(AnonId.create())
				: this.inner.createResource(id));
	}

	/**
	 * Utility function that returns an RDF Anonymous Node without a particular id 
	 * @return a Jena Anonymous Node from the inner Model
	 */
	private Resource getA() {
		return getA((String) null);
	}

	/**
	 * The Merge method create a new JenaAlignment composed of the union of the two input alignments.
	 * 
	 * @param a the first alignment
	 * @param b the second alignment
	 * @return the merged jena alignment
	 */
	public static JenaAlignment merge(JenaAlignment a, JenaAlignment b) {
		Model m = ModelFactory.createDefaultModel().union(a.getModel()).union(b.getModel());
		return new JenaAlignment(m);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.soton.service.mediation.Alignment#matchLHS(com.hp.hpl.jena.graph.Triple
	 * )
	 */
	@Override
	public MatchingResult matchLHS(Triple t) {
		Hashtable<Node, Node> binding = null;
		Hashtable<Triple, List<Triple>> p = this.getPatterns();
		log.log(Level.INFO, "Patterns keys: " + p.keySet());
		for (Triple pt : this.patterns.keySet()) {
			binding = Utility.match(pt, t);
			if (binding != null) {
				return this.particularize(p.get(pt),
						this.fdependencies.get(pt), binding);
			}
		}
		return null;
	}

	/**
	 * The Particularize method instantiate a list of triple patterns with the provided inputs.
	 * 
	 * @param pattern list of triples (with possible variables) to rename
	 * @param fdependencies  functional dependencies between variables
	 * @param binding  binding from the matching phase
	 * @return a MatchingResult instance with all the variables renamed
	 */
	private MatchingResult particularize(List<Triple> pattern,
			Hashtable<Node, FunctionalDependency> fdependencies,
			Hashtable<Node, Node> binding) {
		MatchingResult result = null;
		Hashtable<Node, Node> newBinding = new Hashtable<Node, Node>();
		Hashtable<Node, Node> translation = new Hashtable<Node, Node>();
		ArrayList<Triple> newPattern = new ArrayList<Triple>();
		Hashtable<Node, FunctionalDependency> newFunctionalDep = new Hashtable<Node, FunctionalDependency>();

		for (Node var : binding.keySet()) {
			Node temp = translation.get(var);
			if (temp == null) {
				temp = getNewVar();
				translation.put(var, temp);
			}
			newBinding.put(temp, binding.get(var));
		}
		// Rename the variables in the patterns
		for (Triple pt : pattern) {
			Node s, p, o;
			s = pt.getMatchSubject();
			p = pt.getMatchPredicate();
			o = pt.getMatchObject();
			if (s.isVariable()) {
				Node temp = translation.get(s);
				if (temp == null) {
					temp = getNewVar();
					translation.put(s, temp);
				}
				s = temp;
				if (binding.containsKey(pt.getMatchSubject())) {
					newBinding.put(s, binding.get(pt.getMatchSubject()));
				}
			}
			if (p.isVariable()) {
				Node temp = translation.get(p);
				if (temp == null) {
					temp = getNewVar();
					translation.put(p, temp);
				}
				p = temp;
				if (binding.containsKey(pt.getMatchPredicate())) {
					newBinding.put(p, binding.get(pt.getMatchPredicate()));
				}
			}
			if (o.isVariable()) {
				Node temp = translation.get(o);
				if (temp == null) {
					temp = getNewVar();
					translation.put(o, temp);
				}
				o = temp;
				if (binding.containsKey(pt.getMatchObject())) {
					newBinding.put(o, binding.get(pt.getMatchObject()));
				}
			}
			newPattern.add(new Triple(s, p, o));
		}
		// Rename the functional dependencies
		for (FunctionalDependency fd : fdependencies.values()) {
			Node nvar = translation.get(fd.getVar());
			ArrayList<Node> param = new ArrayList<Node>();
			for (Node par : fd.getParam()) {
				Node npar = (par.isVariable() ? translation.get(par) : par);
				param.add(npar);
			}
			newFunctionalDep.put(nvar,
					new FunctionalDependency(nvar, fd.getFunc(), param));
		}
		result = new MatchingResult(newPattern, newBinding, newFunctionalDep);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.soton.service.mediation.Alignment#getSourceOntologyURIs()
	 */
	@Override
	public List<String> getSourceOntologyURIs() {
		ArrayList<String> result = new ArrayList<String>();
		ExtendedIterator<Triple> source = this.inner.getGraph().find(null,
				Node.createURI(RDFVocabulary.HAS_SOURCE_ONTOLOGY), null);
		for (Triple t : source.toList()) {
			result.add(t.getObject().getURI());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.soton.service.mediation.Alignment#getTargetOntologyURIs()
	 */
	@Override
	public List<String> getTargetOntologyURIs() {
		ArrayList<String> result = new ArrayList<String>();
		ExtendedIterator<Triple> source = this.inner.getGraph().find(null,
				Node.createURI(RDFVocabulary.HAS_TARGET_ONTOLOGY), null);
		for (Triple t : source.toList()) {
			result.add(t.getObject().getURI());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.soton.service.mediation.Alignment#setSourceOntologyURIs(java.util.
	 * List)
	 */
	@Override
	public void setSourceOntologyURIs(List<String> sourceOntologyURIs) {
		for (String uri : sourceOntologyURIs) {
			this.inner.add(this.root, this.inner
					.createProperty(RDFVocabulary.HAS_SOURCE_ONTOLOGY), this
					.getR(uri));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.soton.service.mediation.Alignment#setTargetOntologyURIs(java.util.
	 * List)
	 */
	@Override
	public void setTargetOntologyURIs(List<String> targetOntologyURIs) {
		for (String uri : targetOntologyURIs) {
			this.inner.add(this.root, this.inner
					.createProperty(RDFVocabulary.HAS_TARGET_ONTOLOGY), this
					.getR(uri));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.soton.service.mediation.Alignment#getPatterns()
	 */
	@Override
	public Hashtable<Triple, List<Triple>> getPatterns() {
		if (this.patterns != null) {
			return this.patterns;
		}
		log.log(Level.INFO, "Model: " + this.inner);
		Hashtable<Triple, List<Triple>> result = new Hashtable<Triple, List<Triple>>();
		fdependencies = new Hashtable<Triple, Hashtable<Node, FunctionalDependency>>();
		ResIterator ei = inner.listSubjectsWithProperty(
				getP(RDFVocabulary.RDF_TYPE),
				getR(RDFVocabulary.ENTITY_ALIGNMENT));
		Triple tlhs = null, trhs = null;
		for (Resource al : ei.toList()) {
			StmtIterator lhst = inner.listStatements(al,
					getP(RDFVocabulary.LHS), (RDFNode) null);
			if (!lhst.hasNext()) {
				Logger.getLogger(JenaAlignment.class.getName()).log(
						Level.SEVERE, "No LHS provided");
			}
			for (Statement lhs : lhst.toList()) {
				tlhs = Utility.getReified((Resource) lhs.getObject(),
						this.inner);
				tlhs = Utility.substituteBlankWithVars(tlhs);
			}
			StmtIterator rhst = inner.listStatements(al,
					getP(RDFVocabulary.RHS), (RDFNode) null);
			if (!rhst.hasNext()) {
				Logger.getLogger(JenaAlignment.class.getName()).log(
						Level.SEVERE, "No RHS provided");
			}
			ArrayList<Triple> rhsa = new ArrayList<Triple>();
			for (Statement rt : rhst.toList()) {
				trhs = Utility
						.getReified((Resource) rt.getObject(), this.inner);
				trhs = Utility.substituteBlankWithVars(trhs);
				rhsa.add(trhs);
			}
			if (trhs != null && tlhs != null) {
				result.put(tlhs, rhsa);
			}

			NodeIterator fdt = this.inner.listObjectsOfProperty(al,
					getP(RDFVocabulary.HAS_FUNCTIONAL_DEPENDENCY));
			Hashtable<Node, FunctionalDependency> fdep = new Hashtable<Node, FunctionalDependency>();
			for (RDFNode fd : fdt.toList()) {
				Triple tfd = Utility.getReified((Resource) fd, this.inner);
				// tfd = Utility.substituteBlankWithVars(tfd);
				FunctionalDependency fud = getFunctionalDependency(tfd);
				fdep.put(fud.getVar(), fud);
			}
			this.fdependencies.put(tlhs, fdep);
		}
		this.patterns = result;
		return this.patterns;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.soton.service.mediation.Alignment#addPattern(com.hp.hpl.jena.graph
	 * .Triple, java.util.List, uk.soton.service.mediation.Alignment.Relation)
	 */
	// @Override
	public void addPattern(Triple lhs, List<Triple> rhs, Relation relation) {
		Resource lhsa = this.getA();
		Resource ea = this.getA();
		this.inner.add(ea, this.getP(RDFVocabulary.RDF_TYPE),
				this.getR(RDFVocabulary.ENTITY_ALIGNMENT)).add(ea,
				this.getP(RDFVocabulary.LHS), lhsa);
		this.inner.getGraph().getReifier().reifyAs(lhsa.asNode(), lhs);
		for (Triple t : rhs) {
			Resource rhsa = this.getA();
			this.inner.add(ea, this.getP(RDFVocabulary.RHS), rhsa);
			this.inner.getGraph().getReifier().reifyAs(rhsa.asNode(), t);
		}
		this.inner.add(ea, this.getP(RDFVocabulary.HAS_RELATION),
				this.getR(RDFVocabulary.EQ)).add(this.root,
				this.getP(RDFVocabulary.HAS_ENTITY_ALIGNMENT), ea);
	}

	@Override
	public void addRewritingRule(RewritingRule rule) {
		Resource lhsa = this.getA();
		Resource ea = this.getA();
		this.inner.getGraph().getReifier()
				.reifyAs(lhsa.asNode(), rule.getLHS());
		this.inner.add(ea, this.getP(RDFVocabulary.RDF_TYPE),
				this.getR(RDFVocabulary.ENTITY_ALIGNMENT));
		this.inner.add(ea, this.getP(RDFVocabulary.LHS), lhsa);
		for (Triple t : rule.getRHS()) {
			Resource rhsa = this.getA();
			this.inner.add(ea, this.getP(RDFVocabulary.RHS), rhsa);
			this.inner.getGraph().getReifier().reifyAs(rhsa.asNode(), t);
		}
		for (FunctionalDependency fd : rule.getFD()) {
			Resource fdr = getA();
			Seq params = this.inner.createSeq();
			for (Node p : fd.getParam()) {
				if (p.isURI())
					params.add(this.getR(p.getURI()));
				if (p.isBlank())
					params.add(this.getA(p.getBlankNodeId()));
				if (p.isLiteral())
					params.add(p.getLiteralValue());
				// params.add(p);
			}
			this.inner
					.getGraph()
					.getReifier()
					.reifyAs(
							fdr.asNode(),
							new Triple(fd.getVar(), this.getP(fd.getFuncURI())
									.asNode(), params.asNode()));
			this.inner.add(ea,
					this.getP(RDFVocabulary.HAS_FUNCTIONAL_DEPENDENCY), fdr);
		}
		this.inner.add(ea, this.getP(RDFVocabulary.HAS_RELATION),
				this.getR(RDFVocabulary.EQ));
		this.inner.add(this.root,
				this.getP(RDFVocabulary.HAS_ENTITY_ALIGNMENT), ea);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see uk.soton.service.mediation.Alignment#getTargetDatasetURIs()
	 */
	@Override
	public List<String> getTargetDatasetURIs() {
		ArrayList<String> result = new ArrayList<String>();
		NodeIterator target = this.inner
				.listObjectsOfProperty(getP(RDFVocabulary.HAS_TARGET_DATASET));
		for (RDFNode t : target.toList()) {
			result.add(t.asNode().getURI());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * uk.soton.service.mediation.Alignment#setTargetDatasetURIs(java.util.List)
	 */
	@Override
	public void setTargetDatasetURIs(List<String> targetDatasetURIs) {
		for (String uri : targetDatasetURIs) {
			this.inner
					.add(this.root,
							this.getP(RDFVocabulary.HAS_TARGET_DATASET),
							this.getR(uri));
		}
	}

	/**
	 * Gets the functional dependency.
	 * 
	 * @param t
	 *            Triple that represents a functional dependency
	 * @return FunctionalDependency instance
	 */
	private FunctionalDependency getFunctionalDependency(Triple t) {
		Node var = t.getSubject();
		if (var.isBlank()) {
			var = Utility.getVarFromBlank(var);
		}
		Function func = resolver.get(t.getPredicate());
		Node params_node = t.getObject();
		List<Node> params = getCollection(params_node);
		return new FunctionalDependency(var, func, params, t.getPredicate()
				.getURI());
	}

	/**
	 * Gets the collection.
	 * 
	 * @param root
	 *            Root node of the RDF collection
	 * @return List of Node objects that constitute the RDF collection
	 */
	private List<Node> getCollection(Node root) {
		ArrayList<Node> result = new ArrayList<Node>();
		List<Triple> fi = this.inner.getGraph()
				.find(root, Node.createURI(RDFVocabulary.FIRST), null).toList();
		if (!fi.isEmpty()) {
			Node par = fi.get(0).getMatchObject();
			if (par.isBlank()) {
				par = Utility.getVarFromBlank(par);
			}
			result.add(par);
		} else {
			return result;
		}
		fi = this.inner.getGraph()
				.find(root, Node.createURI(RDFVocabulary.SECOND), null)
				.toList();
		if (!fi.isEmpty()) {
			Node par = fi.get(0).getMatchObject();
			if (par.isBlank()) {
				par = Utility.getVarFromBlank(par);
			}
			result.add(par);
		} else {
			return result;
		}
		fi = this.inner.getGraph()
				.find(root, Node.createURI(RDFVocabulary.THIRD), null).toList();
		if (!fi.isEmpty()) {
			Node par = fi.get(0).getMatchObject();
			if (par.isBlank()) {
				par = Utility.getVarFromBlank(par);
			}
			result.add(par);
		} else {
			return result;
		}
		return result;
	}

	@Override
	public Hashtable<Triple, Hashtable<Node, FunctionalDependency>> getFunctionalDependencies() {
		return this.fdependencies;
	}
}
