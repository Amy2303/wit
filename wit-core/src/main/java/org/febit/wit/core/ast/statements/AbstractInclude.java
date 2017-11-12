// Copyright (c) 2013-2016, febit.org. All Rights Reserved.
package org.febit.wit.core.ast.statements;

import java.util.HashMap;
import java.util.Map;
import org.febit.wit.Context;
import org.febit.wit.Engine;
import org.febit.wit.InternalContext;
import org.febit.wit.Template;
import org.febit.wit.core.ast.Expression;
import org.febit.wit.core.ast.Statement;
import org.febit.wit.core.ast.expressions.DirectValue;
import org.febit.wit.exceptions.ParseException;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.febit.wit.exceptions.ScriptRuntimeException;
import org.febit.wit.util.KeyValuesUtil;
import org.febit.wit.util.StatementUtil;
import org.febit.wit.Vars;

/**
 *
 * @author zqq90
 */
public abstract class AbstractInclude extends Statement {

    private static final Expression EXPR_EMPTY_PARAMS = new DirectValue(null, -1, -1);

    private final Expression pathExpr;
    private final Expression paramsExpr;
    private final String myTemplateName;
    private final Engine engine;

    public AbstractInclude(Expression pathExpr, Expression paramsExpr, Template template, int line, int column) {
        super(line, column);
        this.pathExpr = StatementUtil.optimize(pathExpr);
        this.paramsExpr = paramsExpr == null ? EXPR_EMPTY_PARAMS : StatementUtil.optimize(paramsExpr);
        this.myTemplateName = template.name;
        this.engine = template.engine;
    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> mergeTemplate(final InternalContext context, boolean export) {
        final Object templatePath = pathExpr.execute(context);
        if (templatePath == null) {
            throw new ScriptRuntimeException("Template name should not be null.", pathExpr);
        }
        final Vars params;
        final Object paramsObject = paramsExpr.execute(context);
        if (paramsObject == null) {
            params = KeyValuesUtil.EMPTY_KEY_VALUES;
        } else if (paramsObject instanceof Map) {
            params = KeyValuesUtil.wrap((Map) paramsObject);
        } else {
            throw new ScriptRuntimeException("Template param must be a Map.", paramsExpr);
        }
        try {
            Template template = engine.getTemplate(myTemplateName, String.valueOf(templatePath));
            Vars rootParams = engine.isShareRootData() ? KeyValuesUtil.wrap(context.rootParams, params) : params;
            Context newContext = template.mergeToContext(context, rootParams);

            if (export) {
                Map<String, Object> result = new HashMap<>();
                newContext.exportTo(result);
                return result;
            }
            return null;
        } catch (ResourceNotFoundException | ScriptRuntimeException | ParseException e) {
            throw new ScriptRuntimeException(e, this);
        }
    }
}
