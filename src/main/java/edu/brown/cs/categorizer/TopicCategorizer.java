package edu.brown.cs.categorizer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import opennlp.tools.doccat.DoccatModel;
import opennlp.tools.doccat.DocumentCategorizerME;
import opennlp.tools.doccat.DocumentSample;
import opennlp.tools.doccat.DocumentSampleStream;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class TopicCategorizer {
  DoccatModel model;

  public static void main(String[] args) {
    TopicCategorizer docCategorizer = new TopicCategorizer();
    // docCategorizer.trainModel();

    System.out.println(docCategorizer.classifyNewDoc("The soviets hate me"));
    System.out
        .println(docCategorizer.classifyNewDoc("I went to the gym today"));
    System.out.println(
        docCategorizer.classifyNewDoc("Dogs are fun. I really want one"));
    System.out.println(docCategorizer.classifyNewDoc(
        "My job sucks! I don't understand how to do anything."));
    System.out.println(docCategorizer.classifyNewDoc(
        "I watched the movie the other day. The actors were great. Can't wait to go next year."));
    System.out.println(docCategorizer.classifyNewDoc(
        "I can't afford to go to the doctor. My arm hurts so bad and "
            + "I think it's broken. I need some medicine. Please help me."
            + " I need medical right away."));
    System.out.println(docCategorizer.classifyNewDoc(
        "I wonder what the republicans are going to do next year."));

    System.out.println(docCategorizer
        .classifyNewDoc("The researchers found that these weakened brain "
            + "connections among preschool children raised in poverty"
            + " were associated with greater risk of clinical depression"
            + " at the age of 9 or 10. In this study, we found that the way those"
            + " structures connect with the rest"
            + " of the brain changes in ways we would consider to be less helpful in regulating emotion"
            + " and stress, explains Barch. What is more,"
            + " the team found that the poorer children were at preschool age, the more likely they"
            + " were to have weaker brain connections and depression at school age."));
    System.out.println(docCategorizer.classifyNewDoc(
        "I have kidney disease and I don't know what to do. I eat a ton of veggies but I am still unhealthy."));
    System.out.println(docCategorizer.classifyNewDoc(
        "Light, sweet crude prices for June delivery dropped $1 on the New York market Apr."
            + " 21 to settle above $43/bbl, down from the session’s intraday high of $44.49/bbl"
            + " during a volatile trading day, which traders attributed to economic-related"
            + " comments and statistics by European and US officials."
            + "European Central Bank Pres. Mario Draghi said European interest rates"
            + " would remain at current or lower levels for an extended period, and"
            + " analysts said his statement contributed to a strengthening dollar."
            + " Oil trades in dollars so a stronger dollar puts downward pressure"
            + " on oil prices.The US Department of Labor released statistics Apr."
            + " 21 showing the number of US workers applying for jobless claims at a 43-year low."
            + "Initial jobless claims for unemployment benefits fell by 6,000 to"
            + " 247,000 for the week ended Apr. 16, the DOL said, adding that "
            + "is the lowest initial jobless claims stat since November 1973."
            + "Before the economic data was released, Organization of Petroleum"
            + " Exporting Countries Sec.-Gen. Abdallah Salem el-Badri said OPEC "
            + "members in June might again discuss whether to freeze oil production"
            + " levels at January levels."
            + "An Apr. 17 meeting in Qatar between major producers representing "
            + "both OPEC and non-OPEC countries failed to result in any agreement"
            + " on a proposal to freeze production at January levels."
            + "Analysts said they remain skeptical of the chances of a freeze agreement."
            + " Saudi Arabia wanted Iran to participate in the freeze, but Iran is building "
            + "its oil production and exports since nuclear-related international sanctions"
            + " were lifted in January."
            + "Separately, China reported its crude imports during March were up 21.6%"
            + " from the same time last year. Statistics showed 7.7 million b/d of oil"
            + " imported during March. China is a major oil consumer so analysts watch"
            + " its import numbers closely as one of many indicators of world oil demand."));

    System.out.println(docCategorizer
        .classifyNewDoc("In recent years, Serbia has also made"
            + " progress in its relations with Kosovo through"
            + " European Union-brokered talks."
            + " Kosovo broke away from Belgrade’s rule in 1999 with"
            + " the help of a NATO bombing campaign, and declared independence"
            + " in 2008, though Serbia officially still considers it part of Serbian"
            + " territory. While more than 100 countries, including the United States"
            + " and most European countries, recognize Kosovo’s independence, it remains"
            + " unrecognized at the United Nations thanks to a Russian veto. But improving "
            + "relations with Kosovo’s government is a prerequisite for Serbia’s European Union "
            + "membership."));

    System.out.println(docCategorizer
        .classifyNewDoc("ads sweep across America like thunderstorms. One of the latest"
            + " — selfies — may already be slackening. Colleges in Florida and Rhode"
            + " Island banned selfies at graduation. Reports that the White House was"
            + " discussing the fad brought out the selfie loathing. Even tastemaker"
            + " Katy Perry has tweeted that taking selfies is a disease. No matter."
            + " We will always remember a time when selfies were cool and everybody"
            + " was snapping them. Flagpole sitting, marathon dancing, goldfish swallowing"
            + " – these fads are forever part of the American fabric. Nothing mobilizes"
            + " the population like a big booming fad, wrote Richard Alan Johnson in his"
            + " 1985 history American Fads. Johnson listed 40 all-the-rages from our national"
            + " past, including Hula hoops, waterbeds and Nehru jackets. His book predated the"
            + " Internet, which has given us even more — like planking and Tebowing. But a"
            + " nosedive into the news archives turns up a host of other fads that for one reason"
            + " or another have pretty much been forgotten. Here are five: 1) Hair Lizards."
            + " The Atlanta Constitution reported in 1920 that — at the behest of the Humane "
            + "Society — police were cracking down on the sale of live chameleons to young"
            + " girls who liked to wear them in their hair at dances and soirees. Goodby"
            + " to our pretty hair ornaments, the reporter wrote. But at least the men "
            + "will feel safer, for at a recent dance it is stated when the best beau of "
            + "a Druid Hills girl started to whisper sweet nothings in her ear, the chameleon"
            + " became loosened from her hair and fell into the man's mouth."));
  }

  public TopicCategorizer() {
    trainModel();
  }

  private void trainModel() {
    File serializedModel = new File("../readient/topic_categorizer.ser");
    if (serializedModel.exists()) {
      try {
        InputStream modelIn = new BufferedInputStream(
            new FileInputStream(serializedModel));
        model = new DoccatModel(modelIn);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }

    } else {

      InputStream dataIn = null;
      try {

        dataIn = new FileInputStream("trainingnew.txt");
        ObjectStream<String> lineStream =
            new PlainTextByLineStream(dataIn, "UTF-8");
        ObjectStream<DocumentSample> sampleStream =
            new DocumentSampleStream(lineStream);
        // Specifies the minimum number of times a feature must be seen
        model = DocumentCategorizerME.train("en", sampleStream,5,500);

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        if (dataIn != null) {
          try {
            dataIn.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      OutputStream modelOut = null;
      try {
        modelOut = new BufferedOutputStream(new FileOutputStream(
            new File("../readient/topic_categorizer.ser")));
        model.serialize(modelOut);
      } catch (IOException e) {
        throw new RuntimeException(e);
      } finally {
        if (modelOut != null)
          try {
            modelOut.close();
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
      }
    }
  }

  public String classifyNewDoc(String doc) {
    DocumentCategorizerME myCategorizer = new DocumentCategorizerME(model);
    double[] outcomes = myCategorizer.categorize(doc);
    String category = myCategorizer.getBestCategory(outcomes);
    return category;
  }
}
