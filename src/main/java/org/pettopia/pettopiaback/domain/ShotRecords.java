package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

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
    @CreationTimestamp
    @Column(name = "create_at")
    private LocalDateTime createAt = LocalDateTime.now();

    private String type;

    private int num;

    private int age;

    public static ShotRecords makeShotRecord (Pet pet, String type, int num, int age){
        ShotRecords shotRecords = new ShotRecords();
        shotRecords.pet =pet;
        shotRecords.type=type;
        shotRecords.num = num;
        shotRecords.age=age;
        return shotRecords;

    }

    public void modifyShotRecord(Pet pet, String type, int num, int age){
        this.pet = pet;
        this.type= type;
        this.num=num;
        this.age=age;
    }
}
