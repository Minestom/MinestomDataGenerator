package net.minestom.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.jar.JarFile;

public class ResourceUtils {

    private ResourceUtils() {}

    /**
     * Get a resource listing from within a jar file
     * @param clazz The class to use the {@link ClassLoader} of
     * @param path The path within the classpath to list resources from
     * @return An array containing all the path of all resources within the specified directory
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        var dirURL = clazz.getClassLoader().getResource(path);

        // list use File#list in case path is just a regular file
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
            var me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        // dirURL should not be null at this point
        assert dirURL != null;

        if (dirURL.getProtocol().equals("jar")) {
            // strip out jar file from dirURL
            var jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!"));

            try (var jar = new JarFile(URLDecoder.decode(jarPath, StandardCharsets.UTF_8))) {
                // get all files within the jar
                var entries = jar.entries();
                var result = new HashSet<>();

                while(entries.hasMoreElements()) {
                    var name = entries.nextElement().getName();
                    if (name.startsWith(path)) {
                        var entry = name.substring(path.length());
                        int checkSubdir = entry.indexOf("/");
                        if (checkSubdir >= 0) {
                            entry = entry.substring(0, checkSubdir);
                        }
                        result.add(entry);
                    }
                }

                return result.toArray(new String[result.size()]);
            }
        }

        throw new UnsupportedOperationException("Unable to list files for URL " + dirURL);
    }

}
