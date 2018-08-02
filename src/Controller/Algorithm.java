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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Algorithm {

   
   
  
   Writer writer = null;
    private FileInputStream in;
    private FileOutputStream out;
    //contain sentences in the context
    private ArrayList<Sentence> sentences;
    //contain sentences in the summery
    private ArrayList<Sentence> contentSummary;
     //contain sentences in the summery base on key word
     private ArrayList<Sentence> contentSummaryBaseOnKeyWord;
    //contain final summery
    private String finalSummery;
     //contain final summery base on keyword
    private String finalSummeryBaseOnKeyWord;
    
    private String finaldictionaryOfParagraphNoAndSentence;

    //contain paragraphs in context
    private ArrayList<Paragraph> paragraphs;
    //contain no of sentences in the context
    
    private int noOfSentences;
    //contain no of paragraph in the context
    private int  noOfParagraphs;
  
    private double[][] intersectionMatrix;
   
    private int[] noOfKeyWords;
    //Contain Sentences With Key Words in the context.
    private ArrayList<Sentence> sentencesWithKeyWords;
    //Contain Sentences With Scores
    private LinkedHashMap<Sentence, Double> dictionary;
    //Contain Sentences With paragraph no
    private LinkedHashMap<Sentence, Integer> dictionaryOfParagraphNoAndSentence;

    private double Commpression;
    
    public Algorithm() {
        this.in = null;
        this.out = null;
        this.noOfSentences = 0;
        this.noOfParagraphs = 0;
        
        try {
            
        } catch (Exception e) {
        }
        
        
    }

    public void init() {
        setSentences(new ArrayList<Sentence>());//this array list contain sentences in the context
        setSentencesWithKeyWords(new ArrayList<Sentence>());//this array list contain sentences with key words in the context
        setParagraphs(new ArrayList<Paragraph>());
        setContentSummary(new ArrayList<Sentence>());//this array list contain sentences in the summery
        setContentSummaryBaseOnKeyWord(new ArrayList<Sentence>());
        setDictionary(new LinkedHashMap<Sentence, Double>());
        setDictionaryOfParagraphNoAndSentence(new LinkedHashMap<Sentence, Integer>() );
        setNoOfSentences(0);
        setNoOfParagraphs(0);
        try {
            setIn(new FileInputStream("context.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*Extract sentences from the entire passage*/
    public void extractSentenceFromContext() { 
        int nextChar, j = 0;
        int prevChar = -1;
        try {
            while ((nextChar = getIn().read()) != -1) {                         
                j = 0;
                char[] temp = new char[100000];
                while ((char) nextChar != '.') {
                    temp[j] = (char) nextChar;

                    if ((nextChar = getIn().read()) == -1) {
                        break;
                    }
                    if ((char) nextChar == '\n' && (char) prevChar == '\n') { 
                        setNoOfParagraphs(getNoOfParagraphs() + 1);
                    }

                    j++;
                    prevChar = nextChar;
                }
                getSentences().add(new Sentence(getNoOfSentences(), (new String(temp)).trim(), (new String(temp)).trim().length(), getNoOfParagraphs()));//here noOfSentences=sentence No
                setNoOfSentences(getNoOfSentences() + 1);
                prevChar = nextChar;
            }
              
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void groupSentencesIntoParagraphs() {//from this methode take sentences with  paragraph number and group them with similar pargraph number
        int paraNum = 0;
        Paragraph paragraph = new Paragraph(0);

        for (int i = 0; i < getNoOfSentences(); i++) {
            if (getSentences().get(i).getParagraphNumber() == paraNum) {//check whether the selected sentence paragraph number similar to paraNum
                System.out.println(getSentences().get(i));
                System.out.println("paraNum"+paraNum);
                System.out.println("paragaph number"+getSentences().get(i).getParagraphNumber());
                                getDictionaryOfParagraphNoAndSentence().put(getSentences().get(i), paraNum);
                               

                //continue
            } else {
                getParagraphs().add(paragraph);//if selected sentence paragraph number not similar to paraNum add prev created paragraph in paragraph list here.
                paraNum++;//increase paraNum
                System.out.println("paraNum"+paraNum);
                 paragraph = new Paragraph(paraNum);//create new paragraph with new paraNum. 
                System.out.println("paragaph number else part"+getSentences().get(i).getParagraphNumber());
                getDictionaryOfParagraphNoAndSentence().put(getSentences().get(i),paraNum);

  }

            paragraph.getSentences().add(getSentences().get(i));//if selected sentence paragraph number similar to paraNum insert that selected sentence in to that paragraph sentnce array list.
            System.out.println("paragraph no"+paragraph.getNumber());

        }

        getParagraphs().add(paragraph);
        
    }
    /**
     * check no of common words to create Intersection Matrix base on no option
     * @param str1
     * @param str2
     * @return commonCount
     */

    public double noOfCommonWords(Sentence str1, Sentence str2) {
        double commonCount = 0;

        for (String str1Word : str1.getValue().split("\\s+")) {//"\\s+" is a regex this will split the string in to string of array with seperator as a one space or multiple spaces.  \ is a regex for one or more spaces 

            //  System.out.println(str1Word);
            for (String str2Word : str2.getValue().split("\\s+")) {

                //   System.out.println(str2Word);
                if (str1Word.compareToIgnoreCase(str2Word) == 0) {//if str2Word=-int str2Word is grater than to  str1Word and if str2Word=0 str2Wordis equal to str1Word and if str2Word= +int str2Word is less than the str1Word
                    commonCount++;
                    System.out.println("common count of key word base :"+commonCount);
                }
            }
        }

        return commonCount;
    }
    
   /**
    * Intersection Matrix base on no option
    */
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
    /**
     * check no of key words to create noOfKeyWordsArray 
     * @param str1
     * @param keyWord
     * @return keyWordCount
     */
     
    public int noOfKeyWords(Sentence str1, String keyWord) {
        int keyWordCount = 0;

        for (String str1Word : str1.getValue().split("\\s+")) {//"\\s+" is a regex this will split the string in to string of array with seperator as a one space or multiple spaces.  \ is a regex for one or more spaces 

            //  System.out.println(str1Word);

                //   System.out.println(str2Word);
                if (str1Word.compareToIgnoreCase(keyWord) == 0) {//if str2Word=-int str2Word is grater than to  str1Word and if str2Word=0 str2Wordis equal to str1Word and if str2Word= +int str2Word is less than the str1Word
                    keyWordCount++;
                    System.out.println("keyWordCount : "+keyWordCount);
                }
            
        }

        return keyWordCount;
    }

    
    public void createnoOfKeyWordsArray(String keyWord){
        setNoOfKeyWords(new int[getNoOfSentences()]);
                for (int i = 0; i < getNoOfSentences(); i++) {
                    
                   Sentence str1 = getSentences().get(i);
                    getNoOfKeyWords()[i]=noOfKeyWords(str1, keyWord);
                    if(getNoOfKeyWords()[i]>0){
                       getSentencesWithKeyWords().add(str1);
                        
                    }      
                }
                System.out.println("getSentencesWithKeyWords "+getSentencesWithKeyWords().size());
    }
    
/**
 * Intersection Matrix base on keyword
 */
     public void createIntersectionMatrixBaseOnKeyWords() {
        setIntersectionMatrix(new double[getSentencesWithKeyWords().size()][getSentencesWithKeyWords().size()]);//arr[i][j]
        for (int i = 0; i < getSentencesWithKeyWords().size(); i++) {
            for (int j = 0; j < getSentencesWithKeyWords().size(); j++) {

                if (i <= j) {
                    Sentence str1 = getSentences().get(i);
                    Sentence str2 = getSentences().get(j);
                    getIntersectionMatrix()[i][j] = noOfCommonWords(str1, str2) / ((double) (str1.getNoOfWords() + str2.getNoOfWords()) / 2);
                  //    System.out.println("intersectionMatrix "+intersectionMatrix[i][j]);   
                } else {
                    getIntersectionMatrix()[i][j] = getIntersectionMatrix()[j][i];
                     //System.out.println("intersectionMatrix"+intersectionMatrix[i][j]);   
                }

            }
        }

    }
    
    
    /**
     * Create Dictionary base on no option
     */
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
    /**
     * Create Dictionary base on KeyWord
    */
     public void createDictionaryBaseOnKeyWords() {
        for (int i = 0; i < getSentencesWithKeyWords().size(); i++) {
            double score = 0;
            for (int j = 0; j < getSentencesWithKeyWords().size(); j++) {
                score += getIntersectionMatrix()[i][j];//score=intersectionMatrix[i][j]+score;

            }
            //System.out.println(score);

            getDictionary().put(getSentencesWithKeyWords().get(i), score);
            getSentencesWithKeyWords().get(i).setScore(score);
            //System.out.println(sentences.get(i).score);
        }
    }

    
    /**
     * Create Summary base on no option
     */
    
    public void createSummary(int summeryLevel) {
        System.out.println("summeryLevel"+summeryLevel);
        for (int j = 0; j <= getNoOfParagraphs(); j++) {
            int primary_set = getParagraphs().get(j).getSentences().size() / summeryLevel;//find no of group of 5 sentences
             System.out.println("primary_set"+primary_set);
            //Sort based on score (importance)
            Collections.sort(getParagraphs().get(j).getSentences(), new SentenceComparatorOnScore());//sort here according to(descending order) the score of a sentence(SentenceComparatorOnScore has a compare method to compare and collection has sort method to sort this)
            for (int i = 1; i <= primary_set; i++) {//from a one group select 2 sentences here i assign i to 1 then when there is sentences count belong to 5 there will not be any out put.
                getContentSummary().add(getParagraphs().get(j).getSentences().get(i));
                System.out.println("hai catch the ssummery!");
            }
        }

        //To ensure proper ordering (order should be qual to the original context order)
        Collections.sort(getContentSummary(), new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to comapare and collection has sort method to sort)

    }
    /**
     * Create Summary base on keyword
     */
    public void createSummaryBaseOnKeyWords() {
           int primary_set = getSentencesWithKeyWords().size() / 5;//find no of group of 5 sentences.ex:if has 17 sentences in a paragraph we select  4 group of sentences for summery
            System.out.println("primary_set"+primary_set);
            //Sort based on score (importance)
            Collections.sort(getSentencesWithKeyWords(), new SentenceComparatorOnScore());//sort here according to the score of a sentence(SentenceComparatorOnScore has a compare to do this)
            for (int i = 0; i <= primary_set; i++) {//from a one group select 2 sentences
              //  getContentSummary().add(getParagraphs().get(j).getSentences().get(i));
              getContentSummaryBaseOnKeyWord().add(getSentencesWithKeyWords().get(i));
            }
            System.out.println("getContentSummaryBaseOnKeyWord() "+getContentSummaryBaseOnKeyWord().size());
        

        //To ensure proper ordering (order should be qual to the original context order)
        Collections.sort(getContentSummaryBaseOnKeyWord(), new SentenceComparatorForSummary());//sort here according to the sentence no(SentenceComparatorForSummary has a compare methode to do this)

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

    public void printDicationary(){
		  // Get a set of the entries
	      Set set = dictionary.entrySet();
	      // Get an iterator
	      Iterator i = set.iterator();
	      // Display elements
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
	         System.out.print(((Sentence)me.getKey()).value + ": ");
	         System.out.println(me.getValue());
	      }
	}
     public void DictionaryOfParagraphNoAndSentence(){
	        StringBuilder sb = new StringBuilder();
                 sb.append("   Sentence : ");
                 sb.append("   Paragraph Number");
                 sb.append("\n");
                 sb.append("\n");
         // Get a set of the entries
	      Set set = dictionaryOfParagraphNoAndSentence.entrySet();
	      // Get an iterator
	      Iterator i = set.iterator();
	      // Display elements
	      while(i.hasNext()) {
	         Map.Entry me = (Map.Entry)i.next();
                 sb.append("   "+((Sentence)me.getKey()).value + ": ");
                 sb.append("   "+me.getValue());
                 sb.append("\n");
                 sb.append("\n");
//                 System.out.print(((Sentence)me.getKey()).value + ": ");
//	         System.out.println(me.getValue());
	      }
              setFinaldictionaryOfParagraphNoAndSentence(sb.toString());
              
	}
    
     
     /**
      * Print Summary
      */
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
    
    
     public void printSummaryBaseOnKeyWords() {
        
       // System.out.println("no of paragraphs = " + getNoOfParagraphs());
        StringBuilder sb = new StringBuilder();
        for (Sentence sentence : getContentSummaryBaseOnKeyWord()) {
            sb.append(sentence.getValue());
            sb.append("\n");
           // sb.append("[\\r\\n]+");
          // sb.toString().split("[\\r\\n]+");
           // sb.append("......!");
          //  System.out.println(sentence.value);
        }
        setFinalSummeryBaseOnKeyWord(sb.toString());
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
 /**
     * @return the Commpression
     */
    public double getCommpression() {
        return Commpression;
    }

    /**
     * @param Commpression the Commpression to set
     */
    public void setCommpression() {
        
        this.Commpression= (getWordCount(getContentSummary()) / getWordCount(getSentences()));
    
    }
     /**
     * @return the dictionaryOfParagraphNoAndSentence
     */
    public LinkedHashMap<Sentence, Integer> getDictionaryOfParagraphNoAndSentence() {
        return dictionaryOfParagraphNoAndSentence;
    }

    /**
     * @param dictionaryOfParagraphNoAndSentence the dictionaryOfParagraphNoAndSentence to set
     */
    public void setDictionaryOfParagraphNoAndSentence(LinkedHashMap<Sentence, Integer> dictionaryOfParagraphNoAndSentence) {
        this.dictionaryOfParagraphNoAndSentence = dictionaryOfParagraphNoAndSentence;
    }
     /**
     * @return the finaldictionaryOfParagraphNoAndSentence
     */
    public String getFinaldictionaryOfParagraphNoAndSentence() {
        return finaldictionaryOfParagraphNoAndSentence;
    }

    /**
     * @param finaldictionaryOfParagraphNoAndSentence the finaldictionaryOfParagraphNoAndSentence to set
     */
    public void setFinaldictionaryOfParagraphNoAndSentence(String finaldictionaryOfParagraphNoAndSentence) {
        this.finaldictionaryOfParagraphNoAndSentence = finaldictionaryOfParagraphNoAndSentence;
    }

     /**
     * @return the noOfKeyWords
     */
    public int[] getNoOfKeyWords() {
        return noOfKeyWords;
    }

    /**
     * @param noOfKeyWords the noOfKeyWords to set
     */
    public void setNoOfKeyWords(int[] noOfKeyWords) {
        this.noOfKeyWords = noOfKeyWords;
    }

   

    /**
     * @return the sentencesWithKeyWords
     */
    public ArrayList<Sentence> getSentencesWithKeyWords() {
        return sentencesWithKeyWords;
    }

    /**
     * @param sentencesWithKeyWords the sentencesWithKeyWords to set
     */
    public void setSentencesWithKeyWords(ArrayList<Sentence> sentencesWithKeyWords) {
        this.sentencesWithKeyWords = sentencesWithKeyWords;
    }

 /**
     * @return the contentSummaryBaseOnKeyWord
     */
    public ArrayList<Sentence> getContentSummaryBaseOnKeyWord() {
        return contentSummaryBaseOnKeyWord;
    }

    /**
     * @param contentSummaryBaseOnKeyWord the contentSummaryBaseOnKeyWord to set
     */
    public void setContentSummaryBaseOnKeyWord(ArrayList<Sentence> contentSummaryBaseOnKeyWord) {
        this.contentSummaryBaseOnKeyWord = contentSummaryBaseOnKeyWord;
    }

     /**
     * @return the finalSummeryBaseOnKeyWord
     */
    public String getFinalSummeryBaseOnKeyWord() {
        return finalSummeryBaseOnKeyWord;
    }

    /**
     * @param finalSummeryBaseOnKeyWord the finalSummeryBaseOnKeyWord to set
     */
    public void setFinalSummeryBaseOnKeyWord(String finalSummeryBaseOnKeyWord) {
        this.finalSummeryBaseOnKeyWord = finalSummeryBaseOnKeyWord;
    }

}
