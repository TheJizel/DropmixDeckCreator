package dropmixdeckcreator

import dropmixdeckcreator.Playlist
import dropmixdeckcreator.PlaylistUtil
import dropmixdeckcreator.RandomUtil
import groovy.mock.interceptor.MockFor
import spock.lang.Specification
import spock.lang.Unroll

class PlaylistUtilSpec extends Specification {
    final static Playlist playlist1 = new Playlist(name: "1")
    final static Playlist playlist2 = new Playlist(name: "2")
    final static Playlist playlist3 = new Playlist(name: "3")

    def setup() {
    }

    def cleanup() {
    }

    void "test getNextPlaylistToUse"() {
        given:
        Playlist first = new Playlist(name: 'first')
        Playlist second = new Playlist(name: 'second')
        Playlist last = new Playlist(name: 'last')
        Playlist expected = new Playlist(name: 'expected')
        Map<Playlist, Integer> playlistCounts = [(first): 1, (second): 2, (last): 1]
        List<Playlist> minorityPlaylists = [first, last]

        and:
        def mockForRandomUtil = new MockFor(RandomUtil)
        mockForRandomUtil.demand.with {
            getRandomElement(1) { list ->
                assert list == minorityPlaylists
                expected
            }
        }

        when:
        Playlist actual = null
        mockForRandomUtil.use {
            actual = PlaylistUtil.getNextPlaylistToUse(playlistCounts)
        }

        then:
        actual == expected

    }

    void "test findMinorityPlaylists - null or empty map"() {
        expect:
        PlaylistUtil.findMinorityPlaylists(null) == []
        PlaylistUtil.findMinorityPlaylists([:]) == []
    }

    void "test findMinorityPlaylists - list of one"() {
        expect:
        PlaylistUtil.findMinorityPlaylists([(playlist1): 1]) == [playlist1]
    }

    @Unroll
    void "test findMinorityPlaylists"() {
        expect:
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [3, 2, 1][index]] }) == [playlist3]
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [1, 2, 3][index]] }) == [playlist1]
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [2, 1, 3][index]] }) == [playlist2]
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [1, 1, 2][index]] }) == [playlist1, playlist2]
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [2, 1, 1][index]] }) == [playlist2, playlist3]
        PlaylistUtil.findMinorityPlaylists((0..2).collectEntries { index -> [[playlist1, playlist2, playlist3][index], [1, 2, 1][index]] }) == [playlist1, playlist3]
    }
}
