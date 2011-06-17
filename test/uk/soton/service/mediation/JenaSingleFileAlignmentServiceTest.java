package uk.soton.service.mediation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class JenaSingleFileAlignmentServiceTest {

	@SuppressWarnings("serial")
	@Test
	public void testGetAlignmentForTargetDataset() {
		JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/testa.rdf");
		List<String> targetOntologyURIs = new ArrayList<String>(){{add("http://correndo.ecs.soton.ac.uk/ontology/target#"); add("http://correndo.ecs.soton.ac.uk/ontology/target#");}};
		List<String> sourceOntologyURIs = new ArrayList<String>(){{add("http://correndo.ecs.soton.ac.uk/ontology/source#"); add("http://correndo.ecs.soton.ac.uk/ontology/source#");}};
		@SuppressWarnings("unused")
		JenaAlignment ja1 = (JenaAlignment) jsfa.getAlignmentFromTo(sourceOntologyURIs, targetOntologyURIs);
		
		JenaAlignment ja = (JenaAlignment) jsfa.getAlignmentForTargetDataset("http://correndo.ecs.soton.ac.uk/ontology/target#dataset");
		ArrayList<Triple> t = new ArrayList<Triple>(){{
			add(new Triple(Node.createVariable("guy"), Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"), Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/source#Person")));
			add(new Triple(Node.createVariable("guy"), Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/source#hasKettle"), Node.createVariable("k")));
			add(new Triple(Node.createVariable("guy"), Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/source#name"), Node.createLiteral("John")));
			}};
		EntityTranslationService ets = new EntityTranslationServiceImpl();
		List<Triple> lt = ets.getTranslatedTriples(ja, t).getTranslatedBGP();		
		System.out.println(lt);
	}

}
