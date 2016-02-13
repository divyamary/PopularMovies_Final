package in.divyamary.moviereel.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isInternetConnected(Context context) {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static Uri getImageURI(String baseURL, String imageSize, String imagePath) {
        return Uri.parse(baseURL).buildUpon().appendPath(imageSize)
                .appendPath(imagePath.replaceAll("\\/", "")).build();
    }

    public static Uri getYoutubeURI(String videoKey) {
        return Uri.parse("https://www.youtube.com/watch").buildUpon().appendQueryParameter("v", videoKey).build();
    }

    public static Uri getYoutubeThumbnail(String videoKey) {
        return Uri.parse("https://img.youtube.com/vi").buildUpon().appendPath(videoKey).appendPath("0.jpg").build();
    }

    public static String[] formatIntoNewDate(String releaseDate) {
        DateFormat formatter = new SimpleDateFormat(Constants.DATE_OLD_FORMAT);
        Date date;
        try {
            date = formatter.parse(releaseDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_NEW_FORMAT);
            String formattedReleaseDate = simpleDateFormat.format(date);
            return formattedReleaseDate.split("\\.");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new String[0];
    }

    public static String getYearFromDate(String releaseDate) {
        DateFormat formatter = new SimpleDateFormat(Constants.DATE_OLD_FORMAT);
        Date date;
        String year = "";
        try {
            date = formatter.parse(releaseDate);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Constants.DATE_NEW_FORMAT);
            String formattedReleaseDate = simpleDateFormat.format(date);
            String dateArray[] = formattedReleaseDate.split("\\.");
            year = dateArray[0];
            return year;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return year;
    }

    public static String removeExtension(String s) {
        String separator = System.getProperty("file.separator");
        String filename;
        // Remove the path upto the filename.
        int lastSeparatorIndex = s.lastIndexOf(separator);
        if (lastSeparatorIndex == -1) {
            filename = s;
        } else {
            filename = s.substring(lastSeparatorIndex + 1);
        }
        // Remove the extension.
        int extensionIndex = filename.lastIndexOf(".");
        if (extensionIndex == -1)
            return filename;
        return filename.substring(0, extensionIndex);
    }
}
