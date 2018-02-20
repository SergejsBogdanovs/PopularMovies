package lv.st.sbogdano.popularmovies.ui.list;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.ui.settings.SettingsActivity;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recyclerview_movies) RecyclerView mRecyclerView;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private int mPosition = RecyclerView.NO_POSITION;
    private MainActivityViewModel mViewModel;
    private MoviesAdapter mMoviesAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Set up movies view
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        mMoviesAdapter = new MoviesAdapter(this);
        mRecyclerView.setAdapter(mMoviesAdapter);

        MainViewModelFactory factory =
                InjectorUtils.provideMainActivityViewModelFactory(this.getApplicationContext());

        mViewModel = ViewModelProviders.of(this, factory).get(MainActivityViewModel.class);

        mViewModel.getMovies().observe(this, movies -> {
            mMoviesAdapter.swapMovies(movies);
            if (mPosition == RecyclerView.NO_POSITION) {
                mPosition = 0;
            }
            mRecyclerView.smoothScrollToPosition(mPosition);

            // Show the movies list or the loading screen based on whether the movies data exists
            // and is loaded
            if (movies != null && movies.size() != 0) {
                showMoviesDataView();
            } else {
                showLoading();
            }
        });

    }

//    /**
//     * This method is for responding to clicks from our list.
//     */
//    @Override
//    public void onItemClick(Movie movie) {
//        Intent weatherDetailIntent = new Intent(this, MovieDetailsActivity.class);
//        long timestamp = date.getTime();
//        weatherDetailIntent.putExtra(MovieDetailsActivity.WEATHER_ID_EXTRA, timestamp);
//        startActivity(weatherDetailIntent);
//    }

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
