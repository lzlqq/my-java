package com.leo.java.concurrence;

import java.util.HashMap;
import java.util.Map;

public class MapTest{
    public static void main(String[] args){
        Map<String,Integer> map =new HashMap<>();
        for (int i = 0; i < 20; i++){
            if(i==16) {
                System.out.println();
                map.put(i+"", i);
                continue;
            }
            map.put(i+"", i);
        }
    }
}