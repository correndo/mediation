/*
 * Utility.java
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

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;


import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * The Class Utility provides common usage functions.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class Utility {

  /**
   * Gets the namespace of a URI expressed in string format.
   *
   * @param uri  string representation of a URI
   * @return string representation of the namespace of the given URI
   */
  public static String getNamespace(String uri) {
    if (uri.contains("#")) {
      return uri.split("#")[0] + "#";
    } else {
      return uri.substring(0, uri.lastIndexOf("/") + 1);
    }
  }

  /**
   * Gets the local name of a URI in string format.
   *
   * @param uri  string representation of a URI
   * @return string representation of the local name of the given RUI
   */
  public static String getLocalName(String uri) {
    if (uri.contains("#")) {
      return uri.split("#")[1];
    } else {
      return uri.substring(uri.lastIndexOf("/") + 1, uri.length());
    }
  }

  /**
   * The match function matches two input triples if possible.
   *
   * @param lhs left Triple
   * @param rhs right Triple
   * @return <b>null</b> if the two Triples are not matchable, otherwise it returns
   * an Hashtable with the resulting bindings otherwise
   */
  public static Hashtable<Node, Node> match(Triple lhs, Triple rhs) {
    Hashtable<Node, Node> temp, result;
    //Match subject
    temp = match(lhs.getSubject(), rhs.getSubject());
    if (temp == null) {
      return null;
    } else {
      result = temp;
    }
    //Match predicate
    temp = match(lhs.getPredicate(), rhs.getPredicate());
    if (temp == null) {
      return null;
    } else {
      result.putAll(temp);
    }
    //Match object
    temp = match(lhs.getObject(), rhs.getObject());
    if (temp == null) {
      return null;
    } else {
      result.putAll(temp);
    }
    return result;
  }

  /**
   * The match function matches two nodes if possible.
   *
   * @param lhs left Node
   * @param rhs right node
   * @return <b>null</b> if the two Nodes are not matchable, otherwise
   * an Hashtable with the resulting bindings otherwise
   */
  @SuppressWarnings("serial")
  public static Hashtable<Node, Node> match(final Node lhs, final Node rhs) {
    if (lhs.isBlank()) {
      return new Hashtable<Node, Node>() {

        {
          put(getVarFromBlank(lhs), rhs);
        }
      };
    } else if (lhs.isVariable()) {
      return new Hashtable<Node, Node>() {

        {
          put(lhs, rhs);
        }
      };
    } else {
      if (lhs != rhs) {
        return null;
      } else {
        return new Hashtable<Node, Node>();
      }
    }
  }

  /**
   * Gets the var from blank.
   *
   * @param var blank node
   * @return Variable node whose name is the blank node ID without illegal chars
   */
  public static Node getVarFromBlank(Node var) {
    return Node.createVariable(var.getBlankNodeLabel().replaceAll("\\p{Punct}", ""));
  }

  //TODO : Add Variable conversion from blank node and ANY node from null values
  /**
   * Gets the reified.
   *
   * @param n the n
   * @param g the g
   * @return the reified
   */
  public static Triple getReified(Node n, Graph g) {
    try {
      Triple result = new Triple(
              g.find(n, Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject"), null).next().getMatchObject(),
              g.find(n, Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate"), null).next().getMatchObject(),
              g.find(n, Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#object"), null).next().getMatchObject());
      return result;
    } catch (Exception e) {
      Logger.getLogger(Utility.class.getName()).log(Level.WARNING, e.toString());
    }
    return null;
  }

  public static Triple getReified(Resource n, Model m) {
    try {
      Triple result = new Triple(
              m.listObjectsOfProperty(n, m.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#subject")).next().asNode(),
              m.listObjectsOfProperty(n, m.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#predicate")).next().asNode(),
              m.listObjectsOfProperty(n, m.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#object")).next().asNode());
      return result;
    } catch (Exception e) {
      Logger.getLogger(Utility.class.getName()).log(Level.WARNING, e.toString());
    }
    return null;
  }

  /**
   * Join.
   *
   * @param <T> the generic type
   * @param src the src
   * @param pattern the pattern
   * @param dst the dst
   * @return the t
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static <T extends Appendable> T join(Iterable<? extends CharSequence> src, CharSequence pattern, T dst) throws IOException {
    Iterator<? extends CharSequence> it = src.iterator();
    if (it.hasNext()) {
      dst.append(it.next());
    }
    while (it.hasNext()) {
      dst.append(pattern).append(it.next());
    }
    return dst;
  }

  /**
   * Join.
   *
   * @param src the src
   * @param pattern the pattern
   * @return the string
   */
  public static String join(Iterable<? extends CharSequence> src, CharSequence pattern) {
    try {
      return join(src, pattern, new StringBuilder()).toString();
    } catch (IOException excpt) {
      throw new Error("StringBuilder should not throw IOExceptions!");
    }
  }

  /**
   * Substitute blank with vars.
   *
   * @param lhs the lhs
   * @return the triple
   */
  public static Triple substituteBlankWithVars(Triple lhs) {
    Node s, p, o;
    s = lhs.getSubject();
    if (s.isBlank()) {
      s = getVarFromBlank(s);
    }
    p = lhs.getPredicate();
    if (p.isBlank()) {
      p = getVarFromBlank(p);
    }
    o = lhs.getObject();
    if (o.isBlank()) {
      o = getVarFromBlank(o);
    }
    return new Triple(s, p, o);
  }
}
