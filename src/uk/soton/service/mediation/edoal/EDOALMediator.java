/*
 * EDOALMediator.java
 *
 * Copyright (C) ECS University of Southampton, 2011
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
 */
package uk.soton.service.mediation.edoal;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;

import uk.soton.service.mediation.Alignment;
import uk.soton.service.mediation.JenaAlignment;
import uk.soton.service.mediation.RewritingRule;
import uk.soton.service.mediation.STripleList;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.rdf.model.AnonId;

import fr.inrialpes.exmo.align.impl.edoal.ClassExpression;
import fr.inrialpes.exmo.align.impl.edoal.EDOALCell;
import fr.inrialpes.exmo.align.impl.edoal.Expression;
import fr.inrialpes.exmo.align.impl.edoal.PathExpression;
import fr.inrialpes.exmo.align.impl.edoal.Transformation;

/**
 * The Class EDOALMediator is a utility class that generate internal triples patterns from an EDOAL alignment.
 * @author Gianluca Correndo <gc3@ecs.soton.ac.uk>
 */
public class EDOALMediator {

  private static Logger log = Logger.getLogger(EDOALMediator.class.getName());
  

  /**
   * Mediate an EDOAL alignment into an internal alignment based on RDF pattern rewriting rules.
   *
   * @param a the EDOAL alignment to mediate
   * @return the mediated alignment expressed as BGP rewriting rules
   * @throws AlignmentException the alignment exception
   */
  public static Alignment mediate(org.semanticweb.owl.align.Alignment a) throws AlignmentException {
    JenaAlignment forth = new JenaAlignment();
    JenaAlignment back = new JenaAlignment();
    List<String> source = new ArrayList<String>();
    source.add(a.getOntology1URI().toString());
    List<String> target = new ArrayList<String>();
    target.add(a.getOntology2URI().toString());

    forth.setSourceOntologyURIs(source);
    forth.setTargetOntologyURIs(target);
    back.setSourceOntologyURIs(target);
    back.setTargetOntologyURIs(source);

    for (Cell cell : a) {
      mediate(cell, forth, back);
    }
    Alignment result = JenaAlignment.merge(back, forth);
    return result;
  }

  /**
   * Mediate method that translate an EDOAL alignment cell into an internal alignment based on rewriting rules.
   *
   * @param cell the EDOAL alignment cell
   * @param forth the forward alignment to populate (source->target)
   * @param back the backward alignment to populate (target->source)
 * @throws AlignmentException 
   */
  public static void mediate(Cell cell, Alignment forth, Alignment back) throws AlignmentException {
	EDOALCell ecell = (EDOALCell)cell;
    Object from = cell.getObject1();
    Object to = cell.getObject2();
    final MediationResult altfrom, altto;
    altfrom = mediateExpression((Expression) from);
    altto = mediateExpression((Expression) to);
    mediateTransform(ecell.transformations() , forth , back);
    	altto.getPatterns().replace(altto.getS(), altfrom.getS());
    altto.getPatterns().replace(altto.getO(), altfrom.getO());
    if (altfrom.getPatterns().size() == 1 && (cell.getRelation().getRelation().equals("Equivalence") || cell.getRelation().getRelation().equals("<")|| cell.getRelation().getRelation().equals("="))) {
      forth.addRewritingRule(new RewritingRule() {{
          setLHS(altfrom.getPatterns().get(0).asTriple());
          setRHS(altto.getPatterns().asTripleList());
          setFD(altfrom.getFD());
        }});
    }
    if (altto.getPatterns().size() == 1 && (cell.getRelation().getRelation().equals("Equivalence") || cell.getRelation().getRelation().equals("<")|| cell.getRelation().getRelation().equals("="))) {
      back.addRewritingRule(new RewritingRule() {{
          setLHS(altto.getPatterns().get(0).asTriple());
          setRHS(altfrom.getPatterns().asTripleList());
          setFD(altfrom.getFD());
        }});
    }
  }

  /**
   * Gets the mediated result for a ClassExpression construct.
   *
   * @param ce the ClassExpression
   * @return the mediated result
   * @throws AlignmentException 
   */
  private static MediationResult getClassExpression(final ClassExpression ce) throws AlignmentException {
    final Node v = Utility.getNewVar();
    ClassVisitor cv = ClassVisitor.$().setS(v);
    cv.visit(ce);
    return cv.getMediationResult().setS(v);
  }

  

  /**
   * Gets the mediated result for a PathExpression construct.
   *
   * @param e the PathExpression instance
   * @return the mediation result
   * @throws AlignmentException 
   */
  private static MediationResult getPathExpression(final PathExpression e) throws AlignmentException {
    final Node o = Utility.getNewVar();
    final Node s = Utility.getNewVar();
    PropertyVisitor pv = PropertyVisitor.$().setS(s).setO(o);
    pv.visit(e);
    return pv.getMediationResult().setS(s).setO(o);
  }
  
  
  /**
   * Gets the mediated result for a class expression construct.
   *
   * @param e the Expression
   * @return the mediation result
 * @throws AlignmentException 
   */
  private static MediationResult mediateExpression(Expression e) throws AlignmentException {
    if (e instanceof ClassExpression) {
      return getClassExpression((ClassExpression) e);
    } else if (e instanceof PathExpression) {
      return getPathExpression((PathExpression) e);
    } else {
      log.log(Level.WARNING, String.format("mediateExpression: Expression %s not recognised", e.toString()));
      return new MediationResult() {{ setPatterns(new STripleList()); }};
    }
  }

  private static void mediateTransform(Set<Transformation> ts, Alignment forth, Alignment back) throws AlignmentException{
	  if (ts != null) {
		  for (Transformation t : ts) {
			  TransformationVisitor tv = TransformationVisitor.$();
			  tv.visit(t);
			  if (tv.getRewritingRule() == null)
				  break;
			  if (tv.isForward()){ 
				  forth.addRewritingRule(tv.getRewritingRule());
			  } else {
				  back.addRewritingRule(tv.getRewritingRule());
			  }
		  }
	  }
  }
}
