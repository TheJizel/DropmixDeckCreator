package dropmixdeckcreator

class DeckCreationController {
    def basicDeckCreationService

    final static int CARDS_PER_DECK = 15

    def index() {
        List<Playlist> playlists = Playlist.findAllByReleased(true)

        render model: [playlists: playlists], view: "/index"
    }

    def advanced() {
        List<Playlist> playlists = Playlist.findAllByReleasedAndInSeries(true, false)

        List<Map> series =
                Card.findAllBySeriesIsNotNull().groupBy {
                    it.series
                }.sort {
                    it.key
                }.collect {
                    [id: "$it.key.id", number: "$it.key", packs: (it.value).groupBy { it.pack }.sort { it.key }]
                }

        render model: [playlists: playlists, series: series], view: "/advanced"
    }

    def filter() {
        List<String> json
        int numberOfDecks = params.int('decks') ?: 4

        try {
            json = request.JSON
        } catch (ignore) {
            render text: "<br/>Sorry, an error has occurred. Please try again."
            return
        }

        if (json.size() < CARDS_PER_DECK) {
            render text: "<br/>Not enough cards selected. You need a minimum of ${numberOfDecks * CARDS_PER_DECK} cards to proceed."
            return
        }

        List<Card> includedCards = json?.collect { id -> Card.get(id.substring(4).toLong()) }

        List<List<Card>> decks = []

        try {
            decks = basicDeckCreationService.generateDecks(includedCards?.sort(false, { a, b -> a.id <=> b.id }), numberOfDecks)
        } catch (ignore) { }

        render (decks ? [model: [decks: decks], view: '/decks'] : [text: "<br/>Sorry, an error has occurred. Please try again."])
    }
}
