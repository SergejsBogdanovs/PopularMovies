package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.ui.detail.DetailActivity;
import lv.st.sbogdano.popularmovies.ui.settings.SettingsActivity;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity
        implements MoviesAdapter.MoviesAdapterOnItemClickHandler {

    @BindView(R.id.recyclerview_movies)
    RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar mLoadingIndicator;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    private int mPosition = RecyclerView.NO_POSITION;
    private MainActivityViewModel mViewModel;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        MainViewModelFactory factory =
                InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());
        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);
        mViewModel.init();
        subscribeDataStreams(mViewModel);

        initViews();
    }

    private void initViews() {

        int columns = this.getResources().getInteger(R.integer.columns_count);

        // Set up movies view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, columns));

        mMoviesAdapter = getMoviesAdapter();

        mRecyclerView.setAdapter(mMoviesAdapter);
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

    private void subscribeDataStreams(MainActivityViewModel viewModel) {
        viewModel.getMovies().observe(this, this::showMoviesInUi);
    }

    private void showMoviesInUi(List<MovieEntry> movies) {
        mMoviesAdapter.swapMovies(movies);
        if (mPosition == RecyclerView.NO_POSITION) {
            mPosition = 0;
        }
        mRecyclerView.smoothScrollToPosition(mPosition);
        if (movies != null && movies.size() != 0) {
            showMoviesDataView();
        } else {
            showLoading();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel.onResume();
        subscribeDataStreams(mViewModel);
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
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method will make the View for the movie data visible and hide the error message and
     * loading indicator.
     */
    private void showMoviesDataView() {
        // First, hide the loading indicator
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        // Finally, make sure the movies data is visible
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the loading indicator visible and hide the movie View and error
     * message.
     */
    private void showLoading() {
        // Then, hide the movie data
        mRecyclerView.setVisibility(View.INVISIBLE);
        // Finally, show the loading indicator
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }
}
