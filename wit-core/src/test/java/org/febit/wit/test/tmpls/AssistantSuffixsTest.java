// Copyright (c) 2013-present, febit.org. All Rights Reserved.
package org.febit.wit.test.tmpls;

import org.febit.wit.EngineManager;
import org.febit.wit.exceptions.ResourceNotFoundException;
import org.junit.Test;

/**
 * @author zqq90
 */
public class AssistantSuffixsTest {

    @Test
    public void test() throws ResourceNotFoundException {
        EngineManager.getTemplate("/assistantSuffixsTest.whtml").reload();
        EngineManager.getTemplate("/assistantSuffixsTest.wit2").reload();
    }
}
