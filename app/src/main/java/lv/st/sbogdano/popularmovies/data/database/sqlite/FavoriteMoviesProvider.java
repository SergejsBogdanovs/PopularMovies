package lv.st.sbogdano.popularmovies.data.database.sqlite;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import static lv.st.sbogdano.popularmovies.data.database.sqlite.FavoriteMoviesContract.CONTENT_AUTHORITY;

/**
 * Content Provider for favorite movies.
 */
public class FavoriteMoviesProvider extends ContentProvider{

    public static final int CODE_FAVORITE_MOVIES = 100;
    public static final int CODE_FAVORITE_MOVIE = 101;

    private static final UriMatcher URI_MATCHER;

    private SQLiteOpenHelper mSQLiteHelper;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

        // All favorite movies
        URI_MATCHER.addURI(
                CONTENT_AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES,
                CODE_FAVORITE_MOVIES);

        // Single favorite movie
        URI_MATCHER.addURI(
                CONTENT_AUTHORITY,
                FavoriteMoviesContract.PATH_FAVORITE_MOVIES + "/#",
                CODE_FAVORITE_MOVIE
        );
    }

    @Override
    public boolean onCreate() {
        mSQLiteHelper = new FavoriteMoviesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri,
                        @Nullable String[] projection,
                        @Nullable String selection,
                        @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {

        Cursor cursor;

        switch (URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                cursor = mSQLiteHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            }

            case CODE_FAVORITE_MOVIE: {
                String[] selectionArguments = new String[] {uri.getLastPathSegment()};

                cursor = mSQLiteHelper.getReadableDatabase().query(
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        projection,
                        FavoriteMoviesContract.FavoriteMoviesEntry.MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                long id = database.insertWithOnConflict(
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        null,
                        contentValues,
                        SQLiteDatabase.CONFLICT_REPLACE);
                return ContentUris.withAppendedId(uri, id);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mSQLiteHelper.getWritableDatabase();

        if (null == selection) selection = "1";

        switch (URI_MATCHER.match(uri)) {
            case CODE_FAVORITE_MOVIES: {
                return database.delete(
                        FavoriteMoviesContract.FavoriteMoviesEntry.TABLE_NAME,
                        selection,
                        selectionArgs);
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
