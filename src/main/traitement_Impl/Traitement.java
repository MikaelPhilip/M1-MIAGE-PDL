package main.traitement_Impl;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import main.generationJSON.GenerationInter;
import main.generationJSON_impl.Generation;
import main.traitement.TraitementInter;

/**
 * Class to load and verify pcm
 */
public class Traitement implements TraitementInter{
	/**Logger for feedback**/
	private static final Logger _logger = Logger.getLogger(Traitement.class);
	//Pcm use for traitement
	private PCM pcm;
	//Class we call when everything is ok
	private GenerationInter json ;
	
	
	/**
	 * Method to load pcm
	 */
	@Override
	public void pcmLoad(String files) throws IOException {
		//Create file
        File pcmFile = new File(files);
        //Call Open compare API to load
        PCMLoader loader = new KMFJSONLoader();
        //Load file
        PCM pcm = loader.load(pcmFile).get(0).getPcm();
        _logger.info("Pcm load succefully");
        //Call the method that checks PCM's integrity
        pcmVerify(pcm);
	}

	@Override
	/** 
	 * Method verify pcm
	 * @param : PCM whom Integrity needs to be checked
	 **/
	public void pcmVerify(PCM pcm) {
        // We start by checking if products'name are not null
        for (Product product : pcm.getProducts()) {
        	try{
        		if(product.getName()==null){
        	          throw new TraitementPcmException();
        	     }
        	 }
        	 catch(TraitementPcmException e){
        	      //Message 
        		 _logger.error("Un produit de la PCM est null. ");
        		//End of Programm
             	System.exit(0);
        	 }
        }
        // Then we check if features'name are not null
        for (Feature feature : pcm.getConcreteFeatures()){
        	try{
        		if(feature.getName()==null){
       	          throw new TraitementPcmException();
        		}
       	     }catch(TraitementPcmException e) {
       	    	 //Process message however you would like
       	    	 _logger.error("Une caractéristique de la PCM est null. ");
        		//End of Programm
             	System.exit(0);
       	     }
       	 }
        _logger.info("Pcm valid");
        json = new Generation(this);
        _logger.info("Launch generation");
        this.pcm = pcm; //save pcm (for test)
        //Launch generation of json
		json.generateJSON(pcm);
	}
	public PCM getPcm() {
		return pcm;
	}

}
