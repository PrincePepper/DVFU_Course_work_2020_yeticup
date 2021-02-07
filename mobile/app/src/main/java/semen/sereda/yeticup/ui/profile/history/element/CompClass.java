package semen.sereda.yeticup.ui.profile.history.element;

import java.io.Serializable;

public class CompClass implements Serializable {
    public String year;
    public String name;
    public String address;
    public String date;
    public String total_participants;

    public CompClass(String year, String name, String address, String date, String total_participants) {
        this.year = year;
        this.name = name;
        this.address = address;
        this.date = date;
        this.total_participants = total_participants;
    }
}
