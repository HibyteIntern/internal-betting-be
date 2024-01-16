package ro.hibyte.betting.mapper

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawDTO
import ro.hibyte.betting.entity.PrizeDraw
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.entity.Status
import java.sql.Timestamp


@Service
class PrizeDrawMapper {

    private fun getCurrentLeader(prizeDraw: PrizeDraw): PrizeDrawEntry? =
        prizeDraw.entries.maxByOrNull { it.amount.toDouble() }

    fun prizeDrawDTOToPrizeDraw(prizeDrawDTO: PrizeDrawDTO): PrizeDraw {
        return PrizeDraw(
            null,
            prizeDrawDTO.title,
            prizeDrawDTO.description,
            Status.OPEN,
            Timestamp(System.currentTimeMillis()),
            Timestamp.from(prizeDrawDTO.endsAt),
            prizeDrawDTO.prizeDescription,
            prizeDrawDTO.type,
            null
        )
    }

    fun prizeDrawToPrizeDrawDTO(prizeDraw: PrizeDraw): PrizeDrawDTO {
        return PrizeDrawDTO(
            prizeDraw.id,
            prizeDraw.title,
            prizeDraw.description,
            prizeDraw.status,
            prizeDraw.createdAt.toInstant(),
            prizeDraw.endsAt.toInstant(),
            prizeDraw.prizeDescription,
            prizeDraw.type,
            prizeDraw.winner,
            prizeDraw.entries,
            getCurrentLeader(prizeDraw)
        )
    }
}
