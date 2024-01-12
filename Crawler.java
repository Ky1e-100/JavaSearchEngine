import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Crawler {
    String info;
    ArrayList<Files> files;
    HashMap<String, Integer> totalWordFreq;
    ArrayList<String> finished;
    ArrayList<String> seedsQ;
    ArrayList<ArrayList<Double>> adjMatrix;
    HashMap<String, ArrayList<String>> inLinks;
    HashMap<String, Double> idfValues;
    HashMap<String, Integer> fileIds;
    MathMult m = new MathMult();
    static int idCount = 0;

    public Crawler() {
        files = new ArrayList<>();
        totalWordFreq = new HashMap<>();
        finished = new ArrayList<>();
        seedsQ = new ArrayList<>();
        adjMatrix = new ArrayList<>();
        inLinks = new HashMap<>();
        idfValues = new HashMap<>();
        fileIds = new HashMap<>();
    }

    public void crawl(String seed) {
        seedsQ.add(seed);
        while (!seedsQ.isEmpty()) {
            seedsQ.remove(0);

            finished.add(seed);
            String temp = "";
            try {
                info = WebRequester.readURL(seed);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException f) {
                f.printStackTrace();
            }

            temp = info;
            // find title
            int titleStart = temp.indexOf("<title>") + 7;
            int titleEnd = temp.indexOf("</title>");
            files.add(new Files(temp.substring(titleStart, titleEnd)));
            files.get(idCount).setSeed(seed);
            fileIds.put(seed, files.get(idCount).getId());

            // get all words in body
            ArrayList<String> body = new ArrayList<>();
            int bod = temp.indexOf("<p>");
            int bodStart, bodEnd;
            String temp1;
            while (true) {
                if (bod == -1) {
                    break;
                }
                bodStart = bod + 3;
                bodEnd = temp.indexOf("</p>");
                temp1 = temp.substring(bodStart, bodEnd);
                String[] words = temp1.split("\n");
                for (String a : words) {
                    if (a.equals("")) {
                        continue;
                    }
                    body.add(a.replace("\n", ""));
                }
                temp = temp.substring(bodEnd + 3);
                bod = temp.indexOf("<p>");
            }
            files.get(idCount).setWords(body);
            files.get(idCount).wordFreq();

            temp = info;

            for (String word : files.get(idCount).getUniqueWords()) {
                if (totalWordFreq.containsKey(word)) {
                    totalWordFreq.put(word, totalWordFreq.get(word) + 1);
                } else {
                    totalWordFreq.put(word, 1);
                }
            }

            // Term freqs
            files.get(idCount).termFreq();

            // Get URLs
            ArrayList<String> urls = new ArrayList<>();
            int urlStart, urlEnd;
            int urlBeg = temp.indexOf("href=\"");
            String url;
            while (true) {
                if (urlBeg == -1) {
                    break;
                }
                urlStart = urlBeg + 6;
                temp = temp.substring(urlStart);
                urlEnd = temp.indexOf("\"");
                url = temp.substring(0, urlEnd);
                urls.add(url);
                temp = temp.substring(urlEnd);
                urlBeg = temp.indexOf("href=\"");

            }
            files.get(idCount).setUrls(urls);

            // Queeue for urls
            ArrayList<String> contentOut = new ArrayList<>();
            String link;
            String tempSeed = seed.substring(0, seed.lastIndexOf("/"));
            int totalOut = 0;

            for (String url1 : files.get(idCount).getUrls()) {
                if (finished.contains(url1) || seedsQ.contains(url1)) {
                    continue;
                }
                if (url1.startsWith("http://")) {
                    link = url1;
                    seedsQ.add(url1);
                    contentOut.add(link);
                    totalOut++;
                } else {
                    link = tempSeed + url1.substring(1);
                    seedsQ.add(link);
                    contentOut.add(link);
                    totalOut++;
                }

                if (!inLinks.containsKey(link)) {
                    inLinks.put(link, new ArrayList<String>());
                    inLinks.get(link).add(seed);
                } else if (inLinks.get(link).contains(seed)) {
                    continue;
                } else {
                    inLinks.get(link).add(seed);
                }
                files.get(idCount).setTotalLinksOut(totalOut);
                files.get(idCount).setUrls(contentOut);
            }

            for (String a : new ArrayList<String>(seedsQ)) {
                if (finished.contains(a)) {
                    seedsQ.remove(a);
                }

            }

            if (seedsQ.isEmpty()) {
                break;
            }

            if (seedsQ.get(0) == "") {
                seedsQ.remove(0);
                continue;
            }
            seed = seedsQ.get(0);

            idCount++;
        }

        //set incoming links
        for (String inSeed : inLinks.keySet()) {
            int idSeed = fileIds.get(inSeed);
            files.get(idSeed).setInLinks(inLinks.get(inSeed));
        }

        // idf values
        double idf;
        for (String word : totalWordFreq.keySet()) {
            idf = m.log2((idCount + 1.0) / (1.0 + totalWordFreq.get(word)));
            idfValues.put(word, idf);
        }

        files.get(idCount).setIdf(idfValues); // put it in the last file so it is able to be retreived later

        // TF and TFIDF VALUES
        double tf;
        double tfIdf;
        for (Files f : files) {

            HashMap<String, Double> tfs = new HashMap<>();
            HashMap<String, Double> tfIdfs = new HashMap<>();
            for (String words : f.getWordFreqs().keySet()) {
                double x = f.getWordFreqs().get(words);
                double y = f.getTotalWords();
                tf = x / y;
                tfIdf = m.log2(1 + tf) * idfValues.get(words);
                tfs.put(words, tf);
                tfIdfs.put(words, tfIdf);
                f.setTf(tfs);
                f.setTfIdf(tfIdfs);
            }
        }

        // Matrix (PageRank begins)

        for (int row = 0; row < files.size(); row++) {
            ArrayList<Double> rowValue = new ArrayList<>();
            for (int col = 0; col < files.size(); col++) {
                rowValue.add(0.0);
            }
            adjMatrix.add(rowValue);
        }

        int lineId = 0;
        // String tempSeed = seed.substring(0, seed.lastIndexOf("/"));
        // adjancency matrix
        for (Files file : files) {
            for (String url : file.getUrls()) {
                // url = tempSeed + url.substring(1);
                lineId = fileIds.get(url);

                ArrayList<Double> content = new ArrayList<>();
                content = adjMatrix.get(file.getId());
                content.set(lineId, 1.0);
                adjMatrix.set(file.getId(), content);
            }
        }

        for (int i = 0; i < adjMatrix.size(); i++) {
            int oneCount = 0;
            for (int j = 0; j < adjMatrix.get(i).size(); j++) {
                if (adjMatrix.get(i).get(j) == 1) {
                    oneCount++;
                }
            }

            for (int k = 0; k < adjMatrix.get(i).size(); k++) {
                ArrayList<Double> content = new ArrayList<>();
                content = adjMatrix.get(i);
                content.set(k, adjMatrix.get(i).get(k) / oneCount);
                adjMatrix.set(i, content);
            }
        }
        // scaled ajancency matrix
        double a = 0.1;
        adjMatrix = m.multScaler(adjMatrix, 1 - a);

        // adding alpha/n to each entry
        for (int row = 0; row < adjMatrix.size(); row++) {
            for (int col = 0; col < adjMatrix.get(row).size(); col++) {
                ArrayList<Double> temp = new ArrayList<>();
                temp = adjMatrix.get(row);
                temp.set(col, adjMatrix.get(row).get(col) + a / files.size());
                adjMatrix.set(row, temp);
            }
        }

        // multiplication of vectors

        ArrayList<Double> initVector = new ArrayList<>();
        for (int i = 0; i < files.size(); i++) {
            initVector.add(1.0 / files.size());
        }

        ArrayList<Double> result0 = new ArrayList<>();
        ArrayList<Double> result1 = new ArrayList<>();

        result0 = m.multiply(initVector, adjMatrix);
        result1 = m.multiply(result0, adjMatrix);

        ArrayList<Double> temp = new ArrayList<>();
        ArrayList<ArrayList<Double>> i = new ArrayList<>();
        ArrayList<ArrayList<Double>> o = new ArrayList<>();
        i.add(result0);
        o.add(result1);
        double eD = m.euclideanDist(i, o);
        double target = 0.0001;

        while (eD > target) {
            temp = m.multiply(result1, adjMatrix);
            ArrayList<ArrayList<Double>> p = new ArrayList<>();
            p.add(temp);
            eD = m.euclideanDist(o, p);
            result1 = temp;
            o.set(0, temp);
        }

        // temp is actually page rank
        // set each file's pageRank
        for (int f = 0; f < files.size(); f++) {
            files.get(f).setPageRank(temp.get(f));
        }

        // Make a list of all file names and put it into a file
        try {
            PrintWriter out;
            out = new PrintWriter(new FileWriter("FileNames" + File.separator + "filenames.txt"));

            for (Files file : files) {
                out.println(file.getId() + ".txt");
            }
            out.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }

        // Send files to a file with all of their data (storing objects)
        ObjectOutputStream out;
        for (Files file : files) {
            try {
                out = new ObjectOutputStream(new FileOutputStream("Files" + File.separator + (file.getId() + ".txt")));
                out.writeObject(file);
                out.close();

            } catch (FileNotFoundException e) {
                System.out.println("Error: Cannot open file for writing");
            } catch (IOException e) {
                System.out.println("Error: Cannot write to file");
            }
        }

        System.out.println("Finished Crawl");

    }
}
