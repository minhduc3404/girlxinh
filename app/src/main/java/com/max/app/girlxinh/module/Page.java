package com.max.app.girlxinh.module;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by Forev on 4/2/2016.
 */
public class Page {
    @SerializedName("id")
    public int id;
    @SerializedName("url")
    public String url;
    @SerializedName("cur_pos")
    public int cur_pos;
    @SerializedName("avatar")
    public String avatar;

    public String toJson() {
        JsonObject jo = new JsonObject();
        jo.addProperty("id", id);
        jo.addProperty("url", url);
        jo.addProperty("cur_pos", cur_pos);
        jo.addProperty("avatar", avatar);
        return jo.toString();
    }

    public static String arrayToJson(List<Page> pages) {
        String arrString = "[";
        for (int i = 0; i < pages.size() - 1; i++) {
            arrString += pages.get(i).toJson() + ",";
        }
        arrString += pages.get(pages.size() - 1).toJson() + "]";
        return arrString;
    }

    public static List<Page> toJsonArray(String json) {
        Type type = new TypeToken<List<Page>>() {
        }.getType();
        Gson gs = new Gson();
        return gs.fromJson(json, type);
    }
}
