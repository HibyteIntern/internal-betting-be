package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.Leaderboard

interface LeaderboardRepository: JpaRepository<Leaderboard, Long> {
}
