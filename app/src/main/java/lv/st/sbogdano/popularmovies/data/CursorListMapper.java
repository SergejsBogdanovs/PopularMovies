package lv.st.sbogdano.popularmovies.data;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Function;

/**
 * Converts Cursor to List observables
 */
class CursorListMapper<T> implements Function<Cursor, List<T>> {

    private final Function<Cursor, T> mTransform;

    public CursorListMapper(Function<Cursor, T> transform) {
        mTransform = transform;
    }

    @Override
    public List<T> apply(Cursor cursor) throws Exception {
        List<T> list = new ArrayList<>();
        if (cursor == null || cursor.isClosed() || !cursor.moveToFirst()) {
            return list;
        }
        do {
            list.add(mTransform.apply(cursor));
        } while (cursor.moveToNext());
        return list;
    }
}
