angular.module('myApp', [])
    .controller('MainCtrl', ['$scope', '$http', function($scope, $http) {
        function changeAll(map, value) {
            for (var key in map) {
                map[key] = value;
            }
        }

        $scope.selectAll = function() {
            changeAll($scope.data.playlists, true);
            changeAll($scope.data.cards, true);
        };

        $scope.unselectAll = function() {
            changeAll($scope.data.playlists, false);
            changeAll($scope.data.cards, false);
        };

        $scope.generateDecks = function() {
            var cards = $scope.data.cards;
            var json = [];

            Object.keys(cards).forEach(function (key) {
                if (cards[key]) {
                    json.push(key);
                }
            });

            $http.post('deckCreation/filter?decks=' + $scope.decks, json).then(function(resp) {
                $("section").html(resp.data);
            });
        };

        $scope.clearResults = function() {
            $("section").html('');
        }
    }]);