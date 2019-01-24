package com.myapp.user.google_beveco.Model;

import com.google.android.gms.location.places.Place;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;

public class ShortestPath {

    private static double DEFAULT_MAX_SIZE = Integer.MAX_VALUE;
    boolean[] isVisit;
    private Place[] selectedPoints;
    private double[][] length;
    private int number;
    private Place[] shortestPoints;
    private int startPosition;

    public void setIsVisit(boolean[] newIsVisit) {
        this.isVisit = newIsVisit;
    }

    public boolean[] getIsVisit() {
        return this.isVisit;
    }

    public void setSelectedPoints(Place[] newSelectedPoints) {
        this.selectedPoints = newSelectedPoints;
    }

    public Place[] getSelectedPoints() {
        return this.selectedPoints;
    }

    public void setShortestPoints(Place[] newShortestPoints) {
        this.shortestPoints = newShortestPoints;
    }

    public Place[] getShortestPoints() {
        return this.shortestPoints;
    }

    public void setLength(double[][] newLength) {
        this.length = newLength;
    }

    public double[][] getLength() {
        return this.length;
    }

    public void setLengthAt(int newRow, int newCol, double newLength) {
        this.length[newRow][newCol] = newLength;
    }

    public double getLengthAt(int  newRow, int newCol) {
        return this.length[newRow][newCol];
    }

    public void setNumber(int newNumber) {
        this.number = newNumber;
    }

    public int getNumber() {
        return this.number;
    }

    public void setStartPosition(int newPosition){
        this.startPosition = newPosition;
    }

    //생성자
    public ShortestPath(int givenNumber, Place[] givenSelectedPoints, int givenStartPosition) {
        this.setIsVisit(new boolean[givenNumber]);
        this.setLength(new double[givenNumber][givenNumber]);
        this.setNumber(givenNumber);
        this.setSelectedPoints(givenSelectedPoints);
        this.setShortestPoints(new Place[givenNumber]);
        this.setStartPosition(givenStartPosition);
        this.initVisit();
        this.initLength();
    }

    //방문여부 초기화
    public void initVisit() {
        for (int i = 0; i < this.getNumber(); i++) {
            this.isVisit[i] = false;
        }
    }

    //거리 초기화 함수
    public void initLength() {
        for (int i = 0; i < this.getNumber(); i++) {
            for (int j = 0; j < this.getNumber(); j++) {
                if (i == j) {
                    this.setLengthAt(i, j, 0);
                } else {
                    this.setLengthAt(i, j, this.findPathDistance(this.getSelectedPoints()[i], this.getSelectedPoints()[j]));
                }
            }
        }
    }

    //직선거리 구함
    public double findPathDistance(Place first, Place second) {
        TMapPolyLine tMapPolyLine = new TMapPolyLine();

        TMapPoint start = new TMapPoint(first.getLatLng().latitude, first.getLatLng().longitude);
        TMapPoint end = new TMapPoint(second.getLatLng().latitude, second.getLatLng().longitude);
        tMapPolyLine.addLinePoint(start);
        tMapPolyLine.addLinePoint(end);
        return tMapPolyLine.getDistance();
    }

    //최단경로를 찾는 함수
    public void findShortestPaths() {

        int length = this.getNumber();
        if(length <1){
            return;
        }
        this.isVisit[startPosition] = true;
        //this.getisVisit()[0] = true;
        int visit = startPosition;
        this.getShortestPoints()[0] = this.getSelectedPoints()[startPosition];
        for (int i = 1; i < length; i++) {
            double shortest = DEFAULT_MAX_SIZE;
            int shortestIndex = 0;
            for (int j = 0; j < length; j++) {
                if (visit != j) {
                    if (this.getLengthAt(visit, j) < shortest && !this.getIsVisit()[j]) {
                        shortest = this.getLengthAt(visit, j);
                        shortestIndex = j;
                    }
                }
            }
            this.shortestPoints[i] = this.selectedPoints[shortestIndex];
            this.isVisit[shortestIndex] = true;
            //this.getShortestPoints()[i]= this.getSelectedPoints()[shortestIndex];
            //this.getisVisit()[shortestIndex] = true;
            visit = shortestIndex;
        }

    }

}
