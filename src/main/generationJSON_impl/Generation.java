package main.generationJSON_impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONException;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;
import org.json.JSONObject;
import main.generationJSON.GenerationInter;

public class Generation implements GenerationInter {

	JSONObject json = new JSONObject();

	JSONObject jsonDimension;
	String filepath;
	LireJSONParametres lire;
	

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
		int nbDimension = 0;
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
		
				// Afficher tous les dimensions IntegerValue
		for (Iterator iterator = dimensionsJSON.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = dimensionsJSON.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				System.out.println("cle=" + cle + ", valeur=" + val);
			}

		}
		System.out.println("cle=" + dimensionsJSON);
		jsonDimension = new JSONObject();
		int i = 1, j = 4;
		int limitDim;
		// System.out.println(" S.V.P fait les choix au plus " + j + "
		// dimensions -1 pour exit");

		// The name of the file to open.
		String fileName = "./testParameters/parametreDimension.txt";
		
		// This will reference one line at a time
		String dimension = null;

		//try {
			
		
			// FileReader reads text files in the default encoding.
//			FileReader fileReader = new FileReader(fileName);
//
//			// Always wrap FileReader in BufferedReader.
//			BufferedReader bufferedReader = new BufferedReader(fileReader);
//			System.out.println(bufferedReader.lines().toString());
			System.out.println("jsonDimension = " + dimensionsJSON.length());
			lire = new LireJSONParametres();
			
			org.json.simple.JSONObject jsonParametre = lire.lireJSONParametres();
				switch (jsonParametre.size()) {
			
			case 1:
				System.out.println("la taille de jsonDimension = " + dimensionsJSON.length()  +" impossible de faire choix les dimensions");

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
//			      Object objects = jsonObject.get(i);
		      //System.out.println( "je suis ds generation "+ jsonParametre.get(t+""));
			      //Iterate through the elements of the array i.
			      //Get thier value.
			      //Get the value for the first element and the value for the last element.
			
		      dimension = jsonParametre.get(t+"")+"";
		      
		      
				System.out.println(dimension);
				if (i <= limitDim) {
					dim = dimension;
					if (!dim.equals("-1")) {
						// vérification la dimension choisi n'est pas choidi
						if (jsonDimension.has(dim)) {
							System.out.println(" cette dimension est choisi");

						}

						else if (!dimensionsJSON.has(dim)) {
							// vérification la dimension choisi est existe dans
							// les
							// filteres

							System.out.println("ATTENTION : Cette dimension n'existe pas ");
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

			// Always close files.
			//bufferedReader.close();
//		} catch (FileNotFoundException ex) {
//			System.out.println("Unable to open file '" + fileName + "'");
//		} catch (IOException ex) {
//			System.out.println("Error reading file '" + fileName + "'");
//			// Or we could just do this:
//			// ex.printStackTrace();
//		}
		System.out.println("jsonDimension = " + jsonDimension);

		// Lire les choix des dimensions
		/*
		 * Scanner scanner = new Scanner(System.in); String dimension =
		 * scanner.nextLine(); dim = dimension;
		 * 
		 * while (i <= 4 ) {
		 * 
		 * dim = dimension; if (!dim.equals("-1")) { // vérification la
		 * dimension choisi n'est pas choidi if (jsonDimension.has(dim)) {
		 * System.out.println(" cette dimension est choisi");
		 * 
		 * }
		 * 
		 * else if (!dimensionsJSON.has(dim)) { // vérification la dimension
		 * choisi est existe dans les // filteres
		 * 
		 * System.out.println("ATTENTION : Cette dimension n'existe pas "); }
		 * 
		 * else { // ajout la dimension choisi jsonDimension.put(dimension, "" +
		 * i); j--; i++; } } else { System.out.println("jsonDimension = " +
		 * jsonDimension);
		 * 
		 * System.exit(0); } if (i <= 4) { System.out.println(
		 * " S.V.P fait les choix au plus " + j + "  dimensions -1 pour exit");
		 * scanner = new Scanner(System.in); dimension = scanner.nextLine(); dim
		 * = dimension; }
		 * 
		 * }
		 */
		// System.out.println("cle=" + cle + ", valeur=" + val);

		json.put("DIMENSIONS", jsonDimension);
	}

	public void afficherJSON(JSONObject json) {
		System.out.println("JSON Object: " + json);
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
	
	public boolean verifJSONgenere(String pathJSONgenere , PCM pcm){
		
		
		
		
		org.json.simple.JSONObject jsonGenere = lire.lireJSONgenere(pathJSONgenere);

		
		for (Product product : pcm.getProducts()) {
			
			
			
			
			
		}
		
		
		
		
		return false;
	}

	public boolean isJSONValid(JSONObject json) throws JSONException {
		try {
			
			
			
			
			
			
			
		} catch (JSONException ex) {
			
		
			
			return false;
		}

		return true;
	}

}
