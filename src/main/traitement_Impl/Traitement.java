package main.traitement_Impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.opencompare.api.java.PCM;
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
        setPcm(loader.load(pcmFile).get(0).getPcm());
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
