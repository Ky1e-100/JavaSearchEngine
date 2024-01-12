import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Search {

    public List<SearchResult> search(String query, boolean boost, int X) {
        SearchData s = new SearchData();
        MathMult m = new MathMult();
        ArrayList<SearchResult> result = new ArrayList<>();
        ArrayList<Files> files = new ArrayList<>();
        HashMap<String, Files> seedFile = new HashMap<>();
        HashMap<String, Double> cosineSims = new HashMap<>();

        files = s.getFiles();
        for (Files z : files) {
            seedFile.put(z.getSeed(), z);
        }

        String words[];
        words = query.split(" ");
        ArrayList<String> wordsList = new ArrayList<>();
        for (String w : words) {
            if (s.getIDF(w) > 0) {
                wordsList.add(w);
            }
        }

        ArrayList<Double> searchVector = new ArrayList<>();
        HashMap<String, Double> qis = new HashMap<>();

        for (String w : wordsList) {
            if (qis.containsKey(w)) {
                continue;
            }
            int numOfWords = Collections.frequency(wordsList, w);
            double qi = m.log2(1.0 + ((numOfWords+0.0) / (wordsList.size()+0.0))) * s.getIDF(w);
            qis.put(w, qi);
            searchVector.add(qi);
        }

        HashMap<String, ArrayList<Double>> dis = new HashMap<>();
        for (Files f : files) {
            ArrayList<Double> docVector = new ArrayList<>();
            HashMap<String, Integer> completD = new HashMap<>();
            for (String w : wordsList) {
                if (completD.containsKey(w)) {
                    continue;
                }
                double docTfIdf = s.getTFIDF(f.getSeed(), w);
                docVector.add(docTfIdf);
                completD.put(w, 0);
            }
            dis.put(f.getSeed(), docVector);

            double nume, ldnom, rdenom, denom;
            nume = 0;
            ldnom = 0;
            rdenom = 0;
            denom = 0;

            for (int i = 0; i < searchVector.size(); i++) {
                nume += searchVector.get(i) * docVector.get(i);
                ldnom += Math.pow(searchVector.get(i), 2);
                rdenom += Math.pow(docVector.get(i), 2);
            }
            denom += Math.sqrt(ldnom) * Math.sqrt(rdenom);
            
            if (denom == 0) {
                cosineSims.put(f.getSeed(), 0.0);
                continue;
            }

            double cosineSim = nume / denom;

            if (boost) {
                cosineSims.put(f.getSeed(), cosineSim * s.getPageRank(f.getSeed()));
            } else {
                cosineSims.put(f.getSeed(), cosineSim);
            }
        }

        HashMap<Double, ArrayList<String>> dupes = new HashMap<>();
        HashMap<Double, String> rever = new HashMap<>();
        ArrayList<Double> cosineSim = new ArrayList<>();

        for (String url : cosineSims.keySet()) {
            cosineSim.add(cosineSims.get(url));
            if (rever.containsKey(cosineSims.get(url))) {
                if (dupes.get(cosineSims.get(url)) == null) {
                    ArrayList<String> a = new ArrayList<>();
                    a.add(url);
                    dupes.put(cosineSims.get(url), a);
                } else {
                    dupes.get(cosineSims.get(url)).add(url);
                }
            } else {
                rever.put(cosineSims.get(url), url);
            }
        }
        
        Collections.sort(cosineSim, Collections.reverseOrder());

        HashMap<String, Integer> used = new HashMap<>();

        for (int j = 0; j < X; j++) {
            double CosineSimx = cosineSim.get(j);
            if (used.containsKey(rever.get(CosineSimx))) {
                Collections.sort(dupes.get(CosineSimx)); //alphabetical sorting
                SearchResult y = new SearchResult(seedFile.get(dupes.get(CosineSimx).get(0)).getTitle(), CosineSimx);
                dupes.get(CosineSimx).remove(0);
                result.add(y);
            } else {
                used.put(rever.get(CosineSimx), 0);
                SearchResult y = new SearchResult(seedFile.get(rever.get(CosineSimx)).getTitle(), CosineSimx);
                result.add(y);
            }
        }
        Collections.sort(result);

        return result;
    }

    public static void main(String[] args) {
        Search s = new Search();
        System.out.println(s.search("peach banana coconut peach peach", true, 10));
    }
}
