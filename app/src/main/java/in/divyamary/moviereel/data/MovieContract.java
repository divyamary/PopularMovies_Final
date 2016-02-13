package in.divyamary.moviereel.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;


public class MovieContract {

    public static final String CONTENT_AUTHORITY = "in.divyamary.moviereel";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE_DETAILS = "moviedetails";
    public static final String PATH_MOVIE_REVIEWS = "moviereviews";
    public static final String PATH_MOVIE_VIDEOS = "movievideos";
    public static final String PATH_MOVIE_ID = "id";

    public static final class MovieDetailsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_DETAILS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAILS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_DETAILS;

        public static final String TABLE_NAME = "moviedetails";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_OVERVIEW = "overview";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "release_date";
        public static final String COLUMN_MOVIE_RUNTIME = "runtime";
        public static final String COLUMN_MOVIE_POPULARITY = "popularity";
        public static final String COLUMN_MOVIE_VOTE_AVG = "vote_avg";
        public static final String COLUMN_MOVIE_VOTE_CNT = "vote_cnt";
        public static final String COLUMN_MOVIE_DIRECTOR = "director";
        public static final String COLUMN_MOVIE_GENRES = "genres";
        public static final String COLUMN_MOVIE_CERTFICATION = "certification";
        public static final String COLUMN_MOVIE_LANGUAGES = "spoken_languages";
        public static final String COLUMN_MOVIE_CASTNAMES = "cast_names";
        public static final String COLUMN_MOVIE_CASTPATHS = "cast_paths";
        public static final String COLUMN_MOVIE_VIDEO_URI = "video_uri";

        public static Uri buildMovieDetailsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class MovieReviewsEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_REVIEWS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_REVIEWS;

        public static final String TABLE_NAME = "moviereviews";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_AUTHOR = "author";
        public static final String COLUMN_REVIEW_CONTENT = "content";
        public static final String COLUMN_REVIEW_URL = "url";

        public static Uri buildMovieReviewsUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }

    public static final class MovieVideosEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE_VIDEOS).build();
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_VIDEOS;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE_VIDEOS;

        public static final String TABLE_NAME = "movievideos";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_VIDEO_LANG = "lang_code";
        public static final String COLUMN_VIDEO_KEY = "key";
        public static final String COLUMN_VIDEO_NAME = "name";
        public static final String COLUMN_VIDEO_SITE = "site";
        public static final String COLUMN_VIDEO_SIZE = "size";
        public static final String COLUMN_VIDEO_TYPE = "type";

        public static Uri buildMovieVideosUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getMovieIdSettingFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }
    }
}
