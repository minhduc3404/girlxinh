package com.max.app.girlxinh.bus;

/**
 * Created by Forev on 4/7/2016.
 */
public class SettingEvent {
    private String value;
    private String type;

    public SettingEvent(String type, String value) {
        this.setType(type);
        this.setValue(value);
    }

    public static final String QUALITY = "quality";
    public static final String MUSIC = "music";
    public static final String WALLPAPER = "wallpaper";
    public static final String TIME = "time";


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
