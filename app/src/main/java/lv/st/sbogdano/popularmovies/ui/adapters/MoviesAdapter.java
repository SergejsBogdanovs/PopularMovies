package lv.st.sbogdano.popularmovies.ui.adapters;

import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.room.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.utilities.Images;

/**
 * Exposes a list of movies forecasts from a list of {@link Movie} to a {@link
 * RecyclerView}.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final int mImageWidth;
    private final int mImageHeight;

    private List<MovieEntry> mMovies;

    private final MoviesAdapterOnItemClickHandler mClickHandler;

    public interface MoviesAdapterOnItemClickHandler {
        void onItemClick(MovieEntry movie, ImageView moviePoster);
    }

    public MoviesAdapter(MoviesAdapterOnItemClickHandler clickHandler,
                         List<MovieEntry> movies,
                         int imageWidth,
                         int imageHeight) {
        mClickHandler = clickHandler;
        mMovies = movies;
        mImageWidth = imageWidth;
        mImageHeight = imageHeight;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View movieView = inflater.inflate(R.layout.image_item, parent, false);
        ImageView imageView = movieView.findViewById(R.id.image);
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        layoutParams.height = mImageHeight;
        layoutParams.width = mImageWidth;
        imageView.requestLayout();

        return new ViewHolder(movieView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MovieEntry movie = mMovies.get(position);
        Images.loadMovie(holder.moviePoster, movie, Images.WIDTH_185);
        Images.fetch(movie.getPosterPath(), Images.WIDTH_780);

        ViewCompat.setTransitionName(holder.moviePoster, movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if (null == mMovies) {
            return 0;
        }
        return mMovies.size();
    }

    public void swapMovies(final List<MovieEntry> newMovies) {
        mMovies.clear();
        mMovies.addAll(newMovies);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            MovieEntry movie = mMovies.get(getAdapterPosition());
            mClickHandler.onItemClick(movie, moviePoster);
        }
    }
}
