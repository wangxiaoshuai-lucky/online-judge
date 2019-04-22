package cn.wzy.onlinejudge.util;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

  public static boolean zip(String fileName, String zipName) {
    ZipOutputStream zipOutputStream = null;
    try {
      zipOutputStream = new ZipOutputStream(new FileOutputStream(zipName));
      write_readme(zipOutputStream);
      zipOne(fileName, zipOutputStream, "");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      return false;
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    } finally {
      if (zipOutputStream != null) {
        try {
          zipOutputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
          return false;
        }
      }
    }
    return true;
  }

  private static void zipOne(String filePath, ZipOutputStream zipOutputStream, String relative_path) throws IOException {
    File file = new File(filePath);
    if (!file.exists()) {
      return;
    }
    if (file.isDirectory()) {
      for (File tmp : file.listFiles()) {
        zipOne(tmp.getPath(), zipOutputStream, relative_path + file.getName() + "/");
      }
    } else {
      zipOutputStream.putNextEntry(new ZipEntry(relative_path + file.getName()));
      FileUtils.write(new FileInputStream(file), zipOutputStream, false);
      zipOutputStream.flush();
      zipOutputStream.closeEntry();
    }
  }

  private static void write_readme(ZipOutputStream zipOutputStream) {
    try {
      zipOutputStream.putNextEntry(new ZipEntry("readme.txt"));
      zipOutputStream.write("这是在压缩过程中产生的readme文件====wzy不短不长八字刚好".getBytes());
      zipOutputStream.flush();
      zipOutputStream.closeEntry();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static boolean unzip(String zipName, String target) throws IOException {
    if (!new File(target).exists()) {
      return false;
    }
    ZipFile zipFile = new ZipFile(zipName);
    Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      if (entry.isDirectory()) {
        File file = new File(target + "/" + entry.getName());
        if (!file.exists()) {
          file.mkdirs();
        }
      } else {
        File file = new File(target + "/" + entry.getName());
        if (!file.getParentFile().exists()) {
          file.getParentFile().mkdirs();
        }
        file.createNewFile();
        FileUtils.write(zipFile.getInputStream(entry), new FileOutputStream(file), true);
      }
    }
    zipFile.close();
    return true;
  }
}
