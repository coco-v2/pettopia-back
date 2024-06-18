package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "shot_reocrds")
public class ShotRecords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_pk")
    private Pet pet;

    @NotNull
    @Column(name = "create_at")
    private LocalDate createAt;

    private String type;

    private Integer num;

    private Integer age;

    public static ShotRecords makeShotRecord (Pet pet, LocalDate createAt, String type, Integer num, Integer age){
        ShotRecords shotRecords = new ShotRecords();
        shotRecords.createAt = createAt;
        shotRecords.pet =pet;
        shotRecords.type=type;
        shotRecords.num = num;
        shotRecords.age=age;
        return shotRecords;

    }

    public void modifyShotRecord(Pet pet, String type, Integer num, Integer age){
        this.pet = pet;
        this.type= type;
        this.num=num;
        this.age=age;
    }
}
