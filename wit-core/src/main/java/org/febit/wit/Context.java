// Copyright (c) 2013-present, febit.org. All Rights Reserved.
package org.febit.wit;

import java.util.Map;
import java.util.function.BiConsumer;
import org.febit.wit.exceptions.NotFunctionException;
import org.febit.wit.exceptions.ScriptRuntimeException;
import org.febit.wit.lang.InternalVoid;

/**
 *
 * @author zqq90
 */
public interface Context {

    final Object VOID = InternalVoid.VOID;

    /**
     * Get a variable by name.
     *
     * @param name
     * @return
     * @throws ScriptRuntimeException
     */
    Object get(String name) throws ScriptRuntimeException;

    /**
     * Get a variable by name.
     *
     * @param name
     * @param force
     * @return
     * @throws ScriptRuntimeException
     */
    Object get(String name, boolean force) throws ScriptRuntimeException;

    /**
     * Set a variable by name.
     *
     * @param name
     * @param value
     */
    void set(String name, Object value);

    /**
     * Get a local variable by name.
     *
     * @param name
     * @return
     */
    Object getLocal(Object name);

    /**
     * Set a local variable by name.
     *
     * @param name
     * @param value
     */
    void setLocal(Object name, Object value);

    /**
     * Export a function by name.
     *
     * @since 1.5.0
     * @param name
     * @return
     * @throws NotFunctionException
     */
    Function exportFunction(String name) throws NotFunctionException;

    /**
     * Export variables to a given map.
     *
     * @param map
     */
    void exportTo(final Map<? super String, Object> map);

    /**
     *
     * @since 2.6.0
     * @param action
     */
    void forEachVar(BiConsumer<? super String, Object> action);
}
