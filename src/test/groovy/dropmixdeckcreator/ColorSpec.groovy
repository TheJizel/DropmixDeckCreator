package dropmixdeckcreator

import dropmixdeckcreator.Color
import spock.lang.Specification

import static dropmixdeckcreator.Color.*

class ColorSpec extends Specification {
    List<Color> colors = [ALL, BLUE, GREEN, RED, WHITE, YELLOW]

    def setup() {
    }

    def cleanup() {
    }

    void "test get"() {
        expect:
        get(1) == ALL
        get(2) == BLUE
        get(3) == GREEN
        get(4) == RED
        get(5) == WHITE
        get(6) == YELLOW
        !get(0)
        !get(7)
    }

    void "test toString"() {
        expect:
        "$ALL" == "All"
        "$BLUE" == "Blue"
        "$GREEN" == "Green"
        "$RED" == "Red"
        "$WHITE" == "White"
        "$YELLOW" == "Yellow"
    }

    void "test findByName"() {
        given:
        List<String> names = ["All", "Blue", "Green", "Red", "White", "Yellow"]

        expect:
        names.eachWithIndex { name, index -> assert findByName(name) == colors[index] }
        findByName(null) == null
    }
}
