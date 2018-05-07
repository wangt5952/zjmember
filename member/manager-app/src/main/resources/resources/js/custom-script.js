/*================================================================================
	Item Name: Materialize - Material Design Admin Template
	Version: 4.0
	Author: PIXINVENT
	Author URL: https://themeforest.net/user/pixinvent/portfolio
================================================================================

NOTE:
------
PLACE HERE YOUR OWN JS CODES AND IF NEEDED.
WE WILL RELEASE FUTURE UPDATES SO IN ORDER TO NOT OVERWRITE YOUR CUSTOM SCRIPT IT'S BETTER LIKE THIS. */

function dateTimeString(date) {
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()
    var hour = date.getHours()
    var minutes = date.getMinutes()
    var second = date.getSeconds()

    month = month < 10 ? '0'+month : month
    day = day < 10 ? '0'+day : day
    hour = hour < 10 ? '0'+hour : hour
    minutes = minutes < 10 ? '0'+minutes : minutes
    second = second < 10 ? '0'+second : second

    return year + '-' + month + '-' + day + ' ' + hour + ':'+ minutes + ':' + second
}

function dateString(date) {
    var year = date.getFullYear()
    var month = date.getMonth() + 1
    var day = date.getDate()

    month = month < 10 ? '0'+month : month
    day = day < 10 ? '0'+day : day

    return year + '-' + month + '-' + day
}

function dataWithoutYearString(date) {
    var month = date.getMonth() + 1
    var day = date.getDate()

    month = month < 10 ? '0'+month : month
    day = day < 10 ? '0'+day : day

    return month + '-' + day
}

function dateToMilliseconds(dataString) {
    var dataAndTime = dataString.split(' ')
    var d = dataAndTime[0]
    var t = dataAndTime[1] ? dataAndTime[1] : '00:00:00'

    var dateParts = new Date((Number(d.split("-")[0])), (Number(d.split("-")[1]) - 1), (Number(d.split("-")[2])));
    var date = dateParts.getTime();

    var hAndmAnds = t.split(':')
    var h = Number(hAndmAnds[0])
    var m = Number(hAndmAnds[1])
    var s = Number(hAndmAnds[2])

    var time = (((h * 60 + m) * 60) + s) * 1000;

    log(date+time)

    return date + time
}

var log = console.log

function getClassNameArray(eleName) {
    var ele = document.getElementsByTagName(eleName)[0]

    if (ele) {
        return ele.className.split(/\s+/)
    }
}

$(document).ready(function () {
    var classNames = getClassNameArray('body')
    var className = undefined

    for (var i = 0; i < classNames.length; i++) {
        if (classNames[i].indexOf("active-") != -1) {
            className = classNames[i]
            break
        }
    }

    if (className) {
        var eles = document.getElementsByClassName(className)

        for (var j = 0; j < eles.length; j++) {
            var ele = eles[j]

            if (ele && ele.tagName.toLowerCase() == 'li') {
                var old = ele.getAttribute('class')
                var new_class_name = old + ' active cyan active-white'

                var ele_i = ele.getElementsByTagName('i')[0]
                // log(ele_i.className)
                if (ele_i) {
                    ele_i.style = 'color:#fff;'
                }

                ele.setAttribute('class', new_class_name)
            }
        }
    }
})

function dialog(title, message, status) {

    if (typeof swal !== 'undefined' && typeof swal == 'function' ) {
        swal(title, message, status)
    } else {
        alert(message)
    }
}

function errorDialog(message) {
    dialog("Cancelled", message, "error")
}

function okDialog(message) {
    dialog("Succeed", message, "success")
}

function confirmDialog(_title, message, okButtonText, handler) {
    if (typeof swal !== 'undefined' && typeof swal == 'function') {
        swal({
                title: _title,
                text: message,
                type: "warning",
                showCancelButton: true,
                cancelButtonText: '取消',
                confirmButtonColor: '#DD6B55',
                confirmButtonText: okButtonText,
                closeOnConfirm: false
            },
            handler
        )
    } else {
        alert('检查[SweetAlert]插件')
    }
}

function defaultConfirmDialog(message, okButtonText, handler) {
    confirmDialog("请确认", message, okButtonText, handler)
}

