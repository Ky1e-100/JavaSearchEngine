public class SearchResult implements Comparable<SearchResult>{
    String title;
    double score;
    MathMult m = new MathMult();
    
    public SearchResult(String title, double score) {
        this.title = title;
        this.score = score;
    }

    public String getTitle() {
        return title;
    }

    public double getScore() {
        return score;
    }

    public int compareTo(SearchResult a) {
        if (m.roundThree(a.getScore()) != m.roundThree(this.getScore())) {
            return Double.compare(a.getScore(), this.getScore());
        }
        return this.getTitle().compareTo(a.getTitle());
        
    }
    
    public String toString() {
        return "Title: " + title + " Score: " + score;
    }

}
