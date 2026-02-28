package org.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI shopPlatformOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("商店平台API")
                        .description("商店平台后端接口文档，包含商品管理、订单管理、会员管理等功能")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("Shop Platform Team")
                                .email("support@shop.com")));
    }
}
