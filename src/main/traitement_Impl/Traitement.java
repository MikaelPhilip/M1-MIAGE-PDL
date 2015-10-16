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

import main.traitement.TraitementInter;

public class Traitement implements TraitementInter{

	private PCM pcm;
	
	@Override
	public void pcmLoad(String files) throws IOException {
		//Load a PCM
        File pcmFile = new File(files);
        System.out.println(pcmFile.getClass());
        
        PCMLoader loader = new KMFJSONLoader();
        
        //Récupération de la PCM
        PCM pcm = loader.load(pcmFile).get(0).getPcm();
        //on teste l'intégrité de la PCM
        pcmVerify(pcm);
        
        setPcm(pcm);
        
	}

	//@Override
	/* 
	 * @param : PCM whom Integrity needs to be checked
	 * 
	 */
	public void pcmVerify(PCM pcm) {
        // We start by checking if the names of the products are not null
        for (Product product : pcm.getProducts()) {
        	if(product.getName()==null){
        		System.out.println("product = NULL");
        	}
        	else{
        		System.out.println("product OK : "+product.getName());
        	};
        }
        
     // Then we check the names of the features are not null
        for (Feature feature : pcm.getConcreteFeatures()){
        	if(feature.getName()==null){
        		System.out.println("feature = NULL");
        	}
        	else{
        		System.out.println("feature OK : " + feature.getName());
        	};
        }
		System.out.println("Verif PCM terminée");
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
