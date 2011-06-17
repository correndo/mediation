/*
 * JenaAlignmentService.java
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphExtract;
import com.hp.hpl.jena.graph.TripleBoundary;
import com.hp.hpl.jena.graph.compose.Union;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;

/**
 * The abstract Class JenaAlignmentService implements the AlignmentService using the Jena API.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public abstract class JenaAlignmentService implements AlignmentService {

	/** The query string tds for selecting the alignments that has a given target dataset. */
	private String tds = "SELECT DISTINCT ?a WHERE {?a a <http://ecs.soton.ac.uk/om.owl#Alignment> ; <http://ecs.soton.ac.uk/om.owl#hasTargetDataset> <%tds%>}";
	
	/** The query string st for selecting the alignments that has source and target datasets. */
	private String st = "SELECT DISTINCT ?a WHERE { ?a a <http://ecs.soton.ac.uk/om.owl#Alignment>. %st%}";

	/**
	 * Gets the model that contains the alignments.
	 *
	 * @return the model
	 */
	protected abstract Model getModel();
	
	/** The log. */
	private Logger log = Logger.getLogger(JenaAlignmentService.class.getName());
	
	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.AlignmentService#getAlignmentForTargetDataset(java.lang.String)
	 */
	@Override
	public Alignment getAlignmentForTargetDataset(String targetDatasetURI) {
		Model m = this.getModel();
		if (m == null) {
			log.log(Level.SEVERE, "No model retrieved!");
			return null;
		}
		Graph result = null;
		Query query = QueryFactory.create(tds.replace("%tds%", targetDatasetURI)) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode a = soln.get("a") ; 
		      Graph g = (new GraphExtract(TripleBoundary.stopNowhere)).extract(a.asNode() , m.getGraph());
		      if (result == null) result = g;
		      else result = new Union(result,g);
		    }
		} catch (Exception e){
			log.log(Level.SEVERE, e.toString());
		}
		return new JenaAlignment(ModelFactory.createModelForGraph(result));
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.AlignmentService#getAlignmentFromTo(java.util.List, java.util.List)
	 */
	@Override
	public Alignment getAlignmentFromTo(List<String> sourceOntologyURIs,
			List<String> targetOntologyURIs) {
		Model m = this.getModel();
		if (m == null) {
			log.log(Level.SEVERE, "No model retrieved!");
			return null;
		}
		Graph result = null;
		
		String from = Utility.join(produceTriple(sourceOntologyURIs, " { ?a <http://ecs.soton.ac.uk/om.owl#hasSourceOntology> <%s%>} ", "%s%"), " UNION ");
		String to = Utility.join(produceTriple(targetOntologyURIs, " { ?a <http://ecs.soton.ac.uk/om.owl#hasTargetOntology> <%s%>} ", "%s%"), " UNION ");
			
		Query query = QueryFactory.create(st.replace("%st%", from + " UNION "+ to)) ;
		QueryExecution qexec = QueryExecutionFactory.create(query, m) ;
		try {
		    ResultSet results = qexec.execSelect() ;
		    for ( ; results.hasNext() ; )
		    {
		      QuerySolution soln = results.nextSolution() ;
		      RDFNode a = soln.get("a") ; 
		      Graph g = (new GraphExtract(TripleBoundary.stopNowhere)).extract(a.asNode() , m.getGraph());
		      if (result == null) result = g;
		      else result = new Union(result,g);
		    }
		} catch (Exception e){
			log.log(Level.SEVERE, e.toString());
		}
		return new JenaAlignment(ModelFactory.createModelForGraph(result));		
	}
	
	/**
	 * Produce triple.
	 *
	 * @param uris list of URIs
	 * @param template template to use for the replaceAll
	 * @param field placeholder in the template
	 * @return list of strings obtained by applying replaceAll to the input list of URIs
	 * with the input template and placeholder.
	 */
	private List<String> produceTriple(List<String> uris, String template, String field){
		ArrayList<String> result = new ArrayList<String>();
		for (String el : uris){
			result.add((new String(template)).replaceAll(field, el));
		}
		return result;
	}

}
