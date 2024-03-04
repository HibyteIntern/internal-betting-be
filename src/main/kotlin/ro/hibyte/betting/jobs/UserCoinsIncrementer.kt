package ro.hibyte.betting.jobs

import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import ro.hibyte.betting.service.UserProfileService

@Configuration
@EnableScheduling
@EnableAsync
class UserCoinsIncrementer(private val userProfileService: UserProfileService) {
    @Scheduled(cron = "0 * * * * ?")
    fun updateEventStatusChange() {
        userProfileService.addCoinsToAllUsers()
    }
}
