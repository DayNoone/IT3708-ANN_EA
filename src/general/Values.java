package general;

import enums.EAdultSelection;
import enums.EParentSelection;
import enums.EProblemSelection;
import project3.Board;
import project4.BeerWorld;

/**
 * Created by markus on 18.02.2016.
 */
public class Values {

    /**

        EA VALUES

     **/

    public static EProblemSelection SELECTED_PROBLEM = EProblemSelection.FLATLAND;

    public static int TOURNAMENT_SELECTION_GROUP_SIZE = 5;
    public static double TOURNAMENT_SELECTION_EPSILON = 0.9;

    public static double CROSSOVER_PROBABILITY = 0.8;
    public static double MUTATION_PROBABILITY = 0.01;


    public static int POPULATION_SIZE = 200;
    public static int MAX_ADULT_SIZE = POPULATION_SIZE / 2;
    public static int MAX_PARENT_SIZE = POPULATION_SIZE / 2;
    public static int NUMBER_OF_ELITES = (int) (POPULATION_SIZE * 0.05);

    public static EAdultSelection ADULT_SELECTION = EAdultSelection.GENERATION_MIXING;
    public static EParentSelection PARENT_SELECTION = EParentSelection.TOURNAMENT_SELECTION;


    public static int NUMBER_OF_BITS_IN_PROBLEM = 10;

    //GUI
    public static int GENERATION_PRINT_THROTTLE = 1;
    public static boolean UPDATE_CHARTS = true;

    /**

        general.ANN VALUES

     **/

    public static int ANN_INPUT_NODES = 6;
    public static int ANN_OUTPUT_NODES = 3;

    public static int[] ANN_NODES_IN_HIDDEN_LAYERS = new int[]{5, 5};

    public static int[] CTRNN_NODES_IN_HIDDEN_LAYERS = new int[]{2};
    public static int CTRNN_OUTPUT_NODES = 2;
    public static int CTRNN_INPUT_NODES = 5;

    public static boolean ANN_USE_BIAS_NODES = true;

    /**

        FLATLAND VALUES

     **/

    public static final int FLATLAND_BOARD_SIZE = 10;
    public static final int FLATLAND_GENOTYPE_RANGE = 10; // 0 - range
    public static final int FLATLAND_ITERATIONS = 60;
    public static int FLATLAND_SLEEP_DURATION = 20;


    public static final boolean FLATLAND_DIFFERENT_SCENARIOS = false;
    public static final int FLATLAND_NUMBER_OF_DIFFERENT_SCENARIOS = 5;
    public static boolean FLATLAND_DYNAMIC = false;

    public static final int FLATLAND_MAX_FOOD_COUNT = (Values.FLATLAND_BOARD_SIZE * Values.FLATLAND_BOARD_SIZE) / 3;

    public static final int POISON_PENALTY = 5;



    public static Board BOARD;
    public static Board[] BOARDS;
    public static general.ANN ANN;
    public static project4.CTRNN CTRNN;


    /**

        BEERWORLD VALUES

     **/

    public static boolean DRAW_MOVEMENT = true;

    public static BeerWorld BEERWORLD;



    public static boolean BEERWORLD_NO_WRAP = false;
    public static boolean BEERWORLD_PULL = false;

    public static final int BEERWORLD_ITERATIONS = 600;

    public static final int BEERWORLD_GENOTYPE_RANGE = 20;

    public static final int BEERWORLD_BOARD_HEIGHT = 15;
    public static final int BEERWORLD_BOARD_WIDTH = 30;

    public static int BEERWORLD_FAILEDAVOID_PENALTY = 3;
    public static int BEERWORLD_FAILEDCAPTURE_PENALTY = 3;
    public static int BEERWORLD_CAPTURE_PRIZE = 4;
    public static int BEERWORLD_AVOID_PRIZE = 3;
    public static int BEERWORLD_PULLED_CAPTURE = 8;
    public static int BEERWORLD_PULLED_AVOID = 3;
    public static int BEERWORLD_PULLED_FAILEDCAPTURE = 8;
    public static int BEERWORLD_PULLED_FAILEDAVOID = 20;

    public static int BEERWORLD_WRAP_FAILEDAVOID_PENALTY = 3;
    public static int BEERWORLD_WRAP_FAILEDCAPTURE_PENALTY = 3;
    public static int BEERWORLD_WRAP_CAPTURE_PRIZE = 4;
    public static int BEERWORLD_WRAP_AVOID_PRIZE = 3;

    public static int BEERWORLD_PULL_FAILEDAVOID_PENALTY = 3;
    public static int BEERWORLD_PULL_FAILEDCAPTURE_PENALTY = 3;
    public static final int BEERWORLD_PULL_CAPTURE_PRIZE = 4;
    public static final int BEERWORLD_PULL_AVOID_PRIZE = 3;
    public static final int BEERWORLD_PULL_PULLED_CAPTURE = 8;
    public static final int BEERWORLD_PULL_PULLED_AVOID = 3;
    public static final int BEERWORLD_PULL_PULLED_FAILEDCAPTURE = 8;
    public static final int BEERWORLD_PULL_PULLED_FAILEDAVOID = 20;

    public static int BEERWORLD_NOWRAP_FAILEDAVOID_PENALTY = 2;
    public static int BEERWORLD_NOWRAP_FAILEDCAPTURE_PENALTY = 1;
    public static final int BEERWORLD_NOWRAP_CAPTURE_PRIZE = 4;
    public static final int BEERWORLD_NOWRAP_AVOID_PRIZE = 6;

    public static boolean BEERWORLD_PULLED_OBJECT = false;



    /**

     MTSP VALUES

     **/

    public static int MTSP_NUMBER_OF_CITIES = 48;
    public static int[][] MTSP_COSTS;
    public static int[][] MTSP_DISTANCES;

    public static final boolean MTSP_MULTIPLE_PARETO_LINES_PLOT = false;
}
