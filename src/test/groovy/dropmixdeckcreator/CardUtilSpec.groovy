package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.CardUtil
import dropmixdeckcreator.Color
import dropmixdeckcreator.Level
import dropmixdeckcreator.Playlist
import dropmixdeckcreator.RandomUtil
import groovy.mock.interceptor.MockFor
import spock.lang.Specification

class CardUtilSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test getNextCardToUse"() {
        given:
        Playlist playlist = new Playlist()
        Color color = Color.RED
        Level level = Level.ONE
        Card firstCard = new Card(number: 1, color: color, level: level, playlist: playlist)
        Card secondCard = new Card(number: 2)
        Card thirdCard = new Card(number: 3, color: color, level: level, playlist: playlist)
        Card expected = new Card()
        List<Card> availableCards = [firstCard, secondCard, thirdCard]
        List<Card> eligibleCards = [firstCard, thirdCard]

        and:
        def mockForRandomUtil = new MockFor(RandomUtil)
        mockForRandomUtil.demand.with {
            getRandomElement(1) { list ->
                assert list == eligibleCards
                expected
            }
        }

        when:
        Card actual = null
        mockForRandomUtil.use {
            actual = CardUtil.getNextCardToUse(availableCards, playlist, color, level)
        }

        then:
        actual == expected
    }
}
