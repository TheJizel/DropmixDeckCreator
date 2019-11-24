package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.Color
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class CardSpec extends Specification implements DomainUnitTest<Card> {

    def setup() {
    }

    def cleanup() {
    }

    void "test isWildcard"() {
        expect:
        new Card(color: Color.ALL).wildcard
        !new Card(color: Color.BLUE).wildcard
        !new Card(color: Color.GREEN).wildcard
        !new Card(color: Color.RED).wildcard
        !new Card(color: Color.WHITE).wildcard
        !new Card(color: Color.YELLOW).wildcard
    }

    void "test isFx"() {
        expect:
        !new Card(color: Color.ALL).fx
        !new Card(color: Color.BLUE).fx
        !new Card(color: Color.GREEN).fx
        !new Card(color: Color.RED).fx
        new Card(color: Color.WHITE).fx
        !new Card(color: Color.YELLOW).fx
    }

    void "test getFullSeasonId"() {
        expect:
        new Card(promotional: true, season: 1).fullSeasonId == "P01"
        new Card(promotional: false, season: 2).fullSeasonId == "S02"
    }

    void "test getFullCardId"() {
        expect:
        new Card(number: 1).fullCardId == "C001"
        new Card(number: 20).fullCardId == "C020"
        new Card(number: 200).fullCardId == "C200"
    }

    void "test getFullId"() {
        expect:
        new Card(promotional: true, season: 1, number: 1).fullId == "P01C001"
        new Card(promotional: false, season: 2, number: 20).fullId == "S02C020"
        new Card(promotional: false, season: 30, number: 200).fullId == "S30C200"
    }
}
