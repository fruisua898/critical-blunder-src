package config;

import static org.junit.jupiter.api.Assertions.*;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

class SwaggerConfigTest {

    private final SwaggerConfig swaggerConfig = new SwaggerConfig();

    @Test
    void shouldCreateOpenAPIConfig() {
        OpenAPI openAPI = swaggerConfig.customOpenAPI();
        assertNotNull(openAPI);
        assertEquals("Critical Blunder API", openAPI.getInfo().getTitle());
        assertEquals("1.0", openAPI.getInfo().getVersion());
        assertEquals("API para gestión de masters y jugadores", openAPI.getInfo().getDescription());

        assertTrue(openAPI.getComponents().getSecuritySchemes().containsKey("Bearer Authentication"));
        SecurityScheme scheme = openAPI.getComponents().getSecuritySchemes().get("Bearer Authentication");
        assertNotNull(scheme);
        assertEquals(SecurityScheme.Type.HTTP, scheme.getType());
        assertEquals("bearer", scheme.getScheme());
        assertEquals("JWT", scheme.getBearerFormat());
    }
}
