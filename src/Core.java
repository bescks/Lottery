import javax.swing.*;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Core {

    private HashSet<Integer> input = new HashSet<Integer>();
    private int[] num = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private String[] numS = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11"};

    Core() {
    }

    String calculate(int in1, int in2, int in3) {
        input.add(in1);
        input.add(in2);
        input.add(in3);
        int i = 0;
        StringBuilder sb = new StringBuilder();
        for (int n1 = 0; n1 < 7; n1++) {
            int e1 = input.contains(n1 + 1) ? 1 : 0;
            for (int n2 = n1 + 1; n2 < 8; n2++) {
                int e2 = input.contains(n2 + 1) ? 1 : 0;
                for (int n3 = n2 + 1; n3 < 9; n3++) {
                    int e3 = input.contains(n3 + 1) ? 1 : 0;
                    for (int n4 = n3 + 1; n4 < 10; n4++) {
                        int e4 = input.contains(n4 + 1) ? 1 : 0;
                        for (int n5 = n4 + 1; n5 < 11; n5++) {
                            int e5 = input.contains(n5 + 1) ? 1 : 0;
                            if (e1 + e2 + e3 + e4 + e5 > 1) {
                                i++;
//                                System.out.println(numS[n1] + " " + numS[n2] + " " + numS[n3] + " " + numS[n4] + " " + numS[n5]);
                                sb.append(numS[n1]).append(" ").append(numS[n2]).append(" ").append(numS[n3]).append(" ").append(numS[n4]).append(" ").append(numS[n5]).append("\n");

                            }
                        }
                    }
                }

            }

        }
        return sb.toString();

    }
}
