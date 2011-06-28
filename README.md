=================
Mediation toolkit
=================

It's a lightweight toolkit to implement ontological mediation over RDF.
It uses ontology mappings in order to rewrite SPARQL SELECT queries and to generate SPARQL CONSTRUCT queries to import an external data set.
 
API
___

The tool is divided in the following packages:

* [uk.soton.service.dataset](https://github.com/correndo/mediation/tree/master/src/uk/soton/service/dataset) Provides the classes and interfaces necessaries to manages distributed datasets.
* [uk.soton.service.mediation](https://github.com/correndo/mediation/tree/master/src/uk/soton/service/mediation) Provides the classes and interfaces necessaries to mediate RDF documents and SPARQL queries using graph rewriting rules.
* [uk.soton.service.mediation.algebra](https://github.com/correndo/mediation/tree/master/src/uk/soton/service/mediation/algebra) Provides the classes and interfaces necessaries to manipulate SPARQL at the algebra level.
* [uk.soton.service.mediation.algebra.operation](https://github.com/correndo/mediation/tree/master/src/uk/soton/service/mediation/algebra/operation) Provides the implementation of SPARQL XPath functions.
* [uk.soton.service.mediation.edoal](https://github.com/correndo/mediation/tree/master/src/uk/soton/service/mediation/edoal) Provides the classes and interfaces necessaries to interface with the EDOAL ontology alignment format.