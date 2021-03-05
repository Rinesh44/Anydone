package com.treeleaf.anydone.serviceprovider.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;

import androidx.exifinterface.media.ExifInterface;

import com.google.android.material.datepicker.CalendarConstraints;
import com.orhanobut.hawk.Hawk;
import com.treeleaf.anydone.entities.AnydoneProto;
import com.treeleaf.anydone.serviceprovider.BuildConfig;
import com.treeleaf.anydone.serviceprovider.R;
import com.treeleaf.anydone.serviceprovider.model.Priority;
import com.treeleaf.anydone.serviceprovider.realm.model.Account;
import com.treeleaf.anydone.serviceprovider.realm.model.Inbox;
import com.treeleaf.anydone.serviceprovider.realm.model.Participant;
import com.treeleaf.anydone.serviceprovider.realm.repo.AccountRepo;
import com.treeleaf.anydone.serviceprovider.realm.repo.ParticipantRepo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class GlobalUtils {

    private static final String TAG = "GlobalUtils";

    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = Objects.requireNonNull(cm).getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public static void showLog(String tag, String msg) {
        if (tag != null && msg != null)
            Log.d("APP_FLOW -->" + tag, msg);
    }

    public static String getDate(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("E, d MMM yyyy");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateTimeline(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateDigits(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateLong(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd, MMM yyyy");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateAlternate(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateShort(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getDateNormal(long time) {
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy");
            return sdf.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTime(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm a");
        Date date = new Date(time);
        return dateFormat.format(date);
    }

    public static long getTimeStampLocal(long gmtTimestamp) {
        int offset = TimeZone.getDefault().getRawOffset() + TimeZone.getDefault().getDSTSavings();
        return System.currentTimeMillis() + offset;
    }

    public static String getTimeExcludeMillis(long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa");
        Date date = new Date(time);
        return dateFormat.format(date);
    }


    public static AnydoneProto.Gender getGender(String selectedItem) {
        switch (selectedItem) {
            case "Male":
                return AnydoneProto.Gender.MALE;

            case "Female":
                return AnydoneProto.Gender.FEMALE;

            case "Other":
                return AnydoneProto.Gender.OTHER;

            default:
                break;
        }

        return AnydoneProto.Gender.UNKNOWN_GENDER;
    }

    /*
Limit selectable Date range
*/
    public static CalendarConstraints.Builder limitRange() {
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();

        Calendar calendarEnd = Calendar.getInstance();
        int currentYear = calendarEnd.get(Calendar.YEAR);

        int endYear = currentYear + 5;
        int endMonth = calendarEnd.get(Calendar.MONTH);
        int endDate = calendarEnd.get(Calendar.DATE);
        calendarEnd.set(endYear, endMonth - 1, endDate);

        long maxDate = calendarEnd.getTimeInMillis();

        constraintsBuilderRange.setStart(System.currentTimeMillis());
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new RangeValidator(System.currentTimeMillis(), maxDate));

        return constraintsBuilderRange;
    }

    public static String parseMentions(String text) {
        //change mentioned pattern to name
        String mentionPattern = "(?<=@)[\\w]+";
        Pattern p = Pattern.compile(mentionPattern);
        String msg = text;
        Matcher m = p.matcher(msg);
//                    String changed = m.replaceAll("");
        while (m.find()) {
            GlobalUtils.showLog(TAG, "found: " + m.group(0));
            String employeeId = m.group(0);
            Participant participant = ParticipantRepo.getInstance()
                    .getParticipantByEmployeeAccountId(employeeId);
//                        AssignEmployee participant = AssignEmployeeRepo.getInstance().getAssignedEmployeeByAccountId(employeeId);
            GlobalUtils.showLog(TAG, "participant check: " + participant.getEmployee().getName());
            if (participant != null && employeeId != null) {
                msg = msg.replace(employeeId, participant.getEmployee().getName());
                return msg;
            }
        }

        return text;
    }


    static class RangeValidator implements CalendarConstraints.DateValidator {


        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Parcelable.Creator<RangeValidator> CREATOR = new Parcelable.Creator<RangeValidator>() {

            @Override
            public RangeValidator createFromParcel(Parcel parcel) {
                return new RangeValidator(parcel);
            }

            @Override
            public RangeValidator[] newArray(int size) {
                return new RangeValidator[size];
            }
        };

    }

    @SuppressLint("NewApi")
    public static String getPath(Uri uri, Context context) {
        final boolean needToCheckUri = Build.VERSION.SDK_INT >= 19;
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        // deal with different Uris.
        if (needToCheckUri && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
            /*    final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }

                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));*/

                return getStringPdf(uri, context);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                switch (type) {
                    case "image":
                        uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "video":
                        uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                        break;
                    case "audio":
                        uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                        break;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            try (Cursor cursor = context.getContentResolver().query(uri, projection, selection,
                    selectionArgs, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    return cursor.getString(columnIndex);
                }
            } catch (Exception e) {
                Log.e("on getPath", "Exception", e);
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static String getFormattedDuration(int duration) {
        if (duration != 0) {
            long hours = (TimeUnit.MILLISECONDS
                    .toHours(duration));

            long minutes = (TimeUnit.MILLISECONDS
                    .toMinutes(duration));

            long seconds = (TimeUnit.MILLISECONDS
                    .toSeconds(duration));

            StringBuilder durationBuilder = new StringBuilder();
            if (hours != 0) {
                if (hours > 1) {
                    durationBuilder.append(hours);
                    durationBuilder.append(" hrs ");
                } else {
                    durationBuilder.append(hours);
                    durationBuilder.append(" hr ");
                }
            }

            if (minutes != 0) {
                if (minutes > 1) {
                    durationBuilder.append(minutes);
                    durationBuilder.append(" mins ");
                } else {
                    durationBuilder.append(minutes);
                    durationBuilder.append(" min ");
                }
            }

            if (seconds != 0) {
                if (seconds > 1 && seconds < 60) {
                    durationBuilder.append(seconds);
                    durationBuilder.append(" secs");
                } else if (seconds > 60) {
                    int mod = (int) seconds / 60;
                    int converted = mod * 60;
                    int remainder = (int) seconds - converted;
                    GlobalUtils.showLog(TAG, "seconds che" + seconds);
                    GlobalUtils.showLog(TAG, "mod" + mod);
                    GlobalUtils.showLog(TAG, "converted" + converted);
                    GlobalUtils.showLog(TAG, "remainder" + remainder);
                    durationBuilder.append(remainder);
                    durationBuilder.append(" secs");
                } else {
                    durationBuilder.append(seconds);
                    durationBuilder.append(" sec");
                }
            }

            return durationBuilder.toString();
        } else {
            return "missed";
        }
    }

    public static String getCallDuration(int duration) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(duration);
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        int seconds = cal.get(Calendar.SECOND);

        StringBuilder durationBuilder = new StringBuilder();
        if (hours != 0) {
            if (hours > 1) {
                durationBuilder.append(hours);
                durationBuilder.append("hours");
            } else {
                durationBuilder.append(hours);
                durationBuilder.append("hour");
            }
        }

        if (minutes != 0) {
            if (minutes > 1) {
                durationBuilder.append(minutes);
                durationBuilder.append("minutes");
            } else {
                durationBuilder.append(minutes);
                durationBuilder.append("minute");
            }
        }

        if (seconds != 0) {
            if (seconds > 1) {
                durationBuilder.append(seconds);
                durationBuilder.append("seconds");
            } else {
                durationBuilder.append(seconds);
                durationBuilder.append("second");
            }
        }

        return durationBuilder.toString();
    }


    public static Bitmap rotateBitmap(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }


    public static Bitmap scaleDownBitmap(Bitmap myBitmap, Activity context) {
        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        Log.e("Screen width ", " " + width);
        Log.e("Screen height ", " " + height);
        Log.e("img width ", " " + myBitmap.getWidth());
        Log.e("img height ", " " + myBitmap.getHeight());
        float scaleHt = (float) width / myBitmap.getWidth();
        Log.e("Scaled percent ", " " + scaleHt);
        Bitmap scaled = Bitmap.createScaledBitmap(myBitmap, width, (int) (myBitmap.getWidth() * scaleHt), true);
        return scaled;
    }


    public static Bitmap fixBitmapRotation(Uri uri, Bitmap bitmap, Activity activity) throws IOException {
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23) {
            InputStream input = activity.getContentResolver().openInputStream(uri);
            assert input != null;
            ei = new ExifInterface(input);
        } else {
            ei = new ExifInterface(Objects.requireNonNull(uri.getPath()));
        }
        int rotation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int rotationInDegrees = exifToDegrees(rotation);
        GlobalUtils.showLog(TAG, "image orientation: " + rotationInDegrees);
        Matrix matrix = new Matrix();
        if (rotation != 0) {
            matrix.postRotate(rotationInDegrees);
        }

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                bitmap.getHeight(), matrix, true);
    }

    private static InputStream getInputStreamFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 100, bos);
        byte[] bitmapData = bos.toByteArray();
        return new ByteArrayInputStream(bitmapData);
    }

    private static int exifToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

    /*
    Convert first character to uppercase and lowercase rest
     */
    public static String convertCase(String stringToConvert) {
        // Create a char array of given String
        char ch[] = stringToConvert.toCharArray();
        for (int i = 0; i < stringToConvert.length(); i++) {

            // If first character of a word is found
            if (i == 0 && ch[i] != ' ' ||
                    ch[i] != ' ' && ch[i - 1] == ' ') {

                // If it is in lower-case
                if (ch[i] >= 'a' && ch[i] <= 'z') {

                    // Convert into Upper-case
                    ch[i] = (char) (ch[i] - 'a' + 'A');
                }
            }

            // If apart from first character
            // Any one is in Upper-case
            else if (ch[i] >= 'A' && ch[i] <= 'Z')

                // Convert into Lower-Case
                ch[i] = (char) (ch[i] + 'a' - 'A');
        }

        // Convert the char array to equivalent String
        return new String(ch);
    }

    public static int convertDpToPixel(Context context, float dp) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                r.getDisplayMetrics()
        );
    }

    public static Bitmap decodeSampledBitmapFromResource(String imagePath,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static String getImagePathFromURI(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        String path = null;
        if (cursor != null) {
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = context.getContentResolver().query(
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ",
                    new String[]{document_id}, null);
            if (cursor != null) {
                cursor.moveToFirst();
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();
            }
        }
        Log.d(TAG, "getImagePathFromURI " + path);
        return path;
    }

    public static String getPDFPath(Context context, Uri uri) {

        String id = DocumentsContract.getDocumentId(uri);
        if (id.startsWith("msf:")) {
            id = id.replaceFirst("msf:", "");
        }

        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/all_downloads"), Long.parseLong(id));

        String[] projection = {MediaStore.Images.Media.DATA};
        @SuppressLint("Recycle") Cursor cursor = context.getContentResolver().query(contentUri,
                projection, null, null, null);
        assert cursor != null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    public static int getPriorityNum(Priority selectedPriority) {
        if (selectedPriority != null) {
            switch (selectedPriority.getValue()) {
                case "Highest":
                    return 5;

                case "High":
                    return 4;

                case "Medium":
                    return 3;

                case "Low":
                    return 2;

                case "Lowest":
                    return 1;

            }
        }
        return -1;
    }


    public static List<Priority> getPriorityList() {
        List<Priority> priorityList = new ArrayList<>();
//        Priority select = new Priority("Select priority", -1);
        Priority highest = new Priority("Highest", R.drawable.ic_highest);
        Priority high = new Priority("High", R.drawable.ic_high);
        Priority medium = new Priority("Medium", R.drawable.ic_medium);
        Priority low = new Priority("Low", R.drawable.ic_low);
        Priority lowest = new Priority("Lowest", R.drawable.ic_lowest);

//        priorityList.add(select);
        priorityList.add(highest);
        priorityList.add(high);
        priorityList.add(medium);
        priorityList.add(low);
        priorityList.add(lowest);
        return priorityList;
    }

    public static byte[] bitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        byte[] byteArray = stream.toByteArray();
        bitmap.recycle();
        return byteArray;
    }

    public static String getMonth(int month) {
        switch (month) {
            case 1:
                return "Jan";

            case 2:
                return "Feb";

            case 3:
                return "Mar";

            case 4:
                return "Apr";

            case 5:
                return "May";

            case 6:
                return "Jun";

            case 7:
                return "Jul";

            case 8:
                return "Aug";

            case 9:
                return "Sep";

            case 10:
                return "Oct";

            case 11:
                return "Nov";

            case 12:
                return "Dec";
        }

        return "";
    }

    public static String removeLastCharater(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == ',') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

    public static Retrofit getRetrofitInstance() {

        OkHttpClient client = getOkHttp();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(ProtoConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static Retrofit getRetrofitInstanceJSON() {

        OkHttpClient client = getOkHttpJSON();

        String base_url = Hawk.get(Constants.BASE_URL);
        return new Retrofit.Builder()
                .client(client)
                .baseUrl(base_url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static OkHttpClient getOkHttp() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/protobuf");
                    requestBuilder.header("Accept", "application/protobuf");
                    return chain.proceed(requestBuilder.build());
                })
                .writeTimeout(3, TimeUnit.MINUTES);

        okHttpClient.addInterceptor(logging);
        return okHttpClient.build();
    }

    public static String getAllParticipants(Inbox inbox) {
        Account user = AccountRepo.getInstance().getAccount();
        if (!inbox.getParticipantList().isEmpty()) {
            StringBuilder participants = new StringBuilder();
            for (Participant participant : inbox.getParticipantList()) {
                if (!user.getAccountId().equals(participant.getEmployee().getAccountId())) {
                    participants.append(participant.getEmployee().getName());
                    participants.append(", ");
                }
            }

            String trimmed = participants.toString().trim();
            if (trimmed.length() > 0) return trimmed.substring(0, trimmed.length() - 1);
            else return "";
        } else return "";
    }

    public static OkHttpClient getOkHttpJSON() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .addNetworkInterceptor(chain -> {
                    Request.Builder requestBuilder = chain.request().newBuilder();
                    requestBuilder.header("Content-Type", "application/json");
                    requestBuilder.header("Accept", "application/json");
                    return chain.proceed(requestBuilder.build());
                })
                .writeTimeout(3, TimeUnit.MINUTES);

        okHttpClient.addInterceptor(logging);
        return okHttpClient.build();
    }

    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    public static String getStringPdf(Uri filepath, Context context) {
        InputStream inputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            inputStream = context.getContentResolver().openInputStream(filepath);

            byte[] buffer = new byte[1024];
            byteArrayOutputStream = new ByteArrayOutputStream();

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        byte[] pdfByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(pdfByteArray, Base64.DEFAULT);
    }
}
