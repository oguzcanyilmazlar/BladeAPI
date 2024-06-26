package me.acablade.bladeapi.objects;

import java.util.ArrayList;
import java.util.Collection;

public class NoDuplicateArrayList<T> extends ArrayList<T> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3493293444393599252L;

	public NoDuplicateArrayList(Collection<? extends T> c) {
        super(c);
    }

    @Override
    public boolean add(T t) {
        if(super.contains(t)) return false;
        return super.add(t);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        if(super.containsAll(c)) return false;
        return super.addAll(c);
    }

    public boolean containsComparable(Object o){
        if(!(o instanceof Comparable)) return false;
        Comparable comparable = (Comparable) o;

        Object[] es = this.toArray();
        for(Object entry: es){
            if(comparable.compareTo(entry) == 3) return true;
        }
        return false;
    }
}
