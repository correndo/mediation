/*
 * ValueVisitor.java
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

import com.hp.hpl.jena.datatypes.TypeMapper;
import com.hp.hpl.jena.graph.Node;

import fr.inrialpes.exmo.align.impl.edoal.Apply;
import fr.inrialpes.exmo.align.impl.edoal.InstanceId;
import fr.inrialpes.exmo.align.impl.edoal.PropertyId;
import fr.inrialpes.exmo.align.impl.edoal.RelationId;
import fr.inrialpes.exmo.align.impl.edoal.Value;
import fr.inrialpes.exmo.align.impl.edoal.ValueExpression;


/**
 * The ValueVisitor class implements the AlignmentVisitor interface and produces Node value instances.
 * 
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class ValueVisitor implements AlignmentVisitor , ValueGenerator{

	/**
	 * The field fd is the FunctionalDependency instance
	 */
	private FunctionalDependency fd;
	
	/**
	 * The field value is the Node value returned
	 */
	private Node value;
	
	public ValueVisitor(){
	}
	
	/* (non-Javadoc)
	 * @see org.semanticweb.owl.align.AlignmentVisitor#init(java.util.Properties)
	 */
	@Override
	public void init(Properties arg0) {
		// TODO Auto-generated method stub
	}

	/**
	 * The visit is redirected based on the type of the input object visited.
	 * @see org.semanticweb.owl.align.AlignmentVisitor#visit(org.semanticweb.owl.align.Visitable)
	 */
	@Override
	public void visit(Visitable v) throws AlignmentException {
		if (v instanceof InstanceId) visit((InstanceId) v);
		else if (v instanceof Value) visit((Value) v);
		else if (v instanceof Apply) visit((Apply) v);
		else if (v instanceof PropertyId) visit((PropertyId) v);
		else if (v instanceof RelationId) visit((RelationId) v);
		else Logger.getAnonymousLogger().log(Level.WARNING, "Value type not supported:"+v);
	}
	
	/**
	 * The visit method for Apply instances
	 * @param v Apply instance
	 * @throws AlignmentException
	 * 
	 */
	public void visit(Apply v) throws AlignmentException{
		this.value = (this.value == null ? Node.createAnon() : this.value);
		ArrayList<Node> args = new ArrayList<Node>();
		for (ValueExpression ve : 	v.getArguments()){
			ValueVisitor vv = ValueVisitor.$();
			vv.visit(ve);
			args.add(vv.getValue());			
		}
		this.fd = new FunctionalDependency(this.value , null , args , v.getOperation().toString());
	}

	/**
	 * The visit method for RelationId instances
	 * @param v RelationId instance
	 * 
	 */
	public void visit(RelationId v){
		this.value = Node.createURI(v.getURI().toString());
	}

	/**
	 * The visit method for PropertyId instances
	 * @param v PropertyId instance
	 * 
	 */
	public void visit(PropertyId v){
		this.value = Node.createURI(v.getURI().toString());
	}
	
	/**
	 * The visit method for InstanceId instances
	 * @param v InstanceId instance
	 * 
	 */
	public void visit(InstanceId v){
		this.value = Node.createURI(v.getURI().toString());
	}

	/**
	 * The visit method for Value instances
	 * @param v Value instance
	 * 
	 */
	public void visit(Value v){
		if (v.getType() != null) 
			this.value = Node.createLiteral(v.getValue(), null, TypeMapper.getInstance().getSafeTypeByName(v.getType().toString()));
		else
			this.value = Node.createLiteral(v.getValue());
	}
	
	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.edoal.ValueGenerator#getValue()
	 */
	@Override
	public Node getValue() {
		return value;
	}
	
	/**
	 * The setValue is the setter method for the value field
	 * @param v the Node value to set
	 * @return <b>this</b> instance
	 * 
	 */
	public ValueVisitor setValue(Node v){
		this.value = v;
		return this;
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.edoal.ValueGenerator#getFD()
	 */
	@Override
	public FunctionalDependency getFD() {
		return fd;
	}
	
	/**
	 * The $ method is a constructor shortcut
	 * @return a new instance of the class
	 * 
	 */
	static public ValueVisitor $(){
		return new ValueVisitor();
	}
}
