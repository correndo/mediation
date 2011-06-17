/*
 * DataSetManager.java
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

import java.util.List;

/**
 * The DatasetManager interface provides basics information about datasets and their SPARQL endpoints.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public interface DatasetManager {

	/**
	 * Gets the datasets id.
	 *
	 * @return a list of datasets'IDs
	 */
	public List<String> getDatasetsID();
	
	/**
	 * Gets the SPARQL end-point url for a given dataset id.
	 *
	 * @param id dataset ID
	 * @return string representation of the SPARQL end-point URL
	 */
	public String getSPARQLEndpointURL(String id);
}
