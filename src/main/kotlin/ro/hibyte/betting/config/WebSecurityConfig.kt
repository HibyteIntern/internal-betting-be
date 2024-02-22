package ro.hibyte.betting.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class WebSecurityConfig {

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain =
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers(HttpMethod.OPTIONS).permitAll()
                    .requestMatchers("/api/v1/user-profile/getMe").authenticated()
                    .requestMatchers("/api/v1/user-profile/getMeSimple").authenticated()
                    .requestMatchers(HttpMethod.GET, "/**").permitAll()

                    .requestMatchers("/api/v1/event-templates/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/v1/bet-templates/**").hasAuthority("ADMIN")
                    .requestMatchers("/api/v1/bet-types/**").hasAuthority("ADMIN")

                    .requestMatchers("/api/v1/prize-draws/entry").authenticated()
                    .requestMatchers("/api/v1/prize-draws/**").hasAuthority("ADMIN")

                    .requestMatchers("/api/v1/user-profile/**").authenticated()

                    .anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt {
                    it.jwtAuthenticationConverter(JwtAuthConverter())
                }
            }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .build()
}
