package uk.soton.service.mediation.algebra.operation;

import org.junit.Test;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.sparql.expr.NodeValue;

public class SameAsTest {

	@Test
	public void testExecNodeValueNodeValue() {
		SameAs sa = new SameAs();
		sa.exec(NodeValue.makeNode(Node.createURI("http://southampton.rkbexplorer.com/id/person-02686")), NodeValue.makeString("http://kisti.rkbexplorer.com/id/\\S*"));
	}
}
