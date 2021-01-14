package semen.sereda.yeticup.ui.profile.history.element;

import android.net.Uri;

import java.io.Serializable;

public class HistoryClass implements Serializable {
    private transient String name;
    private transient String role;
    private transient Uri uri = null;
    private transient String date;


    public HistoryClass(String name, String date, String role) {
        this.date = date;
        this.name = name;
        this.role = role;
    }

    public HistoryClass(String name, String date, Uri uri) {
        this.date = date;
        this.name = name;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void SetUri(Uri uri) {
        this.uri = uri;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
