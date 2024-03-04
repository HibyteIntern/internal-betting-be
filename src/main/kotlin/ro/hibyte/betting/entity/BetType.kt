package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.BetTypeDTO

@Entity
data class BetType (
    @Id
    @GeneratedValue
    var id: Long?,

    var name: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "bet_template_id")
    var bets: MutableList<Bet> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eventId")
    var event: Event? = null,

    @ElementCollection
    @CollectionTable(name = "bet_type_options", joinColumns = [JoinColumn(name = "bet_type_id")])
    @Column(name = "option")
    var options: List<String> = ArrayList(),

    @ElementCollection
    @CollectionTable(name = "bet_type_odds", joinColumns = [JoinColumn(name = "bet_type_id")])
    @Column(name = "odds")
    var odds: List<Double> = ArrayList(),

    var finalOutcome : String? = null
) {

    constructor(betTypeDTO: BetTypeDTO) : this(
        id = betTypeDTO.id,
        name = betTypeDTO.name,
        options = betTypeDTO.options,
        odds = betTypeDTO.odds,
    )

    fun update(betType: BetType) {
        name = betType.name
        options = betType.options
        odds = betType.odds
    }
}
