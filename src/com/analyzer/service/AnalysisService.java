package com.analyzer.service;

import com.analyzer.domain.AnalysisResult;
import com.analyzer.domain.Bar;
import com.analyzer.domain.Point;


import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnalysisService {
    private Double level;
    private Integer dateOfCutOff;
    private List<Bar> barsFromFile;
    private List<List<Point>> pointsInputList;
    private AnalysisFileReaderWritter analysisFileReaderWritter = new AnalysisFileReaderWritter();

    public AnalysisService(Double level, Integer dateOfCutOff) {
        this.level = level;
        this.dateOfCutOff = dateOfCutOff;
    }


    public void prepareAnalysis(String fileName) {
        Stream<String> lines = analysisFileReaderWritter.readFromFile(fileName);

        List<String> linesList = lines
                .skip(1)
                .collect(Collectors.toList());
        Collections.reverse(linesList);
        lines.close();

        barsFromFile = (ArrayList<Bar>) linesList
                .stream()
                .map(singleLine -> new Bar(singleLine))
                .filter(singleBar -> singleBar.getId() > dateOfCutOff)
                .collect(Collectors.toList());
        Collections.reverse(barsFromFile);
    }
    public AnalysisResult runAnalysis(){
        AnalysisResult analysisResult = new AnalysisResult();
        if(barsFromFile.size()==0)
            return analysisResult;
        Point firstPoint = new Point(barsFromFile.get(barsFromFile.size() - 1).getId(),barsFromFile.get(barsFromFile.size() - 1).getLow(),barsFromFile.get(barsFromFile.size() - 1));
        ArrayList<Point> firstList = new ArrayList<Point>(Arrays.asList(firstPoint));
        pointsInputList = Point.asList(barsFromFile, firstList); //Lists each sorted [0] is the actual point in time
//        analysisFileReaderWritter.writePointsToFile(pointsInputList);
        analysisResult= analyse(pointsInputList, firstPoint);   //####Wyjebac conditionPoint oraz uzaleznic dane od poprzedniego sczytu
        analysisFileReaderWritter.writeAnalysisToFile(analysisResult);
        return analysisResult;
    }


    public AnalysisResult analyse(List<List<Point>> pointsInputList, Point conditionPoint) {
        AnalysisResult analysisResult= new AnalysisResult();
        for(Integer index=0; index<pointsInputList.size();index++){
            List<Point> singleList = pointsInputList.get(index);
            List<Point> matchList = new ArrayList<>();
            analysisResult=findMin(singleList, matchList, 1, conditionPoint);
            if(analysisResult.getDecision()== AnalysisResult.DecisionStatuses.OK)
                return analysisResult;
        }
        return analysisResult;
    }


    private AnalysisResult findMin(List<Point> pointList, List<Point> matchList, Integer startIndex, Point conditionPoint) {
        AnalysisResult analysisResult = new AnalysisResult(matchList);

        boolean x= matchlistChecker(matchList);

        if (analysisResult.getDecision() == AnalysisResult.DecisionStatuses.LAST_EXTREMUM_FOUND)
            return findFirstBelow(pointList,matchList,startIndex+1,analysisResult);

        TreeMap<Integer, Point> minimumsList = new TreeMap<>();
        Point startingPeakPoint= matchList.size()>0 ? matchList.get(matchList.size()-1) : null;
        Double startingPeak= matchList.size()>0 ? matchList.get(matchList.size()-1).getValue() : null;
        Double previousLow= matchList.size()>1 ? matchList.get(matchList.size()-2).getValue() : null;

        for (Integer index = startIndex; index < pointList.size(); index++) {
            if((startingPeak!=null) && (pointList.get(index).getValue()>startingPeak))  //if one of peaks was over starting peak
                break;

            if((startingPeakPoint==null)||(pointList.get(index).getId()<startingPeakPoint.getId()-1)) {
                if ((index == pointList.size() - 1) || (pointList.get(index).getValue() < pointList.get(index + 1).getValue()))  //Starts to grow or is last - time to stop
                {
                    if ((previousLow == null) || (pointList.get(index).getValue() < previousLow)) { //middle condition D<G<F
                        if (minimumsList.isEmpty() || pointList.get(index).getValue() < minimumsList.lastEntry().getValue().getValue())
                            minimumsList.put(index, pointList.get(index));
                    }
                }
            }
        }



        List<Point> newList = new ArrayList<>();
        for (Map.Entry<Integer, Point> entry : minimumsList.entrySet()) {
            newList.clear();
            newList.addAll(matchList);
            newList.add(entry.getValue());
            analysisResult = findMax(pointList, newList, entry.getKey() + 1);
            if (analysisResult.getDecision() == AnalysisResult.DecisionStatuses.OK)
                return analysisResult;
        }

        return analysisResult;
    }



    private AnalysisResult findMax(List<Point> pointList, List<Point> matchList, Integer startIndex) {
        AnalysisResult analysisResult = new AnalysisResult(matchList);

        boolean x= matchlistChecker(matchList);

        if (analysisResult.getDecision() == AnalysisResult.DecisionStatuses.LAST_EXTREMUM_FOUND)
            return analysisResult;

        TreeMap<Integer, Point> maximumsList = new TreeMap<>();
        Point startingLowPoint= matchList.size()>0 ? matchList.get(matchList.size()-1) : null;
        Double startingLow= matchList.size()>0 ? matchList.get(matchList.size()-1).getValue() : null;
        Double previousPeak= matchList.size()>1 ? matchList.get(matchList.size()-2).getValue() : null;
        Double previousLow= matchList.size()>2 ? matchList.get(matchList.size()-3).getValue() : null;

        for (Integer index = startIndex; index < pointList.size(); index++) {
            if((startingLow!=null)&&(pointList.get(index).getValue()<=startingLow))  //if one of lows was lower than starting Low
                break;
            if((previousPeak!=null)&&(pointList.get(index).getValue()>=previousPeak))  //if item is over previous Peak
                break;
            if((startingLowPoint==null)||(pointList.get(index).getId()<startingLowPoint.getId()-1)) {
                if ((index == pointList.size() - 1) || (pointList.get(index).getValue() > pointList.get(index + 1).getValue()))  //Starts to decrease or is last - time to stop
                {
                    if ((previousLow == null) || (pointList.get(index).getValue() > previousLow)) {    //middle condition <E
                        if (maximumsList.isEmpty() || pointList.get(index).getValue() >= maximumsList.lastEntry().getValue().getValue())
                            maximumsList.put(index, pointList.get(index));
                    }
                }
            }
        }

        List<Point> newList = new ArrayList<>();



        for (Map.Entry<Integer, Point> entry : maximumsList.entrySet()) {
            newList.clear();
            newList.addAll(matchList);
            newList.add(entry.getValue());
            analysisResult = findMin(pointList, newList, entry.getKey(), newList.get(newList.size() - 2));
            if (analysisResult.getDecision() == AnalysisResult.DecisionStatuses.OK)
                return analysisResult;
        }
        return analysisResult;
    }

    private AnalysisResult findFirstBelow(List<Point> pointList, List<Point> matchList, Integer startIndex, AnalysisResult analysisResult) {
        for (Integer index = startIndex; index < pointList.size(); index++) {
            if(pointList.get(index).getValue() >= matchList.get(matchList.size()-1).getValue())
                break;
            if (pointList.get(index).getValue() < matchList.get(matchList.size()-2).getValue()){
                matchList.add(pointList.get(index));
                analysisResult.setMatchLists(matchList);
                return analysisResult;
            }

        }
        return analysisResult;
    }
    private boolean matchlistChecker(List<Point> matchList) {
        boolean res=
                  matchList.stream().filter(x -> x.getValue()==1.86).findFirst().isPresent()
                          &&matchList.stream().filter(x -> x.getValue()==2.35).findFirst().isPresent()
                          &&matchList.stream().filter(x -> x.getValue()==1.54).findFirst().isPresent()
//                          &&matchList.stream().filter(x -> x.getValue()==1.98).findFirst().isPresent()
                ;
        if(res)
            return res;
        else
            return res;
    }
}