package uk.soton.service.mediation;

import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import uk.soton.service.mediation.FunctionalDependency;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.Syntax;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.sparql.algebra.Algebra;
import com.hp.hpl.jena.sparql.algebra.Op;
import com.hp.hpl.jena.sparql.algebra.Transform;
import com.hp.hpl.jena.sparql.algebra.Transformer;
import com.hp.hpl.jena.sparql.function.library.sqrt;
import uk.soton.service.mediation.algebra.EntityTranslation;
import uk.soton.service.mediation.algebra.ExtendedOpAsQuery;
import uk.soton.service.mediation.algebra.FunctionWrapper;
import uk.soton.service.mediation.algebra.operation.SameAs;
import uk.soton.service.mediation.edoal.EDOALQueryGenerator;

public class RewritingRuleTest {

  JenaAlignment ja = null;

  @Before
  public void setUp() throws Exception {
    final Node name = Node.createAnon(AnonId.create("name"));
    final Node pid1 = Node.createAnon(AnonId.create("person1"));
    final Node pid2 = Node.createAnon(AnonId.create("person2"));
    
    Triple lhs = new Triple(pid1, Node.createURI("http://foaf.org/name"), name);
    @SuppressWarnings("serial")
    final FunctionalDependency fd = new FunctionalDependency(pid2, FunctionWrapper.$(new SameAs()), new ArrayList<Node>() {{ add(pid1); add(Node.createLiteral("http://dbpedia.org/*")); }}, RDFVocabulary.SAMEAS);
    @SuppressWarnings("serial")
    ArrayList<Triple> rhs = new ArrayList<Triple>() {{  
        add(new Triple(pid2, Node.createURI("http://dbpedia.org/onto/name"), name));
      }};
    @SuppressWarnings("serial")
    RewritingRule rr = new RewritingRule().setLHS(lhs).setRHS(rhs).setFD(new ArrayList<FunctionalDependency>() {{ add(fd); }});
    ja = new JenaAlignment();
    ja.addRewritingRule(rr);
    //ja.getModel().write(System.out, "RDF/XML-ABBREV");
  }

  @Test
  public void testToString() {
   // ja.getModel().write(System.out, "TURTLE");
  }

  @Test
  public void testQueryRewrite1() {
    String querys = "SELECT DISTINCT ?y WHERE { <http://semanticweb.org/id/Nigel_Shadbolt> <http://foaf.org/name> ?y.} LIMIT 10";
    Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
    Op op = Algebra.compile(query);
    EntityTranslationService ets = new EntityTranslationServiceImpl();
    Transform translation = new EntityTranslation(ets, ja);
    Op translated = Transformer.transform(translation, op);
    Query queryt = ExtendedOpAsQuery.asQuery(translated);
    System.out.println("original query:\n" + query.toString());
    System.out.println("translated query:\n" + queryt.toString());
  }
  
  @Test
  public void testQueryRewrite2() {
    String querys = "SELECT DISTINCT ?v ?y WHERE { ?v <http://foaf.org/name> ?y.} LIMIT 10";
    Query query = QueryFactory.create(querys, Syntax.syntaxARQ);
    Op op = Algebra.compile(query);
    EntityTranslationService ets = new EntityTranslationServiceImpl();
    Transform translation = new EntityTranslation(ets, ja);
    Op translated = Transformer.transform(translation, op);
    Query queryt = ExtendedOpAsQuery.asQuery(translated);
    System.out.println("original query:\n" + query.toString());
    System.out.println("translated query:\n" + queryt.toString());
  }
  
  @Test
  public void testQueryGeneration(){ 
    ArrayList<Query> result = EDOALQueryGenerator.generateQueriesFromAlignment(ja);
    for (Query q : result){
      System.out.println(q.toString());
    }
  }
}
