package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.Color
import dropmixdeckcreator.Level
import dropmixdeckcreator.SetTemplate
import spock.lang.Specification
import spock.lang.Unroll

import static dropmixdeckcreator.Color.*
import static dropmixdeckcreator.ColorTemplate.*
import static dropmixdeckcreator.FxTemplate.*
import static dropmixdeckcreator.Level.*
import static dropmixdeckcreator.SetTemplate.*

class SetTemplateSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test get"() {
        expect:
        SetTemplate.get(1) == COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX
        SetTemplate.get(2) == BOARD_FLEXIBLE_WITH_TWO_PLUS_TWO_FX
        SetTemplate.get(3) == COLOR_BALANCE_WITH_TWO_PLUS_THREE_FX
        SetTemplate.get(4) == BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX
        SetTemplate.get(5) == BOARD_FLEXIBLE_WITH_ONE_PLUS_THREE_FX
        SetTemplate.get(6) == COLOR_BALANCE_WITH_ONE_PLUS_TWO_FX
        SetTemplate.get(7) == BOARD_FLEXIBLE_WITH_THREE_PLUS_THREE
        !SetTemplate.get(0)
        !SetTemplate.get(8)
    }

    @Unroll
    void "test getComposition"() {
        when:
        Map<Color, Map<Level, Integer>> actual = setTemplate.composition

        then:
        actual == expected.collectEntries { k, v -> [k, v.composition] }

        where:
        setTemplate << [COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX,
                        BOARD_FLEXIBLE_WITH_TWO_PLUS_TWO_FX,
                        COLOR_BALANCE_WITH_TWO_PLUS_THREE_FX,
                        BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX,
                        BOARD_FLEXIBLE_WITH_ONE_PLUS_THREE_FX,
                        COLOR_BALANCE_WITH_ONE_PLUS_TWO_FX,
                        BOARD_FLEXIBLE_WITH_THREE_PLUS_THREE]
        expected << [[(ALL): STANDARD_WILDCARDS,
                      (YELLOW): BALANCED,
                      (RED): BALANCED,
                      (BLUE): BALANCED,
                      (GREEN): BALANCED,
                      (WHITE): TWO_PLUS_TWO],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): UNDERPOWERED,
                      (RED): OVERLOAD,
                      (BLUE): OVERLOAD,
                      (GREEN): UNDERPOWERED,
                      (WHITE): TWO_PLUS_TWO],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): BALANCED,
                      (RED): BALANCED,
                      (BLUE): BALANCED,
                      (GREEN): BALANCED,
                      (WHITE): TWO_PLUS_THREE],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): UNDERPOWERED,
                      (RED): OVERLOAD,
                      (BLUE): OVERLOAD,
                      (GREEN): UNDERPOWERED,
                      (WHITE): TWO_PLUS_THREE],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): UNDERPOWERED,
                      (RED): OVERLOAD,
                      (BLUE): OVERLOAD,
                      (GREEN): UNDERPOWERED,
                      (WHITE): ONE_PLUS_THREE],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): BALANCED,
                      (RED): BALANCED,
                      (BLUE): BALANCED,
                      (GREEN): BALANCED,
                      (WHITE): ONE_PLUS_TWO],
                     [(ALL): STANDARD_WILDCARDS,
                      (YELLOW): UNDERPOWERED,
                      (RED): OVERLOAD,
                      (BLUE): OVERLOAD,
                      (GREEN): UNDERPOWERED,
                      (WHITE): THREE_PLUS_THREE]]
    }

    void "test removeNecessaryCardsFromList (cannotBeCreated)"() {
        given:
        List<Card> availableCards = []

        when:
        COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.removeNecessaryCardsFromList(availableCards)

        then:
        Exception e = thrown()
        e.message == "Available cards do not contain all necessary cards to create the given set template."
    }

    void "test removeNecessaryCardsFromList"() {
        given:
        List<Card> availableCards =
                [new Card(level: TWO, color: ALL),
                 new Card(level: ONE, color: YELLOW),
                 new Card(level: TWO, color: YELLOW),
                 new Card(level: THREE, color: YELLOW),
                 new Card(level: ONE, color: RED),
                 new Card(level: TWO, color: RED),
                 new Card(level: THREE, color: RED),
                 new Card(level: ONE, color: BLUE),
                 new Card(level: TWO, color: BLUE),
                 new Card(level: THREE, color: BLUE),
                 new Card(level: ONE, color: GREEN),
                 new Card(level: TWO, color: GREEN),
                 new Card(level: THREE, color: GREEN),
                 new Card(level: TWO, color: WHITE),
                 new Card(level: TWO, color: WHITE)]

        when:
        COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.removeNecessaryCardsFromList(availableCards)

        then:
        availableCards.isEmpty()
    }

    @Unroll
    void "test cannotBeCreated (no List<Card>)"() {
        when:
        boolean result = COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.cannotBeCreated(availableCards)

        then:
        result

        where:
        availableCards << [null, []]
    }

    @Unroll
    void "test cannotBeCreated"() {
        when:
        boolean actual = COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.cannotBeCreated(availableCards)

        then:
        actual == expected

        where:
        expected << [true, false]
        availableCards << [[new Card()],
                           [new Card(level: TWO, color: ALL),
                            new Card(level: ONE, color: YELLOW),
                            new Card(level: TWO, color: YELLOW),
                            new Card(level: THREE, color: YELLOW),
                            new Card(level: ONE, color: RED),
                            new Card(level: TWO, color: RED),
                            new Card(level: THREE, color: RED),
                            new Card(level: ONE, color: BLUE),
                            new Card(level: TWO, color: BLUE),
                            new Card(level: THREE, color: BLUE),
                            new Card(level: ONE, color: GREEN),
                            new Card(level: TWO, color: GREEN),
                            new Card(level: THREE, color: GREEN),
                            new Card(level: TWO, color: WHITE),
                            new Card(level: TWO, color: WHITE)]]
    }

    @Unroll
    void "test canBeCreated (no List<Card>)"() {
        when:
        boolean result = COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.canBeCreated(availableCards)

        then:
        !result

        where:
        availableCards << [null, []]
    }

    @Unroll
    void "test canBeCreated"() {
        when:
        boolean actual = COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX.canBeCreated(availableCards)

        then:
        actual == expected

        where:
        expected << [false, true]
        availableCards << [[new Card()],
                           [new Card(level: TWO, color: ALL),
                            new Card(level: ONE, color: YELLOW),
                            new Card(level: TWO, color: YELLOW),
                            new Card(level: THREE, color: YELLOW),
                            new Card(level: ONE, color: RED),
                            new Card(level: TWO, color: RED),
                            new Card(level: THREE, color: RED),
                            new Card(level: ONE, color: BLUE),
                            new Card(level: TWO, color: BLUE),
                            new Card(level: THREE, color: BLUE),
                            new Card(level: ONE, color: GREEN),
                            new Card(level: TWO, color: GREEN),
                            new Card(level: THREE, color: GREEN),
                            new Card(level: TWO, color: WHITE),
                            new Card(level: TWO, color: WHITE)]]
    }
}
