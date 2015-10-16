package main.traitement;

import java.io.BufferedReader;
import java.io.IOException;

public interface TraitementInter {
	/**
	 * Charger un fichier pcm dans l'objet pcm (permettra de récuperer les données (features et tout le reste)
	 * @param files: chemin du fichier
	 */
	public void pcmLoad(String files) throws IOException;
	
	/**
	 * Méthode de lecture d'un fichier
	 * @param nom_fichier
	 * @return
	 */
	public BufferedReader lirefichier(String nom_fichier);
}
