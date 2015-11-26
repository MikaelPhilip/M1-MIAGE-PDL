package test.junit;

import java.io.IOException;

import main.generationJSON_impl.Generation;
import main.traitement_Impl.Traitement;

import org.junit.*;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestJava {

	public Traitement traitement;
	public Generation generation;
	/**
	 * @throws java.lang.Exception
	 * Méthode d'initialisation
	 */
	@Before
	public void setUp() throws Exception {
		//Créer un objet traitement
		traitement = new Traitement();
		generation = new Generation();
		
	}
	
	/**
	 * Méthode de chargement pcm : cas ok
	 * @throws IOException 
	 */
	@Test
	public void testChargementPcm1() throws IOException{
		traitement.pcmLoad("pcms/example.pcm");
		assertNotNull(traitement.getPcm());
	}
	
	//test sur product not ok
	@Test
	public void testCompareJsonPcm1() throws IOException{
		traitement.pcmLoad("pcms/example.pcm");
		assertNotNull(traitement.getPcm());
		System.out.println(traitement.getPcm());
		generation.generateJSON(traitement.getPcm());
		assertFalse("test sur product not ok",generation.verifJSONgenere("./testJSON/testCompareJsonPcm1.json",traitement.getPcm()));
		//assertTrue(generation.verifJSONgenere("pcms/example.pcm", traitement.pc));
	}
	// test sur les feature not ok ;
	@Test
	public void testCompareJsonPcm2() throws IOException{
		traitement.pcmLoad("pcms/example.pcm");
		assertNotNull(traitement.getPcm());
		System.out.println(traitement.getPcm());
		generation.generateJSON(traitement.getPcm());
		assertFalse("test sur les feature not ok ",generation.verifJSONgenere("./testJSON/testCompareJsonPcm2.json",traitement.getPcm()));
	}
	
	// test sur les cell not ok ;
	@Test
	public void testCompareJsonPcm3() throws IOException{
		traitement.pcmLoad("pcms/example.pcm");
		assertNotNull(traitement.getPcm());
		System.out.println(traitement.getPcm());
		generation.generateJSON(traitement.getPcm());
		assertFalse("test sur les cell not ok  ",generation.verifJSONgenere("./testJSON/testCompareJsonPcm3.json",traitement.getPcm()));
	}
	
	
	/**
	 * Méthode de chargement pcm : cas fichier inexistant
	 * @throws IOException 
	 */
	@Test (expected=IOException.class)
	public void testChargementPcm2() throws IOException{
		traitement.pcmLoad("pcms/fail.pcm");
	}
	
	/**
	 * @throws java.lang.Exception
	 * Méthode de fin de test
	 */
	@After
	public void tearDown() throws Exception {
	}
}
