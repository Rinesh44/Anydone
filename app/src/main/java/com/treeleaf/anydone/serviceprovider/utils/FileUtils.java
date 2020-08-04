package com.treeleaf.anydone.serviceprovider.utils;

import android.os.Environment;

import com.treeleaf.anydone.serviceprovider.AnyDoneServiceProviderApplication;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;

public class FileUtils {
    private static final String TAG = "FileUtils";
    private static final int BUFFER_SIZE = 4096;

    private FileUtils() {
    }

    public static void close(InputStream stream) {
        if (null != stream) {
            try {
                stream.close();
            } catch (IOException ignore) {
                //Empty on purpose
            }
        }
    }

    public static void close(Reader reader) {
        if (null != reader) {
            try {
                reader.close();
            } catch (IOException ignore) {
                //Empty on purpose
            }
        }
    }

    public static void close(OutputStream stream) {
        if (null != stream) {
            try {
                stream.close();
            } catch (IOException ignore) {
                //Empty on purpose
            }
        }
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }

    public static File generateFileFromInputStream(InputStream inputStream) throws IOException {
        File clientKeyFile = new File(AnyDoneServiceProviderApplication.getContext().getCacheDir(), "test.pem");
        try (OutputStream output = new FileOutputStream(clientKeyFile)) {
            byte[] buffer = new byte[4 * 1024]; // or other buffer size
            int read;

            while ((read = inputStream.read(buffer)) != -1) {
                output.write(buffer, 0, read);
            }
            output.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
        }

        return clientKeyFile;
    }

    public static File downloadFileFromUrl(String url, String outputFileName) {
        File dir = new File(Environment.getExternalStorageDirectory() + "/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            URL downloadUrl = new URL(url); // you can write any link here

            File file = new File(dir, outputFileName);
            /* Open a connection to that URL. */
            URLConnection ucon = downloadUrl.openConnection();

            /*
             * Define InputStreams to read from the URLConnection.
             */
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            /*
             * Read bytes to the Buffer until there is nothing more to read(-1).
             */
            byte[] buffer = new byte[BUFFER_SIZE];
            int read;
            while ((read = bis.read(buffer)) != -1) {
                outputStream.write(buffer, 0, read);
            }

            /* Convert the Bytes read to a String. */
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(outputStream.toByteArray());
            fos.flush();
            fos.close();

            return file;

        } catch (IOException e) {
            GlobalUtils.showLog(TAG, "file download exceptionL : " + e.toString());
            e.printStackTrace();
        }

        return null;
    }
}