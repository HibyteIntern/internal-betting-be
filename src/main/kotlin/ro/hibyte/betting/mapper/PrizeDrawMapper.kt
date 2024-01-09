package ro.hibyte.betting.mapper

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
import ro.hibyte.betting.entity.PrizeDraw
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.entity.Status
import java.sql.Timestamp


@Service
class PrizeDrawMapper {

    fun getCurrentLeader(prizeDraw: PrizeDraw): PrizeDrawEntry? =
        prizeDraw.entries.maxByOrNull { it.amount.toDouble() }

    fun prizeDrawRequestToPrizeDraw(prizeDrawRequest: PrizeDrawRequest): PrizeDraw {
        return PrizeDraw(
            null,
            prizeDrawRequest.title,
            prizeDrawRequest.description,
            Status.OPEN,
            Timestamp(System.currentTimeMillis()),
            Timestamp.from(prizeDrawRequest.endsAt),
            prizeDrawRequest.prizeDescription,
            prizeDrawRequest.type,
            null
        )
    }

    fun prizeDrawToPrizeDrawResponse(prizeDraw: PrizeDraw): PrizeDrawResponse {
        return PrizeDrawResponse(
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
