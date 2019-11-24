<html>
    <head>
        <title>Dropmix Deck Creator</title>
    </head>
    <body>
        <g:try>
            <g:each status="deckIndex" var="deck" in="${decks}">
                <h1>Deck #${deckIndex + 1}</h1>
                <div class="deck">
                    <span>
                        <g:each var="card" in="${deck}">
                            <g:link target="_blank" base="http://dropmixin.com/wiki/Card:" uri="${card.fullId}">
                                <asset:image src="cards/${card.playlist.name}/${card.fullId}.jpeg"
                                             width="${768 / 3}"
                                             height="${1024 / 3}"/>
                            </g:link>
                        </g:each>
                    </span>
                </div>
            </g:each>
        </g:try>
        <g:catch>
            <br/>Sorry, an error has occurred. Please try again.
        </g:catch>
    </body>
</html>