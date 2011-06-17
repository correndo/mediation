package uk.soton.service.mediation.edoal;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owl.align.AlignmentException;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.EntityTranslationService;
import uk.soton.service.mediation.EntityTranslationServiceImpl;
import uk.soton.service.mediation.JenaAlignment;
import uk.soton.service.mediation.algebra.EntityTranslation;
import uk.soton.service.mediation.algebra.ExtendedOpAsQuery;
import uk.soton.service.mediation.edoal.EDOALMediator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.Transformer;

import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class EDOALKettleBoilerMediatorTest {
	Alignment ja;
	
	@Before
	public void setUp() throws Exception {
		AlignmentParser parser = new AlignmentParser(2);
		parser.initAlignment(null);
		org.semanticweb.owl.align.Alignment al;
		try {
			al = parser.parse("file:resources/edoal/kettleboiler.xml");
			ja = EDOALMediator.mediate(al);
			Model m = ((JenaAlignment)ja).getModel();
			System.out.println("[total-owlable] model");
			m.write(System.out, "Turtle");
		} catch (AlignmentException e) {
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void testMediateKettleBoilerAlignment() {
		String querys = "PREFIX source:    <http://correndo.ecs.soton.ac.uk/ontology/source#>"
				+ "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX owl:   <http://www.w3.org/2002/07/owl#>"
				+ "SELECT DISTINCT ?v ?y ?t WHERE { "
				+ "           ?v a source:Person ; source:hasKettle ?y ; source:hasTemperature \"100\"^^<http://www.w3.org/2001/XMLSchema#int>.} LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		Transform translation = new EntityTranslation(ets, ja);
		Op translated = Transformer.transform(translation, op);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("[kettle-boiler] original query:\n" + query.toString());
		System.out.println("[kettle-boiler] translated query:\n" + queryt.toString());
	}
}
