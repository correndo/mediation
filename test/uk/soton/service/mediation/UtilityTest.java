package uk.soton.service.mediation;

import java.util.ArrayList;
import java.util.Hashtable;

import org.junit.Test;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;

public class UtilityTest {

	@Test
	public void testMatchStatementStatement() {
		Triple s1 = new Triple(Node.createAnon(), Node.createURI("rdf:type"),Node.createURI(":Person")); 
		Triple s2 = new Triple(Node.createVariable("v"), Node.createURI("rdf:type"),Node.createURI(":Person"));
		Hashtable<Node,Node> r = Utility.match(s1,s2);
		System.out.println(s1 + " = " + s2);
		System.out.println(r);		
		s1 = new Triple(Node.createVariable("x"), Node.createURI("rdf:type"),Node.createURI(":Person")); 
		s2 = new Triple(Node.createVariable("v"), Node.createURI("rdf:type"),Node.createURI(":Person"));
		r = Utility.match(s1,s2);
		System.out.println(s1 + " = " + s2);
		System.out.println(r);
		s1 = new Triple(Node.createVariable("x"), Node.createURI("http://ns/property"),Node.createVariable("j")); 
		s2 = new Triple(Node.createVariable("v"), Node.createURI("http://ns/property"),Node.createURI(":Person"));
		r = Utility.match(s1,s2);
		System.out.println(s1 + " = " + s2);
		System.out.println(r);
		System.out.println("<---->");
	}

	@Test
	public void testMatchNodeNode() {
		Node n1 = Node.createAnon();
		Node n2 = Node.createVariable("v");
		Hashtable<Node,Node> r = Utility.match(n1,n2);
		System.out.println(r);
		n2 = Node.createLiteral("hello");
		n1 = Node.createVariable("v");
		r = Utility.match(n1,n2);
		System.out.println(r);
		n1 = Node.createURI("http://example.org/n1");
		n2 = Node.createVariable("v");
		r = Utility.match(n2,n1);
		System.out.println(r);		
	}
	
	@SuppressWarnings("serial")
	@Test
	public void joinTest(){
		ArrayList<String> elements = new ArrayList<String>(){{add("first");add("second");add("third");}};
		String s = Utility.join(elements, " UNION ");
		System.out.println(s);
	}

}
