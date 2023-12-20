package com.eek.kimpli.moment.dto;

public class Moment {
    private int id;
    private String text;

    // 생성자, 게터, 세터 등...

    // 예시로 간단하게 생성자와 게터, 세터 추가
    public Moment(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    // 세터 등도 필요에 따라 추가할 수 있음
}