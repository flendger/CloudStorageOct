package nio;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.stream.Stream;

public final class FileUtils {

    public static String getFilesList(String path) {
        return String.join(" ", new File(path).list());
    }

    public static String changeDir(String newDir, String curPath) throws NoSuchFileException, NotDirectoryException {
        Path path = Path.of(curPath);

        if (newDir.equals("/")) {
            while (path.getParent() != null) {
                path = path.getParent();
            }
            return path.toString();
        } else if(newDir.equals("..")) {
            if (path.getParent() == null) {
                return path.toString();
            }
            path = path.getParent();
        } else if (newDir.equals(".")) {
            return path.toString();
        } else {
            path = Path.of(curPath, newDir);
        }
        if (Files.exists(path)) {
            if (Files.isDirectory(path)) {
                return path.toString();
            } else {
                throw new NotDirectoryException(newDir);
            }
        } else {
            throw new NoSuchFileException(newDir);
        }
    }

    public static String makeDir(String dirName, String curPath) throws IOException {
        Path path = Path.of(curPath, dirName);
        if (Files.exists(path)){
            throw new FileAlreadyExistsException(dirName);
        }

        Files.createDirectory(path);
        return path.toString();
    }

    public static String touchFile(String fileName, String curPath) throws IOException{
        Path path = Path.of(curPath, fileName);
        if (Files.exists(path)){
            throw new FileAlreadyExistsException(fileName);
        }

        Files.createFile(path);
        return path.toString();
    }

    public static String removeFile(String fileName, String curPath) throws IOException{
        Path path = Path.of(curPath, fileName);
        if (!Files.exists(path)){
            throw new NoSuchFileException(fileName);
        }

        Files.delete(path);
        return path.toString();
    }

    public static String copyFile(String src, String dst) throws IOException {
        Path srcPath = Path.of(src);
        if (!Files.exists(srcPath) || Files.isDirectory(srcPath)) {
            throw new NoSuchFileException(src);
        }

        Path destPath = Path.of(dst);
        Path destDirPath = Files.isDirectory(destPath) ? destPath : destPath.getParent();

        if (!Files.exists(destDirPath)) {
            throw new NoSuchFileException(destDirPath.toString());
        }

        if (!Files.isDirectory(destPath)) {
            if (Files.exists(destPath)) {
                throw new FileAlreadyExistsException(dst);
            }
        } else {
            destPath = destDirPath.resolve(srcPath);
        }

        Files.copy(srcPath, destPath);
        return destPath.toString();
    }

    public static Stream<String> catFile(String fileName) throws IOException{
        Path path = Path.of(fileName);
        if (!Files.exists(path) || Files.isDirectory(path)){
            throw new NoSuchFileException(fileName);
        }

        return Files.newBufferedReader(path).lines();
    }
}
