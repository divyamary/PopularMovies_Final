package in.divyamary.moviereel.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import in.divyamary.moviereel.data.MovieContract.MovieDetailsEntry;
import in.divyamary.moviereel.data.MovieContract.MovieReviewsEntry;
import in.divyamary.moviereel.data.MovieContract.MovieVideosEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIEDETAILS_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieDetailsEntry.TABLE_NAME + " (" +
                MovieDetailsEntry._ID + " INTEGER PRIMARY KEY," +
                MovieDetailsEntry.COLUMN_MOVIE_ID + " INTEGER UNIQUE NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_RUNTIME + " INTEGER NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_POPULARITY + " REAL NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_VOTE_AVG + " REAL NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_VOTE_CNT + " INTEGER NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_DIRECTOR + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_GENRES + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_CERTFICATION + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_LANGUAGES + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_CASTNAMES + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_CASTPATHS + " TEXT NOT NULL, " +
                MovieDetailsEntry.COLUMN_MOVIE_VIDEO_URI + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_MOVIEREVIEWS_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieReviewsEntry.TABLE_NAME + " (" +
                MovieReviewsEntry._ID + " INTEGER PRIMARY KEY," +
                MovieReviewsEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieReviewsEntry.COLUMN_REVIEW_AUTHOR + " TEXT NOT NULL, " +
                MovieReviewsEntry.COLUMN_REVIEW_CONTENT + " TEXT NOT NULL, " +
                MovieReviewsEntry.COLUMN_REVIEW_URL + " TEXT NOT NULL " +
                " );";

        final String SQL_CREATE_MOVIEVIDEOS_TABLE = "CREATE TABLE IF NOT EXISTS " + MovieVideosEntry.TABLE_NAME + " (" +
                MovieVideosEntry._ID + " INTEGER PRIMARY KEY," +
                MovieVideosEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_LANG + " TEXT NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_KEY + " TEXT NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_NAME + " TEXT NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_SITE + " TEXT NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_SIZE + " INTEGER NOT NULL, " +
                MovieVideosEntry.COLUMN_VIDEO_TYPE + " TEXT NOT NULL " +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIEDETAILS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIEREVIEWS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIEVIDEOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieDetailsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieVideosEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieReviewsEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
