package main.traitement_Impl;

public class TraitementPcmException extends Exception{
	//Parameterless Constructor
    public TraitementPcmException() {}

    //Constructor that accepts a message
    public TraitementPcmException(String message)
    {
       super(message);
    }
}
