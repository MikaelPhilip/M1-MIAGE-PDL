package testLibrairies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONObject;
import org.opencompare.api.java.*;
import org.opencompare.api.java.impl.ValueImpl;
import org.opencompare.api.java.util.PCMVisitor;
import org.opencompare.api.java.value.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by gbecan on 02/02/15.
 */
public class MyPCMPrinter implements PCMVisitor{
	private boolean isBooleanCell;
    //JSONObject obj = new JSONObject();
    List l= new ArrayList();
    Map<String, String> map = new HashMap<String,String>();
	Map<String, String> map1 = new HashMap<String,String>();
	public Object propone;
	//Va servir pour convertir les hashmap (données et filtres) en json
	//We have stack overflow because of recursivity: we must add exclusion strategie for avoid this
	Gson gson = new GsonBuilder().create();

    /**
     * Print some information contained in a PCM
     * @param pcm: PCM to print
     */
    public void print(PCM pcm) {
        boolean filters=true;
        String str_type_filter;
        String [] str;  
        
        // We start by listing the names of the products
        System.out.println("--- Products ---");
        for (Product product : pcm.getProducts()) {
           // System.out.println(product.getName());
           // System.out.println(product.getCells());
           
           
           map.put(product.getName(), product.getCells().toString());
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
        
        //map.put("filters",  l);
        //On converti en format json les collections
        String donneeJson= gson.toJson(map); 
        String filtreJson= gson.toJson(map1); 
        //On rajoue les string en format json
        //obj.put("products",map);
        //obj.put("filters", map1);
        //Lecture json
        //TODO: à l'implémentation au lieu d'afficher enregistrer tout cela dans un fichier .json
        System.out.println("---JSON----");
        System.out.println(donneeJson);
        System.out.println(filtreJson);
        System.out.println("---End JSON----");
        // Then, we use a visitor to print the content of the cells that represent a boolean value
        System.out.println("--- Boolean values ---");
        pcm.accept(this);
        


    }


    // Methods for the visitor

    @Override
    public void visit(PCM pcm) {
        for (Product product : pcm.getProducts()) {
            product.accept(this);
          System.out.print( product.getName());
        }
    }

    @Override
    public void visit(Feature feature) {

    }

    @Override
    public void visit(FeatureGroup featureGroup) {

    }

    @Override
    public void visit(Product product) {
        for (Cell cell : product.getCells()) {
            cell.accept(this);
           //System.out.println(cell.getInterpretation()+" "+" "+cell.getFeature().getName());
            
            pcm.Value kValue = null ;
			ValueImpl.wrapValue(kValue);
            pcm.Value k;
            
            
            
           // System.out.println(cell.getInterpretation());
            
        }
    }

    @Override
    public void visit(Cell cell) {
        Value interpretation = cell.getInterpretation();

        // Visit the interpretation of the cell to check if it is a boolean
        isBooleanCell = false;
        if (interpretation != null) {
            interpretation.accept(this);
        }

        // Print content of the cell if it is a boolean
        if (isBooleanCell) {
          System.out.println(cell.getContent());
          
        }
    }

    @Override
    public void visit(BooleanValue booleanValue) {
        isBooleanCell = true;
    }

    @Override
    public void visit(Conditional conditional) {

    }

    @Override
    public void visit(DateValue dateValue) {

    }

    @Override
    public void visit(Dimension dimension) {

    }
    // pour recuperer integerValue
    @Override
    public void visit(IntegerValue integerValue) {
    //	System.out.println( "je suis integer "+integerValue.getValue());
    }

    
    
    @Override
    public void visit(Multiple multiple) {

    }

    @Override
    public void visit(NotApplicable notApplicable) {

    }

    @Override
    public void visit(NotAvailable notAvailable) {

    }

    @Override
    public void visit(Partial partial) {

    }

    @Override
    public void visit(RealValue realValue) {

    }
    //pour recuperer stringValue
    @Override
    public void visit(StringValue stringValue) {
    	//System.out.print( "je suis string "+stringValue.getValue());
    	

    }

    @Override
    public void visit(Unit unit) {

    }

    @Override
    public void visit(Version version) {

    }
}