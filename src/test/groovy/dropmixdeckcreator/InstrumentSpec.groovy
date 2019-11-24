package dropmixdeckcreator

import dropmixdeckcreator.Instrument
import spock.lang.Specification

class InstrumentSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test toString"() {
        expect:
        "$Instrument.HORNS" == "Horns"
        "$Instrument.KEYS" == "Keys"
        "$Instrument.GUITAR" == "Guitar"
        "$Instrument.SAMPLER" == "Sampler"
        "$Instrument.STRINGS" == "Strings"
        "$Instrument.VOCALS" == "Vocals"
        "$Instrument.DRUMS" == "Drums"
    }
}
