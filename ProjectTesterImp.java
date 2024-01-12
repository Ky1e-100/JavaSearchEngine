import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class ProjectTesterImp implements ProjectTester {

    Crawler c = new Crawler();
    SearchData sd;
    Search s;
    List<SearchResult> result;

    @Override
    public void initialize() {
        result = new ArrayList<>();
        File directory0 = new File("Files");
        if (!directory0.exists()) {
            directory0.mkdirs();
        }
        File directory1 = new File("FileNames");
        if (!directory1.exists()) {
            directory1.mkdirs();
        }

        File directory2 = new File("Files" + File.separator);
        for(File f: directory2.listFiles()) 
        f.delete();


        File directory3 = new File("FileNames" + File.separator);
        for(File f: directory3.listFiles()) 
        f.delete();

    }

    @Override
    public void crawl(String seedURL) {
        c.crawl(seedURL);
        sd = new SearchData();
        s = new Search();
    }

    @Override
    public List<String> getOutgoingLinks(String url) {
        return sd.getOutgoingLinks(url);
    }

    @Override
    public List<String> getIncomingLinks(String url) {
        return sd.getIncomingLinks(url);
    }

    @Override
    public double getPageRank(String url) {
        return sd.getPageRank(url);
    }

    @Override
    public double getIDF(String word) {
        return sd.getIDF(word);
    }

    @Override
    public double getTF(String url, String word) {
        return sd.getTF(url, word);
    }

    @Override
    public double getTFIDF(String url, String word) {
        return sd.getTFIDF(url, word);
    }

    public List<SearchResult> getResult() {
        return result;
    }

    @Override
    public List<SearchResult> search(String query, boolean boost, int X) {
        result = s.search(query, boost, X);
        return result;
    }

    public static void main(String[] args) {
        ProjectTesterImp m = new ProjectTesterImp();
        m.initialize();
        m.crawl("https://people.scs.carleton.ca/~davidmckenney/fruits/N-0.html");
        System.out.println(m.search("peach banana coconut peach peach", true, 10));
    }
    
}
