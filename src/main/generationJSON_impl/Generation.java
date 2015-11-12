package main.generationJSON_impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import main.generationJSON.GenerationInter;

public class Generation implements GenerationInter {
	    JSONObject json = new JSONObject();
	    
	    JSONArray joo;
	   	List<HashMap<String, String>> l= new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map1 = new  HashMap<String,String>();
		
		
		
		
		@Override
		public void generateJSON(PCM pcm) {
			// TODO Auto-generated method stub
			
			 boolean filters=true;
		        String str_type_filter;
		        String [] str;
		        
		        
		        // We start by listing the names of the products
		        System.out.println("--- Products ---");
		    	HashMap<String, List> map = new  HashMap<String,List>();
		    	HashMap<String, String> filter = new  HashMap<String,String>();
		    	 JSONObject filterJSON 	=new  JSONObject();

		        for (Product product : pcm.getProducts()) {
		         //  System.out.println(product.getName());
		      //  System.out.println(product.getCells());
		           
		           map.put(product.getName(),  product.getCells());
		      
					// obj.put(product.getName(),map);
		        
		          
		   	    JSONObject jsonCell = new JSONObject();

		            for (Cell cell : product.getCells()) {
		            	jsonCell.put(cell.getFeature().getName(), cell.getContent());
		            	
		            	if(filters){
		            	str_type_filter= cell.getInterpretation()+"";
		            	str_type_filter=str_type_filter.split("@")[0];
		            	str_type_filter=str_type_filter.split("\\.")[6];
		            	
		            	str_type_filter=str_type_filter.substring(0, str_type_filter.length()-4);
		            	if(str_type_filter=="NotAvailable"){
		            		str_type_filter="StringValue";
		            		
		            	}
		            	Pattern p = Pattern.compile("\\d.*") ; 
		            	Matcher m = p.matcher(cell.getContent()) ;    
		            	 boolean b = m.matches() ;
		            	 
		            	if(b){
		            		
		            		str_type_filter="IntegerValue";
		            	}
		            	
		            	 filterJSON.put(cell.getFeature().getName(),str_type_filter);
		            	 
		            	}
		          
		            }
		            filters=false;
		           json.put(product.getName(), jsonCell);
			        json.put("FILTERS",  filterJSON);

		           
		        }
		     
		        
		        l.add(map1);
		        this.afficherJSON(json);
		        map.put("filters",  l);
		        
			
		}
		
		public void afficherJSON(JSONObject json){
			   System.out.println(json);
			for (Iterator iterator = json.keys(); iterator.hasNext();) {
	            Object cle = iterator.next();
	            Object val = json.get(String.valueOf(cle));
	            System.out.println("cle=" + cle + ", valeur=" + val);
	          }
		}

	
	

}
