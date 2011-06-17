/*
 * AlignAPIXMLParser.java
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
package uk.soton.service.mediation.format;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.JenaAlignment;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import uk.soton.service.mediation.RewritingRule;

public class AlignAPIXMLParser {
	//TODO : Probably to delete after EDOAL parser integration finished
	private static int FORWARD = 0;
	private static int BACKWARD = 1;
	private static Node rdftype = Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");

	/**
	 * 
	 * @param fileName
	 * @return an Alignment object that contains two ontology mappings, the
	 *         first from the source ontology to the target, the second for the
	 *         inverse.
	 */
	public static Alignment[] parseXMLOntologyMapping(String fileName) {
		Alignment[] om = null;
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			Document dom = null;
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(fileName);
			om = getAlignment(dom); 
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(AlignAPIXMLParser.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (SAXException ex) {
			Logger.getLogger(AlignAPIXMLParser.class.getName()).log(
					Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(AlignAPIXMLParser.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return om;
	}

	@SuppressWarnings("serial")
	private static Alignment[] getAlignment(Document dom) {
		Element docEle = dom.getDocumentElement();
		Alignment[] al = { new JenaAlignment(), new JenaAlignment() };
		final String sourceDataSet = ((Element) ((Element) docEle.getElementsByTagName("onto1").item(0)).getElementsByTagName("Ontology").item(0)).getAttribute("rdf:about");
		final String targetDataSet = ((Element) ((Element) docEle.getElementsByTagName("onto2").item(0)).getElementsByTagName("Ontology").item(0)).getAttribute("rdf:about");
		al[FORWARD].setSourceOntologyURIs(new ArrayList<String>() {
			{
				add(sourceDataSet);
			}
		});
		al[BACKWARD].setTargetOntologyURIs(new ArrayList<String>() {
			{
				add(sourceDataSet);
			}
		});
		al[FORWARD].setTargetOntologyURIs(new ArrayList<String>() {
			{
				add(targetDataSet);
			}
		});
		al[BACKWARD].setSourceOntologyURIs(new ArrayList<String>() {
			{
				add(targetDataSet);
			}
		});

		// Retrieve the alignments
		NodeList cells = docEle.getElementsByTagName("Cell");
		if (cells != null && cells.getLength() > 0) {
			for (int i = 0; i < cells.getLength(); i++) {
				// get the cell element
				Element el = (Element) cells.item(i);
				addEntityAlignment(el, al);
			}
		}
		return al;
	}

	private static void addEntityAlignment(Element el, Alignment[] al) {
		Element entity1 = (Element) el.getElementsByTagName("entity1").item(0);
		Element entity2 = (Element) el.getElementsByTagName("entity2").item(0);
		String relation = ((Element) el.getElementsByTagName("relation")
				.item(0)).getChildNodes().item(0).getNodeValue();
		String fromURI = entity1.getAttribute("rdf:resource");
		String toURI = entity2.getAttribute("rdf:resource");
		if ("c=".equals(relation)) {
			addConceptAlignment(fromURI, toURI, al[FORWARD]);
			addConceptAlignment(toURI, fromURI, al[BACKWARD]);
		} else {
			addPropertyAlignment(fromURI, toURI, al[FORWARD]);
			addPropertyAlignment(toURI, fromURI, al[BACKWARD]);
		}

	}

	@SuppressWarnings("serial")
	private static void addConceptAlignment(final String from, final String to, Alignment a) {
		Logger.getAnonymousLogger().log(Level.INFO, "ca: " + from + " -> " + to);
		final Node el = Node.createAnon();
    a.addRewritingRule(new RewritingRule(){{
      setLHS(new Triple(el, rdftype, Node.createURI(from)));
      setRHS(new ArrayList<Triple>() {{add(new Triple(el, rdftype, Node.createURI(to)));}});
    }});
		/*a.addPattern(new Triple(el, rdftype, Node.createURI(from)),
				new ArrayList<Triple>() {
					{
						add(new Triple(el, rdftype, Node.createURI(to)));
					}
				}, Alignment.Relation.EQ);*/
	}
	
	@SuppressWarnings("serial")
	private static void addPropertyAlignment(final String from, final String to, Alignment a) {
		Logger.getAnonymousLogger().log(Level.INFO, "pa: " + from + " -> " + to);
		final Node el = Node.createAnon();
		final Node el_val = Node.createAnon();
    a.addRewritingRule(new RewritingRule(){{
      setLHS(new Triple(el, Node.createURI(from), el_val));
      setRHS(new ArrayList<Triple>() {{add(new Triple(el, Node.createURI(to), el_val));}});
    }});
		//a.addPattern(new Triple(el, Node.createURI(from), el_val),
		//		new ArrayList<Triple>() {{add(new Triple(el, Node.createURI(to), el_val));}}, Alignment.Relation.EQ);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
