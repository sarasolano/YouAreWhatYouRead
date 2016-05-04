package edu.brown.cs.categorizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import edu.brown.cs.parsing.ArticleParser;

public class NYTParser {
  public static void main(String[] args)
      throws IOException, InterruptedException, ParseException {
    NYTParser.getArticles("20140901","7b36d3b640e1bb37fe77f74d921a090d:12:75167618");
   // NYTParser.getArticles("20120101","5132d82e4c312ddeae7c0165f36ea3be:0:75165660");
  }

  public static void getArticles(String date,String key)
      throws IOException, InterruptedException, ParseException {

    FileWriter fw = new FileWriter("nyt_topics.txt",true);
    BufferedWriter bw = new BufferedWriter(fw);
    String startDate = date; // Start date

    for (int i = 0; i < 100; i++) {
      System.out.println(i);
      BufferedReader br = new BufferedReader(new FileReader("newsdesk.txt"));
      String newsdesk = br.readLine();
      while (newsdesk != null) {
        System.out.println(newsdesk);
        String urlString = String.format(
            "http://api.nytimes.com/svc/search/v2/articlesearch."
            + "json?fq=news_desk:(%s)&begin_date=%s&"
            + "end_date=%s&api-key=%s",
            "\"" + newsdesk.replaceAll(" ", "%20") + "\"", startDate,
            startDate,key);
        System.out.println(urlString);
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        InputStream input = conn.getInputStream();
        /*
         * Map<String, String> map = new Gson().fromJson( new
         * InputStreamReader(input, "UTF-8"), new TypeToken<Map<String,
         * String>>() { }.getType());
         */

        BufferedReader rd = new BufferedReader(new InputStreamReader(input));
        StringBuilder responseStrBuilder = new StringBuilder();
        String line;

        Gson gson = new Gson();

        while ((line = rd.readLine()) != null) {
          responseStrBuilder.append(line);

          // System.out.println(line);

          JsonElement jelement = new JsonParser().parse(line);
          JsonObject jobject = jelement.getAsJsonObject();
          if (jobject != null) {
            jobject = jobject.getAsJsonObject("response");
            JsonArray docs = jobject.getAsJsonArray("docs");
            if (docs != null && docs.size() > 0) {
              JsonElement obj = docs.get(0);
              JsonObject o = obj.getAsJsonObject();
              if (o != null) {
                String pathQuoted = o.getAsJsonPrimitive("web_url").toString();
                String path = pathQuoted.substring(1, pathQuoted.length() - 1);
                System.out.println(path);
                ArticleParser ap = new ArticleParser(path);
                System.out.println(ap.text());
                bw.write(String.format("%s   %s",
                    newsdesk.replaceAll(" ", "%20"), ap.text()));
                bw.newLine();
                bw.flush();

              }

            }

          }


          // System.out.println(result);
        }
        TimeUnit.MILLISECONDS.sleep(50);
        input.close();
        rd.close();

        // System.out.println(map.get("response"));
        newsdesk = br.readLine();
      }
      br.close();

      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      Calendar c = Calendar.getInstance();
      c.setTime(sdf.parse(startDate));
      c.add(Calendar.DATE, 1); // number of days to add
      startDate = sdf.format(c.getTime());
    }

    bw.close();

  }

}
