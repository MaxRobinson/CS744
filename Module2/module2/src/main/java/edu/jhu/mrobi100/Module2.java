package edu.jhu.mrobi100;


public class Module2 {
  private static String invertedFileName = "/home/max/Documents/JHU-Masters/InformationRetrieval" +
          "/Module2/module2/src/main/java/edu/jhu/mrobi100/invertedFile.txt";
  private static String lexiconFileName = "/home/max/Documents/JHU-Masters/InformationRetrieval" +
          "/Module2/module2/src/main/java/edu/jhu/mrobi100/lexicon.txt";


  public static void main(String[] args) throws Exception {
    // Create Lexicon and Inverted File
    Ingest ingest = new Ingest(Module2.class.getResource("headlines.txt").toURI().toString().substring(5));
    Lexicon lexicon = ingest.read();

    lexicon.writeInvertedFile(invertedFileName);
    lexicon.writeLexicon(lexiconFileName);

    System.out.println("Number of Documents: " + ingest.getNumberOfDocuments());
    System.out.println("Number of unique terms: " + lexicon.getDict().keySet().size());
    System.out.println("Number of terms observed: " + ingest.getNumberOfTerms());
  }
}
