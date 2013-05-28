package com.dawidweiss.xsltfilter;

import java.util.HashMap;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * A pool of templates of precompiled stylesheets.
 * 
 * @author Dawid Weiss
 */
final class TemplatesPool {
    /** 
     * A HashMap of precompiled stylesheets (Templates objects) 
     */
    private volatile HashMap stylesheets = new HashMap();
    
    /**
     * If <code>true</code> the templates will not be cached
     * until the application shuts down. This speeds up the
     * application, but may be annoying, especially during
     * development. 
     */
    private final boolean templateCaching;

    /** transformer factory capable of producing SAX-based transformers */
    public final SAXTransformerFactory tFactory;

    /**
     * Check for required facilities. If not available, an exception will be
     * thrown
     */
    public TemplatesPool(boolean templateCaching) throws Exception {
        final TransformerFactory tFactory = TransformerFactory.newInstance();

        if (!tFactory.getFeature(SAXSource.FEATURE)
                || !tFactory.getFeature(SAXResult.FEATURE)) {
            throw new Exception(
                    "Required source types not supported by the Transformer Factory.");
        }

        if (!tFactory.getFeature(SAXResult.FEATURE)
                || !tFactory.getFeature(StreamResult.FEATURE)) {
            throw new Exception(
                    "Required result types not supported by the Transformer Factory.");
        }

        if (!(tFactory instanceof SAXTransformerFactory)) {
            throw new Exception(
                    "TransformerFactory not an instance of SAXTransformerFactory");
        }

        this.tFactory = ((SAXTransformerFactory) tFactory);
        this.templateCaching = templateCaching;
    }

    /**
     * @return returns the identity transformer handler.
     */
    public TransformerHandler getIdentityTransformerHandler()
            throws TransformerConfigurationException {
        return tFactory.newTransformerHandler();
    }

    /**
     * Retrieves a previously stored template, if available.
     */
    public Templates getTemplate(String templateName) {
        return (Templates) stylesheets.get(templateName);
    }

    /**
     * Add a new template to the pool. Addition is quite costly as it replaces the hashmap.
     */
    public void addTemplate(String templatesURL, Templates template) {
        if (templateCaching == false) {
            return;
        }

        synchronized (this) { 
            final HashMap newMap = new HashMap(this.stylesheets);
            newMap.put(templatesURL, template);
            this.stylesheets = newMap;
        }
    }
}