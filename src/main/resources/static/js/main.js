'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

var username = null;
var stompClient = null;

function connect(event) {
    event.preventDefault();
    username = document.querySelector('#name').value.trim();
    if (username) {
        // Ubah layout Website
        usernamePage.classList.add("hidden");
        chatPage.classList.remove("hidden");

        // Initialize WebSocket dengan SockJS
        var socket = new SockJS('ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError); // Connecting to Web Socket
    }
}

function onConnected() {
    // Subscribe to /topic/public
    stompClient.subscribe('/topic/public', onMessageReceived);

    // Kasih tau server ada user masuk, biar usernamenya disimpan di Header server nantinya
    stompClient.send(
        '/app/chat.addUser',
        {},
        JSON.stringify({sender: username, type: "JOIN"}) // Ini yang nanti jadi Object ChatMessage nya
    );

    connectingElement.classList.add('hidden');
}

function onError() {
    connectingElement.textContent = 'Could not connect to server, please refresh this page and try again!';
    connectingElement.style.color = 'red';
}

// Ini fungsi buat di onConnected
function onMessageReceived(payload) { // payload itu dh inject langsung dari stompClient.subscribe()
    console.log('Our Paylaod : ', payload); // payload itu Message dari WebSocket dia ada Header dan ada Body
    var message = JSON.parse(payload.body);

        var messageElement = document.createElement('li');

        if(message.type === 'JOIN') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' joined!';
        } else if (message.type === 'LEAVE') {
            messageElement.classList.add('event-message');
            message.content = message.sender + ' left!';
        } else {
            messageElement.classList.add('chat-message');

            var avatarElement = document.createElement('i');
            var avatarText = document.createTextNode(message.sender[0]);
            avatarElement.appendChild(avatarText);
            avatarElement.style['background-color'] = getAvatarColor(message.sender);

            messageElement.appendChild(avatarElement);

            var usernameElement = document.createElement('span');
            var usernameText = document.createTextNode(message.sender);
            usernameElement.appendChild(usernameText);
            messageElement.appendChild(usernameElement);
        }

        var textElement = document.createElement('p');
        var messageText = document.createTextNode(message.content);
        textElement.appendChild(messageText);

        messageElement.appendChild(textElement);

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
}

// Ini fungsi buat dapetin warna random
function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }
    var index = Math.abs(hash % colors.length);
    return colors[index];
}

function sendMessage(event) {
    event.preventDefault();

    var messageContent = messageInput.value.trim();
    // Jika pesannya tidak kosong dan Koneksi ke WebSocket tidak terputus maka :
    if(messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageContent,
            type: 'CHAT'
        };

        // Kirim pesannya
        stompClient.send('/app/chat.sendMessage',
            {},
            JSON.stringify(chatMessage)
        );

        // Kosongkan formnya lagi
        messageInput.value = '';
    }

}

usernameForm.addEventListener('submit', connect,true);
messageForm.addEventListener('submit', sendMessage,true);