package main.generationJSON;

import org.json.JSONObject;
import org.opencompare.api.java.PCM;

public interface GenerationInter {

	public void generateJSON(PCM pcm);
	
	public void afficherJSON(JSONObject json);
	
	public void choixDimension(JSONObject json);
	
	public org.json.simple.JSONObject lireJSONParametres(String string);
	
	public boolean verifJSONgenere(JSONObject obj, PCM pcm);
}
