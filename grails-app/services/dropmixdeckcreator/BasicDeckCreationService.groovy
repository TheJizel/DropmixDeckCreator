package dropmixdeckcreator


import grails.gorm.transactions.Transactional

@Transactional(readOnly = true)
class BasicDeckCreationService {
    List<List<Card>> generateDecks(List<Card> cardsToInclude, int numberOfSets) {
        List<List<SetTemplate>> possibilities =
                SetTemplateUtil.computePossibleSetTemplates(cardsToInclude, numberOfSets)

        if (!possibilities) {
            return []
        }

        List<SetTemplate> setTemplates = RandomUtil.getRandomElement(possibilities)

        generateDecks(cardsToInclude, numberOfSets, setTemplates)
    }

    List<List<Card>> generateDecks(List<Card> cardsToInclude, int numberOfSets, List<SetTemplate> setTemplates) {
        if (!cardsToInclude || !setTemplates) {
            return null
        }

        List<List<Card>> cards = []

        numberOfSets.times {
            SetTemplate setTemplate = RandomUtil.getRandomElement(setTemplates)
            setTemplates.remove(setTemplate)
            cards.add(generateDeck(cardsToInclude, setTemplate))
        }

        cards
    }

    List<Card> generateDeck(List<Card> availableCards, SetTemplate setTemplate) {
        // Return null if the list is null/empty or if the set template is null
        if (!availableCards || !setTemplate) {
            return null
        }

        // Get the template to be used for the deck
        Map<Color, Map<Level, Integer>> composition = setTemplate.composition

        // Get a list of applicable playlists
        List<Playlist> playlists = availableCards.collect { it.playlist }.unique()

        // Initialize the counts (setting each playlist at 0 to start)
        Map<Playlist, Integer> playlistCounts = playlists.collectEntries { playlist -> [playlist, 0] }

        // Initialize the list of cards to eventually be returned
        List<Card> cards = []

        // For each color, get the counts that we should have for each level and...
        composition.each { Color color, Map<Level, Integer> counts ->
            // ...for each level per color, add the next card n times
            counts.each { Level level, Integer n ->
                n.times {
                    Card card = getNextCard(availableCards, playlistCounts, color, level)
                    if (card) {
                        cards.add(card)
                    }
                }
            }
        }

        // Return the final list of cards
        cards
    }

    Card getNextCard(List<Card> availableCards, Map<Playlist, Integer> playlistCounts, Color color, Level level) {
        // Return null if any variables are null or (where applicable) empty
        if (!availableCards || !playlistCounts || !color || !level) {
            return null
        }

        // Gather all applicable cards from the available pool (i.e., those that match the desired color and level)
        List<Card> applicableCards = availableCards.findAll { it.color == color && it.level == level }
        // Gather the unique list of playlist from the list of applicable cards
        List<Playlist> applicablePlaylists = applicableCards.collect { it.playlist }.unique()
        // Gather the counts for the applicable playlists
        Map<Playlist, Integer> applicableCounts = playlistCounts.findAll { k, v -> applicablePlaylists.contains(k) }

        // Get a playlist with the lowest representation that contains at least one qualifying card
        Playlist playlist = PlaylistUtil.getNextPlaylistToUse(applicableCounts)
        // Get a random, available, qualifying card from the aforementioned playlist
        Card card = CardUtil.getNextCardToUse(applicableCards, playlist, color, level)

        // If a card could not be found, return null
        if (!card) {
            return null
        }

        // Compute the new count for this playlist by adding one to the old count
        Integer newCountForPlaylist = playlistCounts.remove(playlist) + 1
        // Remove this card from the pool of available cards
        availableCards.remove(card)
        // Update the map containing the counts
        playlistCounts.put(playlist, newCountForPlaylist)

        // Return the card
        card
    }
}
