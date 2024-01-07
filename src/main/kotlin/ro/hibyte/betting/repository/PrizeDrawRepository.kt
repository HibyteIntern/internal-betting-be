package ro.hibyte.betting.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.hibyte.betting.entity.PrizeDraw

interface PrizeDrawRepository: JpaRepository<PrizeDraw, Long>
