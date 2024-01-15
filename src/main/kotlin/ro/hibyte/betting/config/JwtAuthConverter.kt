package ro.hibyte.betting.config

import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtAuthConverter: Converter<Jwt, AbstractAuthenticationToken> {
    override fun convert(source: Jwt): AbstractAuthenticationToken? {
        val subject = source.subject
        return JwtAuthenticationToken(source, Collections.singletonList(SimpleGrantedAuthority("USER")), subject)
    }
}
