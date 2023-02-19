package org.vizzoid.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@SuppressWarnings("ResultOfMethodCallIgnored")
public class VersionFile {

    private final String path;
    private final String current;

    public VersionFile(String path) {
        current = path;
        int dotIndex = path.lastIndexOf('.');
        String until = path.substring(0, dotIndex);
        String extension = path.substring(dotIndex);
        this.path = path.replace(until, until + "%s" + extension);
    }

    public String getPath() {
        return current;
    }

    public boolean exists() {
        return new File(current).exists();
    }

    public String getPath(int id) {
        return getPath(id, false);
    }

    public String getPath(int id, boolean corrupt) {
        return path.formatted((corrupt ? "-corrupt-" : "-") + id);
    }

    public void reserve() {
        reserve(false);
    }

    protected void reserve(boolean corrupt) {
        //<editor-fold desc="Write Contents of Current to Old">
        File currentFile = new File(current);
        if (!currentFile.exists()) return;
        try (FileInputStream input = new FileInputStream(currentFile)) {
            String oldFilename = getPath(getLastId() + 1, corrupt);
            File old = new File(oldFilename);
            old.mkdirs();
            try (ZipOutputStream output = new ZipOutputStream(new FileOutputStream(old))) {
                output.putNextEntry(new ZipEntry(current));
                output.write(input.readAllBytes());
                output.closeEntry();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //</editor-fold>

        //<editor-fold desc="Clear Current's Contents">
        try {
            new PrintWriter(currentFile).close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        //</editor-fold>
    }

    public int getLastId() {
        File current = new File(this.current);

        int lastId = -1;
        File parent = current.getParentFile();
        parent.mkdirs();
        File[] files = parent.listFiles();
        if (files == null) throw new NullPointerException("Directory was not directory: " + parent);
        for (File file : files) {
            if (current.equals(file)) continue;
            String fileName = file.getName();

            String idString = fileName.substring(fileName.lastIndexOf('-') + 1, fileName.lastIndexOf('.'));
            int newId = Integer.parseInt(idString);
            if (newId > lastId) lastId = newId;
        }
        return lastId;
    }

    public void recover() {
        recover(getLastId());
    }

    public void recover(int id) {
        File directory = new File(getPath(id));
        if (!directory.exists()) return;
        try (ZipInputStream stream = new ZipInputStream(new FileInputStream(directory))) {
            ZipEntry entry = stream.getNextEntry();
            if (entry == null) return;

            try (FileOutputStream file = new FileOutputStream(current)) {
                file.write(stream.readAllBytes());
            }
            directory.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void corrupt() {
        corrupt(getLastId());
    }

    public void corrupt(int id) {
        reserve(true);
        recover(id);
    }

}
