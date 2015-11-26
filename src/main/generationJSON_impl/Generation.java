package main.generationJSON_impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
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
	private static final Logger _logger = Logger.getLogger(Generation.class);

	JSONObject _json = new JSONObject();

	JSONObject _jsonDimension;
	String _filepath;
	LireJSONParametres lire;

	@Override
	public void generateJSON(PCM pcmP_) {
		// TODO Auto-generated method stub
		// _logger.setLevel(Level.OFF);
		_logger.debug("msg de debogage");
		_logger.info("msg d'information");
		_logger.warn("msg d'avertissement");
		_logger.error("msg d'erreur");
		_logger.fatal("msg d'erreur fatale");
		boolean filters_ = true;
		String strTtypeFilter_;

		// We start by listing the names of the products

		_logger.info("--- Products ---");

		JSONObject objfilterJSON_ = new JSONObject();

		for (Product product : pcmP_.getProducts()) {
			// _logger.info(product.getName());
			// _logger.info(product.getCells());

			JSONObject objJsonCell_ = new JSONObject();

			for (Cell cell : product.getCells()) {
				objJsonCell_.put(cell.getFeature().getName(), cell.getContent());

				if (filters_) {
					// récuperer le type de Feature
					strTtypeFilter_ = cell.getInterpretation() + "";
					strTtypeFilter_ = strTtypeFilter_.split("@")[0];
					strTtypeFilter_ = strTtypeFilter_.split("\\.")[6];

					strTtypeFilter_ = strTtypeFilter_.substring(0, strTtypeFilter_.length() - 4);
					if (strTtypeFilter_.equals("NotAvailable")) {
						strTtypeFilter_ = "StringValue";
					}

					// tester le cell sous la forme entier suit une chaine de
					// caractere
					Pattern p = Pattern.compile("\\d.*");
					Matcher m = p.matcher(cell.getContent());
					boolean b = m.matches();

					if (b) {

						strTtypeFilter_ = "RealValue";

					}

					objfilterJSON_.put(cell.getFeature().getName(), strTtypeFilter_);
				}

			}

			filters_ = false;
			_json.put(product.getName(), objJsonCell_);

		}
		_json.put("filters_", objfilterJSON_);
		this.vérifDimension(_json.getJSONObject("filters_"));
		choixDimension(_json.getJSONObject("filters_"));

		this.afficherJSON(_json);

		if (verifJSONgenere("./testJSON/testCompareJsonPcm2.json", pcmP_)) {
			_logger.info("True");

		}

	}

	private void vérifDimension(JSONObject jsonfilters_) {
		// _logger.setLevel(Level.OFF);
		// TODO Auto-generated method stub
		int nbDimension = 0;
		for (Iterator iterator = jsonfilters_.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = jsonfilters_.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				nbDimension++;

				_logger.info("cle=" + cle + ", valeur=" + val);
			}

		}

	}

	public void choixDimension(JSONObject dimensionsJsonP_) {
		// _logger.setLevel(Level.OFF);
		String dim = "";

		// Afficher tous les dimensions IntegerValue
		for (Iterator iterator = dimensionsJsonP_.keys(); iterator.hasNext();) {
			Object cle = iterator.next();
			Object val = dimensionsJsonP_.get(String.valueOf(cle));

			if (val.equals("RealValue")) {

				_logger.info("cle=" + cle + ", valeur=" + val);
			}

		}
		_logger.info("cle=" + dimensionsJsonP_);

		_jsonDimension = new JSONObject();
		int i = 1, j = 4;
		int limitDim;
		// _logger.info(" S.V.P fait les choix au plus " + j + "
		// dimensions -1 pour exit");

		// The name of the file to open.
		String pathParametres = "./testParameters/parametreDimension.txt";

		// This will reference one line at a time
		String dimension = null;

		_logger.info("_jsonDimension = " + dimensionsJsonP_.length());
		lire = new LireJSONParametres();

		org.json.simple.JSONObject jsonParametre = lire.lireJSONParametres(pathParametres);
		switch (jsonParametre.size()) {

		case 1:
			_logger.info("la taille de _jsonDimension = " + dimensionsJsonP_.length()
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

		for (int t = 1; t <= jsonParametre.size(); t++) {

			dimension = jsonParametre.get(t + "") + "";

			_logger.info(dimension);
			if (i <= limitDim) {
				dim = dimension;
				if (!dim.equals("-1")) {
					// vérification la dimension choisi n'est pas choisi
					if (_jsonDimension.has(dim)) {
						_logger.info(" cette dimension est choisi");

					}

					else if (!dimensionsJsonP_.has(dim)) {
						// vérification la dimension choisi est existe dans
						// les
						// filteres

						_logger.info("ATTENTION : Cette dimension n'existe pas ");
					}

					else {
						// ajout la dimension choisi
						_jsonDimension.put(dimension, "" + i);
						j--;
						i++;
					}
				}
			}

		}

		_logger.info("_jsonDimension = " + _jsonDimension);

		_json.put("DIMENSIONS", _jsonDimension);
	}

	public void afficherJSON(JSONObject json) {

		_logger.info("JSON Object: " + json);
		if (isJSONValid(json)) {
			try {

				_filepath = "./json/generation.json";

				File objFile_ = new File(_filepath);
				FileWriter objFileWriter_ = new FileWriter(objFile_);
				BufferedWriter objBufferedWriter_ = new BufferedWriter(objFileWriter_);
				objBufferedWriter_.write(json.toString());
				objBufferedWriter_.close();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			_logger.info("error JSON Object: ");
		}

	}

	public boolean verifJSONgenere(String pathJSONgenere, PCM pcmP_) {

		_logger.info("je suis verif json ");

		org.json.simple.JSONObject jsonGenere = lire.lireJSONgenere(pathJSONgenere);

		for (Product product : pcmP_.getProducts()) {

			if (jsonGenere.get(product.getName()) != null) {
				for (Cell cell : product.getCells()) {

					org.json.simple.JSONObject jsonGene = (org.json.simple.JSONObject) jsonGenere
							.get(product.getName());
					if (jsonGene.get(cell.getFeature().getName()) != null) {
						if (cell.getContent().equals(jsonGene.get(cell.getFeature().getName()))) {

							return false;
						} else {

							return false;

						}

					} else {
						return false;
					}

				}

			} else {

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
