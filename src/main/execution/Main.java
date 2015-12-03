package main.execution;

import java.io.IOException;

import main.traitement.TraitementInter;
import main.traitement_Impl.Traitement;
/**
 * Main launch generation and contains parameters
 * @author 12000209
 */
public class Main {
	
	//Global Paths parameters
	public static String PCMPATH="pcms/data.pcm"; //where is the pcm to load
	public static String PARAMPATH="./testParameters/JSONParametres.json"; //where is the json parameters 
	public static String JSONPATH="./json/generation.json"; //where generate json will created
	
	public static void main(String[] args) throws IOException{	
		//Create traitement
		TraitementInter traitement= new Traitement();
		//Launch traitement
		traitement.pcmLoad(PCMPATH);
		
		
	}
}
