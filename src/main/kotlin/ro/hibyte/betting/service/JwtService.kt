package ro.hibyte.betting.service

import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service
import com.auth0.jwt.JWT
import com.auth0.jwt.exceptions.JWTDecodeException
import ro.hibyte.betting.dto.UserPermissions
import ro.hibyte.betting.entity.Role

@Service
class JwtService(private var userProfileService: UserProfileService) {

    fun getUserProfileAndPermissionsFromRequest(request: HttpServletRequest): UserPermissions? {
        val authHeader = request.getHeader("Authorization")
        return if (authHeader != null && authHeader.startsWith("Bearer ")) {
            extractInformationFromToken(authHeader.substring(7))
        } else {
            null
        }
    }

    private fun extractInformationFromToken(token: String): UserPermissions? {
        return try {
            val decodedJWT = JWT.decode(token)
            val userProfile = userProfileService.getUserProfileFromKeycloakId(decodedJWT.subject)
            userProfile?.let {
                UserPermissions(it, Role.USER)
            }
        } catch (e: JWTDecodeException) {
            null
        }
    }
}
