package org.pettopia.pettopiaback.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "Pettopia 백엔드 API 명세서",
                description = """
                  Pettopia 백엔드 API 명세입니다.<br>
                  <h2>401: 만료된 토큰 or 유효하지 않은 토큰 사용</h2>
                  <h2>500: 서버에러</h2>
                  """,
                version = "v1"
        ),
        servers = {
                @Server(url = "http://43.200.68.44:8080", description = "AWS 배포 서버"),
                @Server(url = "http://localhost:8080", description = "Local 테스트용 서버")
        }
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {

    private static final String BEARER_TOKEN_PREFIX = "Bearer";

    @Bean
    public OpenAPI api() {
        String securityJwtName = "JWT";
        SecurityRequirement securityRequirement = new SecurityRequirement()
                .addList(securityJwtName);

        Components components = new Components()
                .addSecuritySchemes(securityJwtName, new SecurityScheme()
                        .name(securityJwtName)
                        .type(SecurityScheme.Type.HTTP)
                        .scheme(BEARER_TOKEN_PREFIX)
                        .bearerFormat(securityJwtName)
                );

        return new OpenAPI()
                .addSecurityItem(securityRequirement)
                .components(components);
    }


    @Bean
    public GroupedOpenApi chatOpenApi(){
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("Pettopia OPEN API v1")
                .pathsToMatch(paths)
                .build();
    }


}