<h2>Series</h2>
<div class="list dragscroll">
    <g:each in="${series}" var="aSeries">
        <span>
            Series ${aSeries.id}
            <input type="checkbox"
                   ng-model="data.series.${aSeries.number}"
                   ng-init="data.series.${aSeries.number} = true"
                   ng-change="<g:each in="${aSeries.packs}" var="pack">data.packs.series${aSeries.id}pack${pack.key} = data.series.${aSeries.number};
                       <g:each in="${pack.value}">data.cards.card${it.id} = data.series.${aSeries.number};</g:each></g:each>">
            <br/>
            <g:each in="${aSeries.packs}" var="pack">
                <hr/>
                <div class="row">
                    <div class="pack-img-div">
                        <asset:image src="series${aSeries.id}pack${pack.key}.jpg" style="width: 120px;"/>
                        <input type="checkbox"
                               class="pack-checkbox"
                               ng-model="data.packs.series${aSeries.id}pack${pack.key}"
                               ng-init="data.packs.series${aSeries.id}pack${pack.key} = true"
                               ng-change="<g:each in="${pack.value}">data.cards.card${it.id} = data.packs.series${aSeries.id}pack${pack.key};</g:each>">
                    </div>
                    <div>
                        <g:each in="${pack.value.sort({it.id})}" var="card">
                            <label style="background-color: ${card.color.hex};">
                                <input type="checkbox"
                                       ng-model="data.cards.card${card.id}"
                                       ng-init="data.cards.card${card.id} = true">
                                [${card.level.id}] ${card.song.artist.name} - ${card.song.name}
                            </label>
                            <br/>
                        </g:each>
                    </div>
                </div>
            </g:each>
            <br/>
        </span>
    </g:each>
</div>