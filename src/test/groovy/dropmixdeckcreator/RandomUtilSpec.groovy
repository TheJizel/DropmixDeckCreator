package dropmixdeckcreator

import dropmixdeckcreator.Card
import dropmixdeckcreator.Playlist
import dropmixdeckcreator.RandomUtil
import groovy.mock.interceptor.MockFor
import spock.lang.Specification
import spock.lang.Unroll

class RandomUtilSpec extends Specification {
    def setup() {
    }

    def cleanup() {
    }

    void "test getRandomElement - no collection"() {
        expect:
        RandomUtil.getRandomElement(null) == null
        RandomUtil.getRandomElement([]) == null
    }

    @Unroll
    void "test getRandomElement"() {
        given:
        def randomMock = new MockFor(Random)
        randomMock.demand.with {
            nextInt(1) { int bound ->
                assert bound == 3
                1
            }
        }

        when:
        Object result = null
        randomMock.use {
            result = RandomUtil.getRandomElement(collection)
        }

        then:
        result.class == expectedClass
        result == collection[1]

        where:
        expectedClass << [Card.class, Playlist.class]
        collection << [new HashSet<Card>([new Card(), new Card(), new Card()]),
                       [new Playlist(), new Playlist(), new Playlist()]]
    }
}
