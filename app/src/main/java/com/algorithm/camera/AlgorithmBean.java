package com.algorithm.camera;

import java.io.Serializable;

public class AlgorithmBean implements Serializable {
    public AlgorithmBean(String name,String method, float params){
        this.name = name;
        this.method = method;
        this.params = params;
        this.hasParams = true;
    }

    public AlgorithmBean(String name,String method){
        this.name = name;
        this.method = method;
        this.hasParams = false;
    }
    private String name;
    private float params;
    private boolean hasParams;

    public boolean isHasParams() {
        return hasParams;
    }

    public void setHasParams(boolean hasParams) {
        this.hasParams = hasParams;
    }

    private String method;

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getParams() {
        return params;
    }

    public void setParams(float params) {
        this.params = params;
    }
}
