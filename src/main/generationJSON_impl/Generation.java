package main.generationJSON_impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import main.generationJSON.GenerationInter;

public class Generation implements GenerationInter {
    private static final Logger logger = Logger.getLogger(Generation.class);
    
    


    
	JSONObject json = new JSONObject(); 

	JSONObject jsonDimension;
	String filepath;
	LireJSONParametres lire;
	

	@Override
	public void generateJSON(PCM pcm) {
		// TODO Auto-generated method stub
	//	logger.setLevel(Level.OFF);
		Map<String, String> map = new HashMap<String, String>();
		boolean filters = true;
		String str_type_filter;
		String[] str;
	    

		
		
		    
		// We start by listing the names of the products
		
		  logger.info("--- Products ---");

		JSONObject filterJSON = new JSONObject();

		for (Product product : pcm.getProducts()) {
			// logger.info(product.getName());
			// logger.info(product.getCells());

			// obj.put(product.getName(),map);

			JSONObject jsonCell = new JSONObject();

			for (Cell cell : product.getCells()) {
				jsonCell.put(cell.getFeature().getName(), cell.getContent());
				
				if (filters) {
					// récuperer le type de Feature
					str_type_filter = cell.getInterpretation() + "";
					str_type_filter = str_type_filter.split("@")[0];
					str_type_filter = str_type_filter.split("\\.")[6];

					str_type_filter = str_type_filter.substring(0, str_type_filter.length() - 4);
					if (str_type_filter.equals("NotAvailable")) {
						str_type_filter = "StringValue";
					}

					// tester le cell sous la forme entier suit une chaine de
					// caractere
					Pattern p = Pattern.compile("\\d.*");
					Matcher m = p.matcher(cell.getContent());
					boolean b = m.matches();

					if (b) {

						str_type_filter = "RealValue";

					}

					filterJSON.put(cell.getFeature().getName(), str_type_filter);
				}

			}

			filters = false;
			json.put(product.getName(), jsonCell);

		}
		json.put("FILTERS", filterJSON);
		this.vérifDimension(json.getJSONObject("FILTERS"));
		choixDimension(json.getJSONObject("FILTERS"));

		this.afficherJSON(json);
		logger.info(verifJSONgenere("./testJSON/testCompareJsonPcm2.json",pcm));
		
				if(verifJSONgenere("./testJSON/testCompareJsonPcm2.json",pcm)){
						logger.info("True");

			
		}

	}

	private void vérifDimension(JSONObject jsonFilters) {
//		logger.setLevel(Level.OFF);
		// TODO Auto-generated method stub
		int nbDimension = 0;
		for (Iterator iterator = jsonFilters.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = jsonFilters.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				nbDimension++;

				logger.info("cle=" + cle + ", valeur=" + val);
			}

		}

	}

	public void choixDimension(JSONObject dimensionsJSON) {
//		logger.setLevel(Level.OFF);
		String dim = "";
		
				// Afficher tous les dimensions IntegerValue
		for (Iterator iterator = dimensionsJSON.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = dimensionsJSON.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				
				logger.info("cle=" + cle + ", valeur=" + val);
			}

		}
		logger.info("cle=" + dimensionsJSON);
		
		jsonDimension = new JSONObject();
		int i = 1, j = 4;
		int limitDim;
		// logger.info(" S.V.P fait les choix au plus " + j + "
		// dimensions -1 pour exit");

		// The name of the file to open.
		String pathParametres = "./testParameters/parametreDimension.txt";
		
		// This will reference one line at a time
		String dimension = null;

	
			
		
		
		logger.info("jsonDimension = " + dimensionsJSON.length());
			lire = new LireJSONParametres();
			
			org.json.simple.JSONObject jsonParametre = lire.lireJSONParametres(pathParametres);
				switch (jsonParametre.size()) {
			
			case 1:
				logger.info("la taille de jsonDimension = " + dimensionsJSON.length()  +" impossible de faire choix les dimensions");

			case 2:
				limitDim = 2;
				break;
			case 3:
				limitDim = 3;
				break;
			default:
				limitDim=4;

			}

			
			
			
			
			for(int t = 1; t <= jsonParametre.size(); t++)
			{
			      
			
		      dimension = jsonParametre.get(t+"")+"";
		      
		      
				logger.info(dimension);
				if (i <= limitDim) {
					dim = dimension;
					if (!dim.equals("-1")) {
						// vérification la dimension choisi n'est pas choisi
						if (jsonDimension.has(dim)) {
							logger.info(" cette dimension est choisi");

						}

						else if (!dimensionsJSON.has(dim)) {
							// vérification la dimension choisi est existe dans
							// les
							// filteres

							logger.info("ATTENTION : Cette dimension n'existe pas ");
						}

						else {
							// ajout la dimension choisi
							jsonDimension.put(dimension, "" + i);
							j--;
							i++;
						}
					}
				}

			}


		logger.info("jsonDimension = " + jsonDimension);

		
		
		json.put("DIMENSIONS", jsonDimension);
	}

	public void afficherJSON(JSONObject json) {
//		logger.setLevel(Level.OFF);
		
		logger.info("JSON Object: " + json);
		if (isJSONValid(json)) {
			try {
				
				
				filepath = "./json/generation.json";

				File file = new File(filepath);
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(json.toString());
				bw.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.info("error JSON Object: ");
		}
		
		
	
		for (Iterator iterator = json.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = json.get(String.valueOf(cle));
			JSONObject s = (JSONObject) json.get("FILTERS");
			// logger.info("cle=" + cle + ", valeur=" + val);

			
			
		}

		// TODO Verifier si la PCM a au moins deux caractéristiques numériques
		/*
		 * int nbFeature=0; for (Feature feature : pcm.getConcreteFeatures()){
		 * //if the feature has an integer type if(feature){ nbFeature++;
		 * 
		 * } //logger.info(feature.getName()+ nbFeature); } //Test if
		 * there is at least 2 features which has an integer type if
		 * (nbFeature<2){ logger.info(
		 * "La PCM a moins de 2 caractéristiques numériques."); //End of
		 * Programm System.exit(0); }
		 */

	}
	
	public boolean verifJSONgenere(String pathJSONgenere , PCM pcm){
//		logger.setLevel(Level.OFF);
		logger.info("je suis verif json ");
		
		
		org.json.simple.JSONObject jsonGenere = lire.lireJSONgenere(pathJSONgenere);

		
		for (Product product : pcm.getProducts()) {
			
			
			if(jsonGenere.get(product.getName())!=null){
				
				for (Cell cell : product.getCells()) {
						
					
					org.json.simple.JSONObject jsonGene = (org.json.simple.JSONObject) jsonGenere.get(product.getName());
					if(jsonGene.get(cell.getFeature().getName())!=null){
						
						if(cell.getContent().equals(jsonGene.get(cell.getFeature().getName()))){
							
							return false;							
					}else{
						
						return false;
						
					}
						
						
						
					}else{
						return false;
					}
					
			}
			
			
		}else{
			
			return false;

		}
		
		}
		
		
		return true;
	
	}
	public boolean isJSONValid(JSONObject json) throws JSONException {
		try {
			
			
			
			
			
			
			
		} catch (JSONException ex) {
			
		
			
			return false;
		}

		return true;
	}

}
