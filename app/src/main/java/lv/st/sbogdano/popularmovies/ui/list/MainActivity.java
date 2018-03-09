package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.MoviesType;
import lv.st.sbogdano.popularmovies.databinding.ActivityMainBinding;
import lv.st.sbogdano.popularmovies.ui.adapters.MoviesAdapter;
import lv.st.sbogdano.popularmovies.ui.detail.DetailActivity;
import lv.st.sbogdano.popularmovies.ui.settings.Preferences;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnItemClickHandler {

    private static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout_state";
    private Parcelable mState;
    private static Bundle mBundleRecyclerViewState;

    private MainActivityViewModel mViewModel;
    private MoviesAdapter mMoviesAdapter;

    private ActivityMainBinding mMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainBinding.executePendingBindings();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MainViewModelFactory factory =
                InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mViewModel.init();

        reloadData();

        initViews();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (mBundleRecyclerViewState != null) {
            mState = mBundleRecyclerViewState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mMainBinding.recyclerviewMovies.getLayoutManager().onRestoreInstanceState(mState);
        }

        MoviesType type = Preferences.getMoviesType();
        switch (type) {
            case FAVORITE:
                mViewModel.onResume();
                getFavoriteMovies();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        mState = mMainBinding.recyclerviewMovies.getLayoutManager().onSaveInstanceState();
        mBundleRecyclerViewState.putParcelable(BUNDLE_RECYCLER_LAYOUT, mState);
    }

    private void reloadData() {
        mViewModel.onResume();
        subscribeDataStreams();
    }

    private void initViews() {

        // Set up movies view
        mMainBinding.recyclerviewMovies.setHasFixedSize(true);
        mMainBinding.recyclerviewMovies.setLayoutManager(new GridLayoutManager(this, numberOfColumns()));

        mMoviesAdapter = getMoviesAdapter();

        mMainBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 500;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2; //to keep the grid aspect
        return nColumns;
    }

    private MoviesAdapter getMoviesAdapter() {

        int columns = this.getResources().getInteger(R.integer.columns_count);
        int imageWidth = this.getResources().getDisplayMetrics().widthPixels / columns;

        TypedValue typedValue = new TypedValue();
        this.getResources().getValue(R.dimen.rows_count, typedValue, true);
        float rowsCount = typedValue.getFloat();
        int actionBarHeight = this.getTheme().resolveAttribute(R.attr.actionBarSize, typedValue, true)
                ? TypedValue.complexToDimensionPixelSize(typedValue.data, this.getResources().getDisplayMetrics())
                : 0;
        int imageHeight = (int) ((this.getResources().getDisplayMetrics().heightPixels - actionBarHeight) / rowsCount);

        return new MoviesAdapter(this, new ArrayList<>(), imageWidth, imageHeight);
    }

    // Getting Popular/TopRated movies using Room
    private void subscribeDataStreams() {
        mViewModel.getMovies().observe(this, resources -> {
            if (resources != null) {
                switch (resources.status) {
                    case SUCCESS: {
                        if (resources.data != null) {
                            showMoviesInUi(resources.data);
                        } else {
                            mMainBinding.noData.setVisibility(View.VISIBLE);
                        }
                        break;
                    }
                    case LOADING: {
                        showLoading();
                    }
                }
            }
        });
    }

    // Getting favorite movies using ContentProvider
    private void getFavoriteMovies() {
       // mViewModel.onResume();
        mViewModel.getFavoriteMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MovieEntry>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MovieEntry> movieEntries) {
                        showMoviesInUi(movieEntries);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mMainBinding.noData.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private void showMoviesInUi(List<MovieEntry> movies) {
        showMoviesDataView();
        mMoviesAdapter.swapMovies(movies);
        mMainBinding.recyclerviewMovies.getLayoutManager().onRestoreInstanceState(mState);
    }

    /**
     * Launch DetailActivity
     */
    @Override
    public void onItemClick(MovieEntry movie, ImageView moviePoster) {
        DetailActivity.start(this, movie, moviePoster);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_filter:
                showTypePopUpMenu();
                break;
        }
        return true;
    }

    private void showTypePopUpMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.movie_types, popup.getMenu());

        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.popular:
                    Preferences.setPrefs(getString(R.string.movie_default_type));
                    reloadData();
                    mMainBinding.recyclerviewMovies.smoothScrollToPosition(0);
                    break;

                case R.id.top_rated:
                    Preferences.setPrefs(getString(R.string.movie_top_rated));
                    reloadData();
                    mMainBinding.recyclerviewMovies.smoothScrollToPosition(0);
                    break;

                case R.id.favorite:
                    Preferences.setPrefs(getString(R.string.movie_favorite));
                    onResume();
                    mMainBinding.recyclerviewMovies.smoothScrollToPosition(0);
                    break;
            }
            return true;
        });

        popup.show();
    }

    /**
     * This method will make the View for the movie data visible and hide the error message and
     * loading indicator.
     */
    private void showMoviesDataView() {
        // First, hide the loading indicator
        mMainBinding.pbLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the movies data is visible
        mMainBinding.recyclerviewMovies.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the movie View and error
     * message.
     */
    private void showLoading() {
        // Then, hide the movie data
        mMainBinding.recyclerviewMovies.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mMainBinding.pbLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
