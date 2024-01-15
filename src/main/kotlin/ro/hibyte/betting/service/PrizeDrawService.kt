package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawEntryRequest
import ro.hibyte.betting.dto.PrizeDrawRequest
import ro.hibyte.betting.dto.PrizeDrawResponse
import ro.hibyte.betting.entity.*
import ro.hibyte.betting.exceptions.types.BadRequestException
import ro.hibyte.betting.exceptions.types.ConflictException
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

    fun getByStatus(status: Status): List<PrizeDrawResponse> =
        prizeDrawRepository.findAllByStatus(status).map { prizeDrawMapper.prizeDrawToPrizeDrawResponse(it) }

    fun update(id: Long, prizeDrawRequest: PrizeDrawRequest) {
        val foundPrizeDraw = prizeDrawRepository.findById(id).orElseThrow{ EntityNotFoundException("Prize Draw", id) }
        foundPrizeDraw.update(prizeDrawRequest)
        prizeDrawRepository.save(foundPrizeDraw)
    }

    fun delete(id: Long) {
        prizeDrawRepository.findById(id).orElseThrow{EntityNotFoundException("Prize Draw", id)}
        prizeDrawRepository.deleteById(id)
    }

    private fun verifyEntryBadRequest(prizeDraw: PrizeDraw,
                                      user: UserProfile,
                                      prizeDrawEntryRequest: PrizeDrawEntryRequest,
    ) {
        if (prizeDraw.type == DrawType.MOST_POINTS) throw BadRequestException("Cannot manually add entries to a MOST_POINTS draw")
        if(prizeDraw.status != Status.OPEN) throw BadRequestException("Cannot add entries to a closed draw")
        if(user.coins.toInt() < prizeDrawEntryRequest.amount.toInt()) throw BadRequestException("Insufficient coins")
        if(prizeDrawEntryRequest.amount.toInt() <= 0) throw BadRequestException("Amount must be greater than 0")
    }

    fun addEntry(prizeDrawEntryRequest: PrizeDrawEntryRequest, userId: Long): PrizeDrawEntry {
        val foundPrizeDraw: PrizeDraw  = prizeDrawRepository
            .findById(prizeDrawEntryRequest.prizeDrawId)
            .orElseThrow{ EntityNotFoundException("Prize Draw", prizeDrawEntryRequest.prizeDrawId) }

        val foundUser: UserProfile = userProfileRepository
            .findById(userId)
            .orElseThrow{ EntityNotFoundException("User Profile", userId) }

        verifyEntryBadRequest(foundPrizeDraw, foundUser, prizeDrawEntryRequest)
        if(foundPrizeDraw.entries.any { it.user.userId == foundUser.userId }) throw ConflictException("User already has an entry in this draw")

        return prizeDrawEntryRepository.save(
            PrizeDrawEntry(
                null,
                foundUser,
                prizeDrawEntryRequest.amount,
                foundPrizeDraw
            )
        )
    }
}
