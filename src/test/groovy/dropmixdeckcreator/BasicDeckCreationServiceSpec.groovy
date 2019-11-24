package dropmixdeckcreator

import dropmixdeckcreator.BasicDeckCreationService
import dropmixdeckcreator.Card
import dropmixdeckcreator.CardUtil
import dropmixdeckcreator.Color
import dropmixdeckcreator.Level
import dropmixdeckcreator.Playlist
import dropmixdeckcreator.PlaylistUtil
import dropmixdeckcreator.RandomUtil
import dropmixdeckcreator.SetTemplate
import dropmixdeckcreator.SetTemplateUtil
import grails.testing.gorm.DomainUnitTest
import grails.testing.services.ServiceUnitTest
import groovy.mock.interceptor.MockFor
import spock.lang.Specification
import spock.lang.Unroll

class BasicDeckCreationServiceSpec extends Specification implements ServiceUnitTest<BasicDeckCreationService>, DomainUnitTest<Playlist> {

    def setup() {
    }

    def cleanup() {
    }

    void "test generateDecks (no possibilities)"() {
        given:
        def mockForSetTemplateUtil = new MockFor(SetTemplateUtil)
        def mockForRandomUtil = new MockFor(RandomUtil)
        BasicDeckCreationService basicDeckCreationServiceSpy = Spy(BasicDeckCreationService)
        List<Card> cardsToInclude = [new Card()]
        int numberOfSets = 4

        and:
        mockForSetTemplateUtil.demand.with {
            computePossibleSetTemplates(1) { List<Card> availableCards, int numberOfSetsArg ->
                assert availableCards == cardsToInclude
                assert numberOfSetsArg == numberOfSets
                null
            }
        }
        mockForRandomUtil.demand.with {
            getRandomElement(0) { }
        }

        when:
        List<List<Card>> result = null
        mockForRandomUtil.use {
            mockForSetTemplateUtil.use {
                result = basicDeckCreationServiceSpy.generateDecks(cardsToInclude, numberOfSets)
            }
        }

        then:
        0 * basicDeckCreationServiceSpy.generateDecks(_ as List<Card>, _ as int, _ as List<SetTemplate>)
        result.isEmpty()
    }

    void "test generateDecks"() {
        given:
        def mockForSetTemplateUtil = new MockFor(SetTemplateUtil)
        def mockForRandomUtil = new MockFor(RandomUtil)
        List<Card> cardsToInclude = [new Card()]
        List<Card> cardsToAdd = [new Card(), new Card()]
        List<List<Card>> expected = [cardsToAdd]
        int numberOfSets = 1
        SetTemplate setTemplate = SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX
        List<SetTemplate> possibility = [setTemplate]
        List<List<SetTemplate>> possibilities = [possibility]

        and:
        BasicDeckCreationService basicDeckCreationServiceSpy = Spy(BasicDeckCreationService)
        mockForSetTemplateUtil.demand.with {
            computePossibleSetTemplates(1) { List<Card> availableCards, int numberOfSetsArg ->
                assert availableCards == cardsToInclude
                assert numberOfSetsArg == numberOfSets
                possibilities
            }
        }
        mockForRandomUtil.demand.with {
            getRandomElement(1) { list ->
                assert list == possibilities
                possibility
            }
        }

        when:
        List<List<Card>> actual = null
        mockForSetTemplateUtil.use {
            mockForRandomUtil.use {
                actual = basicDeckCreationServiceSpy.generateDecks(cardsToInclude, numberOfSets)
            }
        }

        then:
        1 * basicDeckCreationServiceSpy.generateDecks(cardsToInclude, numberOfSets, possibility) >> expected
        actual == expected
    }

    void "test generateDecks with given List<SetTemplate> (null cases)"() {
        expect:
        service.generateDecks(null, 4, null) == null
        service.generateDecks([new Card()], 4, null) == null
        service.generateDecks(null,4, [SetTemplate.BOARD_FLEXIBLE_WITH_ONE_PLUS_THREE_FX]) == null
        service.generateDecks([], 4, []) == null
        service.generateDecks([new Card()], 4, []) == null
        service.generateDecks([],4, [SetTemplate.BOARD_FLEXIBLE_WITH_ONE_PLUS_THREE_FX]) == null
    }

    void "test generateDecks with given List<SetTemplate>"() {
        given:
        def mockForRandomUtil = new MockFor(RandomUtil)
        List<Card> cardsToInclude = [new Card()]
        Card card = Mock(Card)
        int numberOfSets = 1
        SetTemplate setTemplate = SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_THREE_FX
        List<SetTemplate> setTemplates = [setTemplate]
        List<Card> cards = [card]
        List<List<Card>> expected = [cards]

        and:
        BasicDeckCreationService basicDeckCreationServiceSpy = Spy(BasicDeckCreationService)
        mockForRandomUtil.demand.with {
            getRandomElement(1) { list ->
                assert list == setTemplates
                setTemplate
            }
        }

        when:
        List<List<Card>> actual = null
        mockForRandomUtil.use {
            actual = basicDeckCreationServiceSpy.generateDecks(cardsToInclude, numberOfSets, setTemplates)
        }

        then:
        1 * basicDeckCreationServiceSpy.generateDeck(cardsToInclude, setTemplate) >> cards
        actual == expected
    }

    void "test generateDeck (availableCards is null/empty or setTemplate is null)"() {
        expect:
        service.generateDeck([], SetTemplate.BOARD_FLEXIBLE_WITH_TWO_PLUS_TWO_FX) == null
        service.generateDeck(null, SetTemplate.COLOR_BALANCE_WITH_TWO_PLUS_TWO_FX) == null
        service.generateDeck([Mock(Card)], null) == null
    }

    void "test generateDeck"() {
        given:
        BasicDeckCreationService basicDeckCreationServiceSpy = Spy(BasicDeckCreationService)
        SetTemplate setTemplateMock = GroovyMock(SetTemplate)
        Card cardMock = Mock(Card)
        Playlist playlist = Mock(Playlist)
        List<Card> availableCards = [cardMock]
        Color colorMock = GroovyMock(Color)
        Level levelMock = GroovyMock(Level)
        Map<Playlist, Integer> playlistCounts = [(playlist): 0]
        Map<Color, Map<Level, Integer>> composition = [(colorMock): [(levelMock): 1]]
        Card expected = Mock(Card)

        when:
        List<Card> actual = basicDeckCreationServiceSpy.generateDeck(availableCards, setTemplateMock)

        then:
        1 * setTemplateMock.asBoolean() >> true
        1 * setTemplateMock.getComposition() >> composition
        1 * cardMock.getPlaylist() >> playlist
        1 * basicDeckCreationServiceSpy.getNextCard(availableCards, playlistCounts, colorMock, levelMock) >> expected
        actual == [expected]
    }

    @Unroll
    void "test getNextCard (null/empty arg scenarios)"() {
        when:
        Card card = service.getNextCard(availableCards, playlistCounts, color, level)

        then:
        !card

        where:
        availableCards << [null,
                           [],
                           [Mock(Card)],
                           [Mock(Card)],
                           [Mock(Card)],
                           [Mock(Card)]]
        playlistCounts << [[(GroovyMock(Playlist)): 1],
                           [(GroovyMock(Playlist)): 1],
                           null,
                           [:],
                           [(GroovyMock(Playlist)): 1],
                           [(GroovyMock(Playlist)): 1]]
        color << [Color.RED,
                  Color.RED,
                  Color.RED,
                  Color.RED,
                  null,
                  Color.RED]
        level << [Level.ONE,
                  Level.ONE,
                  Level.ONE,
                  Level.ONE,
                  Level.ONE,
                  null]
    }

    void "test getNextCard (applicableCards is empty)"() {
        given:
        Card cardMock = Mock(Card)
        List<Card> availableCards = [cardMock]
        Playlist playlistMock = Mock(Playlist)
        Playlist returnedPlaylist = Mock(Playlist)
        Map<Playlist, Integer> playlistCounts = [(playlistMock): 0]
        Color color = Color.RED
        Level level = Level.ONE

        and:
        def mockForPlaylistUtil = new MockFor(PlaylistUtil)
        mockForPlaylistUtil.demand.with {
            getNextPlaylistToUse(1) { Map<Playlist, Integer> playlistCountsArg ->
                assert playlistCountsArg.isEmpty()
                returnedPlaylist
            }
        }
        def mockForCardUtil = new MockFor(CardUtil)
        mockForCardUtil.demand.with {
            getNextCardToUse(1) { List<Card> applicableCards, Playlist playlistArg, Color colorArg, Level levelArg ->
                assert applicableCards.isEmpty()
                assert playlistArg == returnedPlaylist
                assert colorArg == color
                assert levelArg == level
                null
            }
        }

        when:
        Card card = new Card()
        mockForPlaylistUtil.use {
            mockForCardUtil.use {
                card = service.getNextCard(availableCards, playlistCounts, color, level)
            }
        }

        then:
        1 * cardMock.getColor() >> Color.BLUE
        0 * cardMock.getLevel()
        !card
    }

    void "test getNextCard"() {
        given:
        Card cardMock = Mock(Card)
        List<Card> availableCards = [cardMock]
        Playlist playlistMock = Mock(Playlist)
        Playlist returnedPlaylist = Mock(Playlist)
        Map<Playlist, Integer> playlistCounts = [(playlistMock): 0, (returnedPlaylist): 0]
        Color color = Color.RED
        Level level = Level.ONE

        and:
        def mockForPlaylistUtil = new MockFor(PlaylistUtil)
        mockForPlaylistUtil.demand.with {
            getNextPlaylistToUse(1) { Map<Playlist, Integer> playlistCountsArg ->
                assert playlistCountsArg == [(playlistMock): 0]
                returnedPlaylist
            }
        }
        def mockForCardUtil = new MockFor(CardUtil)
        mockForCardUtil.demand.with {
            getNextCardToUse(1) { List<Card> applicableCards, Playlist playlistArg, Color colorArg, Level levelArg ->
                assert applicableCards == availableCards
                assert playlistArg == returnedPlaylist
                assert colorArg == color
                assert levelArg == level
                cardMock
            }
        }

        when:
        Card card = new Card()
        mockForPlaylistUtil.use {
            mockForCardUtil.use {
                card = service.getNextCard(availableCards, playlistCounts, color, level)
            }
        }

        then:
        1 * cardMock.getColor() >> color
        1 * cardMock.getLevel() >> level
        1 * cardMock.getPlaylist() >> playlistMock
        playlistCounts[(returnedPlaylist)] == 1
        availableCards.isEmpty()
        card == cardMock
    }
}
