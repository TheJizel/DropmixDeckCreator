package dropmixdeckcreator

class CardUtil {
    static Card getNextCardToUse(List<Card> availableCards, Playlist playlist, Color color, Level level) {
        List<Card> eligibleCards =
                availableCards.findAll { it.color == color && it.level == level && it.playlist == playlist }

        RandomUtil.getRandomElement(eligibleCards)
    }
}
