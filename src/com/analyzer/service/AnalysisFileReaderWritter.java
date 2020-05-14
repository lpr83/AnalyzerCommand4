package com.analyzer.service;

import com.analyzer.domain.AnalysisResult;
import com.analyzer.domain.Point;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Stream;

public class AnalysisFileReaderWritter {

    void writePointsToFile(List<List<Point>> pointsInputList) {
        Integer fileNumber=0;
        for(List<Point> singleList : pointsInputList)
        {
            fileNumber++;
            String str ="";
            for(Integer index = singleList.size()-1; index>0; index--){
                Point point=singleList.get(index);
                str +=point.getId().toString()+","+point.getValue().toString()+"\n";
            }
            writeToFile("NORM",fileNumber,str);
        }
    }

    private void writeToFile(String filePrefix, Integer i, String str) {
        String fileName="D:\\"+filePrefix+"_"+i.toString()+".txt";
        Path path = Paths.get(fileName);
        byte[] strToBytes = str.getBytes();

        try {
            Files.write(path, strToBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    Stream<String> readFromFile(String fileName){
        Path path = null;
        path = Paths.get(fileName);

        Stream<String> lines = null;
        try {
            lines = Files.lines(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public void writeAnalysisToFile(AnalysisResult analysisResult) {
        Integer fileNumber=0;
        List<Point> singleList = analysisResult.getMatchLists();
        fileNumber=1;
        String str ="";
        for(Integer index = singleList.size()-1; index>0; index--){
            Point point=singleList.get(index);
            str +=point.getId().toString()+","+point.getValue().toString()+"\n";
        }
        writeToFile("RES",fileNumber,str);

    }
}
