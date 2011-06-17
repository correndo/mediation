/*
 * EDOALExamples.java
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
package uk.soton.examples;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owl.align.AlignmentException;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.Transformer;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.EntityTranslationService;
import uk.soton.service.mediation.EntityTranslationServiceImpl;
import uk.soton.service.mediation.algebra.EntityTranslation;
import uk.soton.service.mediation.algebra.ExtendedOpAsQuery;
import uk.soton.service.mediation.edoal.EDOALMediator;
import uk.soton.service.mediation.edoal.EDOALQueryGenerator;
import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class EDOALExamples {

	private static Alignment a;
	/**
	 * Rewrite a query about wine
	 */
	private static void queryRewriting() {
		String querys = "PREFIX wine:    <http://www.w3.org/TR/2003/CR-owl-guide-20030818/wine#>\n"
				+ "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "SELECT DISTINCT ?v ?y WHERE { \n"
				+ "  ?v a wine:VintageYear , wine:Bordeaux ; wine:yearValue ?y.} LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		Transform translation = new EntityTranslation(ets, a);
		Op translated = Transformer.transform(translation, op);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("Original query:\n" + querys);
		System.out.println("Modified query:\n" + queryt);
	}
	
	private static void constructQuery(){
		List<Query> constructs = EDOALQueryGenerator.generateQueriesFromAlignment(a);
		System.out.println("CONSTRUCT queries from alignment\n");
		for (Query q : constructs){
			System.out.println("<----------------------->");
			System.out.println(q.toString());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AlignmentParser parser = new AlignmentParser(2);
		parser.initAlignment(null);
		try {
			a = EDOALMediator.mediate(parser.parse("file:./resources/wineexample.xml"));
		} catch (AlignmentException e) {
			Logger.getAnonymousLogger().log(Level.SEVERE,
					"Couldn't load the alignment:", e);
		}

		queryRewriting();
		constructQuery();
	}

}
