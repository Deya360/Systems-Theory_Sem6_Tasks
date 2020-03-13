package program;

public class TestsFuncs {
    //https://www.sfu.ca/~ssurjano/mccorm.html
    //McCormick function
    static double mccormick(double[] x) {
        double a = x[0];
        double b = x[1];
        return Math.sin(a + b) + (a - b) * (a - b) + 1.0 + 2.5 * b - 1.5 * a;
    }


    //https://www.sfu.ca/~ssurjano/schwef.html
    //SCHWEFEL FUNCTION
    static double schwefel(double[] x) {
        int d = x.length;
        double sum = 0.0;
        for (int i = 1; i < d; ++i) {
            double j = x[i - 1];
            sum += j * Math.sin(Math.sqrt(Math.abs(j)));
        }
        return (418.9829*d)-sum;
    }

    //https://www.sfu.ca/~ssurjano/michal.html
    //MICHALEWICZ FUNCTION
    static double michalewicz(double[] x) {
        int m = 10;
        int d = x.length;
        double sum = 0.0;
        for (int i = 1; i < d; ++i) {
            double j = x[i - 1];
            double k = Math.sin(i * j * j / Math.PI);
            sum += Math.sin(j) * Math.pow(k, 2.0 * m);
        }
        return -sum;
    }
}
