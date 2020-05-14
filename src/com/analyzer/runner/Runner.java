package com.analyzer.runner;

import com.analyzer.domain.AnalysisResult;
import com.analyzer.service.AnalysisFileReaderWritter;
import com.analyzer.service.AnalysisService;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Runner {
    private  AnalysisService analysisService;

    public Runner(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    public  AnalysisResult analyzeDir(String dirPath){
        AnalysisResult analysisResult = new AnalysisResult();
        String fileLog="";
        String singleLog;
        try (Stream<Path> walk = Files.walk(Paths.get(dirPath))) {

            List<String> result = walk.filter(Files::isRegularFile)
                    .map(x -> x.toString()).collect(Collectors.toList());

            for(String fileName : result){
                analysisService.prepareAnalysis(fileName);
                analysisResult=analysisService.runAnalysis();
                if(analysisResult.getDecision()== AnalysisResult.DecisionStatuses.OK){
//                    AnalysisFileReaderWritter.
                    System.out.println(fileName+"\n"+analysisResult.toString());
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return analysisResult;
    }
    public  AnalysisResult analyzeFile(String fileName) {
        analysisService.prepareAnalysis(fileName);
        AnalysisResult analysisResult = analysisService.runAnalysis();
        System.out.println(analysisResult.toString());
        return analysisResult;
    }
}
