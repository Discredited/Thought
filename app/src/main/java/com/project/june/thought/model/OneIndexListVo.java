package com.project.june.thought.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by June on 2017/3/11.
 */
public class OneIndexListVo implements Serializable {


    /**
     * res : 0
     * data : ["1643","1642","1641","1640","1639","1638","1637","1636","1635","1634"]
     */

    private int res;
    private List<String> data;

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
