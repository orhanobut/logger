package com.orhanobut.logger;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.Pair;

import com.orhanobut.logger.permissions.PermissionRequesterActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileLogTool implements LogTool {
    private static final String FILE_NAME_DEFAULT = "LOGGER.txt";
    private final BlockingQueue mQueue;
    private Writer mLogWriter;
    private Context mContext;
    private String mFileName;

    public FileLogTool(Context context) {
        this(context, FILE_NAME_DEFAULT);
    }

    public FileLogTool(Context context, String fileName) {
        mFileName = fileName;
        mContext = context;
        mQueue = new LinkedBlockingQueue();
    }

    @Override
    public void d(String tag, String message) {
        Log.d(tag, message);
        addToQueue(tag, message);
    }

    @Override
    public void e(String tag, String message) {
        Log.e(tag, message);
        addToQueue(tag, message);
    }

    @Override
    public void w(String tag, String message) {
        Log.w(tag, message);
        addToQueue(tag, message);
    }

    @Override
    public void i(String tag, String message) {
        Log.i(tag, message);
        addToQueue(tag, message);
    }

    @Override
    public void v(String tag, String message) {
        Log.v(tag, message);
        addToQueue(tag, message);
    }

    @Override
    public void wtf(String tag, String message) {
        Log.wtf(tag, message);
        addToQueue(tag, message);
    }

    /**
     * Add log to Queue. If Writer is not running, start it.
     *
     * @param tag
     * @param message
     */
    private void addToQueue(String tag, String message) {
        try {
            mQueue.put(new Pair<String, String>(tag, message));
            if (mLogWriter == null) {
                mLogWriter = new Writer();
                new Thread(mLogWriter).start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class Writer implements Runnable {

        private boolean isExternalStorageWritable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                return true;
            }
            return false;
        }

        private File getFile(String fileName) {
            // Get the directory for the user's public pictures directory.
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), fileName);
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return file;
        }

        private boolean verifyStoragePermissions(Context context) {
            // Check if we have write permission
            int permission = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE);

            if (permission != PackageManager.PERMISSION_GRANTED) {
                // We don't have permission so prompt the user
                Intent resultIntent = new Intent(context, PermissionRequesterActivity.class);
                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                context,
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );

                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(context)
                                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                                .setContentTitle("Permission Needed")
                                .setContentText("Click here to allow!")
                                .setAutoCancel(true)
                                .setContentIntent(resultPendingIntent);

                // Sets an ID for the notification
                int mNotificationId = 1;
                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(mNotificationId, mBuilder.build());

                return false;
            } else
                return true;
        }

        public void run() {
            if (!verifyStoragePermissions(mContext))
                return;

            //open file to write
            File file = null;
            if (isExternalStorageWritable()) {
                file = getFile(mFileName);
            }

            OutputStreamWriter outputStreamWriter = null;
            try {
                outputStreamWriter = new OutputStreamWriter(file == null ? new FileOutputStream(mFileName, true) : new FileOutputStream(file, true));
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            //while mQueue is not empty write every log to file.
            while (!mQueue.isEmpty()) {
                try {
                    Pair<String, String> log = (Pair<String, String>) mQueue.take();
                    outputStreamWriter.write(log.second+System.getProperty("line.separator"));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //close file
            try {
                outputStreamWriter.close();
            } catch (IOException e) {
                Log.e("Exception", "File write failed: " + e.toString());
            }

            //clean reference
            mLogWriter = null;

        }
    }
}
