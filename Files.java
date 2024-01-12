import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Files implements Serializable{

    private static int fileID = 0;
    private int id;
    private double totalWords;
    private String title;
    private ArrayList<String> words;
    private ArrayList<String> urls;
    private ArrayList<String> inLinks;
    private HashMap<String, Integer> wordFreqs;
    private Set<String> uniqueWords;
    private HashMap<String, Double> termFreqs;
    private HashMap<String, Double> tf;
    private HashMap<String, Double> tfIdf;
    private int totalLinkOut;
    private String seed;
    private double pageRank;
    private HashMap<String, Double> idfValues; //will only be used in the last file

    public Files(String title) {
        id = fileID;
        this.title = title;
        words = new ArrayList<>();
        urls = new ArrayList<>();
        inLinks = new ArrayList<>();
        wordFreqs = new HashMap<>();
        uniqueWords = new HashSet<>();
        termFreqs = new HashMap<>();
        tf = new HashMap<>();
        tfIdf = new HashMap<>();
        totalLinkOut = 0;
        fileID++;
    }

    //Getters and Setters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public HashMap<String, Integer> getWordFreqs() {
        return wordFreqs;
    }

    public Set<String> getUniqueWords() {
        return uniqueWords;
    }

    public HashMap<String, Double> getTermFreqs() {
        return termFreqs;
    }


    public HashMap<String, Double> getTf() {
        return tf;
    }

    public HashMap<String, Double> getTfIdf() {
        return tfIdf;
    }

    public int getTotalLinksOut() {
        return totalLinkOut;
    }

    public ArrayList<String> getInlinks() {
        return inLinks;
    }

    public String getSeed() {
        return seed;
    }

    public double getTotalWords() {
        return totalWords;
    }

    public double getPageRank() {
        return pageRank;
    }

    public HashMap<String, Double> getIdf() {
        return idfValues;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    public void setInLinks(ArrayList<String> inLinks) {
        this.inLinks = inLinks;
    }

    public void setWordFreqs(HashMap<String, Integer> wordFreqs) {
        this.wordFreqs = wordFreqs;
    }

    public void setTf(HashMap<String, Double> tf) {
        this.tf = tf;
    }
    
    public void setTfIdf(HashMap<String, Double> tfIdf) {
        this.tfIdf = tfIdf;
    }

    public void setTotalLinksOut(int totalLinkOut) {
        this.totalLinkOut = totalLinkOut;
    }

    public void setSeed(String seed) {
        this.seed = seed;
    }

    public void setPageRank(double pageRank) {
        this.pageRank = pageRank;
    }

    public void setIdf(HashMap<String, Double> idfValues) {
        this.idfValues = idfValues;
    }

    public String toString() {
        return title;
    }


    // Methods

    public void wordFreq() {
        for (String word: words) {
            totalWords++;
            word = word.strip().replace("\n", "");
            uniqueWords.add(word); //Adds to unique word set for word count later
            if (!wordFreqs.containsKey(word)) {
                wordFreqs.put(word, 1);
            } else {
                wordFreqs.put(word, wordFreqs.get(word) + 1);
            }
        }
    }

    public void termFreq() {
        for (String word: wordFreqs.keySet()) {
            termFreqs.put(word, Double.valueOf(wordFreqs.get(word) / totalWords));
        }
    }
}
