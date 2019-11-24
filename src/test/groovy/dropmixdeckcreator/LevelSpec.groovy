package dropmixdeckcreator

import spock.lang.Specification

import static dropmixdeckcreator.Level.*

class LevelSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "test get"() {
        expect:
        get(1) == ONE
        get(2) == TWO
        get(3) == THREE
        !get(0)
        !get(4)
    }

    void "test toString"() {
        expect:
        "$ONE" == "1"
        "$TWO" == "2"
        "$THREE" == "3"
    }
}
