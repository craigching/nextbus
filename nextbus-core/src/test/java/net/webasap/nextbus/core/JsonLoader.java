package net.webasap.nextbus.core;

import lombok.val;
import net.webasap.nextbus.core.service.impl.TestMetroTransitServiceImpl;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class JsonLoader {

    public static String loadJsonFile(String name) throws IOException {
        val fileName = String.format("/%s", name);
        val routesFile = new File(TestMetroTransitServiceImpl.class.getResource(fileName).getFile());
        return FileUtils.readFileToString(routesFile, Charset.forName("UTF-8"));
    }

}
