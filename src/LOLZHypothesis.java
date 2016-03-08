/**
 * Created by markus on 21.02.2016.
 */
public class LOLZHypothesis extends OneMaxHypothesis {

    public LOLZHypothesis(int numberOfBits) {

        super(numberOfBits);
    }

    public LOLZHypothesis(int[] newGenotype) {

        super(newGenotype);
    }

    @Override
    public void calculateFitness() {
        int firstNumber = getPhenotype()[0];
        double tempFitness = 1.0;

        for (int i = 1; i < getPhenotype().length; i++) {
            if (firstNumber == 0 && i + 1 > Values.LOLZ_THRESHOLD){
                break;
            }
            if (getPhenotype()[i] == firstNumber){
                tempFitness += 1;
            }else{
                break;
            }
        }
        setFitness(tempFitness/getPhenotype().length);
    }

    @Override
    public AbstractHypothesis instantiateNewChileWithGenoType(int[] genotype) {

        return new LOLZHypothesis(genotype);
    }

    @Override
    public AbstractHypothesis instantiateNewChild() {
        return new LOLZHypothesis(this.genotype.length);
    }
}
