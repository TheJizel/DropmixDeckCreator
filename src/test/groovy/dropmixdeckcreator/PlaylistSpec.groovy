package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.Playlist
import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class PlaylistSpec extends Specification implements DomainUnitTest<Playlist> {

    def setup() {
    }

    def cleanup() {
    }

    void "test isPromotional"() {
        expect:
        !new Playlist().promotional
        !new Playlist(cards: []).promotional
        !new Playlist(cards: [new Card(promotional: false)]).promotional
        !new Playlist(cards: [new Card(promotional: false), new Card(promotional: true)]).promotional
        new Playlist(cards: [new Card(promotional: true)]).promotional
        new Playlist(cards: [new Card(promotional: true), new Card(promotional: true)]).promotional
    }
}
