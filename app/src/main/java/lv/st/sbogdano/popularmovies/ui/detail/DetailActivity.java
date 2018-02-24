package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import lv.st.sbogdano.popularmovies.BuildConfig;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.databinding.ActivityDetailBinding;
import lv.st.sbogdano.popularmovies.utilities.Images;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

/**
 * Displays movie details
 */
public class DetailActivity extends AppCompatActivity {

    public static final String IMAGE = "poster";
    public static final String MOVIE_EXTRA = "MOVIE_ID_EXTRA";

    private DetailActivityViewModel mDetailViewModel;
    private ActivityDetailBinding mDetailBinding;

    private MovieEntry mMovie;

    public static void start(AppCompatActivity activity, MovieEntry movie, View moviePoster) {
        Intent movieDetailIntent = new Intent(activity, DetailActivity.class);
        movieDetailIntent.putExtra(DetailActivity.MOVIE_EXTRA, movie);
        movieDetailIntent.putExtra(IMAGE, ViewCompat.getTransitionName(moviePoster));

        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        moviePoster,
                        ViewCompat.getTransitionName(moviePoster));

        ActivityCompat.startActivity(activity, movieDetailIntent, options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepareTransition();
        setContentView(R.layout.activity_detail);
        supportPostponeEnterTransition();

        mMovie = getIntent().getParcelableExtra(MOVIE_EXTRA);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        DetailViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), mMovie);
        mDetailViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        mDetailBinding.setMovie(mMovie);
        mDetailBinding.executePendingBindings();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadMovieImageIntoAppBar();
        bindMovieToUi();

        subscribeDataStreams();

    }

    private void loadMovieImageIntoAppBar() {
        ImageView appBarImage = findViewById(R.id.appbar_image);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            appBarImage.setTransitionName(IMAGE);
            String url = BuildConfig.IMAGES_BASE_URL + Images.WIDTH_780 + mMovie.getPosterPath();
            Picasso.with(this)
                    .load(url)
                    .noFade()
                    .into(appBarImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            supportStartPostponedEnterTransition();
                        }

                        @Override
                        public void onError() {
                            supportStartPostponedEnterTransition();
                        }
                    });
        }
    }

    private void bindMovieToUi() {
        /***********************
         * Movie Title and Year*
         **********************/
        String year = mMovie.getReleasedDate().substring(0, 4);
        String title = mMovie.getTitle();
        String formattedTitle = getString(R.string.movie_title, title, year);
        mDetailBinding.movieTitle.setText(formattedTitle);

        /***********************
         * Rating*
         **********************/
        String maxRating = "10";
        String average = String.valueOf(mMovie.getVoteAverage());
        average = average.length() > 3 ? average.substring(0, 3) : average;
        average = average.length() == 3 && average.charAt(2) == '0' ? average.substring(0, 1) : average;
        String formattedRating = getString(R.string.rating, average, maxRating);
        mDetailBinding.rating.setText(formattedRating);
    }

    private void subscribeDataStreams() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void prepareTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

}