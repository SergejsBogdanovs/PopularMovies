package lv.st.sbogdano.popularmovies.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lv.st.sbogdano.popularmovies.AppDelegate;
import lv.st.sbogdano.popularmovies.BuildConfig;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.database.VideoEntry;

/**
 * Adapter to show movie trailers
 */
public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder> {

    private List<VideoEntry> mVideoEntries;
    private Context mContext;

    public VideosAdapter(Context context, @NonNull List<VideoEntry> videoEntries) {
        mVideoEntries = videoEntries;
        mContext = context;
    }

    @Override
    public VideosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewView = inflater.inflate(R.layout.video_item, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(VideosAdapter.ViewHolder holder, int position) {
        VideoEntry videoEntry = mVideoEntries.get(position);

        holder.mYouTubeThumbnailView.initialize(
                BuildConfig.YOUTUBE_API_KEY, new YouTubeThumbnailView.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeThumbnailLoader youTubeThumbnailLoader) {

                youTubeThumbnailLoader.setVideo(videoEntry.getKey());

                youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                    @Override
                    public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                        youTubeThumbnailView.setVisibility(View.VISIBLE);
                        holder.mRelativeLayout.setVisibility(View.VISIBLE);
                        youTubeThumbnailLoader.release();
                    }

                    @Override
                    public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                    }
                });
            }

            @Override
            public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView,
                                                YouTubeInitializationResult youTubeInitializationResult) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if (null == mVideoEntries) {
            return 0;
        }
        return mVideoEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.relative_layout_over_youtube)
        RelativeLayout mRelativeLayout;
        @BindView(R.id.youtube_thumbnail)
        YouTubeThumbnailView mYouTubeThumbnailView;
        @BindView(R.id.btn_youTube_player)
        ImageView mPlayButton;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mPlayButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                    (Activity) mContext,
                    BuildConfig.YOUTUBE_API_KEY,
                    mVideoEntries.get(getLayoutPosition()).getKey(),
                    100,
                    false,
                    true);
            mContext.startActivity(intent);
        }
    }

}
