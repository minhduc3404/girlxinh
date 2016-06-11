
package com.max.app.girlxinh.module;

;import com.google.gson.annotations.SerializedName;


public class Blog {

    @SerializedName("title")

    public String title;
    @SerializedName("name")

    public String name;
    @SerializedName("total_posts")

    public Integer totalPosts;
    @SerializedName("posts")

    public Integer posts;
    @SerializedName("url")

    public String url;
    @SerializedName("updated")

    public Integer updated;
    @SerializedName("description")

    public String description;
    @SerializedName("is_nsfw")

    public Boolean isNsfw;
    @SerializedName("ask")

    public Boolean ask;
    @SerializedName("ask_page_title")

    public String askPageTitle;
    @SerializedName("ask_anon")

    public Boolean askAnon;
    @SerializedName("submission_page_title")

    public String submissionPageTitle;
    @SerializedName("share_likes")

    public Boolean shareLikes;

}
