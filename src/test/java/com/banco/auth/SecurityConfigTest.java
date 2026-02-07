package com.banco.auth;

import com.banco.auth.config.SecurityConfig;
import com.banco.auth.security.JWTAuthenticationFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static org.assertj.core.api.Assertions.assertThat;

class SecurityConfigTest {

    private final JWTAuthenticationFilter jwtAuthenticationFilter = Mockito.mock(JWTAuthenticationFilter.class);
    private final AuthenticationProvider authenticationProvider = Mockito.mock(AuthenticationProvider.class);
    private final SecurityConfig securityConfig = new SecurityConfig(jwtAuthenticationFilter, authenticationProvider);

    private CorsConfiguration getCorsConfigFor(String method, String path) {
        CorsConfigurationSource source = securityConfig.corsConfigurationSource();
        return source.getCorsConfiguration(new MockHttpServletRequest(method, path));
    }

    @Test
    @DisplayName("CORS source should be UrlBasedCorsConfigurationSource and not null")
    void corsSourceShouldBeUrlBasedAndNotNull() {
        var source = securityConfig.corsConfigurationSource();
        assertThat(source).isInstanceOf(UrlBasedCorsConfigurationSource.class);
        var config = getCorsConfigFor("GET", "/");
        assertThat(config).isNotNull();
    }

    @Test
    @DisplayName("CORS should allow configured origins")
    void corsShouldAllowConfiguredOrigins() {
        var config = getCorsConfigFor("GET", "/any");
        assertThat(config.getAllowedOrigins())
                .contains("http://localhost:3000", "http://localhost:8080");
        assertThat(config.getAllowedOrigins()).doesNotContain("http://evil.com");
    }

    @Test
    @DisplayName("CORS should allow expected HTTP methods")
    void corsShouldAllowExpectedMethods() {
        var config = getCorsConfigFor("OPTIONS", "/api");
        assertThat(config.getAllowedMethods())
                .contains("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH");
    }

    @Test
    @DisplayName("CORS should allow expected headers")
    void corsShouldAllowExpectedHeaders() {
        var config = getCorsConfigFor("GET", "/path");
        assertThat(config.getAllowedHeaders())
                .contains("Authorization", "Content-Type", "X-Requested-With", "Accept");
    }

    @Test
    @DisplayName("CORS should expose Authorization header")
    void corsShouldExposeAuthorizationHeader() {
        var config = getCorsConfigFor("GET", "/path");
        assertThat(config.getExposedHeaders()).contains("Authorization");
    }

    @Test
    @DisplayName("CORS should allow credentials and set maxAge")
    void corsShouldAllowCredentialsAndMaxAge() {
        var config = getCorsConfigFor("GET", "/another");
        assertThat(config.getAllowCredentials()).isTrue();
        assertThat(config.getMaxAge()).isEqualTo(3600L);
    }

    @Test
    @DisplayName("CORS configuration should be applied for any path pattern")
    void corsConfigAppliedForAnyPath() {
        var config1 = getCorsConfigFor("GET", "/api/v1/user/profile");
        var config2 = getCorsConfigFor("POST", "/api/v1/admin/dashboard");
        var config3 = getCorsConfigFor("OPTIONS", "/swagger-ui/index.html");
        assertThat(config1).isNotNull();
        assertThat(config2).isNotNull();
        assertThat(config3).isNotNull();
    }
}
