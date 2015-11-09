package main.generationJSON_impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.opencompare.api.java.Cell;
import org.opencompare.api.java.PCM;
import org.opencompare.api.java.Product;

import main.generationJSON.GenerationInter;

public class Generation implements GenerationInter {

	   	List l= new ArrayList();
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


		        for (Product product : pcm.getProducts()) {
		           System.out.println(product.getName());
		      //  System.out.println(product.getCells());
		           
		           map.put("\n"+product.getName(),  product.getCells());
		      
					// obj.put(product.getName(),map);
		            
		           if(filters){
		            for (Cell cell : product.getCells()) {
		            
		            	
		            	// l.add(cell.getFeature().getName());
		            	// l.add(cell.getInterpretation()+"");
		            	str_type_filter= cell.getInterpretation()+"";
		            	str_type_filter=str_type_filter.split("@")[0];
		            	str_type_filter=str_type_filter.split("\\.")[6];
		            	
		            	str_type_filter=str_type_filter.substring(0, str_type_filter.length()-4);
		            	if(str_type_filter=="NotAvailable"){
		            		str_type_filter="StringValue";
		            		
		            	}
		            			
		            	
		            	 map1.put(cell.getFeature().getName(),str_type_filter);
		            	 filters=false;
		            }
		            }
		            
		        }
		        l.add(map1);
		        map.put("filters",  l);

		      
		       
		        System.out.println(map);
			
			
		}

	
	

}
