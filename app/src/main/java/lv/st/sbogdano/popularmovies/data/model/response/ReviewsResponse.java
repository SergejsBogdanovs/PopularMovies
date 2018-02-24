package lv.st.sbogdano.popularmovies.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.model.content.Review;

public class ReviewsResponse {

    @SerializedName("results")
    @Expose
    private List<Review> reviews = null;


    public List<Review> getReviews() {
        return reviews;
    }

}
