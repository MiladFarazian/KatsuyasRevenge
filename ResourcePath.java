import java.io.File;

public class ResourcePath {
    private static String basePath = null;

    public static String getBasePath() {
        if (basePath == null) {
            try {
                String classPath = ResourcePath.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
                File classDir = new File(classPath);
                if (classDir.isFile()) {
                    classDir = classDir.getParentFile();
                }
                basePath = classDir.getAbsolutePath() + File.separator;
            } catch (Exception e) {
                basePath = "";
            }
        }
        return basePath;
    }

    public static String resolve(String relativePath) {
        return getBasePath() + relativePath;
    }
}
