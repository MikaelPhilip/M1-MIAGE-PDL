package main.generationJSON_impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.Feature;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

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
			
			
		    Map<String, String> map = new HashMap<String,String>();
			 boolean filters=true;
		        String str_type_filter;
		        String [] str;
		        
		        
		        // We start by listing the names of the products
		        System.out.println("--- Products ---");
		    	
		    	 JSONObject filterJSON = new  JSONObject();

		        for (Product product : pcm.getProducts()) {
		         //  System.out.println(product.getName());
		      //  System.out.println(product.getCells());
		           
		         
		      
					// obj.put(product.getName(),map);
		        
		          
		        	JSONObject jsonCell = new JSONObject();

		            for (Cell cell : product.getCells()) {
		            	jsonCell.put(cell.getFeature().getName(), cell.getContent());
		            	
		            	if(filters){
		            	// récuperer le type de Feature	
		            	str_type_filter= cell.getInterpretation()+"";
		            	str_type_filter=str_type_filter.split("@")[0];
		            	str_type_filter=str_type_filter.split("\\.")[6];
		            	
		            	str_type_filter=str_type_filter.substring(0, str_type_filter.length()-4);
		            	if(str_type_filter.equals("NotAvailable")){
		            		str_type_filter="StringValue";
		            	}
		            	
		            	
		            	// tester le cell sous la forme entier suit une chaine de caractere 	
		            	Pattern p = Pattern.compile("\\d.*") ; 
		            	Matcher m = p.matcher(cell.getContent()) ;    
		            	boolean b = m.matches() ;
		            	           	 
		            	if(b){
		            		
		            		str_type_filter="IntegerValue";
		            	}
		            	if(str_type_filter.equals("IntegerValue")){
		            		
		            		
		            	}
		            	
		            	 filterJSON.put(cell.getFeature().getName(),str_type_filter);
		            	 	            	}
		          
		            }
		            
		            
			            filters=false;
			            json.put(product.getName(), jsonCell);
			           
		        }
		    	json.put("FILTERS",  filterJSON);
		       choixDimension(json.getJSONObject("FILTERS"));

		        this.afficherJSON(json);
		        
	
		}
		
		
		public void choixDimension(JSONObject filters){
			String dim=""; 
			System.out.println("cle=" + filters);

			for (Iterator iterator = filters.keys(); iterator.hasNext();) {
	            Object cle = iterator.next();
	            Object val = filters.get(String.valueOf(cle));
	            
	            if(val.equals("IntegerValue")){
	            	
	              	System.out.println("cle=" + cle + ", valeur=" + val);
	            }
	            
	          
	          }
			jsonDimension = new JSONObject();
			int i=0;
			Scanner scanner = new Scanner(System.in);
			String dimension = scanner.nextLine();
			dim=dimension;
			while (i<4){
			System.out.println(" S.V.P fait les choix au plus 4  dimensions ");
			
			dim=dimension;
			if(!dim.equals("-1")){
			jsonDimension.put(""+i, dimension );
			System.out.println("dimension "+ dimension);
			scanner = new Scanner(System.in);
			dimension = scanner.nextLine();
			}
			
	            //System.out.println("cle=" + cle + ", valeur=" + val);
	           
	       i++;
			}
			json.put("DIMENSIONS", jsonDimension); 	
		}
			
			
		
		
		
		public void afficherJSON(JSONObject json){
		    System.out.println(json);
			for (Iterator iterator = json.keys(); iterator.hasNext();) {
	            Object cle = iterator.next();
	            Object val = json.get(String.valueOf(cle));
	            JSONObject s = (JSONObject) json.get("FILTERS");
	            //System.out.println("cle=" + cle + ", valeur=" + val);
	          
	          }
			
	        //TODO Verifier si la PCM a au moins deux caractéristiques numériques
	     /*   int nbFeature=0; 
	        for (Feature feature : pcm.getConcreteFeatures()){
	        	//if the feature has an integer type
	        	if(feature){
	        		nbFeature++;
	        		
	        	}
	        	//System.out.println(feature.getName()+ nbFeature);
	        }
	        //Test if there is at least 2 features which has an integer type
	        if (nbFeature<2){
	        	System.out.println("La PCM a moins de 2 caractéristiques numériques.");
	        	//End of Programm
	        	System.exit(0);
	        }*/

		}
}
