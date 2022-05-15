package webcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebCrawler {
    public static Queue<String> list_of_websites = new LinkedList<>();
    public static Set<String> marked = new HashSet<>();
    public static String regex = "http[s]*://(\\w+\\.)*(\\w+)";

    public static Queue<String> get_list_websites(String websites){
        Queue<String> list_of_websites_returned = new LinkedList<>();
        List<String> arrayString = Arrays.asList(websites.split(","));
        list_of_websites_returned.addAll(arrayString);
        return list_of_websites_returned;
    }

    public static void bufferAlgorithm(String root) throws IOException{

        list_of_websites = get_list_websites(root);

        while (!list_of_websites.isEmpty()){
            String crawledUrl = list_of_websites.poll();
            System.out.println("\n====== Site crawled: " + crawledUrl + " =====");

            boolean ok = false;
            URL url;
            BufferedReader br = null;

            while (!ok){
                try{
                    url = new URL(crawledUrl);
                    br = new BufferedReader((new InputStreamReader(url.openStream())));
                    ok = true;
                }catch (MalformedURLException e){
                    System.out.println("Malformed URL: " + crawledUrl);
                    crawledUrl = list_of_websites.poll();
                }catch (IOException e){
                    System.out.println("IOException fro URL: " + crawledUrl);
                    crawledUrl = list_of_websites.poll();
                }
            }

            StringBuilder sb = new StringBuilder();

            while((crawledUrl = br.readLine()) != null){
                sb.append(crawledUrl);
            }

            crawledUrl = sb.toString();
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(crawledUrl);

            while (matcher.find()){
                String w = matcher.group();
                if (marked.size() >= 100)
                    break;
                if (!marked.add(w)){
                    marked.add(w);
                    System.out.println("Site added for crawling: " + w);
                }
            }
        }
    }

    public static void showResults(){
        System.out.println("\n\nResults: ");
        System.out.println("Websites crawled: " + marked.size() + "\n");

        for (String s: marked){
            System.out.println("- " + s);
        }
    }

    public static  void main(String[] args){
        try{
            bufferAlgorithm("http://www.ssaurel.com/blog,https://support.google.com,https://www.python.org,https://www.youtube.com,https://code.google.com");
            showResults();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
