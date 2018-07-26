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
	public int number;
	public ArrayList<Sentence> sentences;

	public Paragraph(int number){
		this.number = number;
		sentences = new ArrayList<Sentence>();
	}
}