package com.analyzer.domain;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Point {
    private Integer id;
    private Double value;
    private Bar referencePoint;

    public Point(Integer id, Double value, Bar referencePoint) {
        this.id = id;
        this.value = value;
        this.referencePoint = referencePoint;
    }

    public Point() {

    }

    public Integer getId() {
        return id;
    }

    public Double getValue() {
        return value;
    }

    public Bar getReferencePoint() {
        return referencePoint;
    }
    public static List<List<Point>> asList(List<Bar> bars, List<Point> inputList){
        return recursive(bars,bars.size()-2,inputList);
    }

    @Override
    public String toString() {
        return "{id="+id.toString()+",value="+value.toString()+"}";
    }

    public static List<List<Point>> recursive(List<Bar> bars, Integer index, List<Point> inputList){

        if(index==-1)
            return  new ArrayList<List<Point>>(Arrays.asList(inputList));

        List<List<Point>> outputList = null;

        Bar nextBar= bars.get(index+1);
        Bar currentBar= bars.get(index);
            if(currentBar.getHigh()>nextBar.getHigh()) {
                if(currentBar.getLow()<nextBar.getLow()){
                    List<Point> newList=  new ArrayList<>();
                    newList.addAll(inputList);
                    newList.add(new Point(currentBar.getId(), currentBar.getLow(), currentBar));
                    newList.add(new Point(currentBar.getId(), currentBar.getHigh(), currentBar));
                    outputList=Point.mergeList(outputList,recursive(bars, index - 1, newList));
                    inputList.add(new Point(currentBar.getId(), currentBar.getHigh(), currentBar));
                    inputList.add(new Point(currentBar.getId(), currentBar.getLow(), currentBar));
                    outputList=Point.mergeList(outputList,recursive(bars, index - 1, inputList));
                }
                else {
                    inputList.add(new Point(currentBar.getId(), currentBar.getHigh(), currentBar));
                    outputList=Point.mergeList(outputList,recursive(bars, index - 1, inputList));
                }
            }
            else{
                if(currentBar.getLow()<nextBar.getLow())
                    inputList.add(new Point(currentBar.getId(), currentBar.getLow(), currentBar));
                outputList=Point.mergeList(outputList,recursive(bars, index - 1, inputList));
            }
            return outputList;
    }

    private static List<List<Point>> mergeList(List<List<Point>> outputList, List<List<Point>> listToMerge) {
        if(outputList==null)
            return listToMerge;
        else {
            outputList.addAll(listToMerge);
            return outputList;
        }
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setReferencePoint(Bar referencePoint) {
        this.referencePoint = referencePoint;
    }
}
