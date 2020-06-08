package com.example.myapplication.util;

import android.content.Context;
import android.content.ContextWrapper;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SavePictureUtil {

    public static void writeToFile(byte[] array, String fileName, Context applicationContext, File filesDir) {     // TODO vratiti sliku zbog postavljanja profilne
        try {
            ContextWrapper contextWrapper = new ContextWrapper(applicationContext);
            File directory = contextWrapper.getDir(filesDir.getName(), Context.MODE_PRIVATE);
            File file =  new File(directory, fileName);

//            String path = "pictures/" + fileName;
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(array);
            stream.close();
//            FileUtils.writeByteArrayToFile(new File(fileName), array);

        } catch ( IOException e) {
            e.printStackTrace();
        }
    }

    public static File readFromFile(String fileName, Context applicationContext, File filesDir) {
        ContextWrapper contextWrapper = new ContextWrapper(applicationContext);
        File directory = contextWrapper.getDir(filesDir.getName(), Context.MODE_PRIVATE);
        File file =  new File(directory, fileName);
        return file;
    }
}
