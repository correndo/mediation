package uk.soton.service.mediation.algebra;

import java.util.ArrayList;

import org.junit.Test;

import uk.soton.service.mediation.AlignmentService;
import uk.soton.service.mediation.EntityTranslationService;
import uk.soton.service.mediation.EntityTranslationServiceImpl;
import uk.soton.service.mediation.JenaAlignment;
import uk.soton.service.mediation.JenaMultipleFileAlignmentService;
import uk.soton.service.mediation.JenaSingleFileAlignmentService;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.Transformer;

public class EntityTranslationTest {

	@SuppressWarnings("serial")
	@Test
	public void testTransformOpBGP() {
		//JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/test.rdf");
		JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/akt-kisti-modified.rdf");
		JenaAlignment ja = (JenaAlignment) jsfa.getAlignmentFromTo(
				new ArrayList<String>() {
					{
						add("http://www.aktors.org/ontology/portal#");
					}
				}, new ArrayList<String>() {
					{
						add("http://www.kisti.re.kr/isrl/ResearchRefOntology#");
					}
				});
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		String querys = "PREFIX id:    <http://southampton.rkbexplorer.com/id/>" +
						"PREFIX rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#>" +
						"PREFIX rdfs:  <http://www.w3.org/2000/01/rdf-schema#>" +
						"PREFIX owl:   <http://www.w3.org/2002/07/owl#>" +
						"PREFIX akt:   <http://www.aktors.org/ontology/portal#>" +
						"PREFIX akts:  <http://www.aktors.org/ontology/support#>" +
						"SELECT DISTINCT ?p ?b WHERE { " +
						"           ?p akt:has-author id:person-02686," +
						"           ?b." + 
						" } LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		Transform translation = new EntityTranslation(ets, ja);
		Op translated = Transformer.transform(translation, op);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("original query:\n" + query.toString());
		System.out.println("translated query:\n" + queryt.toString());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testTransformOpBGP2() {
		JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/ecs-foaf-dbpedia.rdf");
		JenaAlignment ja = (JenaAlignment) jsfa.getAlignmentFromTo(
				new ArrayList<String>() {
					{
						add("http://rdf.ecs.soton.ac.uk/ontology/ecs#");
					}
				}, new ArrayList<String>() {
					{
						add("http://xmlns.com/foaf/0.1/");
					}
				});
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		String querys = "PREFIX id:    <http://southampton.rkbexplorer.com/id/>" +
						"PREFIX ecs:   <http://rdf.ecs.soton.ac.uk/ontology/ecs#>" +						
						"SELECT ?p ?o WHERE { " +					
						"           id:person-02686 ?p ?o." +						
						" } LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		Transform translation = new EntityTranslation(ets, ja);
		Op translated = Transformer.transform(translation, op);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("original query:\n" + query.toString());
		System.out.println("translated query:\n" + queryt.toString());
	}
	
	@SuppressWarnings("serial")
	@Test
	public void testTransformOpBGP3() {
		AlignmentService jsfa = new JenaMultipleFileAlignmentService("/Users/gc3/Development/workspace/swmediator/war/WEB-INF/alignments");
		JenaAlignment ja = (JenaAlignment) jsfa.getAlignmentFromTo(
				new ArrayList<String>() {
					{
						add("http://rdf.ecs.soton.ac.uk/ontology/ecs#");
					}
				}, new ArrayList<String>() {
					{
						add("http://xmlns.com/foaf/0.1/");
					}
				});
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		String querys = "PREFIX id:    <http://southampton.rkbexplorer.com/id/>" +
						"PREFIX ecs:   <http://rdf.ecs.soton.ac.uk/ontology/ecs#>" +						
						"SELECT ?p ?o WHERE { " +					
						"           id:person-02686 ?p ?o." +						
						" } LIMIT 10";
		Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
		Op op = Algebra.compile(query);
		Transform translation = new EntityTranslation(ets, ja);
		Op translated = Transformer.transform(translation, op);
		Query queryt = ExtendedOpAsQuery.asQuery(translated);
		System.out.println("---->");
		System.out.println("original query:\n" + query.toString());
		System.out.println("translated query:\n" + queryt.toString());
	}
	
}