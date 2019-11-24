package dropmixdeckcreator

class PlaylistUtil {
    static Playlist getNextPlaylistToUse(Map<Playlist, Integer> playlistCounts) {
        List<Playlist> minorityPlaylists = findMinorityPlaylists(playlistCounts)

        RandomUtil.getRandomElement(minorityPlaylists)
    }

    private static List<Playlist> findMinorityPlaylists(Map<Playlist, Integer> playlistCounts) {
        if (!playlistCounts) {
            return []
        }

        playlistCounts.findAll {
            it.value ==  playlistCounts.sort { a, b -> a.value <=> b.value }.values().first()
        }.keySet().toList()
    }
}
