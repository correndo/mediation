/*
 * JenaSingleFileAlignmentService.java
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.GraphUtil;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;

/**
 * The Class JenaSingleFileAlignmentService extends the JenaAlignmentService class using a single file to contain the alignments.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class JenaSingleFileAlignmentService extends JenaAlignmentService {

	/** The filename of the RDF model. */
	private String filename;
	
	/** The Model instance. */
	protected Model m;
	
	/**
	 * Instantiates a new jena single file alignment service.
	 *
	 * @param filename the name of the file of the RDF model
	 */
	public JenaSingleFileAlignmentService(String filename){
		this.filename = filename;
	}
	
	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.JenaAlignmentService#getModel()
	 */
	@Override
	protected Model getModel() {
		if (this.m != null) return m;
		try {
			this.m = FileManager.get().loadModel(this.filename);
		} catch (JenaException je){
			Logger.getLogger(JenaSingleFileAlignmentService.class.getName()).log(Level.SEVERE, "Error in loading file: " + this.filename);
			this.m = ModelFactory.createDefaultModel();
		}
		return this.m;
	}

	/* (non-Javadoc)
	 * @see uk.soton.service.mediation.AlignmentService#addAlignment(uk.soton.service.mediation.Alignment)
	 * TODO: To implement
	 */
	@Override
	public void addAlignment(Alignment a) {
		if (a instanceof JenaAlignment){
			JenaAlignment ja = (JenaAlignment)a;
			Model m = this.getModel();
			Graph g = m.getGraph();
			for (Triple t : GraphUtil.findAll(g).toList()){
				m.getGraph().add(t);
			}
			try {
				m.write(new FileOutputStream(this.filename), "RDF/XML-ABBREV");
			} catch (FileNotFoundException e) {
				Logger.getLogger(JenaSingleFileAlignmentService.class.getName()).log(Level.SEVERE, "Error in saving file: " + this.filename);
			}
		} else {
			Logger.getLogger(JenaSingleFileAlignmentService.class.getName()).log(Level.SEVERE, "No JenaAlignment provided - to do (cross saving)");
		}		
	}
}
