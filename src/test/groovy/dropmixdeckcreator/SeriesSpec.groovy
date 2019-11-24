package dropmixdeckcreator

import spock.lang.Specification

import static dropmixdeckcreator.Series.*

class SeriesSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test get"() {
        expect:
        get(1) == ONE
        get(2) == TWO
        get(3) == THREE
        get(4) == FOUR
        !get(0)
        !get(5)
    }

    void "test toString"() {
        expect:
        "$ONE" == "One"
        "$TWO" == "Two"
        "$THREE" == "Three"
        "$FOUR" == "Four"
    }
}
