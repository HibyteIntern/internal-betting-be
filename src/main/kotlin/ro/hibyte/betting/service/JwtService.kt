package ro.hibyte.betting.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import ro.hibyte.betting.repository.UserProfileRepository
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import ro.hibyte.betting.entity.UserProfile

@Service
class JwtService(private var userProfileService: UserProfileService) {

    fun getUserProfileFromRequest(request: HttpServletRequest): UserProfile? {
        val authHeader = request.getHeader("Authorization")
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            extractIdFromToken(authHeader.substring(7))
        } else {
            null
        }
    }

    private fun extractIdFromToken(token: String): UserProfile? {
        return try {
            val decodedJWT = JWT.decode(token)
            userProfileService.getUserProfileFromKeycloakId(decodedJWT.subject)
        } catch (e: JWTDecodeException) {
            null
        }
    }
}
