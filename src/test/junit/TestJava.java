package test.junit;

import java.io.IOException;
import java.util.Iterator;

import main.generationJSON_impl.Generation;
import main.traitement_Impl.Traitement;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJava {

	public Traitement traitement;
	public Generation generation;
	public String path;
	public JSONObject _jsonGenere ;
	public JSONObject _json2 ;
	/**
	 * @throws java.lang.Exception
	 * M�thode d'initialisation
	 */
	@Before
	public void setUp() throws Exception {
		//Cr�er un objet traitement
		traitement = new Traitement();
		generation = new Generation();
		path = generation.get_filepath();
		//_jsonGenere = generation.get_json();
		
	}
	
	/**
	 * M�thode de chargement pcm : cas ok
	 * @throws IOException 
	 */
	@Test
	public void testChargementPcm1() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
	}
	
	
	
	// test ok sur json genere
	@Test
	public void testCompareJsonPcm1() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		assertTrue("Remove the first Product" , generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
	}
	//test sur product not ok
	@Test
	public void testCompareJsonPcm2() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		Iterator<String> it =  _jsonGenere.keys();
		_jsonGenere.remove(it.next());
		assertFalse("Remove the first Product" , generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
		
	}
	// test sur les feature not ok ;
	@Test
	public void testCompareJsonPcm3() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
		_jsonGenere=generation.get_json();
		Iterator<String> it =  _jsonGenere.keys();
		
		JSONObject _json22 =_jsonGenere.getJSONObject(it.next());
		Iterator<String> it1 =  _json22.keys();
		String s=it1.next();
		_json2.put(s, "toto");
		//System.out.println(traitement.getPcm());
		assertFalse("Remove the first Product" , generation.verifJSONgenere(_json2,traitement.getPcm()));
	}
	
	// test sur les cell not ok ;
	@Test
	public void testCompareJsonPcm4() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
		//System.out.println(traitement.getPcm().getName());
//		assertFalse("test sur les cell not ok  ",generation.verifJSONgenere("./testJSON/testCompareJsonPcm3.json",traitement.getPcm(),null));
	}
	
	// cas json genere ok
	@Test
	public void testCompareJsonPcm5() throws IOException{
		traitement.pcmLoad("pcms/data.pcm");
		assertNotNull(traitement.getPcm());
		//System.out.println(generation.get_filepath());
//		assertTrue("test sur les cell not ok  ",generation.verifJSONgenere(null,traitement.getPcm(),generation.get_json()));
	}
	
	/**
	 * M�thode de chargement pcm : cas fichier inexistant
	 * @throws IOException 
	 */
	@Test (expected=IOException.class)
	public void testChargementPcm2() throws IOException{
		traitement.pcmLoad("pcms/fail.pcm");
	}
	
	/**
	 * @throws java.lang.Exception
	 * M�thode de fin de test
	 */
	@After
	public void tearDown() throws Exception {
	}
}
