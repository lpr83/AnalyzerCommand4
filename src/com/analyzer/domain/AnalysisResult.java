package com.analyzer.domain;

import java.util.ArrayList;
import java.util.List;

public class AnalysisResult {
    public AnalysisResult(List<Point> matchList) {
        this.matchLists=matchList;
        this.calculateDecision();
    }

    public AnalysisResult() {
        this.matchLists= new ArrayList<>();
        this.calculateDecision();
    }

    public enum DecisionStatuses{ OK,LAST_EXTREMUM_FOUND,NOT_FOUND};
    private DecisionStatuses decision;
    private List<Point> matchLists;


    public DecisionStatuses getDecision() {
        return decision;
    }

    public List<Point> getMatchLists() {
        return matchLists;
    }

    public void setMatchLists(List<Point> matchLists) {
        this.matchLists = matchLists;
        this.calculateDecision();
    }

    private void calculateDecision(){
        this.decision= DecisionStatuses.NOT_FOUND;
        if(this.matchLists.size()==6)
            this.decision= DecisionStatuses.LAST_EXTREMUM_FOUND;
        if(this.matchLists.size()==7)
            this.decision= DecisionStatuses.OK;
    }

    @Override
    public String toString() {
        if(decision!=DecisionStatuses.OK)
            return "";
        String response="";
        response+=this.getDecision()+"\n";
        response+=this.matchLists.get(0).toString()+"G"+"\n";
        response+=this.matchLists.get(1).toString()+"F"+"\n";
        response+=this.matchLists.get(2).toString()+"E"+"\n";
        response+=this.matchLists.get(3).toString()+"D"+"\n";
        response+=this.matchLists.get(4).toString()+"C"+"\n";
        response+=this.matchLists.get(5).toString()+"B"+"\n";
        response+=this.matchLists.get(6).toString()+"A"+"\n";
        response+="Highs B<D<F\n";
        response+=this.matchLists.get(5).getValue()+"<";
        response+=this.matchLists.get(3).getValue()+"<";
        response+=this.matchLists.get(1).getValue()+"\n";
        response+="Lows A<C<E<G\n";
        response+=this.matchLists.get(6).getValue()+"<";
        response+=this.matchLists.get(4).getValue()+"<";
        response+=this.matchLists.get(2).getValue()+"<";
        response+=this.matchLists.get(0).getValue()+"\n";
        response+="E<G<D\n";
        response+=this.matchLists.get(2).getValue()+"<";
        response+=this.matchLists.get(0).getValue()+"<";
        response+=this.matchLists.get(3).getValue()+"\n";
        response+="C<E<B\n";
        response+=this.matchLists.get(4).getValue()+"<";
        response+=this.matchLists.get(2).getValue()+"<";
        response+=this.matchLists.get(5).getValue()+"\n";

        return  response;
    }
}
