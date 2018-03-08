package lv.st.sbogdano.popularmovies.utilities;

import android.database.Cursor;

import java.util.concurrent.Callable;

import io.reactivex.Observable;

/**
 * Makes Cursor observable
 */
public class CursorObservable {

    private static Callable<Cursor> getData(Cursor cursor) {
        return () -> cursor;
    }

    private static <T>Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(e -> {
            try {
                T observed = func.call();
                if (observed != null) {
                    e.onNext(observed);
                }
                e.onComplete();
            } catch (Exception ex) {
                e.onError(ex);
            }
        });
    }

    public static Observable<Cursor> getCursorObservable(Cursor cursor) {
        return makeObservable(getData(cursor));
    }
}
