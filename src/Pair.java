/**
 * Created by markus on 18.02.2016.
 */
public class Pair<L, R> {
    private L element1;
    private R element2;
    public Pair(L l, R r){
        this.element1 = l;
        this.element2 = r;
    }
    public L getElement1(){ return element1; }
    public R getElement2(){ return element2; }
}
