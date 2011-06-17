/*
 * MediationResult.java
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

import uk.soton.service.mediation.FunctionalDependency;
import uk.soton.service.mediation.STripleList;

import com.hp.hpl.jena.graph.Node;

  /**
   * The Class MediationResult implements the result of a mediation applied to an EDOAL expression.
   * 
   * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
   */
  class MediationResult {

    /**
     * The setFD method is the setter for the fds field (FunctionalDependency entities)
     * 
	 * @param fds the Functional Dependencies to set
	 * @return <b>this</b> instance
	 */
	public MediationResult setFD(ArrayList<FunctionalDependency> fds) {
		this.fds = fds;
		return this;
	}

	/**
	 * The getFD is the getter method for the fds field
	 * 
	 * @return the list of functional dependencies
	 */
	public ArrayList<FunctionalDependency> getFD() {
		return fds;
	}

	/**
     * Instantiates a new mediation result.
     */
    public MediationResult() {
      this.patterns = new STripleList();
    }

    /**
     * Sets the p field.
     *
     * @param p the eventual replacement to be applied to the predicate.
	 * @return <b>this</b> instance
     */
    public MediationResult setP(Node p) {
      this.p = p;
      return this;
    }

    /**
     * Gets the eventual replacement to be applied to the predicate.
     *
     * @return the p field
     */
    public Node getP() {
      return p;
    }

    /**
     * Sets the s.
     *
     * @param s the eventual replacement to be applied to the subject.
	 * @return <b>this</b> instance
     */
    public MediationResult setS(Node s) {
      this.s = s;
      return this;

    }

    /**
     * Gets the s field.
     *
     * @return the s field
     */
    public Node getS() {
      return s;
    }

    /**
     * Sets the o field.
     *
     * @param o the eventual replacement to be applied to the object.
	 * @return <b>this</b> instance
     */
    public MediationResult setO(Node o) {
      this.o = o;
      return this;
    }

    /**
     * Gets the o field.
     *
     * @return the o field
     */
    public Node getO() {
      return o;
    }

    /**
     * Sets the triple patterns of the mediation result.
     *
     * @param patterns the patterns
	 * @return <b>this</b> instance
     */
    public MediationResult setPatterns(STripleList patterns) {
      this.patterns = patterns;
      return this;
    }

    /**
     * Merge a mediation result to <b>this</b>: adding the triples and unifying s,p,o.
     *
     * @param other the other mediation result
	 * @return <b>this</b> instance
     */
    public MediationResult merge(MediationResult other) {
      STripleList triples = other.getPatterns();
      if (other.getS() != null) {
        if (this.s != null) {
          triples.replace(other.getS(), this.s);
        } else {
          this.s = other.getS();
        }
      }
      if (other.getP() != null) {
        if (this.p != null) {
          triples.replace(other.getP(), this.p);
        } else {
          this.p = other.getP();
        }
      }
      if (other.getO() != null) {
        if (this.o != null) {
          triples.replace(other.getO(), this.o);
        } else {
          this.o = other.getO();
        }
      }
      this.patterns.addAll(triples);
      this.fds.addAll(other.getFD());
      return this;
    }

    /**
     * Gets the triple patterns.
     *
     * @return the patterns
     */
    public STripleList getPatterns() {
      return patterns;
    }
    /** The s field: the eventual replacement to be applied to the subject. */
    private Node s;
    /** The p parameter: the eventual replacement to be applied to the predicate. */
    private Node p;
    /** The o parameter: the eventual replacement to be applied to the object. */
    private Node o;
    /** The triple patterns that represents the EDOAL expression. */
    private STripleList patterns;
    /** The functional dependencies between eventual variables in transformations are involved */
    private ArrayList<FunctionalDependency> fds = new ArrayList<FunctionalDependency>();
  }