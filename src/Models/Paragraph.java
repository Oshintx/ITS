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
import java.util.ArrayList;

public class Paragraph{

        private int number;
	private ArrayList<Sentence> sentences;

        /**
         * Paragraph has array list of sentences
         * @param number 
         */
        public Paragraph(int number){
		this.number = number;
		sentences = new ArrayList<Sentence>();
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
     * @return the sentences
     */
    public ArrayList<Sentence> getSentences() {
        return sentences;
    }

    /**
     * @param sentences the sentences to set
     */
    public void setSentences(ArrayList<Sentence> sentences) {
        this.sentences = sentences;
    }
}