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

public class Algorithm {

   


    private FileInputStream in;
    private FileOutputStream out;
    private ArrayList<Sentence> sentences;
    private ArrayList<Sentence> contentSummary;
    private String finalSummery;
    private ArrayList<Paragraph> paragraphs;
    private int noOfSentences;
    private int  noOfParagraphs;
    private double[][] intersectionMatrix;
    private LinkedHashMap<Sentence, Double> dictionary;
    
    public Algorithm() {
        in = null;
        out = null;
        noOfSentences = 0;
        noOfParagraphs = 0;
    }

    public void init() {
        setSentences(new ArrayList<Sentence>());
        setParagraphs(new ArrayList<Paragraph>());
        setContentSummary(new ArrayList<Sentence>());//this array list contain sentences 
        setDictionary(new LinkedHashMap<Sentence, Double>());
        setNoOfSentences(0);
        setNoOfParagraphs(0);
        try {
       
            setIn(new FileInputStream("samples/amazon/nexus_6p"));
            setOut(new FileOutputStream("output.txt"));
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
            while ((nextChar = getIn().read()) != -1) {                          // The java.io.InputStream.read() method reads the next byte of the data from the the input stream and returns int in the range of 0 to 255.
                //   If no byte is available because the end of the stream has been reached, the returned value is -1.
                j = 0;
                char[] temp = new char[100000];
                while ((char) nextChar != '.') {
                    //System.out.println(nextChar + " ");
                    temp[j] = (char) nextChar;

                    if ((nextChar = getIn().read()) == -1) {//here for nextChar assign the next char charcter value(ascy-bytes between 0-255) and need to check whether in.read() return -1(which means end of the file) and if it is need to break.other wise if the last sentence is not contain full stop this loop will go continuesly and will prompt to an error.
                        break;
                    }
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') {//here checks whether nextchar and prev char both are two lines(empty lines)-if the text body is seperate with two lines(empty lines(two \n )) consider as 1 paragraph
                        setNoOfParagraphs(getNoOfParagraphs() + 1);//here paragraphNo of considering =noOfParagraphs
                    }

                    j++;
                    prevChar = nextChar;
                }

                getSentences().add(new Sentence(getNoOfSentences(), (new String(temp)).trim(), (new String(temp)).trim().length(), getNoOfParagraphs()));//here noOfSentences=sentence No

                // System.out.println(new Sentence(noOfSentences,(new String(temp)).trim(),(new String(temp)).trim().length(),noOfParagraphs));
                setNoOfSentences(getNoOfSentences() + 1);//here this is mention noOfSentences
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

        for (int i = 0; i < getNoOfSentences(); i++) {
            if (getSentences().get(i).getParagraphNumber() == paraNum) {
                //continue
            } else {
                getParagraphs().add(paragraph);
                paraNum++;
                paragraph = new Paragraph(paraNum);

            }
            paragraph.sentences.add(getSentences().get(i));
            //System.out.println("paragraph no"+paragraph.number);

        }

        getParagraphs().add(paragraph);
    }

    public double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.getValue().split("\\s+")) {//"\\s+" is a regex this will split the string in to string of array with seperator as a one space or multiple spaces.  \ is a regex for one or more spaces 

            //  System.out.println(str1Word);
            for (String str2Word : str2.getValue().split("\\s+")) {

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
        setIntersectionMatrix(new double[getNoOfSentences()][getNoOfSentences()]);//arr[i][j]
        for (int i = 0; i < getNoOfSentences(); i++) {
            for (int j = 0; j < getNoOfSentences(); j++) {

                if (i <= j) {
                    Sentence str1 = getSentences().get(i);
                    Sentence str2 = getSentences().get(j);
                    getIntersectionMatrix()[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.getNoOfWords() + str2.getNoOfWords()) / 2);
                    //   System.out.println(intersectionMatrix[i][j]);   
                } else {
                    getIntersectionMatrix()[i][j] = getIntersectionMatrix()[j][i];
                    // System.out.println(intersectionMatrix[i][j]);   
                }

            }
        }

    }

    public void createDictionary() {
        for (int i = 0; i < getNoOfSentences(); i++) {
            double score = 0;
            for (int j = 0; j < getNoOfSentences(); j++) {
                score += getIntersectionMatrix()[i][j];//score=intersectionMatrix[i][j]+score;

            }
            //System.out.println(score);

            getDictionary().put(getSentences().get(i), score);
            sentences.get(i).setScore(score);
            // System.out.println(sentences.get(i).score);
        }
    }

    public void createSummary() {

        for (int j = 0; j <= getNoOfParagraphs(); j++) {
            int primary_set = getParagraphs().get(j).sentences.size() / 5;//find no of group of 5 sentences.ex:if has 17 sentences in a paragraph we select  4 group of sentences for summery
            //  System.out.println(primary_set);
            //Sort based on score (importance)
            Collections.sort(getParagraphs().get(j).sentences, new SentenceComparator());//sort here according to the score of a sentence(SentenceComparator has a compare to do this)
            for (int i = 0; i <= primary_set; i++) {//from a one group select 2 sentences
                getContentSummary().add(getParagraphs().get(j).sentences.get(i));
            }
        }

        //To ensure proper ordering (order should be qual to the original context order)
        Collections.sort(getContentSummary(), new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to do this)

    }

    public void printSentences() {
        for (Sentence sentence : getSentences()) {
            System.out.println(sentence.getNumber() + " => " + sentence.getValue() + " => " + sentence.getStringLength() + " => " + sentence.getNoOfWords() + " => " + sentence.getParagraphNumber());
        }
    }

    public void printIntersectionMatrix() {
        for (int i = 0; i < getNoOfSentences(); i++) {
            for (int j = 0; j < getNoOfSentences(); j++) {
                System.out.print(getIntersectionMatrix()[i][j] + "    ");
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
        
       // System.out.println("no of paragraphs = " + getNoOfParagraphs());
        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : getContentSummary()) {
            sb.append(sentence.getValue());
            sb.append("\n");
           // sb.append("[\\r\\n]+");
          // sb.toString().split("[\\r\\n]+");
           // sb.append("......!");
          //  System.out.println(sentence.value);
        }
        setFinalSummery(sb.toString());
    }

    public double getWordCount(ArrayList<Sentence> sentenceList) {//here find the total no of words in the summery
        double wordCount = 0.0;
        for (Sentence sentence : sentenceList) {
            wordCount += sentence.getNoOfWords();
        }
        return wordCount;
    }

    public void printStats() {
        System.out.println("number of words in Context : " + getWordCount(getSentences()));
        System.out.println("number of words in Summary : " + getWordCount(getContentSummary()));
        System.out.println("Commpression : " + getWordCount(getContentSummary()) / getWordCount(getSentences()));
    }

    
    
    /**
     * @return the in
     */
    public FileInputStream getIn() {
        return in;
    }

    /**
     * @param in the in to set
     */
    public void setIn(FileInputStream in) {
        this.in = in;
    }

    /**
     * @return the out
     */
    public FileOutputStream getOut() {
        return out;
    }

    /**
     * @param out the out to set
     */
    public void setOut(FileOutputStream out) {
        this.out = out;
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

    /**
     * @return the contentSummary
     */
    public ArrayList<Sentence> getContentSummary() {
        return contentSummary;
    }

    /**
     * @param contentSummary the contentSummary to set
     */
    public void setContentSummary(ArrayList<Sentence> contentSummary) {
        this.contentSummary = contentSummary;
    }

    /**
     * @return the paragraphs
     */
    public ArrayList<Paragraph> getParagraphs() {
        return paragraphs;
    }

    /**
     * @param paragraphs the paragraphs to set
     */
    public void setParagraphs(ArrayList<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
    }

    /**
     * @return the noOfSentences
     */
    public int getNoOfSentences() {
        return noOfSentences;
    }

    /**
     * @param noOfSentences the noOfSentences to set
     */
    public void setNoOfSentences(int noOfSentences) {
        this.noOfSentences = noOfSentences;
    }

    /**
     * @return the noOfParagraphs
     */
    public int getNoOfParagraphs() {
        return noOfParagraphs;
    }

    /**
     * @param noOfParagraphs the noOfParagraphs to set
     */
    public void setNoOfParagraphs(int noOfParagraphs) {
        this.noOfParagraphs = noOfParagraphs;
    }

    /**
     * @return the intersectionMatrix
     */
    public double[][] getIntersectionMatrix() {
        return intersectionMatrix;
    }

    /**
     * @param intersectionMatrix the intersectionMatrix to set
     */
    public void setIntersectionMatrix(double[][] intersectionMatrix) {
        this.intersectionMatrix = intersectionMatrix;
    }

    /**
     * @return the dictionary
     */
    public LinkedHashMap<Sentence, Double> getDictionary() {
        return dictionary;
    }

    /**
     * @param dictionary the dictionary to set
     */
    public void setDictionary(LinkedHashMap<Sentence, Double> dictionary) {
        this.dictionary = dictionary;
    }
    
     /**
     * @return the finalSummery
     */
    public String getFinalSummery() {
        return finalSummery;
    }

    /**
     * @param finalSummery the finalSummery to set
     */
    public void setFinalSummery(String finalSummery) {
        this.finalSummery = finalSummery;
    }
}
