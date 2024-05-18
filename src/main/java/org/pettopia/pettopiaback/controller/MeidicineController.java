package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.pettopia.pettopiaback.dto.MedicineDTO;
import org.pettopia.pettopiaback.dto.PrincipalDetail;
import org.pettopia.pettopiaback.service.MedicineService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "약 컨트롤러", description = "약 관련 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/medicine")
public class MeidicineController {

    private final MedicineService medicineService;

    @Operation(summary = "반려동물 약 정보 조회", description = """
    [로그인 필요]
    200: 성공
    <br>
    """)
    @GetMapping("/{petPk}")
    public ResponseEntity<MedicineDTO.ResponseMedicineList> getMedicineList(
            @AuthenticationPrincipal PrincipalDetail userDetails
            , @PathVariable Long petPk
    ) throws RuntimeException {

        MedicineDTO.ResponseMedicineList medicineList = medicineService.getMedicinesByPetPk(petPk);

        return ResponseEntity.ok(medicineList);
    }


}
