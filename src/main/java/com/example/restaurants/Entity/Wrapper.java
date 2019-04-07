package com.example.restaurants.Entity;

import java.math.BigInteger;
import java.util.List;

public class Wrapper {
    int min;
    List<Integer> tablesCapacity;
    List<BigInteger> ret;

    public Wrapper(int min, List<Integer> tablesCapacity, List<BigInteger> ret) {
        this.min = min;
        this.tablesCapacity = tablesCapacity;
        this.ret = ret;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public List<Integer> getTablesCapacity() {
        return tablesCapacity;
    }

    public void setTablesCapacity(List<Integer> tablesCapacity) {
        this.tablesCapacity = tablesCapacity;
    }

    public List<BigInteger> getRet() {
        return ret;
    }

    public void setRet(List<BigInteger> ret) {
        this.ret = ret;
    }
}
