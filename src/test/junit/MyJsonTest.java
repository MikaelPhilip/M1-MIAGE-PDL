package test.junit;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Test;

public class MyJsonTest {

	
	@Test
	public void afficherContenu() {
		//Lire le fichier Test.json
		BufferedReader br = lirefichier("WebSite/Test.json");
		try {
			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
//				System.out.println(sCurrentLine);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	
	@Test
	public void pcmToJson(){
		BufferedReader br = lirefichier("pcms/example.pcm");
		String content = new String();
		try {

			String sCurrentLine;
			while ((sCurrentLine = br.readLine()) != null) {
				content = content + sCurrentLine + "\n";
			}

			File file = new File("WebSite/filename.json");

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void testContenu() {
		BufferedReader br = lirefichier("WebSite/Test.json");
		assertTrue("Le fichier n'est pas vide", br.toString().length() > 0);
		
		BufferedReader br2 = lirefichier("WebSite/filename.json");
		assertTrue("Le fichier n'est pas vide", br2.toString().length() > 0);
	}
	

	private BufferedReader lirefichier(String nom_fichier) {
		BufferedReader bread = null;
		try {
			bread = new BufferedReader(new FileReader(nom_fichier));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bread;
	}

}
