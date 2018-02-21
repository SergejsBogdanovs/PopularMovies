package lv.st.sbogdano.popularmovies.ui.detail;

import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.databinding.ActivityDetailBinding;
import lv.st.sbogdano.popularmovies.utilities.InjectorUtils;

/**
 * Displays movie details
 */
public class DetailActivity extends AppCompatActivity{

    public static final String MOVIE_ID_EXTRA = "MOVIE_ID_EXTRA";

    private DetailActivityViewModel mViewModel;
    private ActivityDetailBinding mDetailBinding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDetailBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, -1);

        DetailViewModelFactory factory =
                InjectorUtils.provideDetailViewModelFactory(this.getApplicationContext(), movieId);

        mViewModel = ViewModelProviders.of(this, factory).get(DetailActivityViewModel.class);

        mViewModel.getMovie().observe(this, movieEntry -> {
            if (movieEntry == null) {
                bindMovieToUI(movieEntry);
            }
        });
    }

    private void bindMovieToUI(MovieEntry movieEntry) {

    }
}
