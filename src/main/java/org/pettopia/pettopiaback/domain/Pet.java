package org.pettopia.pettopiaback.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue
    private Long pk;

    private Species species;

    @NotNull
    private int dogRegNo;
    @NotNull
    private String dogNum;
    @NotNull
    private int birth;
    @NotNull
    private boolean sexNum;
    @NotNull
    private boolean neuterYn;
    @NotNull
    private LocalDateTime createAt;
    @NotNull
    private int environment;
    @NotNull
    private int exercise;
    @NotNull
    private int foodCnt;
    @NotNull
    private int snackCnt;
    @NotNull
    private String protectorName;
    @NotNull
    private String protectorPhoneNum;


}
