// Copyright (c) 2013-2014, Webit Team. All Rights Reserved.
package webit.script.util.iter;

import webit.script.Context;
import webit.script.method.MethodDeclare;
import webit.script.util.ALU;

/**
 *
 * @author zqq
 */
public class IterMethodFilter extends IterFilter{

    protected final Context context;
    protected final MethodDeclare method;

    public IterMethodFilter(Context context, MethodDeclare method, Iter Iter) {
        super(Iter);
        this.context = context;
        this.method = method;
    }

    @Override
    protected boolean valid(Object item) {
        return ALU.isTrue(method.invoke(context, new Object[]{item}));
    }
}