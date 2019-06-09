package com.yjy.api.domain;

import java.io.Serializable;

/**
 * 用户模型
 */
public class User implements Serializable {
    private static final long serialVersionUID = 8711662357908258317L;
    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
