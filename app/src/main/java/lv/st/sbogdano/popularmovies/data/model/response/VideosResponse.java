package lv.st.sbogdano.popularmovies.data.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lv.st.sbogdano.popularmovies.data.model.content.Video;

public class VideosResponse {

    @SerializedName("results")
    @Expose
    private List<Video> videos = null;

    public List<Video> getVideos() {
        return videos;
    }
}
