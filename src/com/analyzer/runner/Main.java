package com.analyzer.runner;

import com.analyzer.domain.AnalysisResult;
import com.analyzer.service.AnalysisService;

public class Main {

    public static void main(String[] args) {
        Double level = 178.00;
        Integer dateOfCutOff = 20200204;  //Today minus 3 months
        Runner runner = new Runner(new AnalysisService(level,dateOfCutOff));

//        args[0]="-f";
//        args[1]="D:\\tst\\INVESTORMS.mst";
        if(args[0].isEmpty())
            return;

        if(args[0].equals("-f"))
            if(!args[1].isEmpty())
                runner.analyzeFile(args[1]);
            else
                return;

        if(args[0].equals("-d"))
            if(!args[1].isEmpty())
                runner.analyzeDir(args[1]);
            else
                return;

    }
}
