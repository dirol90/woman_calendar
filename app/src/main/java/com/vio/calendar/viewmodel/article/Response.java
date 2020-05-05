package com.vio.calendar.viewmodel.article;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Response {

    Map<String, Double> total_stat;
    ArrayList<ResponseInnerClass> charts_stat = new ArrayList<>();

    class ResponseInnerClass{
        Date date;
        Map<String, Double> stat;

        ResponseInnerClass(Date date, Map<String, Double> stat){
            this.date = date;
            this.stat = stat;
        }
    }

}
