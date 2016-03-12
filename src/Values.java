import enums.EAdultSelection;
import enums.EParentSelection;
import enums.EPoblemSelection;
import enums.ESurprisingSequenceMode;

/**
 * Created by markus on 18.02.2016.
 */
public class Values {

    public static final int BOARD_SIZE = 15;
    public static int GENERATION_PRINT_THROTTLE = 1;
    public static boolean UPDATE_CHARTS = true;

    public static int[] RANDOM_ONEMAX;

    public static EPoblemSelection SELECTED_PROBLEM = EPoblemSelection.ONEMAX;

    public static int TOURNAMENT_SELECTION_GROUP_SIZE = 5;
    public static double TOURNAMENT_SELECTION_EPSILON = 0.5;

    public static double CROSSOVER_PROBABILITY = 0.8;
    public static double MUTATION_PROBABILITY = 0.01;


    public static int POPULATION_SIZE = 60;
    public static int MAX_ADULT_SIZE = POPULATION_SIZE / 2;
    public static int MAX_PARENT_SIZE = POPULATION_SIZE / 2;
    public static int NUMBER_OF_ELITES = (int) (POPULATION_SIZE * 0.05);

    public static EAdultSelection ADULT_SELECTION = EAdultSelection.GENERATION_MIXING;
    public static EParentSelection PARENT_SELECTION = EParentSelection.SIGMA_SCALING;


    public static int NUMBER_OF_BITS_IN_PROBLEM = 10;

    public static int LOLZ_THRESHOLD = 21;

    public static int SURPRISING_SYMBOL_SIZE= 3;

    public static ESurprisingSequenceMode SURPRISING_MODE = ESurprisingSequenceMode.Global;

}
