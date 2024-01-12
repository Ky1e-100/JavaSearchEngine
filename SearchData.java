import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchData {

    ArrayList<Files> files;
    HashMap<String, Double> idfValues;

    public SearchData() {
        files = new ArrayList<>();

        ArrayList<String> fileNames = new ArrayList<>();
        idfValues = new HashMap<>();

        try {
            BufferedReader in;
            in = new BufferedReader(new FileReader("FileNames" + File.separator + "filenames.txt"));
            String name = in.readLine();
            while (name != null) {
                fileNames.add(name);
                name = in.readLine();
            }
            in.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot write to file");
        }

        try {
            for (String name : fileNames) {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("Files" + File.separator + name));
                Files file = (Files) in.readObject();
                files.add(file);
                in.close();
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Error: Object's class does not match");
        } catch (FileNotFoundException e) {
            System.out.println("Error: Cannot open file for writing");
        } catch (IOException e) {
            System.out.println("Error: Cannot read from file");
        }

        idfValues = files.get(files.size() - 1).getIdf();
    }

    public List<String> getOutgoingLinks(String url) {
        for (Files f : files) {
            if (f.getSeed().equals(url)) {
                return f.getUrls();
            }
        }
        return null;
    }

    public List<String> getIncomingLinks(String url) {
        for (Files f : files) {
            if (f.getSeed().equals(url)) {
                return f.getInlinks();
            }
        }
        return null;
    }

    public double getPageRank(String url) {
        for (Files f : files) {
            if (f.getSeed().equals(url)) {
                return f.getPageRank();
            }
        }
        return -1;
    }

    public double getIDF(String word) {
        if (idfValues.keySet().contains(word)) {
            return idfValues.get(word);
        }
        return 0;
    }

    public double getTF(String url, String word) {
        for (Files f : files) {
            if (f.getSeed().equals(url)) {
                if (f.getTf().containsKey(word)) {
                    return f.getTf().get(word);
                }
                break;
            }
        }
        return 0;
    }

    public double getTFIDF(String url, String word) {
        for (Files f : files) {
            if (f.getSeed().equals(url)) {
                if (f.getTfIdf().containsKey(word)) {
                    return f.getTfIdf().get(word);
                }
                break;
            }
        }
        return 0;
    }

    public ArrayList<Files> getFiles() {
        return files;
    }

}
