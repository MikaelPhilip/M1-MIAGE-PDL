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
	
	@Test
	public void testCompareJsonPcm1() throws IOException{
		traitement.pcmLoad("pcms/example.pcm");
		//assertTrue(generation.verifJSONgenere("pcms/example.pcm", traitement.pc));
	}
	@Test
	public void testCompareJsonPcm2() throws IOException{
		traitement.pcmLoad("pcms/testGenereJSON.pcm");
		assertNotNull(traitement.getPcm());
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
