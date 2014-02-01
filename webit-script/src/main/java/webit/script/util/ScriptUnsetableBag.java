// Copyright (c) 2013-2014, Webit Team. All Rights Reserved.
package webit.script.util;


/**
 *
 * @param <K> the type of keys maintained by this bag
 * @param <V> the type of mapped values
 *
 * @author zqq90
 * @since 1.4.0
 */
public interface ScriptUnsetableBag<K, V> {

    V get(K key);

    void set(K key, V value);

}
