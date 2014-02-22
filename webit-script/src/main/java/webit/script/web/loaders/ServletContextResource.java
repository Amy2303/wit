// Copyright (c) 2013-2014, Webit Team. All Rights Reserved.
package webit.script.web.loaders;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import javax.servlet.ServletContext;
import webit.script.exceptions.ResourceNotFoundException;
import webit.script.loaders.Resource;
import webit.script.util.StreamUtil;

/**
 *
 * @author zqq90
 */
public class ServletContextResource implements Resource {

    private final String path;
    private final String encoding;
    private final ServletContext servletContext;

    public ServletContextResource(String path, String encoding, ServletContext servletContext) {
        this.path = path;
        this.encoding = encoding;
        this.servletContext = servletContext;
    }

    public boolean isModified() {
        return false;
    }

    public Reader openReader() throws IOException {
        final InputStream in;
        if ((in = servletContext.getResourceAsStream(path)) != null) {
            return new InputStreamReader(in, encoding);
        } else {
            throw new ResourceNotFoundException("Resource Not Found: ".concat(path));
        }
    }

    /**
     * @since 1.4.1
     */
    public boolean exists() {
        final InputStream inputStream;
        if ((inputStream = servletContext.getResourceAsStream(path)) != null) {
            StreamUtil.close(inputStream);
            return true;
        }
        return false;
    }
}
