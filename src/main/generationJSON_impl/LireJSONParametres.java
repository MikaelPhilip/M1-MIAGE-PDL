
package main.generationJSON_impl;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class LireJSONParametres {
  

    	 
    	 public JSONObject lireJSONParametres(){
    		 
    		 JSONObject jsonObject = null;
	JSONParser parser = new JSONParser();

	try {

		Object obj = parser.parse(new FileReader("./testParameters/JSONParametres.json"));

		jsonObject = (JSONObject) obj;

		
		System.out.println("jsonParametres " +jsonObject);

		for(int i = 1; i <= jsonObject.size(); i++)
		{
//		      Object objects = jsonObject.get(i);
	      //System.out.println( jsonObject.get(i+""));
		      //Iterate through the elements of the array i.
		      //Get thier value.
		      //Get the value for the first element and the value for the last element.
		}
		
		// loop array
		JSONArray msg = (JSONArray) jsonObject.get("messages");
//		Iterator<String> iterator = jsonObject.iterator();
//		while (iterator.hasNext()) {
//			//String name = (String) jsonObject.get("1");
//			System.out.println(iterator.next());
//		}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}

	
	return jsonObject; 
	
     }
    	 
 public JSONObject lireJSONgenere(String path){
    		 
    		 JSONObject jsonObject = null;
	JSONParser parser = new JSONParser();

	try {

		Object obj = parser.parse(new FileReader(path));

		jsonObject = (JSONObject) obj;

		
		System.out.println("jsonParametres " +jsonObject);

		for(int i = 1; i <= jsonObject.size(); i++)
		{
//		      Object objects = jsonObject.get(i);
	      //System.out.println( jsonObject.get(i+""));
		      //Iterate through the elements of the array i.
		      //Get thier value.
		      //Get the value for the first element and the value for the last element.
		}
		
		// loop array
		JSONArray msg = (JSONArray) jsonObject.get("messages");
//		Iterator<String> iterator = jsonObject.iterator();
//		while (iterator.hasNext()) {
//			//String name = (String) jsonObject.get("1");
//			System.out.println(iterator.next());
//		}

	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} catch (ParseException e) {
		e.printStackTrace();
	}

	
	return jsonObject; 
	
     }
    
     
}
