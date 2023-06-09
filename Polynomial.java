import java.io.File;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.io.PrintStream;

public class Polynomial {
    double[] coeff;
    int[] exp;

    public Polynomial() {
    }

    public Polynomial(double[] coeff, int[] exp) {
        this.coeff = new double[exp.length];
        this.exp = new int[exp.length];
        for (int i = 0; i < exp.length; i++) {
            this.coeff[i] = coeff[i];
            this.exp[i] = exp[i];
        }
    }

    public Polynomial(File f) throws Exception {
        String polyString;
        try {
            Scanner sc = new Scanner(f);
            polyString = sc.nextLine();
            sc.close();
        } catch (NoSuchElementException e) {
            return;
        }
        if (polyString.isEmpty()) return;
        
        String[] terms = polyString.split("\\+|-");
        if (terms.length == 0) {
            coeff = null;
            exp = null;
            return;
        }
        coeff = new double[terms.length];
        exp = new int[terms.length];

        int[] signs = new int[terms.length];
        int idx = 0;
        if (polyString.charAt(0) != '-') {
            signs[0] = 1;
            idx++;
        }
        for (char c : polyString.toCharArray()) {
            if (c != '+' && c != '-') continue;
            if (c == '+') signs[idx] = 1;
            else signs[idx] = -1;
            idx++;
        }
        
        for (int i = 0; i < terms.length; i++) {
            if (terms[i].isEmpty()) continue;
            if (terms[i].equals("x")) {
                exp[i] = 1;
                coeff[i] = 1;
                continue;
            }
            String[] nums = terms[i].split("x");
            if (nums.length == 1) 
                exp[i] = terms[i].charAt(terms[i].length()-1) == 'x' ? 1 : 0;
            else 
                exp[i] = Integer.parseInt(nums[1]);

            coeff[i] = Double.parseDouble(nums[0]) * signs[i];
        }
        this.removeZeros();
        this.sortByExp();
    }

    private void removeZeros() {
        int nonzero_count = 0;
        for (double c : coeff) nonzero_count += c != 0 ? 1 : 0;
        double[] result_c = new double[nonzero_count];
        int[] result_e = new int[nonzero_count];
        for (int i = 0, result_idx = 0; i < coeff.length; i++) {
            if (coeff[i] != 0) {
                result_c[result_idx] = coeff[i];
                result_e[result_idx] = exp[i];
                result_idx++;
            }
        }
        coeff = result_c;
        exp = result_e;
    }

    public Polynomial add(Polynomial poly) {
        if (poly == null || poly.exp == null) return new Polynomial(coeff, exp);
        if (exp == null) return new Polynomial(poly.coeff, poly.exp);

        int max_exp = -1;
        for (int e : poly.exp) max_exp = Math.max(max_exp, e);
        for (int e : exp) max_exp = Math.max(max_exp, e);

        double[] tmp = new double[max_exp+1];
        for (int i = 0; i < coeff.length; i++) tmp[exp[i]] += coeff[i];
        for (int i = 0; i < poly.coeff.length; i++) tmp[poly.exp[i]] += poly.coeff[i];

        int[] tmp_e = new int[max_exp+1];
        for (int i = 1; i <= max_exp; i++) tmp_e[i] = i;
        Polynomial result = new Polynomial(tmp, tmp_e);
        result.removeZeros();
        return result;
    }

    public double evaluate(double x) {
        if (exp == null) return 0;
        double result = 0;
        for (int i = 0; i < coeff.length; i++) {
            result += coeff[i] * Math.pow(x, exp[i]);
        }
        return result;
    }

    public boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < 1e-9;
    }

    public Polynomial simpleMultiply(double a, int e) {
        // multiply by ax^e
        if (a == 0 || exp == null) return new Polynomial();
        Polynomial result = new Polynomial(coeff, exp);
        for (int i = 0; i < result.exp.length; i++) {
            result.coeff[i] *= a;
            result.exp[i] += e;
        }
        return result;
    }

    public Polynomial multiply(Polynomial poly) {
        if (poly.exp == null || exp == null) return new Polynomial();
        Polynomial result = new Polynomial();
        for (int i = 0; i < poly.exp.length; i++) {
            result = result.add(this.simpleMultiply(poly.coeff[i], poly.exp[i]));
        }
        result.sortByExp();
        return result;
    }

    public void sortByExp() {
        if (exp == null) return;
        for (int i = 0; i < exp.length; i++) {
            for (int j = i+1; j < exp.length; j++) {
                if (exp[i] > exp[j]) {
                    int tmpe = exp[i];
                    exp[i] = exp[j];
                    exp[j] = tmpe;
                    double tmpc = coeff[i];
                    coeff[i] = coeff[j];
                    coeff[j] = tmpc;
                }
            }
        }
    }

    public void saveToFile(String filename) throws Exception {
        PrintStream output = new PrintStream(filename);
        output.print(this.getString());
        output.close();
    }

    public String getString() {
        if (exp == null) return "0";
        this.sortByExp();
        String s = "";
        for (int i = 0; i < exp.length; i++) {
            if (coeff[i] > 0 && i > 0) s += "+";
            s += coeff[i];
            if (exp[i] >= 1) s += "x";
            if (exp[i] > 1) s += exp[i];
        }
        return s.length() > 0 ? s : "0";
    }
}
