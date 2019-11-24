package dropmixdeckcreator


import static Color.*
import static ColorTemplate.*
import static FxTemplate.*

enum SetTemplate {
    COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX(1, TWO_PLUS_TWO, [(ALL)   : STANDARD_WILDCARDS,
                                                             (YELLOW): BALANCED,
                                                             (RED)   : BALANCED,
                                                             (BLUE)  : BALANCED,
                                                             (GREEN) : BALANCED]),
    BOARD_FLEXIBLE_WITH_TWO_PLUS_TWO_FX(2, TWO_PLUS_TWO, [(ALL)   : STANDARD_WILDCARDS,
                                                              (YELLOW): UNDERPOWERED,
                                                              (RED)   : OVERLOAD,
                                                              (BLUE)  : OVERLOAD,
                                                              (GREEN) : UNDERPOWERED]),
    COLOR_BALANCE_WITH_TWO_PLUS_THREE_FX(3, TWO_PLUS_THREE, [(ALL)   : STANDARD_WILDCARDS,
                                                                 (YELLOW): BALANCED,
                                                                 (RED)   : BALANCED,
                                                                 (BLUE)  : BALANCED,
                                                                 (GREEN) : BALANCED]),
    BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX(4, TWO_PLUS_THREE, [(ALL)   : STANDARD_WILDCARDS,
                                                                  (YELLOW): UNDERPOWERED,
                                                                  (RED)   : OVERLOAD,
                                                                  (BLUE)  : OVERLOAD,
                                                                  (GREEN) : UNDERPOWERED]),
    BOARD_FLEXIBLE_WITH_ONE_PLUS_THREE_FX(5, ONE_PLUS_THREE, [(ALL)   : STANDARD_WILDCARDS,
                                                                  (YELLOW): UNDERPOWERED,
                                                                  (RED)   : OVERLOAD,
                                                                  (BLUE)  : OVERLOAD,
                                                                  (GREEN) : UNDERPOWERED]),
    COLOR_BALANCE_WITH_ONE_PLUS_TWO_FX(6, ONE_PLUS_TWO, [(ALL)   : STANDARD_WILDCARDS,
                                                             (YELLOW): BALANCED,
                                                             (RED)   : BALANCED,
                                                             (BLUE)  : BALANCED,
                                                             (GREEN) : BALANCED]),
    BOARD_FLEXIBLE_WITH_THREE_PLUS_THREE(7, THREE_PLUS_THREE, [(ALL)   : STANDARD_WILDCARDS,
                                                                   (YELLOW): UNDERPOWERED,
                                                                   (RED)   : OVERLOAD,
                                                                   (BLUE)  : OVERLOAD,
                                                                   (GREEN) : UNDERPOWERED])

    int id
    FxTemplate fxTemplate
    Map<Color, ColorTemplate> composition

    static SetTemplate get(int id) {
        values().find { it.id == id }
    }

    private SetTemplate(int id, FxTemplate fxTemplate, Map<Color, ColorTemplate> composition) {
        this.id = id
        this.fxTemplate = fxTemplate
        this.composition = composition
    }

    Map<Color, Map<Level, Integer>> getComposition() {
        Map<Color, Map<Level, Integer>> fxComposition = [(WHITE): this.fxTemplate.composition]

        this.composition.collectEntries(fxComposition) { key, value -> [key, value.composition] }
    }

    void removeNecessaryCardsFromList(List<Card> availableCards) throws Exception {
        if (this.cannotBeCreated(availableCards)) {
            throw new Exception("Available cards do not contain all necessary cards to create the given set template.")
        }

        getComposition().each { Color color, Map<Level, Integer> counts ->
            counts.each { Level level, Integer count ->
                count.times {
                    availableCards.remove(availableCards.find { it.color == color && it.level == level })
                }
            }
        }
    }

    boolean cannotBeCreated(List<Card> availableCards) {
        !this.canBeCreated(availableCards)
    }

    boolean canBeCreated(List<Card> availableCards) {
        if (!availableCards) {
            return false
        }

        List<Card> cardPool = availableCards.clone() as List<Card>

        Map<Color, Map<Level, Integer>> composition = getComposition()

        for (Map.Entry<Color, Map<Level, Integer>> colorCounts : composition) {
            Color color = colorCounts.key
            Map<Level, Integer> counts = colorCounts.value

            for (Map.Entry<Level, Integer> levelCounts : counts) {
                Level level = levelCounts.key
                Integer number = levelCounts.value

                while (number) {
                    Card card = cardPool.find { it.color == color && it.level == level }

                    if (!card) {
                        return false
                    }

                    cardPool.remove(card)
                    number--
                }
            }
        }

        true
    }
}