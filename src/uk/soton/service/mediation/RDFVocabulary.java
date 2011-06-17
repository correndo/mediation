/*
 * RDFVocabulary.java
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


/**
 * The RDFVocabulary contains the Alignment entities' URIs plus some other utility URIs.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 *
 */
public abstract class RDFVocabulary {
  /** The Constant HAS_FUNCTIONAL_DEPENDENCY. */
  public static final String HAS_FUNCTIONAL_DEPENDENCY = "http://ecs.soton.ac.uk/om.owl#hasFunctionalDependency";
  /** The Constant ALIGNMENT. */
  public static final String ALIGNMENT = "http://ecs.soton.ac.uk/om.owl#Alignment";
  /** The Constant HAS_SOURCE_ONTOLOGY. */
  public static final String HAS_SOURCE_ONTOLOGY = "http://ecs.soton.ac.uk/om.owl#hasSourceOntology";
  /** The Constant HAS_TARGET_ONTOLOGY. */
  public static final String HAS_TARGET_ONTOLOGY = "http://ecs.soton.ac.uk/om.owl#hasTargetOntology";
  /** The Constant RHS. */
  public static final String RHS = "http://ecs.soton.ac.uk/om.owl#rhs";
  /** The Constant HAS_TARGET_DATASET. */
  public static final String HAS_TARGET_DATASET = "http://ecs.soton.ac.uk/om.owl#hasTargetDataset";
  /** The Constant HAS_ENTITY_ALIGNMENT. */
  public static final String HAS_ENTITY_ALIGNMENT = "http://ecs.soton.ac.uk/om.owl#hasEntityAlignment";
  /** The Constant EQ. */
  public static final String EQ = "http://ecs.soton.ac.uk/om.owl#EQ";
  /** The Constant LHS. */
  public static final String LHS = "http://ecs.soton.ac.uk/om.owl#lhs";
  /** The Constant ENTITY_ALIGNMENT. */
  public static final String ENTITY_ALIGNMENT = "http://ecs.soton.ac.uk/om.owl#EntityAlignment";
  /** The Constant RDF_TYPE. */
  public static final String RDF_TYPE = "http://www.w3.org/1999/02/22-rdf-syntax-ns#type";
  /** The Constant HAS_RELATION. */
  public static final String HAS_RELATION = "http://ecs.soton.ac.uk/om.owl#hasRelation";
  /** The Constant SAMEAS. */
  public static final String SAMEAS = "http://ecs.soton.ac.uk/om.owl#sameas";
  /** The Constant FIRST. */
  public static final String FIRST = "http://www.w3.org/1999/02/22-rdf-syntax-ns#_1";
  /** The Constant SECOND. */
  public static final String SECOND = "http://www.w3.org/1999/02/22-rdf-syntax-ns#_2";
  /** The Constant THIRD. */
  public static final String THIRD = "http://www.w3.org/1999/02/22-rdf-syntax-ns#_3";
  /** The Constant FN (SPARQL XPath function namespace) */
  private static final String FN = "http://www.w3.org/2005/xpath-functions/";
  /** The constant for fn:sum*/
  public static final String FN_SUM = FN+"sum";
  /** The constant for fn:sub*/
  public static final String FN_SUB = FN+"sub";
}
