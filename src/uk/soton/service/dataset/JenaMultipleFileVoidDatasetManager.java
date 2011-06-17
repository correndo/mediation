/*
 * JenaMultipleFileVoidDatasetManager.java
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

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.shared.JenaException;
import com.hp.hpl.jena.util.FileManager;

/**
 * 
 * The Class JenaMultipleFileVoidDatasetManager extends the JenaVoidDatasetManager class using multiple RDF files parsed with Jena API.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class JenaMultipleFileVoidDatasetManager extends JenaVoidDatasetManager {

	/** The Jena Model m that contains the alignments. */
	protected Model m;
	
	/** The list of file names that contain the alignments. */
	protected List<String> filenames;
	
	/**
	 * Instantiates a new jena multiple file void dataset manager.
	 *
	 * @param dirname the name of the directory that contains the RDF models.
	 */
	public JenaMultipleFileVoidDatasetManager(String dirname){
		this.filenames = new ArrayList<String>();
		File dir = new File(dirname);
	    // list the files using our FileFilter
	    File[] files = dir.listFiles(new RDFFileFilter());
	    for (File f : files)
	    {
	    	this.filenames.add(f.getAbsolutePath());
	    }
	}
	
	/* (non-Javadoc)
	 * @see uk.soton.service.dataset.JenaVoidDatasetManager#getModel()
	 */
	@Override
	protected Model getModel() {
		if (this.m != null) return m;
		try {
			this.m = ModelFactory.createDefaultModel();
			Model temp = null;
			for (String uri : this.filenames){
				temp = FileManager.get().loadModel(uri);
				this.m.add(temp);
			}
		} catch (JenaException je){
			Logger.getLogger(JenaMultipleFileVoidDatasetManager.class.getName()).log(Level.SEVERE, "Error in loading file: " + this.filenames);
			this.m = ModelFactory.createDefaultModel();
		}
		return this.m;
	}
	
	/**
	 * The Class RDFFileFilter.
	 * FileFilter for RDF and OWL files.
	 */
	class RDFFileFilter implements FileFilter
	{
	  
  	/** The ok file extensions. */
  	private final String[] okFileExtensions = 
	    new String[] {"rdf", "owl"};

	  /* (non-Javadoc)
  	 * @see java.io.FileFilter#accept(java.io.File)
  	 */
  	@Override
	  public boolean accept(File file)
	  {
	    for (String extension : okFileExtensions)
	    {
	      if (file.getName().toLowerCase().endsWith(extension))
	      {
	        return true;
	      }
	    }
	    return false;
	  }	
	}

}
