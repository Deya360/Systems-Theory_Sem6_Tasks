package program;

import java.awt.*;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

/*
Algorithm Steps:
Step 1. Initialization
For each particle 𝑖 = 1, . . . , 𝑁𝑃, do
    (a) Initialize the particle’s position with a uniformly distribution as 𝑃𝑖(0)∼U(LB, UB), where LB and UB represent the lower
        and upper bounds of the search space
    (b) Initialize 𝑝𝑏𝑒𝑠𝑡 to its initial position: 𝑝𝑏𝑒𝑠𝑡(𝑖, 0) = 𝑃𝑖(0).
    (c) Initialize 𝑔𝑏𝑒𝑠𝑡 to the minimal value of the swarm: 𝑔𝑏𝑒𝑠𝑡(0) = argmin𝑓[𝑃𝑖(0)].
    (d) Initialize velocity: 𝑉𝑖 ∼ 𝑈(−|UB − LB|, |UB − LB|).

Step 2. Repeat until a termination criteria is met
For each particle 𝑖 = 1, . . . , 𝑁𝑃, do
    (a) Pick random numbers: 𝑟1, 𝑟2 ∼ 𝑈(0, 1).
    (b) Update particle’s velocity. See formula (2).
    (c) Update particle’s position. See formula (3).
    (d) If 𝑓[𝑃𝑖(𝑡)] < 𝑓[𝑝𝑏𝑒𝑠𝑡(𝑖, 𝑡)], do
        (i) Update the best known position of particle 𝑖: 𝑝𝑏𝑒𝑠𝑡(𝑖, 𝑡) = 𝑃𝑖(𝑡).
        (ii) If 𝑓[𝑃𝑖(𝑡)] < 𝑓[𝑔𝑏𝑒𝑠𝑡(𝑡)], update the swarm’s best known position: 𝑔𝑏𝑒𝑠𝑡(𝑡) = 𝑃𝑖(𝑡).
    (e) 𝑡 ← (𝑡 + 1);

Step 3. Output 𝑔𝑏𝑒𝑠𝑡(𝑡) that holds the best found solution.
* */

/* Code borrowed from: https://rosettacode.org/wiki/Particle_Swarm_Optimization */

public class PSO {
    static Viewer viewer = new Viewer();
    static Plot plot = Plot.plot(null);
    static double[] trueVal;

    static class Parameters {
        double omega;   //inertia
        double phip;    //acc c1
        double phig;    //acc c2

        Parameters(double omega, double phip, double phig) {
            this.omega = omega;
            this.phip = phip;
            this.phig = phig;
        }
    }

    static class State {
        int iter;
        double[] gbpos;         //Global Pos
        double gbval;           //Global value
        double[] min;
        double[] max;
        Parameters parameters;
        double[][] pos;         //position
        double[][] vel;         //velocity
        double[][] bpos;        //best pos?
        double[] bval;          //best value?
        int nParticles;         //particle count
        int nDims;              //dimensions?

        State(int iter, double[] gbpos, double gbval, double[] min, double[] max, Parameters parameters,
              double[][] pos, double[][] vel, double[][] bpos, double[] bval, int nParticles, int nDims) {

            this.iter = iter;
            this.gbpos = gbpos;
            this.gbval = gbval;
            this.min = min;
            this.max = max;
            this.parameters = parameters;
            this.pos = pos;
            this.vel = vel;
            this.bpos = bpos;
            this.bval = bval;
            this.nParticles = nParticles;
            this.nDims = nDims;
        }

        void report(String testfunc) {
            System.out.printf("Test Function        : %s\n", testfunc);
            System.out.printf("Iterations           : %d\n", iter);
            System.out.printf("PSO Best: \t%s: \t%.15f\n", Arrays.toString(gbpos), gbval);
        }
    }

    static State psoInit(double[] min, double[] max, Parameters parameters, int nParticles) {
        int nDims = min.length;
        double[][] pos = new double[nParticles][];
        for (int i = 0; i < nParticles; ++i) {
            pos[i] = min.clone();
        }
        double[][] vel = new double[nParticles][nDims];
        double[][] bpos = new double[nParticles][];
        for (int i = 0; i < nParticles; ++i) {
            bpos[i] = min.clone();
        }
        double[] bval = new double[nParticles];
        for (int i = 0; i < bval.length; ++i) {
            bval[i] = Double.POSITIVE_INFINITY;
        }
        int iter = 0;
        double[] gbpos = new double[nDims];
        for (int i = 0; i < gbpos.length; ++i) {
            gbpos[i] = Double.POSITIVE_INFINITY;
        }
        double gbval = Double.POSITIVE_INFINITY;
        return new State(iter, gbpos, gbval, min, max, parameters, pos, vel, bpos, bval, nParticles, nDims);
    }

    private static Random r = new Random();

    private static State pso(Function<double[], Double> fn, State y) {
        Parameters p = y.parameters;
        double[] v = new double[y.nParticles];
        double[][] bpos = new double[y.nParticles][];
        for (int i = 0; i < y.nParticles; ++i) {
            bpos[i] = y.min.clone();
        }
        double[] bval = new double[y.nParticles];
        double[] gbpos = new double[y.nDims];
        double gbval = Double.POSITIVE_INFINITY;
        for (int j = 0; j < y.nParticles; ++j) {
            // evaluate
            v[j] = fn.apply(y.pos[j]);
            // update
            if (v[j] < y.bval[j]) {
                bpos[j] = y.pos[j];
                bval[j] = v[j];
            } else {
                bpos[j] = y.bpos[j];
                bval[j] = y.bval[j];
            }
            if (bval[j] < gbval) {
                gbval = bval[j];
                gbpos = bpos[j];
            }
        }
        double rg = r.nextDouble();
        double[][] pos = new double[y.nParticles][y.nDims];
        double[][] vel = new double[y.nParticles][y.nDims];
        for (int j = 0; j < y.nParticles; ++j) {
            // migrate
            double rp = r.nextDouble();
            boolean ok = true;
            Arrays.fill(vel[j], 0.0);
            Arrays.fill(pos[j], 0.0);
            for (int k = 0; k < y.nDims; ++k) {
                vel[j][k] = p.omega * y.vel[j][k] +
                        p.phip * rp * (bpos[j][k] - y.pos[j][k]) +
                        p.phig * rg * (gbpos[k] - y.pos[j][k]);
                pos[j][k] = y.pos[j][k] + vel[j][k];
                ok = ok && y.min[k] < pos[j][k] && y.max[k] > pos[j][k];
            }
            if (!ok) {
                for (int k = 0; k < y.nDims; ++k) {
                    pos[j][k] = y.min[k] + (y.max[k] - y.min[k]) * r.nextDouble();
                }
            }
        }
        int iter = 1 + y.iter;
        return new State(
                iter, gbpos, gbval, y.min, y.max, y.parameters,
                pos, vel, bpos, bval, y.nParticles, y.nDims
        );
    }

    static State iterate(Function<double[], Double> fn, int n, State y, boolean graph, boolean debug) {
        State r = y;
        if (n == Integer.MAX_VALUE) {
            State old = y;
            while (true) {
                r = pso(fn, r);
                if (Objects.equals(r, old)) break;
                old = r;
            }
        } else {
            Plot.DataSeriesOptions opts = Plot.seriesOpts().marker(Plot.Marker.CIRCLE).markerColor(Color.GREEN).color(Color.BLACK);

            for (int i = 0; i < n; ++i) {
                r = pso(fn, r);

                //Etc.
                if (graph) {
                    initPlotter(y.max, y.min);

                    for (int i1 = 0; i1 < r.pos.length; i1++) {
                        plot.series(String.valueOf(i1), Plot.data().xy(r.pos[i1][0], r.pos[i1][1]), opts);
                    }
                    viewer.addImage(plot.draw());
                }

                if (debug) Debug.print(i, r);
            }
        }
        return r;
    }

    private static void initPlotter(double[] max , double[] min) {
        plot = Plot.plot(null);

        plot.xAxis("x", Plot.axisOpts().range(min[0], max[1]))
                .yAxis("y", Plot.axisOpts().range(min[1], max[1]));

        //Add true value
        plot.series("True", Plot.data().xy(trueVal[0], trueVal[1]),
                Plot.seriesOpts().marker(Plot.Marker.DIAMOND).markerColor(Color.CYAN));
    }
}