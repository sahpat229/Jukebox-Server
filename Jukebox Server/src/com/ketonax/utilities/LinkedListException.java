package com.ketonax.utilities;
public class LinkedListException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public LinkedListException() {
		
		super("Fatal Error!");
	}
	
	public LinkedListException(String message){
		
		super(message);
	}

}
