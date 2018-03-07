package lv.st.sbogdano.popularmovies.ui.adapters;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import lv.st.sbogdano.popularmovies.BR;
import lv.st.sbogdano.popularmovies.R;
import lv.st.sbogdano.popularmovies.data.model.content.Review;

/**
 * ReviewAdapter for movie reviews
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    private List<Review> mReviewEntries;

    public ReviewsAdapter(@NonNull List<Review> reviews) {
        mReviewEntries = reviews;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View reviewView = inflater.inflate(R.layout.review_item, parent, false);
        return new ViewHolder(reviewView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Review review = mReviewEntries.get(position);
        holder.getDataBinding().setVariable(BR.review, review);
        holder.getDataBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        if (null == mReviewEntries) {
            return 0;
        }
        return mReviewEntries.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ViewDataBinding mDataBinding;

        ViewHolder(View itemView) {
            super(itemView);
            mDataBinding = DataBindingUtil.bind(itemView);
        }

        public ViewDataBinding getDataBinding() {
            return mDataBinding;
        }
    }
}
