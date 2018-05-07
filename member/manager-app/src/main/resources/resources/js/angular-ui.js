/*
 * AngularUI - Angular JS
 */

var app = angular.module('materializeApp', ['ui.materialize'])
    .controller('BodyController', ["$scope", function($scope) {
        $scope.select = {
            value1: "Option1",
            value2: "I'm an option",
            choices: ["Option1", "I'm an option", "This is materialize", "No, this is Patrick."]
        };

        $scope.dummyInputs = {};

    }])
    .controller('CollapsibleController', ["$scope", function($scope) {
        $scope.collapsibleElements = [{
            icon: 'mdi-image-filter-drama',
            title: 'First',
            content: 'Lorem ipsum dolor sit amet.'
        }, {
            icon: 'mdi-maps-place',
            title: 'Second',
            content: 'Lorem ipsum dolor sit amet.'
        }, {
            icon: 'mdi-social-whatshot',
            title: 'Third',
            content: 'Lorem ipsum dolor sit amet.'
        }];
    }]).controller('PaginationController', ["$scope", function($scope) {
        $scope.changePage = function(page) {
            // Materialize.toast("Changed to page " + page, 1000);
            var $index = page

            var isNotNull = document.getElementById('form-filter')

            if (isNotNull) {
                if ($('#page')) {
                    $('#page').val(parseInt($index))
                }

                if ($('#size')) {
                    $('#size').val(10)
                }

                $('#form-filter').submit()
            } else {
                var pageObj = {
                    'index': $index,
                    'size': 10,
                    'total': 1000,
                }

                $('#pageJson').val(JSON.stringify(pageObj))

                console.log($('#pageJson').val())
                $('#pageForm').submit()
            }
        }
    }])
    .controller('DateController', ["$scope", function($scope) {
        var currentTime = new Date();
        // $scope.currentTime = currentTime;
        // $scope.month = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
        $scope.month = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
        // $scope.monthShort = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        $scope.monthShort = ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月'];
        // $scope.weekdaysFull = ['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];
        $scope.weekdaysFull = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        $scope.weekdaysShort = ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'];
        // $scope.weekdaysLetter = ['S', 'M', 'T', 'W', 'T', 'F', 'S'];
        $scope.weekdaysLetter = ['日', '一', '二', '三', '四', '五', '六'];
        // $scope.disable = [false, 1, 7];
        $scope.today = 'Today';
        $scope.clear = 'Clear';
        $scope.close = 'Close';
        $scope.format = 'yyyy-mm-dd';
        $scope.formatSubmit = 'yyyy-mm-dd';
        // var days = 15;
        // $scope.minDate = (new Date($scope.currentTime.getTime() - (1000 * 60 * 60 * 24 * days))).toISOString();
        // $scope.maxDate = (new Date($scope.currentTime.getTime() + (1000 * 60 * 60 * 24 * days))).toISOString();
        $scope.onStart = function() {
            // console.log('onStart');
        };
        $scope.onRender = function() {
            // console.log('onRender');
        };
        $scope.onOpen = function() {
            // console.log('onOpen');
        };
        $scope.onClose = function() {
            // console.log('onClose');
        };

        if (typeof thisSet != 'undefined' && thisSet instanceof Function) {
            $scope.onSet = thisSet
        } else {
            $scope.onSet = function () {
                console.log('onSet');
                // log($('shoppingDate1').val())
            };
        }
        // }
        $scope.onStop = function() {
            // console.log('onStop');
        };
    }]);