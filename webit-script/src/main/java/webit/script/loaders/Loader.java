// Copyright (c) 2013, Webit Team. All Rights Reserved.
package webit.script.loaders;

import webit.script.exceptions.ResourceNotFoundException;

public interface Loader {

    Resource get(String name) throws ResourceNotFoundException;

    String concat(String parent, String name);

    String normalize(String name);
}