package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.mapper.PrizeDrawMapper
import ro.hibyte.betting.repository.PrizeDrawRepository

@Service
class PrizeDrawService(
    private val prizeDrawRepository: PrizeDrawRepository,
    private val prizeDrawMapper: PrizeDrawMapper,
) {

    fun create(prizeDrawRequest: PrizeDrawRequest): PrizeDrawResponse {
        val prizeDraw = prizeDrawMapper.prizeDrawRequestToPrizeDraw(prizeDrawRequest)
        return prizeDrawMapper.prizeDrawToPrizeDrawResponse(prizeDrawRepository.save(prizeDraw))
    }

    fun getById(id: Long): PrizeDrawResponse =
        prizeDrawRepository
            .findById(id)
            .map { prizeDrawMapper.prizeDrawToPrizeDrawResponse(it) }
            .orElseThrow{EntityNotFoundException("Prize Draw", id)}

    fun getAll(): List<PrizeDrawResponse> =
        prizeDrawRepository.findAll().map { prizeDrawMapper.prizeDrawToPrizeDrawResponse(it) }

    fun delete(id: Long) {
        prizeDrawRepository.findById(id).orElseThrow{EntityNotFoundException("Prize Draw", id)}
        prizeDrawRepository.deleteById(id)
    }
}
