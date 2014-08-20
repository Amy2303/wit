// Copyright (c) 2013-2014, Webit Team. All Rights Reserved.
package webit.script.lang.iter;

/**
 *
 * @author Zqq
 */
public class DoubleArrayIterAdapter extends AbstractIter {

    private final double[] array;
    private final int max;

    public DoubleArrayIterAdapter(double[] array) {
        this.array = array;
        this.max = array.length - 1;
    }

    protected Double _next() {
        return array[_index];
    }

    public boolean hasNext() {
        return _index < max;
    }
}