package ro.hibyte.betting.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*

enum class BetTemplateType {
    NUMERIC,
    BOOLEAN,
    MULTIPLE_CHOICE,
    STRING
}

@Entity
data class BetTemplate (
    @Id
    @GeneratedValue
    var id: Long,

    var name: String?,

    @Enumerated(EnumType.STRING)
    var type: BetTemplateType?,

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "betTemplate", cascade = [CascadeType.ALL])
    var betTypes: List<BetType> = emptyList()

) {
    fun update(betTemplate: BetTemplate) {
        name = betTemplate.name
        type = betTemplate.type
    }
}