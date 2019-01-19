package com.myapp.user.google_beveco.Model;

public class HotelList {
    private String _title; //이름
    private String _adress; //주소
    private String _tel; //전화번호

    public HotelList(String givenTitle, String givenAdress, String givenTel){
        this._title = givenTitle;
        this._adress = givenAdress;
        this._tel = givenTel;
    }

    public String title(){
        return this._title;
    }

    public String adress(){
        return this._adress;
    }

    public String tel(){
        return this._tel;
    }
}
