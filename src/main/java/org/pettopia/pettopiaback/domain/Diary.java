package org.pettopia.pettopiaback.domain;

import java.time.LocalDateTime;

public class Diary {

    private Long pk;
    private Pet pet;
    private LocalDateTime createAt;
    private int mealCnt;
    private int snackCnt;
    private int walkCnt;
    private ConditionOfDefecation conditionOfDefecation;
    private String defecationText;

}
