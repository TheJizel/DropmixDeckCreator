<h2>Playlists</h2>
<div class="list dragscroll">
    <g:each in="${playlists}" var="playlist">
        <span>
            ${playlist.name}
            <input type="checkbox"
                   ng-model="data.playlists.${playlist.name}"
                   ng-init="data.playlists.${playlist.name} = true"
                   ng-change="<g:each in="${playlist.cards}">data.cards.card${it.id} = data.playlists.${playlist.name};</g:each>">
            <br/>
            <g:each in="${playlist.cards.sort({it.id})}" var="card">
                <label style="background-color: ${card.color.hex};">
                    <input type="checkbox"
                           ng-model="data.cards.card${card.id}"
                           ng-init="data.cards.card${card.id} = true">
                    [${card.level.id}] ${card.song.artist.name} - ${card.song.name}
                </label>
                <br/>
            </g:each>
            <br/>
        </span>
    </g:each>
</div>