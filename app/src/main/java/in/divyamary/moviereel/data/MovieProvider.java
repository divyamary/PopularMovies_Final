package in.divyamary.moviereel.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class MovieProvider extends ContentProvider {

    static final int MOVIEDETAILS = 100;
    static final int MOVIEDETAILS_WITH_ID = 101;
    static final int MOVIEREVIEWS = 200;
    static final int MOVIEREVIEWS_WITH_ID = 201;
    static final int MOVIEVIDEOS = 300;
    static final int MOVIEVIDEOS_WITH_ID = 301;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String sMovieDetailsIdsSettingSelection = MovieContract.MovieDetailsEntry.TABLE_NAME + "."
            + MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID + " =? ";
    //private static final SQLiteQueryBuilder sMovieJoinQueryBuilder;
    private static final String sMovieReviewsIdsSettingSelection = MovieContract.MovieReviewsEntry.TABLE_NAME + "."
            + MovieContract.MovieReviewsEntry.COLUMN_MOVIE_ID + " =? ";

    /*static {
        sMovieJoinQueryBuilder = new SQLiteQueryBuilder();
        sMovieJoinQueryBuilder.setTables(
                MovieContract.MovieDetailsEntry.TABLE_NAME
                        + " INNER JOIN " + MovieContract.MovieReviewsEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieDetailsEntry.TABLE_NAME +
                        "." + MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.MovieReviewsEntry.TABLE_NAME +
                        "." + MovieContract.MovieReviewsEntry.COLUMN_MOVIE_ID
                        + " INNER JOIN " + MovieContract.MovieVideosEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieDetailsEntry.TABLE_NAME +
                        "." + MovieContract.MovieDetailsEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.MovieVideosEntry.TABLE_NAME +
                        "." + MovieContract.MovieVideosEntry.COLUMN_MOVIE_ID);
    }*/
    private static final String sMovieVideosIdsSettingSelection = MovieContract.MovieVideosEntry.TABLE_NAME + "."
            + MovieContract.MovieVideosEntry.COLUMN_MOVIE_ID + " =? ";
    private MovieDbHelper mOpenHelper;

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAILS, MOVIEDETAILS);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_VIDEOS, MOVIEVIDEOS);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_REVIEWS, MOVIEREVIEWS);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAILS + "/*", MOVIEDETAILS_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_REVIEWS + "/*", MOVIEREVIEWS_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_MOVIE_VIDEOS + "/*", MOVIEVIDEOS_WITH_ID);
        //matcher.addURI(authority, MovieContract.PATH_MOVIE_DETAILS, ALL_MOVIES_WITH_DETAILS);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIEDETAILS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIEDETAILS_WITH_ID: {
                String movieIdSetting = MovieContract.MovieDetailsEntry.getMovieIdSettingFromUri(uri);
                selectionArgs = new String[]{movieIdSetting};
                selection = sMovieDetailsIdsSettingSelection;
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIEREVIEWS_WITH_ID: {
                String movieIdSetting = MovieContract.MovieReviewsEntry.getMovieIdSettingFromUri(uri);
                selectionArgs = new String[]{movieIdSetting};
                selection = sMovieReviewsIdsSettingSelection;
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieReviewsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIEVIDEOS_WITH_ID: {
                String movieIdSetting = MovieContract.MovieVideosEntry.getMovieIdSettingFromUri(uri);
                selectionArgs = new String[]{movieIdSetting};
                selection = sMovieVideosIdsSettingSelection;
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieVideosEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            /*case ALL_MOVIES_WITH_DETAILS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieDetailsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }*/
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIEDETAILS: {
                return MovieContract.MovieDetailsEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIEREVIEWS: {
                return MovieContract.MovieDetailsEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIEVIDEOS: {
                return MovieContract.MovieDetailsEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIEDETAILS_WITH_ID: {
                return MovieContract.MovieDetailsEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIEREVIEWS_WITH_ID: {
                return MovieContract.MovieReviewsEntry.CONTENT_TYPE;
            }
            case MOVIEVIDEOS_WITH_ID: {
                return MovieContract.MovieVideosEntry.CONTENT_TYPE;
            }
           /* case ALL_MOVIES_WITH_DETAILS: {
                return MovieContract.MovieDetailsEntry.CONTENT_TYPE;
            }*/
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case MOVIEDETAILS: {
                long _id = db.insert(MovieContract.MovieDetailsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.MovieDetailsEntry.buildMovieDetailsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case MOVIEREVIEWS: {
                long _id = db.insert(MovieContract.MovieReviewsEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.MovieReviewsEntry.buildMovieReviewsUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case MOVIEVIDEOS: {
                long _id = db.insert(MovieContract.MovieVideosEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = MovieContract.MovieVideosEntry.buildMovieVideosUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIEVIDEOS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieVideosEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MOVIEREVIEWS: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieReviewsEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        if (null == selection) {
            selection = "1";
        }
        switch (match) {
            case MOVIEDETAILS: {
                rowsDeleted = db.delete(
                        MovieContract.MovieDetailsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIEREVIEWS: {
                rowsDeleted = db.delete(
                        MovieContract.MovieReviewsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIEVIDEOS: {
                rowsDeleted = db.delete(
                        MovieContract.MovieVideosEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;
        switch (match) {
            case MOVIEDETAILS: {
                rowsUpdated = db.update(MovieContract.MovieDetailsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case MOVIEREVIEWS: {
                rowsUpdated = db.update(MovieContract.MovieReviewsEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            case MOVIEVIDEOS: {
                rowsUpdated = db.update(MovieContract.MovieVideosEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

}