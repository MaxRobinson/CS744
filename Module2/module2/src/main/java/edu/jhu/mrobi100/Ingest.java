package edu.jhu.mrobi100;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ingest {
  private final String fileName;
  private final String startParagraph = "<P ID=";
  private final Pattern start = Pattern.compile(startParagraph);
  private final Pattern stop = Pattern.compile("</P>");

  public Ingest(String fileName) throws Exception {
    this.fileName = fileName;
  }

  public void read() {
    try (BufferedReader bf = new BufferedReader(new FileReader(fileName))) {
      String line;
      int id = -1;
      while ((line = bf.readLine()) != null) {

        line = line.trim();

        if (start.matcher(line).find()) {
          parseStart(line);
        } else if (stop.matcher(line).find()) {
          parseStop(line, id);
        } else {

        }
      }

    } catch (Exception ex) {

    }
  }

  private String parseStop(String line, int docId) {
    return null;
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
}
