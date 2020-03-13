package program;

import java.util.Arrays;

public class Debug {
    static void print(int i, PSO.State s) {
        System.out.println("========= Iter: " + (i+1) + " ==========");

        for (int i1 = 0; i1 < s.pos.length; i1++) {
            System.out.println("======= Particle: " + (i1+1) + " ========");

            System.out.printf("curValue: \t\t%.5f\n", s.bval[i1]);
            System.out.printf("curPosition: \t%s\n",  Arrays.toString(s.pos[i1]));
            System.out.printf("curVelocity: \t%s\n", Arrays.toString(s.vel[i1]));
            System.out.printf("bestPosition: \t%s\n", Arrays.toString(s.bpos[i1]));

            System.out.print("\n");
        }

        System.out.printf("globalVal: \t\t%.5f\n", s.gbval);
        System.out.printf("globalPos: \t\t%s\n", Arrays.toString(s.gbpos));

        System.out.print("\n");
    }
}
