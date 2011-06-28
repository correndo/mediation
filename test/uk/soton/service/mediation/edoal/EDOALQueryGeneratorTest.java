package uk.soton.service.mediation.edoal;

import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.semanticweb.owl.align.AlignmentException;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.edoal.EDOALMediator;
import uk.soton.service.mediation.edoal.EDOALQueryGenerator;

import com.hp.hpl.jena.query.Query;

import fr.inrialpes.exmo.align.parser.AlignmentParser;

public class EDOALQueryGeneratorTest {

	Alignment ja, ja2, ja3;
	org.semanticweb.owl.align.Alignment al, al2, al3;

	@Before
	public void setUp() throws Exception {
		AlignmentParser parser = new AlignmentParser(2);
		parser.initAlignment(null);

		try {
			al = parser.parse("file:./resources/wine.xml");
			ja = EDOALMediator.mediate(al);
			al2 = parser.parse("file:./resources/total.xml");
			ja2 = EDOALMediator.mediate(al2);

			// al3 =
			// parser.parse("file:/Users/gc3/Development/workspace/mediation/resources/edoal.xml");
		} catch (AlignmentException e) {
			// TODO Auto-generated catch block
			fail();
			e.printStackTrace();
		}
	}

	@Test
	public void generateQueriesFromAlignmentTest() {
		ArrayList<Query> result = EDOALQueryGenerator
				.generateQueriesFromAlignment(ja);
		for (Query q : result) {
			System.out.println(q.toString());
		}
	}
}
