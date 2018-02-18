package com.buzzlers.jhelpdesk.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.FileUtils.byteCountToDisplaySize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileUtils {

    private final static Logger LOG = LoggerFactory.getLogger(FileUtils.class);

    private File attachmentsTmpDirectory;
    private File attachmentsDirectory;

    public static String toDisplaySize(long size) {
        return byteCountToDisplaySize(size);
    }

    public static void cleanPaths(Collection<String> paths) {
        for (String path : paths) {
            File pathToDelete = new File(path);
            LOG.debug("Attempt to delete directory: " + pathToDelete.getAbsolutePath());
            if (pathToDelete.exists()) {
                cleanDirectory(pathToDelete);
                boolean deletionRes = pathToDelete.delete();
                LOG.debug(deletionRes ? " [deleted]" : " [error]");
            } else {
                LOG.debug(" [not exists]");
            }
        }
    }

    public static void cleanPathsForTicketstamp(Collection<String> paths, String ticketstamp) {
        List<String> pathsToDelete = new ArrayList<>();
        for (String path : paths) {
            if (path.endsWith(ticketstamp)) {
                pathsToDelete.add(path); // dodajemy do kolekcji do usuniecią
                paths.remove(path);      // i usuwamy z kolekcji w sesji
                break;
            }
        }
        cleanPaths(pathsToDelete);
    }
    
    public File getAttachmentsDirectory() {
        return attachmentsDirectory;
    }

    public File createTmpDirForTicketstamp(String ticketstamp) {
        try {
            File f = new File(attachmentsTmpDirectory, ticketstamp);
            if (!f.exists()) {
                f.mkdirs();
            }
            return f;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Value("${tickets.attachments.tmpdir}")
    public void setAttachmentsTmpDirectory(String attachmentsTmpDir) {
        attachmentsTmpDirectory = createDirIfNotExist(attachmentsTmpDir);
    }

    @Value("${tickets.attachments.dir}")
    public void setAttachmentsDirectory(String attachmentsDir) {
        attachmentsDirectory = createDirIfNotExist(attachmentsDir);
    }

    private File createDirIfNotExist(String directory) {
        try {
            LOG.info("Attempt to create directory: " + directory);
            File dir = new File(directory);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return dir;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static boolean cleanDirectory(File directory) {
        for (File f : directory.listFiles()) {
            boolean result = f.delete();
            if (!result) {
                LOG.warn("Can't delete file: " + f.getName());
                return false;
            }
        }
        return true;
    }
}
