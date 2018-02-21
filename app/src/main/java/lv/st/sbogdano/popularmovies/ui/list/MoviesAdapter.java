package lv.st.sbogdano.popularmovies.ui.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.MovieEntry;
import lv.st.sbogdano.popularmovies.data.model.content.Movie;
import lv.st.sbogdano.popularmovies.utilities.Images;

/**
 * Exposes a list of movies forecasts from a list of {@link MovieEntry} to a {@link
 * RecyclerView}.
 */
public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private final int mImageWidth;
    private final int mImageHeight;

    private List<MovieEntry> mMovies;
    private Context mContext;

    private final MoviesAdapterOnItemClickHandler mClickHandler;

    public interface MoviesAdapterOnItemClickHandler {
        void onItemClick(int movieId);
    }

    public MoviesAdapter(Context context,
                         MoviesAdapterOnItemClickHandler clickHandler,
                         List<MovieEntry> movies,
                         int imageWidth,
                         int imageHeight) {
        mContext = context;
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


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.image) ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            int movieId = mMovies.get(adapterPosition).getMovieId();
            mClickHandler.onItemClick(movieId);
        }
    }
}
