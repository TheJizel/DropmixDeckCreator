<html ng-app="myApp">
<head>
    <title>Dropmix Deck Creator</title>
    <asset:javascript src="jquery-3.3.1.min.js"/>
    <asset:javascript src="angular.min.js"/>
    <asset:javascript src="dragscroll.js"/>
    <asset:javascript src="index.js"/>
    <asset:stylesheet src="index.css"/>
</head>
<body>
    <div ng-controller="MainCtrl">
        <div>
            <input class="changeSortMode" type="button" value="Sort by series" onclick="window.location.href = '/advanced'">
        </div>
        <br/>
        <form ng-model="decks" ng-init="decks = 4">
            Number of decks:
            <g:each in="${1..4}">
                <input type="radio" ng-model="decks" value="${it}" ng-checked="decks == ${it}">${it}
            </g:each>
        </form>
        <div>
            <input type="button" value="Select all" ng-click="selectAll()"/>
            <input type="button" value="Unselect all" ng-click="unselectAll()"/>
        </div>
        <br/>
        <div>
            <input type="button"
                   ng-model="showResults"
                   ng-init="showResults = false"
                   value="Go!"
                   ng-click="generateDecks(); showResults = true;"
                   ng-disabled="!decks"/>
            <input type="button"
                   value="Clear results"
                   ng-click="clearResults(); showResults = false;"/>
        </div>
        <div ng-model="data" ng-init="data = {}" ng-hide="showResults">
            <g:render template="/templates/playlists"/>
        </div>
    </div>
<section>
</section>
</body>
</html>