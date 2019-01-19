package com.myapp.user.google_beveco.Model;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Hotel {

    private StringBuilder urlBuilder = new StringBuilder("http://api.visitkorea.or.kr/openapi/service/rest/KorService/searchStay");

    public void searchSetting(){

        try { //인증키를 확인
            urlBuilder.append("?" + URLEncoder.encode("ServiceKey", "UTF-8") + "=wAzhhNfXps4BKRzNDtdridy3993OmoOumv3Et9IWGrErLnNYS0drR9UatK5cN1QnPhXpPoin5I%2FzOCm2KLv62w%3D%3D");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try { //한 페이지 당 요청 개수
            urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("20", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try { //현재 페이지 번호
            urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try { //휴대폰 기종 확인
            urlBuilder.append("&" + URLEncoder.encode("MobileOS","UTF-8") + "=" + URLEncoder.encode("AND", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try { //서비스명 = 어플명
            urlBuilder.append("&" + URLEncoder.encode("MobileApp","UTF-8") + "=" + URLEncoder.encode("BEVECO", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try { //콘텐츠ID
            urlBuilder.append("&" + URLEncoder.encode("contentId","UTF-8") + "=" + URLEncoder.encode("201702034", "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public void resultSearch() throws IOException{

        URL url = null;
        url = new URL(urlBuilder.toString());

        HttpURLConnection conn = null;
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET"); //웹 서버로부터 리소스를 가져온다.
        conn.setRequestProperty("Content-type", "application/json"); //권한 요청

        //서버 응답에 성공했을 시 응답 코드가 생성
        System.out.println("Response code: " + conn.getResponseCode());
        //응답 메시지
        System.out.println("Response message: " + conn.getResponseMessage());


        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            //200에서 300사이일 경우 서버 응답에 성공한 것
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            //서버 응답 실패
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        String[] lines = null;

        if((line = rd.readLine()) != null){
            lines = line.split("\n");
        }

        rd.close();
        conn.disconnect();

        for(String output : lines){
            System.out.println(output);
        }
    }

}


