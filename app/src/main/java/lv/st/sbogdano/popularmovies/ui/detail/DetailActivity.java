package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.MaybeObserver;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import lv.st.sbogdano.popularmovies.BuildConfig;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Review;
import lv.st.sbogdano.popularmovies.data.model.content.Video;
import lv.st.sbogdano.popularmovies.databinding.ActivityDetailBinding;
import lv.st.sbogdano.popularmovies.ui.adapters.ReviewsAdapter;
import lv.st.sbogdano.popularmovies.ui.adapters.VideosAdapter;
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
        mDetailBinding.setMovie(mMovie);
        mDetailBinding.executePendingBindings();

        // Setting up ReviewsRecyclerView
        mDetailBinding.reviewsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mDetailBinding.reviewsRecyclerview.setHasFixedSize(true);

        // Setting up VideosRecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mDetailBinding.videosRecyclerview.setLayoutManager(linearLayoutManager);
        mDetailBinding.videosRecyclerview.setHasFixedSize(true);

        // Setup toolbar
        setSupportActionBar(mDetailBinding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Setup ViewModel
        DetailViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext());
        mDetailViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        // Add/Remove from favorites
        mDetailBinding.fabFavorite.setOnClickListener(view ->
                mDetailViewModel.getFavoriteMovie(mMovie.getMovieId())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<MovieEntry>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(List<MovieEntry> movieEntries) {
                                if (movieEntries != null && !movieEntries.isEmpty()) {
                                    mDetailBinding.fabFavorite.setImageResource(R.drawable.star_off);
                                    mDetailViewModel.removeFromFavorite(mMovie);
                                } else {
                                    mDetailBinding.fabFavorite.setImageResource(R.drawable.star);
                                    mDetailViewModel.addToFavorite(mMovie);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        }));

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
        String year = "-";
        if (!mMovie.getReleasedDate().isEmpty()) {
            year = mMovie.getReleasedDate().substring(0, 4);
        }
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

        /***********************
         * FAB image *
         **********************/
        mDetailViewModel.getFavoriteMovie(mMovie.getMovieId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<MovieEntry>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(List<MovieEntry> movieEntries) {
                        if (movieEntries != null && !movieEntries.isEmpty()) {
                            mDetailBinding.fabFavorite.setImageResource(R.drawable.star);
                        } else {
                            mDetailBinding.fabFavorite.setImageResource(R.drawable.star_off);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void subscribeDataStreams() {

        mDetailViewModel.getReviews(mMovie).observe(this, listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS: {
                        if (listResource.data != null && !listResource.data.isEmpty()) {
                            bindReviewToUi(listResource.data);
                        } else {
                            mDetailBinding.reviewTitle.setVisibility(View.GONE);
                            mDetailBinding.reviewsRecyclerview.setVisibility(View.GONE);
                        }
                        break;
                    }
                    case ERROR: {
                        mDetailBinding.reviewTitle.setVisibility(View.GONE);
                        mDetailBinding.reviewsRecyclerview.setVisibility(View.GONE);
                    }
                }
            }
        });

        mDetailViewModel.getVideos(mMovie).observe(this, listResource -> {
            if (listResource != null) {
                switch (listResource.status) {
                    case SUCCESS: {
                        if (listResource.data != null && !listResource.data.isEmpty()) {
                            bindVideoToUi(listResource.data);
                        } else {
                            mDetailBinding.videoTitle.setVisibility(View.GONE);
                            mDetailBinding.videosRecyclerview.setVisibility(View.GONE);
                        }
                        break;
                    }
                    case ERROR: {
                        mDetailBinding.videoTitle.setVisibility(View.GONE);
                        mDetailBinding.videosRecyclerview.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void bindReviewToUi(List<Review> reviews) {
        mDetailBinding.reviewTitle.setVisibility(View.VISIBLE);
        mDetailBinding.reviewsRecyclerview.setVisibility(View.VISIBLE);
        mDetailBinding.reviewsRecyclerview.setAdapter(new ReviewsAdapter(reviews));
    }

    private void bindVideoToUi(List<Video> videos) {
        mDetailBinding.videoTitle.setVisibility(View.VISIBLE);
        mDetailBinding.videosRecyclerview.setVisibility(View.VISIBLE);
        mDetailBinding.videosRecyclerview.setAdapter(new VideosAdapter(this, videos));
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
