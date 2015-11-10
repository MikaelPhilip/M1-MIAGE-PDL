package main.traitement_Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.impl.io.KMFJSONLoader;
import org.opencompare.api.java.io.PCMLoader;

import main.generationJSON_impl.Generation;
import main.traitement.TraitementInter;

public class Traitement implements TraitementInter{

	private PCM pcm;
	private Generation json ;
	
	@Override
	public void pcmLoad(String files) throws IOException {
		//Load a PCM
        File pcmFile = new File(files);
        System.out.println(pcmFile.getClass());
        
        PCMLoader loader = new KMFJSONLoader();
        
        PCM pcm = loader.load(pcmFile).get(0).getPcm();
        //Call the method that checks PCM's integrity
        pcmVerify(pcm);
        setPcm(pcm);
	}

	//@Override
	/** 
	 * @param : PCM whom Integrity needs to be checked
	 * @throws : throw an exception if the PCM's integrity is not checked
	 * This method check if products or features name are not null
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
        		 System.out.println("Un produit de la PCM est null. ");
        	 }
        	finally{
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
        		 System.out.println("Une caractéristique de la PCM est null. ");       	    	 
       	     }
        	finally{
        		//End of Programm
        		System.exit(0);
        	}
       	 }
        //TODO Verifier si la PCM a au moins deux caractéristiques numériques
        int nbFeature=0; 
        for (Feature feature : pcm.getConcreteFeatures()){
        	//if the feature has an integer type
        	if(feature.getName()==null){
        		nbFeature++;
        	}
        }
        //Test if there is at least 2 features which has an integer type
        if (nbFeature<2){
        	System.out.println("La PCM a moins de 2 caractéristiques numériques.");
        	//End of Programm
        	System.exit(0);
        }

        json = new Generation();
		json.generateJSON(pcm);
		
        
}

	@Override
	public BufferedReader lirefichier(String nom_fichier) {
		BufferedReader bread = null;
		try {
			bread = new BufferedReader(new FileReader(nom_fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bread;
	}

	public PCM getPcm() {
		return pcm;
	}

	public void setPcm(PCM pcm) {
		this.pcm = pcm;
	}

}
