package com.max.app.girlxinh.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Forev on 4/6/2016.
 */
public abstract class SaveFileStorage {
    public static void saveBitmap(Bitmap bitmap, String nameFile, SaveFileTask.OnCompleteListener listener) {
        SaveFileTask task = new SaveFileTask(nameFile, bitmap);
        task.setCompleteListener(listener);
        task.execute();
    }


    public static class SaveFileTask extends AsyncTask<Void, Void, Void> {
        String filename;
        Bitmap bm;

        public SaveFileTask(String file, Bitmap bm) {
            this.filename = file;
            this.bm = bm;
        }

        @Override
        protected Void doInBackground(Void... params) {
            final String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath();
            File file = new File(path + "/BeautifulGirls/" + this.filename + ".jpg");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(file);
                this.bm.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                if (completeListener != null)
                    completeListener.onCompleted(null, false);
            } finally {
                if (fileOutputStream != null)
                    try {
                        fileOutputStream.flush();
                        fileOutputStream.close();
                        if (completeListener != null)
                            completeListener.onCompleted(file, true);
                    } catch (IOException e) {
                        e.printStackTrace();
                        if (completeListener != null)
                            completeListener.onCompleted(null, false);
                    }
            }
            return null;
        }

        OnCompleteListener completeListener;

        public void setCompleteListener(OnCompleteListener completeListener) {
            this.completeListener = completeListener;
        }

        public interface OnCompleteListener {
            void onCompleted(File file, boolean isSuccess);
        }
    }
}
