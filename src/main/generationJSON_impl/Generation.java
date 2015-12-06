package main.generationJSON_impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import main.execution.Main;
import main.generationJSON.GenerationInter;
import main.traitement_Impl.Traitement;

/**
 * Class for create and check JSON with pcm Data
 */
public class Generation implements GenerationInter {
	/**Logger for feedback**/
	private static final Logger _logger = Logger.getLogger(Generation.class);

	/**Json generate**/
	private JSONObject _json ;
	
	/**Class traitement**/
	private Traitement t;
	

	/**
	 * Constructor
	 */
	public Generation(Traitement t) {
		this._json = new JSONObject();
		this.t = t;
	}

	/**
	 * Method to complete json with data in PCM
	 */
	@Override
	public void generateJSON(PCM pcmP_) {
		
		/*For each product we add an object on JSON, during the first iteration we create a Filters Object and Dimensions Object too*/
		// _logger.setLevel(Level.OFF); //disable log
		
		//True= we must create list of filters in JSON, false: list of filters was already create
		boolean filters_ = true;
		//Type of filter
		String strTtypeFilter_;
		//Object to contains filters
		JSONObject objfilterJSON_ = new JSONObject();
		
		//For each product
		for (Product product : pcmP_.getProducts()) {
			//Create an JSONObject to get content of cells for each product
			JSONObject objJsonCell_ = new JSONObject();
			//if product exist or is not empty
			if(product.getName() != null && !product.getName().equals("")){
				//for each cells
				for (Cell cell : product.getCells()) {
					//Adding cells value
					objJsonCell_.put(cell.getFeature().getName(), cell.getContent());
					
					/*Filters*/
					//If we don't have already define filter, we register features in objfilterJSON_
					if (filters_) {
						//Determine type of filter
						strTtypeFilter_ = cell.getInterpretation() + ""; //get type
						//Delete useless data
						strTtypeFilter_ = strTtypeFilter_.split("@")[0];
						strTtypeFilter_ = strTtypeFilter_.split("\\.")[6];
						strTtypeFilter_ = strTtypeFilter_.substring(0, strTtypeFilter_.length() - 4);
						
						//If type not know we put string type
						if (strTtypeFilter_.equals("NotAvailable")) {
							strTtypeFilter_ = "StringValue";
						}
	
						//Check with regExp if some string value can be number
						Pattern p = Pattern.compile("\\d.*"); //if string start wtih numbers, we considers feature type like number (ex: 52 lb)
						Matcher m = p.matcher(cell.getContent());
						//if regexp valid
						if (m.matches()) {
							strTtypeFilter_ = "RealValue"; //we change type of filter
						}
						//register filter in objfilterJSON_ (name,type)
						objfilterJSON_.put(cell.getFeature().getName(), strTtypeFilter_);
					}
	
				}
				//We indicate we generate filter
				filters_ = false;
				
				//We add new object/product in JSON (nameproduct, objectJSON with all features value)
				_json.put(product.getName(), objJsonCell_);		
			}

		}
		//Add objfilterJSON_ in JSON
		_json.put("FILTERS", objfilterJSON_);
		//Call method to select dimension
		choixDimension(_json.getJSONObject("FILTERS"));
		//Call method to register json in real file
		this.afficherJSON(_json);
	}


	/**
	 * Select dimension and save it in JSON
	 * @param dimensionsJsonP_
	 */
	@Override
	public void choixDimension(JSONObject dimensionsJsonP_) {
		// _logger.setLevel(Level.OFF); //disabled log
		
		//Json dimension
		JSONObject jsonDimension_ = new JSONObject();
		//Indice for dimension
		int i = 1;
		//Number limit of dimension
		int limitDim;
		// The name of the file to open.
		String pathParametres = Main.PARAMPATH;

		// This will reference one line at a time
		String dimension = null;

		_logger.info("Liste des filtres.Filters=" + dimensionsJsonP_);
		_logger.info("Nombre dimension: " + dimensionsJsonP_.length());

		//Call method for get list of selected features
		org.json.simple.JSONObject jsonParametre = lireJSONParametres(pathParametres);
		
		switch (jsonParametre.size()) {
			case 1:
				_logger.info("la taille de _jsonDimension est trop basse= " + dimensionsJsonP_.length()
						+ " impossible de faire choix les dimensions");
			case 2:
				limitDim = 2;
				break;
			case 3:
				limitDim = 3;
				break;
			default:
				limitDim = 4;
		}

		//for each features selected
		for (int t = 1; t <= jsonParametre.size(); t++) {

			//get name of feature
			dimension = jsonParametre.get(t + "") + "";

			_logger.info("Nom dimension: "+dimension);
			if (i <= limitDim) {
				String dim = dimension;
				if (!dim.equals("-1")) {
					//Check if not already chose				
					if (jsonDimension_.has(dim)) {
						_logger.warn(" cette dimension est déja choisi");
					}
					//Check if feature exist in JSON
					else if (!dimensionsJsonP_.has(dim)) {
						_logger.warn("ATTENTION : Cette dimension n'existe pas ");
					}else {
						//Add dimension (value,num dimension)
						jsonDimension_.put(dimension, "" + i);
						i++;
					}
				}
			}

		}
		//show result
		_logger.info("_jsonDimension = " + jsonDimension_);
		//Add Dimension object in JSON
		_json.put("DIMENSIONS", jsonDimension_);
	}

	/**
	 * Method to register JSON in file
	 * @param json
	 */
	@Override
	public void afficherJSON(JSONObject json) {
		//Check if json valid
		if(verifJSONgenere(json,t.getPcm())){
		//Show json data
		//_logger.info("JSON Object: " + json);
			try {
				//Create new file
				File objFile_ = new File(Main.JSONPATH);
				FileWriter objFileWriter_ = new FileWriter(objFile_);
				BufferedWriter objBufferedWriter_ = new BufferedWriter(objFileWriter_);
				//Register data in file
				objBufferedWriter_.write(json.toString());
				objBufferedWriter_.close();
				_logger.info("Json generé");
			} catch (IOException e) {
				_logger.error("error IOExceptionObject");
				e.printStackTrace();
			} catch (JSONException e){
				_logger.error("error JSONException");
				e.printStackTrace();
			}
		}else{
			_logger.error("incohérence json/pcm");
		}
	}

	/**
	 * Check if json is correct
	 * @param jsonP_ JSON generate
	 * @param pcmP_ Original Pcm
	 * @return
	 */
	@Override
	public boolean verifJSONgenere(JSONObject jsonP_, PCM pcmP_ ) {
		//for each product in pcm
		for (Product product : pcmP_.getProducts()) {
			//check if we have product register in JSON
			if (jsonP_.has(product.getName())) {
				//for each cells of product
				for (Cell cell : product.getCells()) {
					JSONObject jsonGene = (JSONObject) jsonP_.get(product.getName());
					//check if we have feature
					if (jsonGene.has(cell.getFeature().getName())) {
						//check if value is equals
						if (!(cell.getContent().equals(jsonGene.get(cell.getFeature().getName())))) {
							_logger.warn("ATTENTION :"+product.getName()+": valeur "+ cell.getFeature().getName()+" non cohérente: " +cell.getContent()+ "=/=" + jsonGene.get(cell.getFeature().getName()));
							return false;
						}
					} else {
						_logger.warn("ATTENTION :"+product.getName()+": feature non présente");
						return false;
					}
				}
			} else {
				_logger.warn("ATTENTION : Objet/produit non présent");
				return false;}
		}
		return true;
	}
	
	/**
	 * Read JSON parameters to get dimensions selected
	 * @param pathParametres
	 * @return JSONObject: list of dimensions
	 */
	@Override
	public org.json.simple.JSONObject lireJSONParametres(String pathParametres){
		//JSON Object to get data of file parameters
		org.json.simple.JSONObject jsonObject = null;
		//For read file
		JSONParser parser = new JSONParser();
		try {
			 //Get all data on file and save in jsonObject
			 Object obj = parser.parse(new FileReader(pathParametres));
			 jsonObject = (org.json.simple.JSONObject) obj;
			 _logger.info("jsonParametres " +jsonObject);
		 } catch (FileNotFoundException e) {
			_logger.error("error FileNotFoundException (Fichier inexistant)");
			 //End of Programm
          	 System.exit(0);
		 } catch (IOException e) {
			 _logger.error("error IOException (Erreur lecture)");
			 //End of Programm
          	 System.exit(0);
		 } catch (ParseException e) {
			 _logger.error("error ParseException (JSON non valide)");
			 //End of Programm
          	 System.exit(0);
		 }
		 return jsonObject; 
	}
	
	/**
	 * @return generate json
	 */
	public JSONObject get_json() {
		return _json;
	}

}
