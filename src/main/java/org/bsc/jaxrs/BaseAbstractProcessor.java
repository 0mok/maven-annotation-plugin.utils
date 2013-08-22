/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bsc.jaxrs;

import java.io.IOException;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.tools.FileObject;
import javax.tools.StandardLocation;


import java.util.Collections;


/**
 *
 * @author bsorrentino
 * 
 * 
 */
//@SupportedSourceVersion(SourceVersion.RELEASE_6)
public abstract class BaseAbstractProcessor extends AbstractProcessor {

    protected void info( String msg ) {
        processingEnv.getMessager().printMessage(Kind.NOTE, msg );
    }

    protected void warn( String msg ) {
        //logger.warning(msg);
        processingEnv.getMessager().printMessage(Kind.WARNING, msg );
    }

    protected void warn( String msg, Throwable t ) {
        //logger.log(Level.WARNING, msg, t );
        processingEnv.getMessager().printMessage(Kind.WARNING, msg );
        t.printStackTrace(System.err);
    }

    protected void error( String msg ) {
        //logger.severe(msg);
        processingEnv.getMessager().printMessage(Kind.ERROR, msg );
    }

    protected void error( String msg, Throwable t ) {
        //logger.log(Level.SEVERE, msg, t );
        processingEnv.getMessager().printMessage(Kind.ERROR, msg );
        t.printStackTrace(System.err);
    }

    /**
     * 
     * @param resource
     * @param packageName
     * @return
     * @throws IOException 
     */
    protected FileObject getResourceFormClassPath(
            final String resource, 
            final String packageName ) throws IOException 
    {
        final Filer filer = processingEnv.getFiler();
        
        FileObject f = filer.getResource(
                            StandardLocation.CLASS_PATH, 
                            packageName, 
                            resource);

        java.io.InputStream is = f.openInputStream();

        if( is==null ) {
            warn( String.format("resource [%s] not found!", resource) );
            return null;
        }
        
        is.close();
        
        return f;
    }
    
    
    /**
     * 
     * @param subfolder subfolder 
     * @param filePath relative path 
     * @return
     * @throws IOException 
     */
    protected FileObject getOutputFile( 
                String subfolder, 
                String filePath ) throws IOException 
    {

        final Filer filer = processingEnv.getFiler();
        
    	Element e = null;
    	FileObject res = filer.createResource(
                            StandardLocation.SOURCE_OUTPUT, 
                            subfolder, 
                            filePath, 
                            e);
        return res;
    }
    
    /**
     * 
     * @param e
     * @return
     * @throws ClassNotFoundException
     */
    protected Class<?> getClassFromElement( Element e ) throws ClassNotFoundException 
    {
    	if( null==e ) throw new IllegalArgumentException("e is null!");
    	if( ElementKind.CLASS!=e.getKind() ) {
            throw new IllegalArgumentException( String.format("element [%s] is not a class!", e));
        }
    	
    	TypeElement te = (TypeElement) e;
    	
    	info( String.format("loading class [%s]", te.getQualifiedName().toString()));
    	
    	return Class.forName(te.getQualifiedName().toString());
    	
    }
    
    /**
     * 
     * @return com.sun.source.util.Trees
     */
    /*
    protected com.sun.source.util.Trees newTreesInstance() {

        return com.sun.source.util.Trees.instance(processingEnv);
        
    };
    */
    
    /**
     * 
     * @return 
     */
    protected java.util.Map<String,String> getOptions() 
    {
        java.util.Map<String,String> optionMap = processingEnv.getOptions();
        
        if(optionMap==null) {
            optionMap = Collections.emptyMap();
        }
        return optionMap ;
    }
    
    /**
     * 
     * @param typeElement
     * @return 
     */
    protected String getFullClassName( Element typeElement ) {
     
        if( typeElement instanceof TypeElement  ) {
            
            return ((TypeElement)typeElement).getQualifiedName().toString();
        }
        
        return typeElement.getSimpleName().toString();
    }
    
		


}
