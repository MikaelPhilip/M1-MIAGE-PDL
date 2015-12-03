package test.junit;

import java.io.IOException;
import java.util.Iterator;

import main.execution.Main;
import main.generationJSON_impl.Generation;
import main.traitement_Impl.Traitement;

import static org.junit.Assert.*;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for traitement.java
**/
public class TestJava {

	public Traitement traitement;
	public Generation generation;
	public JSONObject _jsonGenere ;
	
	/**
	 * @throws java.lang.Exception
	 * Initialisation
	 **/
	@Before
	public void setUp() throws Exception {
		//Créer un objet traitement
		traitement = new Traitement();
		generation = new Generation(traitement);
	
	}
	
	/**
	 * Method load pcm ok
	 * @throws IOException 
	 */
	@Test
	public void testChargementPcm1() throws IOException{
		traitement.pcmLoad(Main.PCMPATH);
		assertNotNull(traitement.getPcm());
	}
	
	/**
	 *  Method load pcm error: file don't exist
	 * @throws IOException 
	**/
	@Test (expected=IOException.class)
	public void testChargementPcm2() throws IOException{
		traitement.pcmLoad("pcms/fail.pcm");
	}
	
	/**
	 * Method check json error: product missing
	**/
	@Test
	public void testCompareJsonPcm1() throws IOException{
		//load pcm
		traitement.pcmLoad(Main.PCMPATH);
		assertNotNull(traitement.getPcm());
		//generate json
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		//we delete first product
		Iterator<String> it =  _jsonGenere.keys();
		_jsonGenere.remove(it.next());
		//VerifJSONgenere must send false
		assertFalse("Remove the first Product" , generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
	}
	
	
	/**
	 * Method check json error: feature for product missing
	**/
	@Test
	public void testCompareJsonPcm2() throws IOException{
		traitement.pcmLoad(Main.PCMPATH);
		assertNotNull(traitement.getPcm());
		//generate json
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		//we delete one feature of first product
		Iterator<String> it =  _jsonGenere.keys();
		JSONObject product=_jsonGenere.getJSONObject(it.next()); //get first product
		Iterator<String> it2 =  product.keys(); //iterator on features
		product.remove(it2.next());  //remove first feature of first product
		//VerifJSONgenere must send false
		assertFalse("Remove the first feature Product" , generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
	}
	
	/**
	 *	Method check json error: bad value feature for product missing
	 * @throws IOException
	 */
	@Test
	public void testCompareJsonPcm3() throws IOException{
		traitement.pcmLoad(Main.PCMPATH);
		assertNotNull(traitement.getPcm());
		//generate json
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		//we change value one feature of first product
		Iterator<String> it =  _jsonGenere.keys();
		JSONObject product=_jsonGenere.getJSONObject(it.next()); //get first product
		Iterator<String> it2 =  product.keys(); //iterator on features
		product.put(it2.next(),"toto"); //change value for first feature
		assertFalse("Bad value for first feature" , generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
	}
	
	/**
	 * Methode check json ok
	 * @throws IOException
	 */
	@Test
	public void testCompareJsonPcm4() throws IOException{
		traitement.pcmLoad(Main.PCMPATH);
		assertNotNull(traitement.getPcm());
		//generate json
		generation.generateJSON(traitement.getPcm());
		_jsonGenere=generation.get_json();
		//no modification must be ok
		assertTrue("Check Json ok ", generation.verifJSONgenere(_jsonGenere,traitement.getPcm()));
	}
	
	/**
	 * @throws java.lang.Exception
	 * Méthode de fin de test
	 */
	@After
	public void tearDown() throws Exception {
	}
}
