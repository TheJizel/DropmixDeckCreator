package dropmixdeckcreator


import grails.testing.gorm.DomainUnitTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification
import spock.lang.Unroll

class DeckCreationControllerSpec extends Specification implements ControllerUnitTest<DeckCreationController>, DomainUnitTest<Card> {
    def setup() {
        controller.basicDeckCreationService = Mock(BasicDeckCreationService)
    }

    def cleanup() {
    }

    void "test index closure"() {
        given:
        Playlist playlist1 = new Playlist().save(false)
        Playlist playlist2 = new Playlist().save(false)
        Playlist playlist3 = new Playlist().save(false)
        Playlist playlist4 = new Playlist(released: false).save(false)

        when:
        controller.index()

        then:
        getView() == "/index"
        model.size() == 1
        model['playlists'] == [playlist1, playlist2, playlist3]
    }

    void "test advanced"() {
        given:
        Playlist playlist1 = new Playlist().save(false)
        Playlist playlist2 = new Playlist().save(false)
        Playlist playlist3 = new Playlist().save(false)
        Playlist playlist4 = new Playlist(released: false).save(false)
        Playlist playlist5 = new Playlist(released: true, inSeries: true).save(false)

        and:
        Card card1 = new Card(series: Series.ONE, pack: 1).save(false)
        Card card2 = new Card(series: Series.TWO, pack: 1).save(false)
        Card card3 = new Card(series: Series.THREE, pack: 1).save(false)
        Card card4 = new Card(series: Series.FOUR, pack: 1).save(false)
        Card card5 = new Card(series: Series.ONE, pack: 2).save(false)
        Card card6 = new Card(series: Series.TWO, pack: 2).save(false)
        Card card7 = new Card(series: Series.THREE, pack: 2).save(false)
        Card card8 = new Card(series: Series.FOUR, pack: 2).save(false)

        when:
        controller.advanced()

        then:
        getView() == "/advanced"
        model.size() == 2
        model['playlists'] == [playlist1, playlist2, playlist3]
        model['series'] == [[id: "1", packs: [1: [card1], 2: [card5]], number: "One"],
                            [id: "2", packs: [1: [card2], 2: [card6]], number: "Two"],
                            [id: "3", packs: [1: [card3], 2: [card7]], number: "Three"],
                            [id: "4", packs: [1: [card4], 2: [card8]], number: "Four"]]
    }

    void "test filter (bad request)"() {
        given:
        request.JSON = [not: "alist"]

        when:
        controller.filter()

        then:
        0 * controller.basicDeckCreationService.generateDecks(*_)
        response.text == "<br/>Sorry, an error has occurred. Please try again."
    }

    void "test filter (minimum number of cards error)"() {
        given:
        request.JSON = []

        when:
        controller.filter()

        then:
        0 * controller.basicDeckCreationService.generateDecks(*_)
        response.text == "<br/>Not enough cards selected. You need a minimum of 60 cards to proceed."
    }

    @Unroll
    void "test filter (return null or throw exception on service call)"() {
        given:
        List<Card> cards = []
        List<String> cardList = []
        controller.CARDS_PER_DECK.times {
            cardList.add("card${it + 1}")
            Card newCard = new Card().save(false)
            cards.add(newCard)
        }
        [cardList, cards].each {
            assert it.size() == controller.CARDS_PER_DECK
        }

        and:
        request.JSON = cardList

        when:
        controller.filter()

        then:
        1 * controller.basicDeckCreationService.generateDecks(cards, 4) >> closure
        response.text == "<br/>Sorry, an error has occurred. Please try again."

        where:
        closure << [{ return null }, { throw new Exception() }]
    }
}