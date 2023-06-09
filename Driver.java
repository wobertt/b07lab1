import java.io.File;

public class Driver {
    private static void testNull() throws Exception {
        Polynomial p = new Polynomial();
        Polynomial q = new Polynomial();
        Polynomial f = new Polynomial(new double[]{1, 2}, new int[]{1, 0});
        Polynomial g = new Polynomial(new double[]{5, 2, 4, 5, 2, 3, 2}, new int[]{6, 2, 5, 4, 1, 2, 0});
        System.out.println(g.getString());
        System.out.println(p.getString());
        System.out.println(p.add(q).getString());
        System.out.println(p.add(f).getString());
        System.out.println(f.add(p).getString());
        System.out.println(p.evaluate(-10.1));
        System.out.println(p.hasRoot(0));
        System.out.println(p.hasRoot(1));
        System.out.println(p.multiply(q).getString());
        System.out.println(p.multiply(f).getString());
        System.out.println(f.multiply(p).getString());

        p.sortByExp();
        f.sortByExp();
        p.saveToFile("p_file");
        f.saveToFile("f_file");
    }

    private static void testFiles() throws Exception {
        for (int i = 1; i <= 5; i++) {
            File f = new File("Test Files/t"+i+".txt");
            Polynomial p = new Polynomial(f);
            System.out.println(p.getString());
            p.saveToFile(i+".txt");
        }
    }

    private static void testCalculations() {
        Polynomial p1 = new Polynomial(new double[]{1, 2, 4}, new int[]{1, 2, 0});
        System.out.println(p1.getString());
        for (int i = 0; i < 2; i++) {
            p1 = p1.multiply(p1);
        }
        System.out.println(p1.getString());
    }
    public static void main(String[] args) throws Exception {
        testNull();
        testFiles();
        testCalculations();
    }
}