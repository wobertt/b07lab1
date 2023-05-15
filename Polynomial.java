public class Polynomial {
    double[] coefficients;

    public Polynomial() {
        coefficients = new double[]{0};
    }

    public Polynomial(double[] coeff) {
        coefficients = coeff;
    }

    private double getCoeff(int i) {
        if (i >= coefficients.length)
            return 0;
        return coefficients[i];
    }

    public Polynomial add(Polynomial poly) {
        int size = Math.max(coefficients.length, poly.coefficients.length);
        Polynomial result = new Polynomial(new double[size]);

        for (int i = 0; i < size; i++) {
            result.coefficients[i] = getCoeff(i) + poly.getCoeff(i);
        }

        return result;
    }

    public double evaluate(double x) {
        double cur = 1, result = 0;
        for (int i = 0; i < coefficients.length; i++) {
            result += cur * getCoeff(i);
            cur *= x;
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}