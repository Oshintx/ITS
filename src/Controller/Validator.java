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
public class Validator {

    Algorithm algorithm;
    private String allowePercentage;

    public Validator() {

    }

    public void checkNoOfSentencesWithKeyWord(Algorithm algorithm) {

        this.algorithm = algorithm;
        System.out.println("key words count" + algorithm.getSentencesWithKeyWords().size());

        if (algorithm.getSentencesWithKeyWords().size() < 5) {
            this.setAllowePercentage("Not Allowe any Percentage");
        }

        if (algorithm.getSentencesWithKeyWords().size() >= 5.0 && algorithm.getSentencesWithKeyWords().size() < 10) {
            this.setAllowePercentage("Allowe Percentage 25");
        }

        if (algorithm.getSentencesWithKeyWords().size() >= 10.0 && algorithm.getSentencesWithKeyWords().size() < 15) {
            this.setAllowePercentage("Allowe Percentage 50 & 25");
        }
        if (algorithm.getSentencesWithKeyWords().size() >= 15.0 && algorithm.getSentencesWithKeyWords().size() < 20) {
            this.setAllowePercentage("Allowe Percentage 50 & 25 & 75");
        }
        if (algorithm.getSentencesWithKeyWords().size() >= 20.0) {
            this.setAllowePercentage("Allowe Percentage 50 & 25 & 75 && 100");
        }

    }

    public void checkNoOfSentencesWithPercentageSummarization(Algorithm algorithm) {

        this.algorithm = algorithm;
        System.out.println("getNoOfSentences" + algorithm.getNoOfSentences());

        if (algorithm.getNoOfSentences() < 5) {
            this.setAllowePercentage("Not Allowe any Percentage");
        }

        if (algorithm.getNoOfSentences() >= 5.0 && algorithm.getNoOfSentences() < 10) {
            this.setAllowePercentage("Allowe Percentage 25");
        }

        if (algorithm.getNoOfSentences() >= 10.0 && algorithm.getNoOfSentences() < 15) {
            this.setAllowePercentage("Allowe Percentage 50 & 25");
        }
        if (algorithm.getNoOfSentences() >= 15.0 && algorithm.getNoOfSentences() < 20) {
            this.setAllowePercentage("Allowe Percentage 50 & 25 & 75");
        }
        if (algorithm.getNoOfSentences()>= 20.0) {
            this.setAllowePercentage("Allowe Percentage 50 & 25 & 75 && 100");
        }

    }

    /**
     * @return the allowePercentage
     */
    public String getAllowePercentage() {
        return allowePercentage;
    }

    /**
     * @param allowePercentage the allowePercentage to set
     */
    public void setAllowePercentage(String allowePercentage) {
        this.allowePercentage = allowePercentage;
    }

}
