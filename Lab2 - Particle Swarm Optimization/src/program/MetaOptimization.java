package program;

import java.util.function.Function;

import static program.PSO.iterate;

/* This class was used during development to find best parameters for each test function */
public class MetaOptimization {
    private static double[] min;
    private static double[] max;
    private static int iterations;
    private static int particleCount;
    private static Function<double[], Double> testFunc;

    public static void init(double[] min, double[] max, int iterations,
                            int particleCount, Function<double[], Double> testFunc) {
        MetaOptimization.min = min;
        MetaOptimization.max = max;
        MetaOptimization.iterations = iterations;
        MetaOptimization.particleCount = particleCount;
        MetaOptimization.testFunc = testFunc;
    }

    public static void startTest(double iA, double iB, double iC, double jA, double jB, double jC,
                          double increment, double[] funcTrue) {

        double best = 0;
        for (; iA < jA; iA+=increment) {
            for (; iB < jB; iB+=increment) {
                for (; iC < jC; iC+=increment) {
                    PSO.State state = iterate(testFunc, iterations
                            , PSO.psoInit(min, max, new PSO.Parameters(iA, iB, iC), particleCount), false, false
                    );

                    double sum = 0.0;
                    for (double[] pos: state.pos){
                        sum += ((pos[0]/funcTrue[0]) + (pos[1]/funcTrue[1]))/2;
                    }
                    sum /= state.pos.length;

                    double percentRes = sum*100;
                    if (percentRes>best) {
                        best = percentRes;
                        System.out.printf("a:%.2f b:%.2f c:%.2f %f%%\n", iA, iB, iC, percentRes);
                    }
                }
            }
        }
    }
}
