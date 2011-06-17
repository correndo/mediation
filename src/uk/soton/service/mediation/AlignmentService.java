/*
 * AlignmentService.java
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

import java.util.List;

/**
 * The Interface AlignmentService defines a service that provides ontology alignments depending on some input parameters.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public interface AlignmentService {

	/**
	 * Gets the alignment from the source ontology URIs to the target ontology URIs.
	 *
	 * @param sourceOntologyURIs URI of the source ontology
	 * @param targetOntologyURIs URI of the target ontology
	 * @return an Alignment object that incorporate all the
	 * alignments from the source to the target ontology
	 */
	public Alignment getAlignmentFromTo(List<String> sourceOntologyURIs , List<String> targetOntologyURIs);
	
	/**
	 * Gets the alignment for target dataset.
	 *
	 * @param targetDatasetURI URI for the target dataset
	 * @return an Alignment object that incorporate all the
	 * alignments for the target dataset
	 */
	public Alignment getAlignmentForTargetDataset(String targetDatasetURI);
	
	/**
	 * Adds the alignment to the collection of known alignments.
	 *
	 * @param a Alignment to be added to the collection managed by this service
	 */
	public void addAlignment(Alignment a);
}
