package program;

public class Main {
    public static void main(String[] args) {
        PSO.trueVal = new double[]{-0.54719, -1.54719};
        PSO.State state1 = PSO.iterate(TestsFuncs::mccormick, 40,
                PSO.psoInit(new double[]{-1.5, -3.0}, new double[]{4.0, 4.0}, new PSO.Parameters(0.0, 0.6, 0.3), 100),
                true, false);

        state1.report("McCormick");
        System.out.printf("True: \t\t[%f, %f]: \t%.15f\n",
                PSO.trueVal[0], PSO.trueVal[1], TestsFuncs.mccormick(PSO.trueVal));


//        PSO.trueVal = new double[]{2.20, 1.57};
//        PSO.State state2 = PSO.iterate(TestsFuncs::michalewicz, 30,
//                PSO.psoInit(new double[]{0.0, 0.0}, new double[]{Math.PI, Math.PI}, new PSO.Parameters(0.3, 3.0, 0.3), 1000),
//                true, false);
//
//        state2.report("Michalewicz (2D)");
//        System.out.printf("True: \t\t[%f, %f]: \t%.15f\n",
//                PSO.trueVal[0], PSO.trueVal[1], TestsFuncs.michalewicz(PSO.trueVal));


//        PSO.trueVal = new double[]{417.719376749399, 427.67593780463};
//        PSO.State state3 = PSO.iterate(TestsFuncs::schwefel      //Test Function
//                , 40                                //Iterations
//                , PSO.psoInit(
//                        new double[]{-500, -500},       //MIN
//                        new double[]{500, 500},         //MAX
//                        new PSO.Parameters(0, -3, 1.1),
//                        15                              //Particle Count
//                ), true, false
//        );
//
//        state3.report("Schwefel");
//        System.out.printf("True: \t\t[%f, %f]: \t%.15f\n",
//                PSO.trueVal[0], PSO.trueVal[1], TestsFuncs.schwefel(PSO.trueVal));

        PSO.viewer.run(false, 50); //Start graph viewer
    }

    static void metaOpt() {
        MetaOptimization.init(new double[]{-500, -500}, new double[]{500, 500}, 40, 300, TestsFuncs::schwefel);
        MetaOptimization.startTest(3f, 3f, 3f, -3f, -3f, -3f, 0.1f,
                new double[]{417.719376749399, 427.67593780463});
    }
}
