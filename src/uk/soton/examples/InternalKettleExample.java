package uk.soton.examples;

import java.util.List;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.EntityTranslationService;
import uk.soton.service.mediation.EntityTranslationServiceImpl;
import uk.soton.service.mediation.JenaAlignment;
import uk.soton.service.mediation.algebra.EntityTranslation;
import uk.soton.service.mediation.algebra.ExtendedOpAsQuery;
import uk.soton.service.mediation.edoal.EDOALQueryGenerator;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.Transformer;


public class InternalKettleExample {

	private static Alignment ja;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Model m = ModelFactory.createDefaultModel().read("file:resources/kettle_internal.ttl","TTL");
		ja = new JenaAlignment(m);
		String querys = "PREFIX source:    <http://correndo.ecs.soton.ac.uk/ontology/source#>"
				+ "PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
				+ "PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>"
				+ "PREFIX owl:   <http://www.w3.org/2002/07/owl#>"
				+ "SELECT DISTINCT ?v ?y ?z ?lt WHERE { "
				+ "           ?v a source:Person ; source:hasKettle ?y ; source:hasKettle ?l1 , ?l2. " 
				+ "           ?y source:hasTemperature 10 ." 
				+ "           ?l1 source:hasTemperature ?lt1 . ?l2 source:hasTemperature ?lt2 } LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		Transform translation = new EntityTranslation(ets, ja);
		Op translated = Transformer.transform(translation, op);
		System.out.println("[kettle-boiler] ARQ original:" + op);
		System.out.println("[kettle-boiler] ARQ translated:" + translated);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("[kettle-boiler] original query:\n"
				+ query.toString());
		System.out.println("[kettle-boiler] translated query:\n"
				+ queryt.toString());
		List<Query> qs = EDOALQueryGenerator.generateQueriesFromAlignment(ja);
		System.out.println("[kettle-boiler] - CONSTRUCT Queries");
		for (Query q : qs){
			System.out.println(q.toString());
		}
	}
}
