// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.core.runtime.variant;

/**
 *
 * @author Zqq
 */
public interface VariantMap {

    int size();

    int getIndex(String name);

    String getName(int index);
}
