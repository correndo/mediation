package uk.soton.service.mediation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.Model;

import uk.soton.service.mediation.Alignment.Relation;

public class JenaAlignmentTest {

	@SuppressWarnings("serial")
	@Test
	public void testAddPattern() {
		//JenaSingleFileAlignmentService jsfas = new JenaSingleFileAlignmentService(	"./resources/testa.rdf");
		Alignment a = new JenaAlignment();
		a.setSourceOntologyURIs(new ArrayList<String>() {
			{
				add("http://correndo.ecs.soton.ac.uk/ontology/source#");
			}
		});
		a.setTargetOntologyURIs(new ArrayList<String>() {
			{
				add("http://correndo.ecs.soton.ac.uk/ontology/target#");
			}
		});
		a.setTargetDatasetURIs(new ArrayList<String>() {
			{
				add("http://correndo.ecs.soton.ac.uk/ontology/target#dataset");
			}
		});
			
		Relation relation = Alignment.Relation.EQ;
		final Triple lhs = new Triple(
				Node.createAnon(new AnonId("a")),
				Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
				Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/source#Kettle"));
		final List<Triple> ths = new ArrayList<Triple>(){{add(new Triple(
										Node.createAnon(new AnonId("a")),
										Node.createURI("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
										Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/target#Boiler")));
								add(new Triple(
										Node.createAnon(new AnonId("a")),
										Node.createURI("http://correndo.ecs.soton.ac.uk/ontology/target#type"),
										Node.createLiteral("kettle-type")));
		}};
    a.addRewritingRule(new RewritingRule(){{
      setLHS(lhs);
      setRHS(ths);
    }});
		//a.addPattern(lhs, ths, relation);
    Model m = ((JenaAlignment)a).getModel();
    m.write(System.out, "TURTLE"); 
		//jsfas.addAlignment(a);
	}

}
