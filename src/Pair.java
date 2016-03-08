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
    public void setElement1(L element1){ this.element1 = element1; }
    public void setElement2(R element2){ this.element2 = element2; }
}
