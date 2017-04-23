package com.embedded_system.Entity;

import java.util.ArrayList;

/**
 * Created by onion on 4/15/2017.
 */
public class SearchOBJ {
    public ArrayList<ArrayList<Integer>> arrayList;
    public int count;
    public int itop;
    public int ilow;
    public int jtop;
    public int jlow;

    public SearchOBJ(ArrayList<ArrayList<Integer>> arrayList, int count) {
        this.arrayList = arrayList;
        this.count = count;
    }

    public SearchOBJ() {
    }
}
