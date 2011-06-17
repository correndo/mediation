/*
 * ClassVisitor.java
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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owl.align.Visitable;

import uk.soton.service.mediation.STriple;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.vocabulary.RDF;

import fr.inrialpes.exmo.align.impl.edoal.ClassConstruction;
import fr.inrialpes.exmo.align.impl.edoal.ClassDomainRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassExpression;
import fr.inrialpes.exmo.align.impl.edoal.ClassId;
import fr.inrialpes.exmo.align.impl.edoal.ClassOccurenceRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassTypeRestriction;
import fr.inrialpes.exmo.align.impl.edoal.ClassValueRestriction;
import fr.inrialpes.exmo.align.impl.edoal.Comparator;
import fr.inrialpes.exmo.align.parser.SyntaxElement;


/**
 * The ClassVisitor class implements the AlignmentVisitor interface and produces MediationResults instances.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class ClassVisitor implements AlignmentVisitor  , MediationResultGenerator{

	/**
	 * The field mr is the result of the mediation after the alignment visit.
	 */
	MediationResult mr;
	
	/**
	 * The field s is the subject node of the produced pattern
	 */
	Node s = null;
	
	/**
	 * The field o is the object node of the produced pattern
	 */
	Node o = null;
	
	public ClassVisitor() {
		this.mr = new MediationResult();
	}

	/**
	 * The setS is the setter method for the s field
	 * @param s the subject node
	 * @return <b>this</b> instance
	 * 
	 */
	public ClassVisitor setS(Node s) {
		this.s = s;
		return this;
	}

	/**
	 * The setO is the setter method for the o field
	 * @param o the object node
	 * @return <b>this</b> instance
	 * 
	 */
	public ClassVisitor setO(Node o) {
		this.o = o;
		return this;
	}


	/**
	 * The setMediationResult method is the setter for the mr field.
	 * It is possible to set an external mediation result, partially composed by other MediationResultGenerator
	 * @param mr the MediationResult instance to set
	 * @return <b>this</b> instance
	 * 
	 */
	public ClassVisitor setMediationResult(MediationResult mr) {
		this.mr = mr;
		return this;
	}


	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.edoal.MediationResultGenerator#getMediationResult()
	 */
	@Override
	public MediationResult getMediationResult() {
		return this.mr;
	}
	
	/* (non-Javadoc)
	 * @see org.semanticweb.owl.align.AlignmentVisitor#init(java.util.Properties)
	 */
	@Override
	public void init(Properties arg0) {
		// NO OP
	}

	/**
	 * The visit is redirected based on the type of the input object visited.
	 * @see org.semanticweb.owl.align.AlignmentVisitor#visit(org.semanticweb.owl.align.Visitable)
	 */
	@Override
	public void visit(Visitable ce) throws AlignmentException {
		if (ce instanceof ClassId) this.visit((ClassId) ce);
		else if (ce instanceof ClassConstruction) this.visit((ClassConstruction) ce);
		else if (ce instanceof ClassValueRestriction) this.visit((ClassValueRestriction) ce);
		else if (ce instanceof ClassDomainRestriction) this.visit((ClassDomainRestriction) ce);
		else if (ce instanceof ClassOccurenceRestriction) this.visit((ClassOccurenceRestriction) ce);
		else if (ce instanceof ClassTypeRestriction) this.visit((ClassTypeRestriction) ce);
		else Logger.getAnonymousLogger().log(Level.WARNING,	"Class not handled yet:" + ce.getClass());
	}

	/**
	 * The visit method for ClassDomainRestriction instances
	 * @param cdr ClassDomainRestriction instance
	 * @throws AlignmentException
	 * 
	 */
	private void visit(ClassDomainRestriction cdr) throws AlignmentException {
		Node x = getNode(this.s);
		Node y = getNode(this.o); 
		PropertyVisitor.$().setS(x).setO(y).setMediationResult(this.mr).visit(cdr.getRestrictionPath());
		ClassVisitor.$().setS(y).setMediationResult(this.mr).visit(cdr.getDomain());
	}

	/**
	 * The visit method for ClassTypeRestriction instances
	 * @param cdr ClassTypeRestriction instance
	 * @throws AlignmentException
	 * 
	 */
	private void visit(ClassTypeRestriction cdr) throws AlignmentException {
		Node x = getNode(this.s);
		Node y = getNode(this.o); 
		PropertyVisitor.$().setS(x).setO(y).setMediationResult(this.mr).visit(cdr.getRestrictionPath());
		//TODO - handle case properly - find good test case
	}
	
	/**
	 * The visit method for ClassOccurenceRestriction instances
	 * @param cor ClassOccurenceRestriction instance
	 * @throws AlignmentException
	 * 
	 */
	private void visit(ClassOccurenceRestriction cor) throws AlignmentException {
		if ((cor.getComparator() == Comparator.EQUAL) && (cor.getOccurence() > 0)) 
			for (int index = 0 ; index < cor.getOccurence() ; index++){
				Node x = getNode(this.s);
				Node y = getNode(null); 
				PropertyVisitor.$().setS(x).setO(y).setMediationResult(this.mr).visit(cor.getRestrictionPath());
			}
		else Logger.getAnonymousLogger().log(Level.WARNING, "Only EQUAL comparator handled.");
	}
	
	/**
	 * The visit method for ClassValueRestriction instances
	 * @param cvr ClassValueRestriction instance
	 * @throws AlignmentException
	 * 
	 */
	private void visit(ClassValueRestriction cvr) throws AlignmentException {
		if (cvr.getComparator() == Comparator.EQUAL){
			Node cs = (this.s == null ? Utility.getNewVar() : this.s);
			ValueVisitor vv = ValueVisitor.$();
			vv.visit(cvr.getValue());
			Node os = vv.getValue();
			this.mr.getFD().add(vv.getFD());
			//TODO : manage all cases of ValueExpression
			PropertyVisitor.$().setS(cs).setO(os).setMediationResult(this.mr).visit(cvr.getRestrictionPath());
		} else Logger.getAnonymousLogger().log(Level.WARNING, "Only EQUAL restriction allowed.");
	}

	/**
	 * The visit method for ClassId instances
	 * @param ce ClassId instance
	 * 
	 */
	private void visit(ClassId ce) {
		Node cs = getNode(this.s);
		this.mr.getPatterns().add(new STriple(cs, RDF.type.asNode(), Node.createURI(ce.getURI().toString())));
	}
	
	/**
	 * The visit method for ClassConstruction instances
	 * @param ce ClassConstruction instance
	 * @throws AlignmentException
	 * 
	 */
	private void visit(ClassConstruction ce) throws AlignmentException {
		if (ce.getOperator() == SyntaxElement.Constructor.AND){
			Node cs = getNode(this.s);
			for (ClassExpression ice : ce.getComponents()){
				$().setS(cs).setMediationResult(this.mr).visit(ice);
			}
		} else Logger.getAnonymousLogger().log(Level.WARNING, "Only AND constructor supported.");
	}
	

	/**
	 * The $ method is a constructor shortcut
	 * @return a new instance of the class
	 * 
	 */
	public static ClassVisitor $(){
		return new ClassVisitor();
	}
	
	/**
	 * The getNode method returns the input Node if provided, a new Var otherwise
	 * @param n input Node
	 * @return the input Node if provided, a new Var otherwise
	 * 
	 */
	private Node getNode(Node n){
		return (n == null ? Utility.getNewVar() : n);
	}
}
