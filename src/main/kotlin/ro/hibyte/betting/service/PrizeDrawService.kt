package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
import ro.hibyte.betting.entity.DrawType
import ro.hibyte.betting.entity.PrizeDraw
import ro.hibyte.betting.entity.PrizeDrawEntry
import ro.hibyte.betting.exceptions.types.BadRequestException
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.mapper.PrizeDrawMapper
import ro.hibyte.betting.repository.PrizeDrawEntryRepository
import ro.hibyte.betting.repository.PrizeDrawRepository
import ro.hibyte.betting.repository.UserProfileRepository

@Service
class PrizeDrawService(
    private val prizeDrawRepository: PrizeDrawRepository,
    private val prizeDrawMapper: PrizeDrawMapper,
    private val userProfileRepository: UserProfileRepository,
    private val prizeDrawEntryRepository: PrizeDrawEntryRepository
) {

    private fun createRoulette(prizeDraw: PrizeDraw): PrizeDrawResponse {
        return prizeDrawMapper.prizeDrawToPrizeDrawResponse(prizeDrawRepository.save(prizeDraw))
    }

    private fun createMostPoints(prizeDraw: PrizeDraw): PrizeDrawResponse {
        val savedPrizeDraw: PrizeDraw = prizeDrawRepository.save(prizeDraw)
        userProfileRepository
                .findAll()
                .stream()
                .map { PrizeDrawEntry(null, it, it.coins, prizeDraw) }
                .forEach { prizeDrawEntryRepository.save(it) }

        savedPrizeDraw.entries = prizeDrawEntryRepository.getAllByPrizeDraw_Id(savedPrizeDraw.id!!)
        return prizeDrawMapper.prizeDrawToPrizeDrawResponse(prizeDrawRepository.save(savedPrizeDraw))
    }

    fun create(prizeDrawRequest: PrizeDrawRequest): PrizeDrawResponse {
        val prizeDraw = prizeDrawMapper.prizeDrawRequestToPrizeDraw(prizeDrawRequest)
        return if (prizeDraw.type == DrawType.ROULETTE)
            createRoulette(prizeDraw) else createMostPoints(prizeDraw)
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

    fun addEntry(id: Long, amount: Number) {
        val foundPrizeDraw: PrizeDraw  = prizeDrawRepository.findById(id).orElseThrow{EntityNotFoundException("Prize Draw", id)}
        if (foundPrizeDraw.type == DrawType.MOST_POINTS) throw BadRequestException("Cannot manually add entries to a MOST_POINTS draw")
    }
}
