package main.generationJSON_impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.opencompare.api.java.value.IntegerValue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import main.generationJSON.GenerationInter;

public class Generation implements GenerationInter {

	JSONObject json = new JSONObject();

	JSONObject jsonDimension; 

	@Override
	public void generateJSON(PCM pcm) {
		// TODO Auto-generated method stub

		Map<String, String> map = new HashMap<String, String>();
		boolean filters = true;
		String str_type_filter;
		String[] str;

		// We start by listing the names of the products
		System.out.println("--- Products ---");

		JSONObject filterJSON = new JSONObject();

		for (Product product : pcm.getProducts()) {
			// System.out.println(product.getName());
			// System.out.println(product.getCells());

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

	}

	private void vérifDimension(JSONObject jsonFilters) {
		// TODO Auto-generated method stub
		int nbDimension =0;
		for (Iterator iterator = jsonFilters.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = jsonFilters.get(String.valueOf(cle));

			if (val.equals("RealValue")) {
				
				nbDimension++;

				System.out.println("cle=" + cle + ", valeur=" + val);
			}

		}
		
	}

	public void choixDimension(JSONObject dimensionsJSON) {

		String dim = "";
		System.out.println("cle=" + dimensionsJSON);
		// Afficher tous les dimensions IntegerValue
		for (Iterator iterator = dimensionsJSON.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = dimensionsJSON.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				System.out.println("cle=" + cle + ", valeur=" + val);
			}

		}
		jsonDimension = new JSONObject();
		int i = 1, j = 4;
//		System.out.println(" S.V.P fait les choix au plus " + j + "  dimensions -1 pour exit");
		
		// The name of the file to open.
        String fileName = "./testParameters/parametreDimension.txt";

        // This will reference one line at a time
        String dimension = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(fileName);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader =  new BufferedReader(fileReader);
            System.out.println( bufferedReader.lines().toString());
            while((dimension = bufferedReader.readLine()) != null) {
            	
                System.out.println(dimension);
                
                dim = dimension;
    			if (!dim.equals("-1")) {
    				// vérification la dimension choisi n'est pas choidi
    				if (jsonDimension.has(dim)) {
    					System.out.println(" cette dimension est choisi");

    				}

    				else if (!dimensionsJSON.has(dim)) {
    					// vérification la dimension choisi est existe dans les
    					// filteres

    					System.out.println("ATTENTION : Cette dimension n'existe pas ");
    				}

    				else {
    					// ajout la dimension choisi
    					jsonDimension.put(dimension, "" + i);
    					j--;
    					i++;
    				}
    			} else {
    				System.out.println("jsonDimension = " + jsonDimension);

    				//System.exit(0);
    			}
                
            }   

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");                
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
            // Or we could just do this: 
            // ex.printStackTrace();
        }
		
		
		
		
		// Lire les choix des dimensions
		/*Scanner scanner = new Scanner(System.in);
		String dimension = scanner.nextLine();
		dim = dimension;

		while (i <= 4 ) {
			
			dim = dimension;
			if (!dim.equals("-1")) {
				// vérification la dimension choisi n'est pas choidi
				if (jsonDimension.has(dim)) {
					System.out.println(" cette dimension est choisi");

				}

				else if (!dimensionsJSON.has(dim)) {
					// vérification la dimension choisi est existe dans les
					// filteres

					System.out.println("ATTENTION : Cette dimension n'existe pas ");
				}

				else {
					// ajout la dimension choisi
					jsonDimension.put(dimension, "" + i);
					j--;
					i++;
				}
			} else {
				System.out.println("jsonDimension = " + jsonDimension);

				System.exit(0);
			}
			if (i <= 4) {
				System.out.println(" S.V.P fait les choix au plus " + j + "  dimensions -1 pour exit");
				scanner = new Scanner(System.in);
				dimension = scanner.nextLine();
				dim = dimension;
			}

		}*/
		// System.out.println("cle=" + cle + ", valeur=" + val);
		
		json.put("DIMENSIONS", jsonDimension);
	}

	public void afficherJSON(JSONObject json) {
		System.out.println("JSON Object::: "+json);
		if(isJSONValid(json)){
		try {
			String filepath = "./json/generation.json";
		
			File file = new File(filepath);
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(json.toString());
			bw.close();
			
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
		else {
			System.out.println("error JSON Object: ");
		}
		for (Iterator iterator = json.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = json.get(String.valueOf(cle));
			JSONObject s = (JSONObject) json.get("FILTERS");
			// System.out.println("cle=" + cle + ", valeur=" + val);

		}

		// TODO Verifier si la PCM a au moins deux caractéristiques numériques
		/*
		 * int nbFeature=0; for (Feature feature : pcm.getConcreteFeatures()){
		 * //if the feature has an integer type if(feature){ nbFeature++;
		 * 
		 * } //System.out.println(feature.getName()+ nbFeature); } //Test if
		 * there is at least 2 features which has an integer type if
		 * (nbFeature<2){ System.out.println(
		 * "La PCM a moins de 2 caractéristiques numériques."); //End of
		 * Programm System.exit(0); }
		 */

	}
	public boolean isJSONValid(JSONObject json) throws JSONException {
	    try {
	        
	    	
	    	
	    } catch (JSONException ex) {
	    }
	    return true;
	}
	
	
	
}
