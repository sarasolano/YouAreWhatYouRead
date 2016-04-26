package edu.brown.cs.categorizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TopicParser {

  public static void main(String[] args) {
    new TopicParser();

  }

  public TopicParser() {
    try {
      FileWriter fw = new FileWriter("../readient/topicTraining.txt");
      BufferedWriter bw = new BufferedWriter(fw);
      for (int docs = 0; docs <= 21; docs++) {
        String num;
        if (docs < 10) {
          num = "0" + docs;

        } else {
          num = Integer.toString(docs);

        }
        if (docs != 17) {
          String fileName = "../readient/topic_data/reut2-0" + num + ".sgm";
          System.out.println(fileName);
          File inputFile = new File(fileName);
          DocumentBuilderFactory dbFactory = DocumentBuilderFactory
              .newInstance();
          DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
          Document doc = dBuilder.parse(inputFile);
          doc.getDocumentElement().normalize();
          System.out.println(
              "Root element :" + doc.getDocumentElement().getNodeName());
          NodeList nList = doc.getElementsByTagName("REUTERS");
          System.out.println("----------------------------");
          for (int temp = 0; temp < nList.getLength(); temp++) {
            Node nNode = nList.item(temp);
            NamedNodeMap attributes = nNode.getAttributes();
            String topic = attributes.getNamedItem("TOPICS").getNodeValue();
            if (topic.equals("YES")) {
              NodeList subNodes = nNode.getChildNodes();
              NodeList topics = null;
              for (int i = 0; i < subNodes.getLength(); i++) {
                if (subNodes.item(i).getNodeName().equals("TOPICS")) {
                  Element topicElement = (Element) subNodes.item(i);
                  topics = topicElement.getElementsByTagName("D");

                }
                if (subNodes.item(i).getNodeName().equals("TEXT")) {

                  NodeList subSubNodes = subNodes.item(i).getChildNodes();
                  for (int y = 0; y < subSubNodes.getLength(); y++) {
                    if (subSubNodes.item(y).getNodeName().equals("BODY")) {
                      Element e = (Element) subNodes.item(i);
                      String text = e.getElementsByTagName("BODY").item(0)
                          .getTextContent();
                      System.out.println(text);

                      for (int n = 0; n < topics.getLength(); n++) {
                        Element t = (Element) topics.item(n);
                        String top = t.getTextContent();
                        String output = String.format("%s    %s", top,
                            text.replaceAll("\n", ""));
                        bw.write(output);
                        bw.newLine();

                      }

                    }
                  }
                }
              }
            }

          }

        }

      }
      File dir = new File("../readient/20_newsgroup");
      File[] directoryListing = dir.listFiles();
      if (directoryListing != null) {
        for (File child : directoryListing) {
          File[] childListing = child.listFiles();
          System.out.println(child);
          if (childListing != null) {
            for (File trainingFile : childListing) {
              BufferedReader br = new BufferedReader(
                  new FileReader(trainingFile));
              String line = br.readLine();
              Boolean readRemaining = false;
              while (line != null) {
                if (!readRemaining) {
                  String[] splitLine = line.split(" ");
                  if (splitLine.length >= 2) {
                    if (splitLine[0].equals("In")
                        && splitLine[1].equals("article")) {
                      readRemaining = true;
                      String output = String.format("%s   ",
                          child.getName().replaceAll("\n", ""));
                      bw.write(output.replaceAll("\n", ""));
                    }

                  }

                } else {
                  String writeLine = line.replaceAll("\n", " ");
                  writeLine = writeLine.replaceAll(">", "");
                  writeLine = writeLine.replaceAll("|", "");
                  if (!writeLine.replaceAll(" ", "").isEmpty()) {
                    String[] splitLine = line.split(" ");
                    if (splitLine.length >= 2) {
                      if (!splitLine[0].equals("In")
                          || !splitLine[1].equals("article")) {
                        String output = String.format("%s", writeLine);
                        bw.write(output.replaceAll("\n", ""));
                      }

                    } else {
                      String output = String.format("%s", writeLine);
                      bw.write(output.replaceAll("\n", ""));

                    }

                  }

                }

                line = br.readLine();
              }
              br.close();
              bw.newLine();

            }

          }

        }

      }

      bw.close();
      fix();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void fix() throws IOException {
    File file = new File("../readient/topicTraining.txt");
    BufferedReader br = new BufferedReader(new FileReader(file));
    FileWriter fw = new FileWriter("../readient/topicTrainingFinal.txt");
    BufferedWriter bw = new BufferedWriter(fw);
    String line = br.readLine();
    while (line != null) {
      if (!line.replaceAll(" ", "").isEmpty()) {
        bw.write(line.replaceAll("\n", ""));
        bw.newLine();
      }

      line = br.readLine();
    }
    br.close();
    bw.close();

  }

}
