const $ = require('jquery');
global.$ = global.jQuery = $;
require('bootstrap');
global.SockJS = require('sockjs-client');
require('stompjs/lib/stomp.min.js');

$('.custom-file-input').on('change', function () {
    let fileName = $(this).val().split("\\").pop();
    $(this).next('.custom-file-label').html(fileName);
});
