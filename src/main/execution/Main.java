package main.execution;

import java.io.IOException;

import main.traitement_Impl.Traitement;

import testLibrairies.MyPCMPrinterTest;

public class Main {
	public static void main(String[] args) throws IOException{
		//Créer un objet traitement : va charger le fichier pcm et le vérifier
		Traitement traitement= new Traitement();
		traitement.pcmLoad("pcms/example.pcm");
	}
}
