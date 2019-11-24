package dropmixdeckcreator

class SetTemplateUtil {
    static List<List<SetTemplate>> computePossibleSetTemplates(List<Card> availableCards, int numberOfDecks) {
        Map<List<SetTemplate>, List<Card>> possibilities = [:]

        computeNewPossibilities(possibilities, availableCards)

        computePossibleSetTemplates(numberOfDecks, possibilities)
    }

    static List<List<SetTemplate>> computePossibleSetTemplates(int numberOfDecks,
                                                               Map<List<SetTemplate>, List<Card>> currentPossibilities) {
        if (currentPossibilities == null) {
            return null
        }

        Map<List<SetTemplate>, List<Card>> newPossibilities = [:]

        currentPossibilities.findAll { it.key.size() < numberOfDecks }.each { List<SetTemplate> currentSetTemplates,
                                                                              List<Card> remainingCards ->
            computeNewPossibilities(newPossibilities, remainingCards, currentSetTemplates)
        }

        if (newPossibilities.isEmpty()) {
            currentPossibilities.keySet().findAll { it.size() == numberOfDecks }.toList()
        } else {
            computePossibleSetTemplates(numberOfDecks, newPossibilities)
        }
    }

    static void computeNewPossibilities(Map<List<SetTemplate>, List<Card>> possibilitiesToUpdate,
                                        List<Card> availableCards,
                                        List<SetTemplate> existingSetTemplates = []) {
        if (possibilitiesToUpdate != null && availableCards != null) {
            SetTemplate.values().each { SetTemplate setTemplate ->
                try {
                    List<Card> cards = availableCards.clone() as List<Card>
                    setTemplate.removeNecessaryCardsFromList(cards)
                    possibilitiesToUpdate.put(existingSetTemplates + setTemplate, cards)
                } catch (ignore) { }
            }
        }
    }
}
