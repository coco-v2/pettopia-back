package org.pettopia.pettopiaback.domain;

import jakarta.persistence.*;
import lombok.*;
import org.jetbrains.annotations.NotNull;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "diary_medicine")
public class DiaryMedicine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_pk")
    private Diary diary;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="medicine_pk")
    private Medicine medicine;

    private Integer medicineCnt;
}
