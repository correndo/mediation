package uk.soton.service.mediation;

import java.util.ArrayList;
import org.junit.Test;

public class JenaMultipleFileAlignmentServiceTest {

	@Test
	public void testJenaMultipleFileAlignmentServiceCollectionOfString() {
		ArrayList<String> files = new ArrayList<String>();
		files.add("./resources/testa.rdf");
		files.add("./resources/akt-kisti.rdf");
		@SuppressWarnings("unused")
		JenaMultipleFileAlignmentService jsfa = new JenaMultipleFileAlignmentService(files);
    jsfa.getModel().write(System.out, "TURTLE");
	}

}
