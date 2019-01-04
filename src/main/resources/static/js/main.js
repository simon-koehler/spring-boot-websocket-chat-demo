'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#chat-page');
var usernameForm = document.querySelector('#usernameForm');
var messageForm = document.querySelector('#messageForm');
var messageInput = document.querySelector('#message');
var messageArea = document.querySelector('#messageArea');
var connectingElement = document.querySelector('.connecting');

var stompClient = null;
var username = null;
var key = null;

var colors = [
    '#2196F3', '#32c787', '#00BCD4', '#ff5652',
    '#ffc107', '#ff85af', '#FF9800', '#39bbb0'
];

function connect(event) {
    username = document.querySelector('#name').value.trim();
    key = document.querySelector('#key').value.trim();

    if (username && key) {
        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
}


function onConnected() {
    // Subscribe to the Public Topic
    stompClient.subscribe('/topic/room-' + key, onPayloadReceived);

    // Tell your username to the server
    stompClient.send("/app/chat.addUser-" + key,
        {},
        JSON.stringify({sender: username, type: 'JOIN', key: key})
    );

    connectingElement.classList.add('hidden');
}


function onError() {
    connectingElement.textContent = 'Could not connect to WebSocket server. Please refresh this page to try again!';
    connectingElement.style.color = 'red';
}


function sendMessage(event) {
    var messageContent = messageInput.value.trim();

    if (messageContent && stompClient) {
        var chatMessage = {
            sender: username,
            content: messageInput.value,
            type: 'CHAT',
            key: key
        };

        stompClient.send("/app/chat.sendMessage-" + key, {}, JSON.stringify(chatMessage));
        messageInput.value = '';
    }
    event.preventDefault();
}


function onPayloadReceived(payload) {
    var message = JSON.parse(payload.body);
    onMessageReceived(message);
}

function onMessageReceived(message) {
    console.log("new message: ", message);

    var messageElement = document.createElement('li');

    var dateElement = document.createElement('p');
    dateElement.className = "message-date";
    var dateText = document.createTextNode(formatDate(new Date(message.timestamp)));
    dateElement.appendChild(dateText);
    messageElement.appendChild(dateElement);

    if (message.type === 'JOIN') {
        messageElement.classList.add('event-message');
        message.content = message.sender + ' joined!';
        if (message.recentMessages && message.sender === username)
            for (var i = 0; i < message.recentMessages.length; i++) {
                console.log('length: ', message.recentMessages.length, 'call for ', message.recentMessages[i]);
                onMessageReceived(message.recentMessages[i]);
            }
        else
            console.log(message.recentMessages, message.sender === username);
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


function getAvatarColor(messageSender) {
    var hash = 0;
    for (var i = 0; i < messageSender.length; i++) {
        hash = 31 * hash + messageSender.charCodeAt(i);
    }

    var index = Math.abs(hash % colors.length);
    return colors[index];
}


/**
 * From: https://gist.github.com/hurjas/2660489
 * Return a timestamp with the format "dd.mm.yy hh:MM:ss"
 * @type {Date}
 */
function formatDate(input) {
    // Create an array with the current month, day and time
    var date = [input.getDate(), input.getMonth() + 1, input.getFullYear()];

    // Create an array with the current hour, minute and second
    var time = [input.getHours(), input.getMinutes(), input.getSeconds()];

    // If day and month are less than 10, add a zero
    for (var i = 0; i < 2; i++) {
        if (date[i] < 10) {
            date[i] = "0" + date[i];
        }
    }

    // If seconds and minutes are less than 10, add a zero
    for (i = 1; i < 3; i++) {
        if (time[i] < 10) {
            time[i] = "0" + time[i];
        }
    }

    // Return the formatted string
    return date.join(".") + " " + time.join(":");
}

usernameForm.addEventListener('submit', connect, true);
messageForm.addEventListener('submit', sendMessage, true);
