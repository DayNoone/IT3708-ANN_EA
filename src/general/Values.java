package general;

import enums.EAdultSelection;
import enums.EParentSelection;
import enums.EProblemSelection;
import project3.Board;
import project4.CTRNN;
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
    public static double TOURNAMENT_SELECTION_EPSILON = 0.5;

    public static double CROSSOVER_PROBABILITY = 0.8;
    public static double MUTATION_PROBABILITY = 0.01;


    public static int POPULATION_SIZE = 60;
    public static int MAX_ADULT_SIZE = POPULATION_SIZE / 2;
    public static int MAX_PARENT_SIZE = POPULATION_SIZE / 2;
    public static int NUMBER_OF_ELITES = (int) (POPULATION_SIZE * 0.1);

    public static EAdultSelection ADULT_SELECTION = EAdultSelection.GENERATION_MIXING;
    public static EParentSelection PARENT_SELECTION = EParentSelection.SIGMA_SCALING;


    public static int NUMBER_OF_BITS_IN_PROBLEM = 10;

    //GUI
    public static int GENERATION_PRINT_THROTTLE = 1;
    public static boolean UPDATE_CHARTS = true;

    /**

        general.ANN VALUES

     **/

    public static int ANN_INPUT_NODES = 6;
    public static int ANN_OUTPUT_NODES = 3;

    public static int[] ANN_NODES_IN_HIDDEN_LAYERS = new int[]{2};

    /**

        FLATLAND VALUES

     **/

    public static final int FLATLAND_BOARD_SIZE = 10;
    public static final int FLATLAND_GENOTYPE_RANGE = 10; // 0 - range
    public static final int FLATLAND_ITERATIONS = 60;
    public static int FLATLAND_SLEEP_DURATION = 20;

    public static boolean FLATLAND_DYNAMIC = false;

    public static final int FLATLAND_MAX_FOOD_COUNT = (Values.FLATLAND_BOARD_SIZE * Values.FLATLAND_BOARD_SIZE) / 3;

    public static final int POISON_PENALTY = 3;



    public static Board BOARD;
    public static general.ANN ANN;
    public static project4.CTRNN CTRNN;


    /**

        BEERWORLD VALUES

     **/

    public static BeerWorld BEERWORLD;

    public static final int BEERWORLD_ITERATIONS = 600;

    public static final int BEERWORLD_GENOTYPE_RANGE = 10;

    public static final int BEERWORLD_BOARD_HEIGHT = 15;
    public static final int BEERWORLD_BOARD_WIDTH = 30;
    public static int BEERWORLD_FAILEDAVOID_PENALTY = 4;
    public static int BEERWORLD_FAILEDCAPTURE_PENALTY = 2;
}
