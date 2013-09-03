// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.util.collection;

/**
 *
 * @author Zqq
 */
public class LongArrayIterAdapter extends AbstractIter<Long> {

    private final long [] array;
    private final int max;

    public LongArrayIterAdapter(long[] array) {
        this.array = array;
        this.max = array.length - 1;
    }

    protected Long _next() {
        return array[_index];
    }

    public boolean hasNext() {
        return _index < max;
    }
}
