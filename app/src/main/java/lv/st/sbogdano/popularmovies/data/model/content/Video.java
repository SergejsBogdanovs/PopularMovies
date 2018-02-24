package lv.st.sbogdano.popularmovies.data.model.content;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Video {

    @SerializedName("key")
    @Expose
    private String key;

    @SerializedName("name")
    @Expose
    private String name;

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
