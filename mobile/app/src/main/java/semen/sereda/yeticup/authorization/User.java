package semen.sereda.yeticup.authorization;

import java.io.Serializable;

public class User implements Serializable {
    public long id;
    public String name;
    public String password;
    public String mail;
    public String address;
    public String phone;
    public String photo;
    public String date;

    public User(long id, String name, String password, String mail, String address, String phone, String photo, String date) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.address = address;
        this.phone = phone;
        this.photo = photo;
        this.date = date;
    }

    public User(String name, String password, String mail, String address, String phone) {
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.address = address;
        this.phone = phone;
    }
}
