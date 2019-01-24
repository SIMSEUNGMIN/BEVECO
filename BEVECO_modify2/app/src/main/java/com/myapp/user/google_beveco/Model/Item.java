package com.myapp.user.google_beveco.Model;

public class Item {
    private String _title;
    private String _address;
    private String _tel;

    //getter/setter
    public void setTitle(String newTitle){
        this._title = newTitle;
    }

    public void setAddress(String newAddress){
        this._address = newAddress;
    }

    public void setTel(String newTel){
        this._tel = newTel;
    }

    public String title(){
        return this._title;
    }

    public String address(){
        return this._address;
    }

    public String tel(){
        return this._tel;
    }
}
