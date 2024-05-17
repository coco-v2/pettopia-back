package org.pettopia.pettopiaback.domain;


import lombok.*;
import org.jetbrains.annotations.NotNull;
import jakarta.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Table(name = "medicine")
public class Medicine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    @NotNull
    private String name;

    @NotNull
    private Integer cnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_pk")
    private Pet pet;

    public void setCnt(Integer cnt){
        this.cnt = cnt;
    }

}
