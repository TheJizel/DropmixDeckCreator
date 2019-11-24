package dropmixdeckcreator

import dropmixdeckcreator.Key
import spock.lang.Specification
import spock.lang.Unroll

import static dropmixdeckcreator.Key.*

class KeySpec extends Specification {
    List<Key> keys =  [C, Cm, Db, Dbm, D, Dm, Eb, Ebm, E, Em, F, Fm, Gb, Gbm, G, Gm, Ab, Abm, A, Am, Bb, Bbm, B, Bm]

    def setup() {
    }

    def cleanup() {
    }

    void "test get"() {
        expect:
        keys.eachWithIndex { key, index -> assert get(index + 1) == key }
        get(0) == NA
        !get(25)
    }

    void "test isMajor"() {
        expect:
        keys.eachWithIndex { key, index -> assert (index % 2 == 0 ? key.major : !key.major) }
        !NA.major
    }

    void "test isMinor"() {
        expect:
        keys.eachWithIndex { key, index -> assert (index % 2 == 0 ? !key.minor : key.minor) }
        !NA.minor
    }

    void "test toString"() {
        given:
        List<String> names =
                ["C Major",
                 "C Minor",
                 "Db Major",
                 "Db Minor",
                 "D Major",
                 "D Minor",
                 "Eb Major",
                 "Eb Minor",
                 "E Major",
                 "E Minor",
                 "F Major",
                 "F Minor",
                 "Gb Major",
                 "Gb Minor",
                 "G Major",
                 "G Minor",
                 "Ab Major",
                 "Ab Minor",
                 "A Major",
                 "A Minor",
                 "Bb Major",
                 "Bb Minor",
                 "B Major",
                 "B Minor"]

        expect:
        names.eachWithIndex { name, index -> assert "${keys[index]}" == name }
        "$NA" == "n/a"
    }

    void "test findByName"() {
        given:
        List<String> names =
                ["C Major",
                 "C Minor",
                 "Db Major",
                 "Db Minor",
                 "D Major",
                 "D Minor",
                 "Eb Major",
                 "Eb Minor",
                 "E Major",
                 "E Minor",
                 "F Major",
                 "F Minor",
                 "Gb Major",
                 "Gb Minor",
                 "G Major",
                 "G Minor",
                 "Ab Major",
                 "Ab Minor",
                 "A Major",
                 "A Minor",
                 "Bb Major",
                 "Bb Minor",
                 "B Major",
                 "B Minor"]

        expect:
        names.eachWithIndex{ name, index ->  assert findByName(name) == keys[index] }
        findByName("n/a") == NA
        findByName(null) == null
    }

    @Unroll
    void "test harmonic helper methods (getSubDominant, getDominant, getRelative, and getParallel)"() {
        expect:
        expecteds.eachWithIndex { expected, index -> assert keys[index]."$method" == expected }
        NA."$method" == NA

        where:
        method << ["subDominant", "dominant", "relative", "parallel"]
        expecteds << [[F, Fm, Gb, Gbm, G, Gm, Ab, Abm, A, Am, Bb, Bbm, B, Bm, C, Cm, Db, Dbm, D, Dm, Eb, Ebm, E, Em],
                      [G, Gm, Ab, Abm, A, Am, Bb, Bbm, B, Bm, C, Cm, Db, Dbm, D, Dm, Eb, Ebm, E, Em, F, Fm, Gb, Gbm],
                      [Am, Eb, Bbm, E, Bm, F, Cm, Gb, Dbm, G, Dm, Ab, Ebm, A, Em, Bb, Fm, B, Gbm, C, Gm, Db, Abm, D],
                      [Cm, C, Dbm, Db, Dm, D, Ebm, Eb, Em, E, Fm, F, Gbm, Gb, Gm, G, Abm, Ab, Am, A, Bbm, Bb, Bm, B]]
    }

    void "test getMixSet"() {
        given:
        List<List<Key>> expecteds =
                [[C, F, G, Am],
                 [Cm, Fm, Gm, Eb],
                 [Db, Gb, Ab, Bbm],
                 [Dbm, Gbm, Abm, E],
                 [D, G, A, Bm],
                 [Dm, Gm, Am, F],
                 [Eb, Ab, Bb, Cm],
                 [Ebm, Abm, Bbm, Gb],
                 [E, A, B, Dbm],
                 [Em, Am, Bm, G],
                 [F, Bb, C, Dm],
                 [Fm, Bbm, Cm, Ab],
                 [Gb, B, Db, Ebm],
                 [Gbm, Bm, Dbm, A],
                 [G, C, D, Em],
                 [Gm, Cm, Dm, Bb],
                 [Ab, Db, Eb, Fm],
                 [Abm, Dbm, Ebm, B],
                 [A, D, E, Gbm],
                 [Am, Dm, Em, C],
                 [Bb, Eb, F, Gm],
                 [Bbm, Ebm, Fm, Db],
                 [B, E, Gb, Abm],
                 [Bm, Em, Gbm, D]]

        expect:
        expecteds.eachWithIndex { expected, index -> assert keys[index].mixSet == expected }
    }
}
