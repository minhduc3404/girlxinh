package com.max.app.girlxinh.util;

import android.os.Environment;

import com.max.app.girlxinh.main.MainApp;
import com.max.app.girlxinh.module.Page;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Forev on 4/9/2016.
 */
public class ReadWriteJsonFileUtils {
    public static String readFileJson(String name) {
        File dir = MainApp.getInstance().getCacheDir();
        File fileCache = new File(dir.getPath() + "/" + name);
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(fileCache));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        } catch (IOException e) {
            //You'll need to add proper error handling here
        } finally {
            return text.toString();
        }
    }

    public static void writeFileJson(String nameFile, String json) {
        File dir = MainApp.getInstance().getCacheDir();
        File fileCache = new File(dir.getPath() + "/" + nameFile);
        FileOutputStream fOutStream;
        try {
            fOutStream = new FileOutputStream(fileCache);
            fOutStream.write(json.getBytes());
            fOutStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
