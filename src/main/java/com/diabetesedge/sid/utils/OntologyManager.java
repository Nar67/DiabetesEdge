package com.diabetesedge.sid.utils;

import java.util.Iterator;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntDocumentManager;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;

public class OntologyManager
{
    private static OntModel model;

    private static Integer incrementalID;

    private static OntDocumentManager dm;

    public OntologyManager(final String JENA_PATH, final String filePath,
        final String uri)
    {
        model = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_TRANS_INF);
        dm = model.getDocumentManager();
        dm.addAltEntry(uri, "file:" + JENA_PATH + filePath);
        model.read(uri);
        incrementalID = 0;
    }

    public void releaseOntology()
    {
        if (!model.isClosed())
        {
            model.close();
        }
    }

    public void listIndividuals()
    {
        for (Iterator i = model.listIndividuals(); i.hasNext();)
        {
            Individual dummy = (Individual) i.next();
            System.out.println("Ontology has individual: ");
            System.out.println("   ·Individual: " + dummy);
            Property nameProperty = model.getProperty(
                "http://www.sid-upc.edu/nars/ontologies/2020/4/edge_diabetes#glucose_measurement");
            RDFNode nameValue = dummy.getPropertyValue(nameProperty);
            System.out.println("   hasPizzaName Property: " + nameValue);
        }
    }

    public Individual createIndividual(final String resourceURI)
    {
        return model.createIndividual(resourceURI + incrementalID++,
            model.createResource(resourceURI));
    }

    public Property getProperty(final String uri)
    {
        return model.getProperty(uri);
    }

    public DatatypeProperty getDataTypeProperty(final String uri)
    {
        return model.getDatatypeProperty(uri);
    }
}
