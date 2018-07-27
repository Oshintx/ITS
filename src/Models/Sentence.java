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
        private int paragraphNumber;
	private int number;
	private int stringLength; //Dont need this 
	private double score;
	private int noOfWords;
	private String value;
/**
 * Sentence Class devide Entire text in to paragraph and then paragraphs devided in to number of sentences
 * @param number
 * @param value
 * @param stringLength
 * @param paragraphNumber 
 */
	public Sentence(int number, String value, int stringLength, int paragraphNumber){
		this.number = number;
		this.value = new String(value);
		this.stringLength = stringLength;
		noOfWords = value.split("\\s+").length;
		score = 0.0;
		this.paragraphNumber=paragraphNumber;
	}
          /**
     * @return the paragraphNumber
     */
    public int getParagraphNumber() {
        return paragraphNumber;
    }

    /**
     * @param paragraphNumber the paragraphNumber to set
     */
    public void setParagraphNumber(int paragraphNumber) {
        this.paragraphNumber = paragraphNumber;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * @return the stringLength
     */
    public int getStringLength() {
        return stringLength;
    }

    /**
     * @param stringLength the stringLength to set
     */
    public void setStringLength(int stringLength) {
        this.stringLength = stringLength;
    }

    /**
     * @return the score
     */
    public double getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * @return the noOfWords
     */
    public int getNoOfWords() {
        return noOfWords;
    }

    /**
     * @param noOfWords the noOfWords to set
     */
    public void setNoOfWords(int noOfWords) {
        this.noOfWords = noOfWords;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
} 