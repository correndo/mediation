package uk.soton.service.mediation.format;

import org.junit.Test;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.JenaSingleFileAlignmentService;

public class AlignAPIXMLParserTest {

	@Test
	public void testParseXMLOntologyMapping() {
		//Alignment[] al = AlignAPIXMLParser.parseXMLOntologyMapping("./resources/akt-kisti.rdf");
		Alignment[] al = AlignAPIXMLParser.parseXMLOntologyMapping("./resources/ecs-foaf.rdf");
		//JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/akt-kisti-test.rdf");
		JenaSingleFileAlignmentService jsfa = new JenaSingleFileAlignmentService("./resources/ecs-foaf-modified.rdf");
		jsfa.addAlignment(al[0]);
		jsfa.addAlignment(al[1]);
	}

}
