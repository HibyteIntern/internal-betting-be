package ro.hibyte.betting.service

import org.springframework.stereotype.Service
import ro.hibyte.betting.dto.PrizeDrawDTO
import ro.hibyte.betting.dto.PrizeDrawEntryDTO
import ro.hibyte.betting.entity.*
import ro.hibyte.betting.exceptions.types.BadRequestException
import ro.hibyte.betting.exceptions.types.ConflictException
import ro.hibyte.betting.exceptions.types.EntityNotFoundException
import ro.hibyte.betting.mapper.PrizeDrawMapper
import ro.hibyte.betting.repository.PrizeDrawEntryRepository
import ro.hibyte.betting.repository.PrizeDrawRepository
import ro.hibyte.betting.repository.UserProfileRepository
import kotlin.random.Random

@Service
class PrizeDrawService(
    private val prizeDrawRepository: PrizeDrawRepository,
    private val prizeDrawMapper: PrizeDrawMapper,
    private val userProfileRepository: UserProfileRepository,
    private val prizeDrawEntryRepository: PrizeDrawEntryRepository,
) {

    private fun createRoulette(prizeDraw: PrizeDraw): PrizeDrawDTO {
        return prizeDrawMapper.prizeDrawToPrizeDrawDTO(prizeDrawRepository.save(prizeDraw))
    }

    private fun createMostPoints(prizeDraw: PrizeDraw): PrizeDrawDTO {
        val savedPrizeDraw: PrizeDraw = prizeDrawRepository.save(prizeDraw)
        userProfileRepository
                .findAll()
                .stream()
                .map { PrizeDrawEntry(null, it, it.coins, prizeDraw) }
                .forEach { prizeDrawEntryRepository.save(it) }

        savedPrizeDraw.entries = prizeDrawEntryRepository.getAllByPrizeDraw_Id(savedPrizeDraw.id!!)
        return prizeDrawMapper.prizeDrawToPrizeDrawDTO(prizeDrawRepository.save(savedPrizeDraw))
    }

    private fun calculateMostPointsWinner(prizeDraw: PrizeDraw): UserProfile? {
        return prizeDraw.entries.map { it.user }.maxByOrNull { it.coins.toDouble() }
    }

    private fun calculateRouletteWinner(prizeDraw: PrizeDraw): UserProfile? {
        val totalCoins = prizeDraw.entries.sumOf { it.amount.toInt() }
        var randomPoint = Random.nextInt(totalCoins)

        var winner: UserProfile? = null
        for (entry in prizeDraw.entries) {
            randomPoint -= entry.amount.toInt()
            if (randomPoint < 0) {
                winner = entry.user
                break
            }
        }
        return winner
    }

    private fun resetCoins(users: List<UserProfile>) {
        users.forEach { user ->
            user.coins = 0
            userProfileRepository.save(user)
        }
    }

    fun create(prizeDrawDTO: PrizeDrawDTO): PrizeDrawDTO {
        val prizeDraw = prizeDrawMapper.prizeDrawDTOToPrizeDraw(prizeDrawDTO)
        return if (prizeDraw.type == DrawType.ROULETTE)
            createRoulette(prizeDraw) else createMostPoints(prizeDraw)
    }

    fun getById(id: Long): PrizeDrawDTO =
        prizeDrawRepository
            .findById(id)
            .map { prizeDrawMapper.prizeDrawToPrizeDrawDTO(it) }
            .orElseThrow{EntityNotFoundException("Prize Draw", id)}

    fun getAll(): List<PrizeDrawDTO> =
        prizeDrawRepository.findAll().map { prizeDrawMapper.prizeDrawToPrizeDrawDTO(it) }

    fun getByStatus(status: Status): List<PrizeDrawDTO> =
        prizeDrawRepository.findAllByStatus(status).map { prizeDrawMapper.prizeDrawToPrizeDrawDTO(it) }

    fun update(id: Long, prizeDrawDTO: PrizeDrawDTO): PrizeDrawDTO {
        val foundPrizeDraw = prizeDrawRepository.findById(id).orElseThrow{ EntityNotFoundException("Prize Draw", id) }
        foundPrizeDraw.update(prizeDrawDTO)
        return prizeDrawMapper.prizeDrawToPrizeDrawDTO(prizeDrawRepository.save(foundPrizeDraw))
    }

    fun delete(id: Long) {
        prizeDrawRepository.findById(id).orElseThrow{EntityNotFoundException("Prize Draw", id)}
        prizeDrawRepository.deleteById(id)
    }

    private fun verifyEntryBadRequest(
        prizeDraw: PrizeDraw,
        user: UserProfile,
        prizeDrawEntryDTO: PrizeDrawEntryDTO,
    ) {
        if (prizeDraw.type == DrawType.MOST_POINTS) throw BadRequestException("Cannot manually add entries to a MOST_POINTS draw")
        if(prizeDraw.status != Status.OPEN) throw BadRequestException("Cannot add entries to a closed draw")
        if(user.coins.toInt() < prizeDrawEntryDTO.amount.toInt()) throw BadRequestException("Insufficient coins")
        if(prizeDrawEntryDTO.amount.toInt() <= 0) throw BadRequestException("Amount must be greater than 0")
    }

    fun addEntry(prizeDrawEntryDTO: PrizeDrawEntryDTO, keycloakId: String): PrizeDrawEntry {

        val userProfile: UserProfile = userProfileRepository
            .findByKeycloakId(keycloakId).orElseThrow { EntityNotFoundException("User Profile", 0) }

        val foundPrizeDraw: PrizeDraw  = prizeDrawRepository
            .findById(prizeDrawEntryDTO.prizeDrawId)
            .orElseThrow{ EntityNotFoundException("Prize Draw", prizeDrawEntryDTO.prizeDrawId) }

        verifyEntryBadRequest(foundPrizeDraw, userProfile, prizeDrawEntryDTO)
        if(foundPrizeDraw.entries.any { it.user.userId == userProfile.userId }) throw ConflictException("User already has an entry in this draw")

        userProfile.coins = userProfile.coins.toInt() - prizeDrawEntryDTO.amount.toInt()
        userProfileRepository.save(userProfile)
        return prizeDrawEntryRepository.save(
            PrizeDrawEntry(
                null,
                userProfile,
                prizeDrawEntryDTO.amount,
                foundPrizeDraw
            )
        )
    }

    fun endDraw(id: Long): PrizeDrawDTO {
        val foundPrizeDraw: PrizeDraw = prizeDrawRepository.findById(id).orElseThrow{ EntityNotFoundException("Prize Draw", id) }
        if(foundPrizeDraw.status == Status.CLOSED) throw BadRequestException("Draw is already closed")

        if(foundPrizeDraw.type == DrawType.MOST_POINTS) {
            foundPrizeDraw.winner = calculateMostPointsWinner(foundPrizeDraw)
            resetCoins(foundPrizeDraw.entries.map { it.user })
        }
        else foundPrizeDraw.winner = calculateRouletteWinner(foundPrizeDraw)

        foundPrizeDraw.status = Status.CLOSED
        return prizeDrawMapper.prizeDrawToPrizeDrawDTO(prizeDrawRepository.save(foundPrizeDraw))
    }
}
