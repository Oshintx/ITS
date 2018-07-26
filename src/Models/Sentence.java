/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

/**
 *
 * @author Oshin
 */
public class Sentence{
	public int paragraphNumber;
	public int number;
	public int stringLength; //Dont need this 
	public double score;
	public int noOfWords;
	public String value;

	public Sentence(int number, String value, int stringLength, int paragraphNumber){
		this.number = number;
		this.value = new String(value);
		this.stringLength = stringLength;
		noOfWords = value.split("\\s+").length;
		score = 0.0;
		this.paragraphNumber=paragraphNumber;
	}
}