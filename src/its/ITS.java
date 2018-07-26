/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package its;

/**
 *
 * @author Oshin
 */
public class ITS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
       SummaryTool summary = new SummaryTool();
		summary.init();
		summary.extractSentenceFromContext();
		summary.groupSentencesIntoParagraphs();
		summary.createIntersectionMatrix();
                summary.createDictionary();
                System.out.println("SUMMMARY");
		summary.createSummary();
		summary.printSummary();
                summary.printStats();
            
                //summary.printSentences();
		//System.out.println("INTERSECTION MATRIX");
		//summary.printIntersectionMatrix();
                //summary.printDicationary();

    }
    
}
