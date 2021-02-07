package semen.sereda.yeticup.ui.profile.history.element;

import java.io.Serializable;

public class HistoryClass implements Serializable {
    public transient String name;
    public transient String role;

    public transient String date;


    public HistoryClass(String name, String date, String role) {
        this.date = date;
        this.name = name;
        this.role = role;
    }
}
