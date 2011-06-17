/*
 * SameAs.java
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
package uk.soton.service.mediation.algebra.operation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.NodeValue;
import com.hp.hpl.jena.sparql.function.FunctionBase2;

/**
 * The SameAs class implements a function that retrieve the equivalent URI from the <a href="http://sameas.org">SameAs</a> 
 * service that satisfy a given regex pattern. The default return value it's the input URI itself.
 * 
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class SameAs extends FunctionBase2 {

  /* (non-Javadoc)
 * @see com.hp.hpl.jena.sparql.function.FunctionBase2#exec(com.hp.hpl.jena.sparql.expr.NodeValue, com.hp.hpl.jena.sparql.expr.NodeValue)
 */
@Override
  public NodeValue exec(NodeValue uri, NodeValue pattern) {
    if (uri.asNode().isVariable()) {
      return uri;
    }
    //Simulate the identity
    Node nuri = uri.asNode();
    String result = null;
    String regexPattern = pattern.asUnquotedString();
    try {
      result = getSameAs(nuri.getURI(), regexPattern);
      if (result == null) {
        Logger.getAnonymousLogger().log(Level.INFO, "No results found for uri:" + nuri);
        return NodeValue.makeNode(nuri);
      } else {
        return NodeValue.makeNode(Node.createURI(result));
      }
    } catch (Exception e)  {
      return uri;
    }
  }

  /**
 * The getSameAs method calls the online sameas service for retrieving the bundle of equivalent URIs.
 * @param uri the String representation of the entity URI
 * @param pattern The String representation of the regex pattern to satisfy.
 * @return the first URI that satisfies the conditions.
 * 
 */
private static String getSameAs(String uri, String pattern) {
    String service_base = "http://sameas.org/text?uri=";
    String result = null;
    try {
      URL service_url = new URL(service_base + URLEncoder.encode(uri, "UTF-8"));
      URLConnection conn = service_url.openConnection();
      BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = rd.readLine()) != null) {
        if (line.matches("<" + pattern + ">")) {
          result = line.substring(1, line.length() - 1);
          Logger.getAnonymousLogger().log(Level.INFO, "result: " + result);
        }
      }
      rd.close();

    } catch (MalformedURLException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, "uri: " + uri);
    } catch (UnsupportedEncodingException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
    } catch (IOException e) {
      Logger.getAnonymousLogger().log(Level.SEVERE, e.toString());
    }
    return result;
  }
}
