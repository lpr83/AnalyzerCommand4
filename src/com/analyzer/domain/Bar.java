package com.analyzer.domain;

public class Bar {
    private Integer id;
    private Double open;
    private Double high;
    private Double low;
    private Double close;
    private Integer volume;

    public Integer getId() {
        return id;
    }

    public Double getOpen() {
        return open;
    }

    public Double getHigh() {
        return high;
    }

    public Double getLow() {
        return low;
    }

    public Double getClose() {
        return close;
    }

    public Integer getVolume() {
        return volume;
    }

    public Bar(Integer id, Double open, Double high, Double low, Double close, Integer volume) {
        this.id = id;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }
    public Bar (String singleLine) {
        String[] splittedLine= singleLine.split(",");
                this.id= Integer.parseInt(splittedLine[1]);
                this.open= Double.parseDouble(splittedLine[2]);
                this.high= Double.parseDouble(splittedLine[3]);
                this.low= Double.parseDouble(splittedLine[4]);
                this.close= Double.parseDouble(splittedLine[5]);
//                this.volume= Integer.parseInt(Double.parseDouble(splittedLine[6]).);
    }
}
