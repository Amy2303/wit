// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.core.ast.statments;

import java.util.Map;
import webit.script.Context;
import webit.script.Engine;
import webit.script.Template;
import webit.script.core.ast.AbstractStatment;
import webit.script.core.ast.Expression;
import webit.script.exceptions.ScriptRuntimeException;
import webit.script.util.StatmentUtil;
import webit.script.util.keyvalues.KeyValues;
import webit.script.util.keyvalues.KeyValuesUtil;

/**
 *
 * @author Zqq
 */
public abstract class AbstractIncludeStatment extends AbstractStatment {

    private final Expression templateNameExpr;
    private final Expression paramsExpr;
    private final String myTemplateName;
    private final Engine engine;

    public AbstractIncludeStatment(Expression templateNameExpr, Expression paramsExpr, Template template, int line, int column) {
        super(line, column);
        this.templateNameExpr = templateNameExpr;
        this.paramsExpr = paramsExpr;
        this.myTemplateName = template.name;
        this.engine = template.engine;
    }

    @SuppressWarnings("unchecked")
    protected Context mergeTemplate(final Context context) {
        final Object templateName;
        if ((templateName = StatmentUtil.execute(templateNameExpr, context)) != null) {
            final KeyValues params;
            final Object paramsObject;
            if (paramsExpr != null
                    && (paramsObject = StatmentUtil.execute(paramsExpr, context)) != null) {
                if (paramsObject instanceof Map) {
                    params = KeyValuesUtil.wrap((Map) paramsObject);
                } else {
                    throw new ScriptRuntimeException("Template param must be a Map.", paramsExpr);
                }
            } else {
                params = KeyValuesUtil.EMPTY_KEY_VALUES;
            }
            try {
                return engine.getTemplate(myTemplateName, String.valueOf(templateName))
                        .merge(engine.isShareRootData()
                                ? KeyValuesUtil.wrap(context.rootValues, params)
                                : params, context.getOut());
            } catch (Throwable e) {
                throw new ScriptRuntimeException(e, this);
            }
        } else {
            throw new ScriptRuntimeException("Template name should not be null.", templateNameExpr);
        }
    }
}