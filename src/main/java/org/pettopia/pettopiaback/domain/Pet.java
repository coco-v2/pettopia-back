package org.pettopia.pettopiaback.domain;

import java.time.LocalDateTime;

public class Pet {

    private Long pk;
    // 종
    private int dogRegNo;
    private String dogNum;
    private int birth;
    private boolean sexNum;
    private boolean neuterYn;
    private LocalDateTime createAt;
    private int environment;
    private int exercise;
    private int foodCnt;
    private int snackCnt;
    private String protectorName;
    private String protectorPhoneNum;


}
