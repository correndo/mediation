/*
 * JenaVoidDatasetManager.java
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
package uk.soton.service.dataset;

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * 
 * The Class JenaVoidDatasetManager implements a DatasetManager using Jena as an RDF API and Void as dataset descriptor vocabulary.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public abstract class JenaVoidDatasetManager implements DatasetManager {

	/** The Constant RDF_TYPE. */
	private static final String RDF_TYPE   = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";	
	
	/** The Constant VOID_DATASET. */
	private static final String VOID_DATASET = "http://rdfs.org/ns/void#Dataset";
	
	/** The Constant VOID_SPARQLENDPOINT. */
	private static final String VOID_SPARQLENDPOINT = "http://rdfs.org/ns/void#sparqlEndpoint";	
	
	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	protected abstract Model getModel();
	
	/* (non-Javadoc)
	 * @see uk.soton.service.dataset.DatasetManager#getDatasetsID()
	 */
	@Override
	public List<String> getDatasetsID() {
		Model m = this.getModel();
		ArrayList<String> result = new ArrayList<String>();
		ResIterator ri = m.listResourcesWithProperty(m.getProperty(RDF_TYPE), Node.createURI(VOID_DATASET));
		for(Resource r : ri.toList()){
			result.add(r.getURI());
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.dataset.DatasetManager#getSPARQLEndpointURL(java.lang.String)
	 */
	@Override
	public String getSPARQLEndpointURL(String id) {
		Model m = this.getModel();
		String result = null;
		NodeIterator ni = m.listObjectsOfProperty( m.getResource(id), m.getProperty(VOID_SPARQLENDPOINT));
		if (ni.hasNext()) result = ni.next().asNode().getURI();		
		return result;
	}

}
