package uk.soton.external;

import fr.inrialpes.exmo.align.parser.AlignmentParser;
import fr.inrialpes.exmo.align.parser.RDFParser;
import org.junit.Test;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;


public class EDOALParsing {
	
	@Test
	 public void ParseTest() throws AlignmentException{
		AlignmentParser parser = new AlignmentParser( 2 );
		parser.initAlignment(null);
		final Alignment al = parser.parse("file:resources/wine.xml");
		for ( Cell c : al ) {
			System.out.println(c);
		}
	 }
}
