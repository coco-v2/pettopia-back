package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.domain.TipCategory;
import org.pettopia.pettopiaback.dto.TipDTO;
import org.pettopia.pettopiaback.service.TipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "반려동물 팁 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/life/tip")
public class TipController {

    private final TipService tipService;

    @GetMapping
    public ResponseEntity<TipDTO.ResponseTipList> getTipList( @RequestParam Long petTypePk,
                                                              @RequestParam TipCategory tipCategory
        ) throws RuntimeException {

        TipDTO.ResponseTipList responseTipList = tipService.getTipList(petTypePk, tipCategory);

        return ResponseEntity.ok(responseTipList);
    }

}
