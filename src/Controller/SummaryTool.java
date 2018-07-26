/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

/**
 *
 * @author Oshin
 */
import Models.Sentence;
import Models.Paragraph;
import View.View;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collections;

public class SummaryTool {

    FileInputStream in;
    FileOutputStream out;
    public ArrayList<Sentence> sentences, contentSummary;
    public ArrayList<Paragraph> paragraphs;
    public int noOfSentences, noOfParagraphs;

    public double[][] intersectionMatrix;
    public LinkedHashMap<Sentence, Double> dictionary;

    public SummaryTool() {
        in = null;
        out = null;
        noOfSentences = 0;
        noOfParagraphs = 0;
    }

    public void init() {
        sentences = new ArrayList<Sentence>();
        paragraphs = new ArrayList<Paragraph>();
        contentSummary = new ArrayList<Sentence>();//this array list contain sentences 
        dictionary = new LinkedHashMap<Sentence, Double>();
        noOfSentences = 0;
        noOfParagraphs = 0;
        try {
//          String originalContext;
//          originalContext=v.txtAreaInputDocument.getText();
            in = new FileInputStream("samples/amazon/nexus_6p");
            out = new FileOutputStream("output.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Gets the sentences from the entire passage*/
    public void extractSentenceFromContext() { //this extract sentences from the document and creates a sentence object and it will store in a sentence type array list name call sentences
        int nextChar, j = 0;
        int prevChar = -1;
        try {
            while ((nextChar = in.read()) != -1) {                          // The java.io.InputStream.read() method reads the next byte of the data from the the input stream and returns int in the range of 0 to 255.
                //   If no byte is available because the end of the stream has been reached, the returned value is -1.
                j = 0;
                char[] temp = new char[100000];
                while ((char) nextChar != '.') {
                    //System.out.println(nextChar + " ");
                    temp[j] = (char) nextChar;

                    if ((nextChar = in.read()) == -1) {//here for nextChar assign the next char charcter value(ascy-bytes between 0-255) and need to check whether in.read() return -1(which means end of the file) and if it is need to break.other wise if the last sentence is not contain full stop this loop will go continuesly and will prompt to an error.
                        break;
                    }
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') {//here checks whether nextchar and prev char both are two lines(empty lines)-if the text body is seperate with two lines(empty lines(two \n )) consider as 1 paragraph
                        noOfParagraphs++;//here paragraphNo of considering =noOfParagraphs
                    }

                    j++;
                    prevChar = nextChar;
                }

                sentences.add(new Sentence(noOfSentences, (new String(temp)).trim(), (new String(temp)).trim().length(), noOfParagraphs));//here noOfSentences=sentence No

                // System.out.println(new Sentence(noOfSentences,(new String(temp)).trim(),(new String(temp)).trim().length(),noOfParagraphs));
                noOfSentences++;//here this is mention noOfSentences
                prevChar = nextChar;

            }
            //     System.out.println(noOfParagraphs);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void groupSentencesIntoParagraphs() {//from this methode take 
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        for (int i = 0; i < noOfSentences; i++) {
            if (sentences.get(i).paragraphNumber == paraNum) {
                //continue
            } else {
                paragraphs.add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }
            paragraph.sentences.add(sentences.get(i));
            //System.out.println("paragraph no"+paragraph.number);

        }

        paragraphs.add(paragraph);
    }

    public double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.value.split("\\s+")) {//"\\s+" is a regex this will split the string in to string of array with seperator as a one space or multiple spaces.  \ is a regex for one or more spaces 

            //  System.out.println(str1Word);
            for (String str2Word : str2.value.split("\\s+")) {

                //   System.out.println(str2Word);
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {//if str2Word=-int str2Word is grater than to  str1Word and if str2Word=0 str2Wordis equal to str1Word and if str2Word= +int str2Word is less than the str1Word
                    commonCount++;
                    //   System.out.println(commonCount);
                }
            }
        }

        return commonCount;
    }

    public void createIntersectionMatrix() {
        intersectionMatrix = new double[noOfSentences][noOfSentences];//arr[i][j]
        for (int i = 0; i < noOfSentences; i++) {
            for (int j = 0; j < noOfSentences; j++) {

                if (i <= j) {
                    Sentence str1 = sentences.get(i);
                    Sentence str2 = sentences.get(j);
                    intersectionMatrix[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.noOfWords + str2.noOfWords) / 2);
                    //   System.out.println(intersectionMatrix[i][j]);   
                } else {
                    intersectionMatrix[i][j] = intersectionMatrix[j][i];
                    // System.out.println(intersectionMatrix[i][j]);   
                }

            }
        }

    }

    public void createDictionary() {
        for (int i = 0; i < noOfSentences; i++) {
            double score = 0;
            for (int j = 0; j < noOfSentences; j++) {
                score += intersectionMatrix[i][j];//score=intersectionMatrix[i][j]+score;

            }
            //System.out.println(score);

            dictionary.put(sentences.get(i), score);
            sentences.get(i).score = score;
            // System.out.println(sentences.get(i).score);
        }
    }

    public void createSummary() {

        for (int j = 0; j <= noOfParagraphs; j++) {
            int primary_set = paragraphs.get(j).sentences.size() / 5;//find no of group of 5 sentences.ex:if has 17 sentences in a paragraph we select  4 group of sentences for summery
            //  System.out.println(primary_set);
            //Sort based on score (importance)
            Collections.sort(paragraphs.get(j).sentences, new SentenceComparator());//sort here according to the score of a sentence(SentenceComparator has a compare to do this)
            for (int i = 0; i <= primary_set; i++) {//from a one group select 2 sentences
                contentSummary.add(paragraphs.get(j).sentences.get(i));
            }
        }

        //To ensure proper ordering (order should be qual to the original context order)
        Collections.sort(contentSummary, new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to do this)

    }

    public void printSentences() {
        for (Sentence sentence : sentences) {
            System.out.println(sentence.number + " => " + sentence.value + " => " + sentence.stringLength + " => " + sentence.noOfWords + " => " + sentence.paragraphNumber);
        }
    }

    public void printIntersectionMatrix() {
        for (int i = 0; i < noOfSentences; i++) {
            for (int j = 0; j < noOfSentences; j++) {
                System.out.print(intersectionMatrix[i][j] + "    ");
            }
            System.out.print("\n");
        }
    }

//	void printDicationary(){
//		  // Get a set of the entries
//	      Set set = dictionary.entrySet();
//	      // Get an iterator
//	      Iterator i = set.iterator();
//	      // Display elements
//	      while(i.hasNext()) {
//	         Map.Entry me = (Map.Entry)i.next();
//	         System.out.print(((Sentence)me.getKey()).value + ": ");
//	         System.out.println(me.getValue());
//	      }
//	}
    public void printSummary() {
        
        System.out.println("no of paragraphs = " + noOfParagraphs);
        for (Sentence sentence : contentSummary) {
            System.out.println(sentence.value);
        }
    }

    public double getWordCount(ArrayList<Sentence> sentenceList) {//here find the total no of words in the summery
        double wordCount = 0.0;
        for (Sentence sentence : sentenceList) {
            wordCount += sentence.noOfWords;
        }
        return wordCount;
    }

    public void printStats() {
        System.out.println("number of words in Context : " + getWordCount(sentences));
        System.out.println("number of words in Summary : " + getWordCount(contentSummary));
        System.out.println("Commpression : " + getWordCount(contentSummary) / getWordCount(sentences));
    }

}
