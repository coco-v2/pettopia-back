package org.pettopia.pettopiaback.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "반려동물 팁 컨트롤러")
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/v1/life/tip")
public class TipController {

}
