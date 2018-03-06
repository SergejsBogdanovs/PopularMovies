package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.Resource;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.databinding.ActivityMainBinding;
import lv.st.sbogdano.popularmovies.ui.adapters.MoviesAdapter;
import lv.st.sbogdano.popularmovies.ui.detail.DetailActivity;
import lv.st.sbogdano.popularmovies.ui.settings.Preferences;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnItemClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private int mPosition = RecyclerView.NO_POSITION;
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

        subscribeDataStreams();

        initViews();
    }

    private void initViews() {

        int columns = this.getResources().getInteger(R.integer.columns_count);

        // Set up movies view
        mMainBinding.recyclerviewMovies.setHasFixedSize(true);
        mMainBinding.recyclerviewMovies.setLayoutManager(new GridLayoutManager(this, columns));

        mMoviesAdapter = getMoviesAdapter();

        mMainBinding.recyclerviewMovies.setAdapter(mMoviesAdapter);
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

    private void subscribeDataStreams() {
        mViewModel.getMovies().observe(this, resources -> {
            switch (resources.status) {
                case SUCCESS: {
                    showMoviesInUi(resources.data);
                    break;
                }
                case LOADING: {
                    showLoading();
                }
            }
        });
    }

    private void showMoviesInUi(List<MovieEntry> movies) {
        mMoviesAdapter.swapMovies(movies);
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mMainBinding.recyclerviewMovies.smoothScrollToPosition(mPosition);
        if (movies != null && movies.size() != 0) {
            showMoviesDataView();
        } else {
            showLoading();
        }
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
                    restartViewModel();
                    break;

                case R.id.top_rated:
                    Preferences.setPrefs(getString(R.string.movie_top_rated));
                    restartViewModel();
                    break;

                case R.id.favorite:
                    Preferences.setPrefs(getString(R.string.movie_favorite));
                    restartViewModel();
                    break;
            }
            return true;
        });

        popup.show();
    }

    private void restartViewModel() {
        mViewModel.onResume();
        subscribeDataStreams();
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
