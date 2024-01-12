import java.util.ArrayList;

public class MathMult {
    
    public ArrayList<ArrayList<Double>> multScaler(ArrayList<ArrayList<Double>> a, double x) {
        ArrayList<ArrayList<Double>> newValue = new ArrayList<>();
        newValue = a;

        for (int i = 0; i < newValue.size(); i++) {
            for (int j = 0; j < newValue.get(i).size(); j++) {
                ArrayList<Double> temp = new ArrayList<>();
                temp = newValue.get(i);
                temp.set(j, newValue.get(i).get(j) * x);
                newValue.set(i, temp);
            }
        }

        return newValue;
    }
    
    public ArrayList<Double> multiply (ArrayList<Double> a, ArrayList<ArrayList<Double>> b) {
        ArrayList<Double> result = new ArrayList<>();
        for (int i = 0; i < b.get(0).size(); i++) {
            double total = 0;
            for (int j = 0; j< a.size(); j++) {
                total += a.get(j) * b.get(j).get(i);
            }
            result.add(total);
        }

        return result;
    }

    public double euclideanDist(ArrayList<ArrayList<Double>> a, ArrayList<ArrayList<Double>> b) {
        double distance = 0;
        ArrayList<Double> sums = new ArrayList<>();

        for (int i = 0; i < a.size(); i++) {
            for (int j = 0; j < a.get(0).size(); j++) {
                double sum = Math.pow((a.get(i).get(j) - b.get(i).get(j)), 2);
                sums.add(sum);
            }
        }

        double tot = 0;

        for (double k : sums) {
            tot += k;
        }
        distance += Math.sqrt(tot);
        return distance;
    }

    public double log2(double x) {
        return (Math.log(x) / Math.log(2));
    }
    
    public double roundThree(double x) {
        return (double)Math.round(x * 1000d) / 1000d;
    }

    // public static void main(String[] args) {
    //     MathMult m = new MathMult();
    //     ArrayList<ArrayList<Double>> a = new ArrayList<>();
    //     ArrayList<ArrayList<Double>> b = new ArrayList<>();

    //     ArrayList<Double> x = new ArrayList<>();
    //     ArrayList<Double> y = new ArrayList<>();

    //     x.add(8.0);
    //     x.add(3.0);
    //     x.add(6.0);
    //     x.add(5.0);
    //     x.add(4.0);

    //     y.add(2.0);
    //     y.add(5.0);
    //     y.add(10.0);
    //     y.add(9.0);
    //     y.add(7.0);

    //     a.add(x);
    //     b.add(y);

    //     System.out.println(m.euclideanDist(a, b));
    // }

}
