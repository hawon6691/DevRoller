package com.devroller.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Swagger / OpenAPI 설정
 * 접속: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Value("${spring.profiles.active:local}")
    private String activeProfile;

    @Bean
    public OpenAPI openAPI() {
        String jwtSchemeName = "bearerAuth";
        
        return new OpenAPI()
                .info(apiInfo())
                .servers(servers())
                .addSecurityItem(new SecurityRequirement().addList(jwtSchemeName))
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName, securityScheme()));
    }

    private Info apiInfo() {
        return new Info()
                .title("DevRoller API")
                .description("""
                        ## 🎲 DevRoller - 개발 주제 추첨기 API
                        
                        개발 프로젝트 주제를 다양한 방식으로 추첨하고, 
                        게이미피케이션 요소로 개발 동기를 부여하는 서비스입니다.
                        
                        ### 주요 기능
                        - 🎰 **추첨 시스템**: 룰렛, 사다리, 제비뽑기, 랜덤
                        - 📊 **상태창**: 레벨, 경험치, 업적, 칭호
                        - 🏆 **게이미피케이션**: 스트릭, 뱃지, 랭킹
                        - 📝 **프로젝트 관리**: 진행 상태 추적
                        
                        ### 인증
                        JWT Bearer Token을 사용합니다.
                        우측 상단의 **Authorize** 버튼을 클릭하여 토큰을 입력하세요.
                        """)
                .version("v1.0.0")
                .contact(new Contact()
                        .name("DevRoller Team")
                        .email("contact@devroller.com")
                        .url("https://github.com/devroller"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"));
    }

    private List<Server> servers() {
        return switch (activeProfile) {
            case "prod" -> List.of(
                    new Server().url("https://api.devroller.com").description("Production")
            );
            case "dev" -> List.of(
                    new Server().url("http://localhost:8080").description("Development")
            );
            default -> List.of(
                    new Server().url("http://localhost:8080").description("Local")
            );
        };
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .name("bearerAuth")
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .description("JWT 토큰을 입력하세요. (Bearer 접두사 제외)");
    }
}
