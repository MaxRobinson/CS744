package edu.jhu.mrobi100;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class Ingest {
  private final String fileName;
  private final String startParagraph = "<P ID=";
  private final Pattern start = Pattern.compile(startParagraph);
  private final Pattern stop = Pattern.compile("</P>");
  private final List<String> punct =
      Arrays.asList(".", ",", "'", "?", "!", ":", ";", "(", ")", "[", "]", "{", "}", "&", "|", "-");

  private final Postings postings;
  private final Lexicon lexicon;

  private int numberOfDocuments = 0;
  private int numberOfTerms = 0;

  public Ingest(String fileName) throws Exception {
    this.fileName = fileName;
    this.postings = new Postings();
    this.lexicon = new Lexicon();
  }

  public Lexicon read() throws IOException {
    try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
      String line;
      StringBuilder document = new StringBuilder();
      int id = -1;
      while ((line = bf.readLine()) != null) {

        line = line.trim();

        if (start.matcher(line).find()) {
          id = parseStart(line);
        } else if (stop.matcher(line).find()) {
          updateTerms(document.toString(), id);
          document.delete(0, document.length());
          id = -1;
          numberOfDocuments+=1;
        } else {
          document.append(line);
        }
      }

    } catch (Exception ex) {
      throw ex;
    }

    return lexicon;
  }

  private void updateTerms(String line, int id) {
    HashMap<String, Integer> termFreq = new HashMap<>();

    String[] tokens = line.split(" ");
    for (String token : tokens) {
      token = token.toLowerCase();
      if (punct.contains(token)) {
        continue;
      }

      numberOfTerms+=1;
      Integer df = termFreq.get(token);
      if(df == null){
        termFreq.put(token, 1);
      } else {
        termFreq.put(token, df + 1);
      }
    }

    for(Map.Entry<String, Integer> entry: termFreq.entrySet()){
      String key = entry.getKey();
      Integer df = entry.getValue();
      lexicon.addTerm(key, id, df);
    }
  }

  private int parseStart(String line) {
    line = line.replace("<P ID=", "");
    line = line.replace(">", "");
    int id = -1;
    try {
      id = Integer.parseInt(line);
    } catch (Exception ex) {
    }
    return id;
  }

  // <editor-fold desc="Accessors">

  public int getNumberOfDocuments() {
    return numberOfDocuments;
  }

  public int getNumberOfTerms() {
    return numberOfTerms;
  }

  // </editor-fold>
}
