package org.pettopia.pettopiaback.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.PetType;
import org.pettopia.pettopiaback.domain.Tip;
import org.pettopia.pettopiaback.domain.TipCategory;
import org.pettopia.pettopiaback.dto.TipDTO;
import org.pettopia.pettopiaback.repository.PetTypeRepository;
import org.pettopia.pettopiaback.repository.TipRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Transactional
@Service
public class TipService {

    private final TipRepository tipRepository;
    private final PetTypeRepository petTypeRepository;

    public TipDTO.ResponseTipList getTipList(Long petTypePk, TipCategory tipCategory) {

        PetType petType = petTypeRepository.findById(petTypePk)
                .orElseThrow(() -> new RuntimeException("해당하는 펫 종류가 존재하지 않습니다."));

        List<Tip> tips = tipRepository.findByPetTypeAndTipCategory(petType, tipCategory);


        TipDTO.ResponseTipList responseTipList = new TipDTO.ResponseTipList();
        responseTipList.setCnt(tips.size());
        responseTipList.setResponses(tips.stream()
                .map(tip -> new TipDTO.Response(tip.getContent()))
                .collect(Collectors.toList()));

        return responseTipList;
    }
}
