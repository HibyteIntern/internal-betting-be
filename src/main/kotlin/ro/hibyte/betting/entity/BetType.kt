package ro.hibyte.betting.entity

import jakarta.persistence.*
import ro.hibyte.betting.dto.CompleteBetTypeDto

@Entity
data class BetType (
    @Id
    @GeneratedValue
    var id: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_template_id")
    var betTemplate: BetTemplate,

    @ElementCollection
    @CollectionTable(name = "bet_type_odds", joinColumns = [JoinColumn(name = "bet_type_id")])
    @Column(name = "odds")
    var odds: List<Double> = ArrayList()
) {

    constructor(completeBet: CompleteBetTypeDto) : this(
        id = completeBet.id,
        betTemplate = BetTemplate(
            name = completeBet.name,
            type = BetTemplateType.valueOf(completeBet.type),
            multipleChoiceOptions = completeBet.multipleChoiceOptions ?: emptyList()
        ),
        odds = completeBet.odds ?: ArrayList()
    )

    fun update(betType: BetType) {
        odds = betType.odds
        betTemplate = betType.betTemplate
    }
}
