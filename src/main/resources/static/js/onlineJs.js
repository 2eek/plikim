//
//
//     const stompClient = new StompJs.Client({
//     brokerURL: 'ws://localhost:9090/gs-guide-websocket' //'wss://plikim.com/gs-guide-websocket'
// });
//
//     stompClient.onConnect = (frame) => {
//         setConnected(true);
//         console.log('Connected: ' + frame);
//         stompClient.subscribe('/topic/greetings', (greeting) => {
//             const status = JSON.parse(greeting.body).content;
//             showGreeting(status);
//             updateStatus(status);
//             console.log(status);
//         });
//     };
//
//
//
//     stompClient.onWebSocketError = (error) => {
//         console.error('Error with websocket', error);
//     };
//
//     stompClient.onStompError = (frame) => {
//         console.error('Broker reported error:', frame);
//         console.error('Broker reported error: ' + frame.headers['message']);
//         console.error('Additional details: ' + frame.body);
//     };
//
//     function setConnected(connected) {
//         $("#connect").prop("disabled", connected);
//         $("#disconnect").prop("disabled", !connected);
//         if (connected) {
//             $("#conversation").show();
//         } else {
//             $("#conversation").hide();
//         }
//         $("#greetings").html("");
//     }
//
//     function connect() {
//         stompClient.activate();
//     }
//
//     function disconnect() {
//         stompClient.deactivate();
//         setConnected(false);
//         console.log("Disconnected");
//     }
//
//     function sendName() {
//         stompClient.publish({
//             destination: "/app/hello",
//             body: JSON.stringify({'name': $("#loggedInUserId").val()})
//         });
//     }
//
function showGreeting(message) {
    const dataUsername = message.trim(); // 수신된 메시지의 공백 제거

    // 중복을 방지하기 위해 이미 존재하는 사용자인지 확인
    if ($("#greetings").find("td:contains('" + dataUsername + "')").length === 0) {
        // 존재하지 않으면 새로운 행 추가
        const newRow = $("<tr><td>" + dataUsername + " <span style='color: green;'>●</span></td></tr>");
        $("#greetings").append(newRow);

        // 여기서 JSON.parse(greeting.body).content 값이 사용자 아이디와 일치하면
        // 해당하는 span 요소의 내용을 '온라인'으로 변경
        updateStatus(dataUsername, '온라인');
    }
}
//
function updateStatus(username, status) {
    // 사용자 이름(username)을 기반으로 해당하는 span 요소를 찾아 상태를 변경
    const statusElement = $("#status-" + username);
    statusElement.text(status).css("color", "green");

    // 사용자의 아이디를 읽어오기 위해 data-username을 사용
    const dataUsername = statusElement.attr('data-username');
    console.log('User ID:', dataUsername);
}

// function updateStatus(username, status) {
//     // 사용자 아이디와 일치하는 행을 찾아 상태 업데이트
//     $("#user-table td:contains('" + username + "')").next().next().next().text(status);
// }


//
//
//     $(function () {
//         $("form").on('submit', (e) => e.preventDefault());
//         $("#connect").click(() => connect());
//         $("#disconnect").click(() => disconnect());
//         $("#send").click(() => sendName());
//     });
const stompClient = new StompJs.Client({
    brokerURL: 'wss://plikim.com/gs-guide-websocket' //'wss://plikim.com/gs-guide-websocket'   'ws://localhost:9090/gs-guide-websocket'
});

stompClient.onConnect = (frame) => {
    // setConnected(true);
    console.log('Connected: ' + frame);

    // 연결이 성공하면 메시지를 보냄
    sendName();

    stompClient.subscribe('/topic/greetings', (greeting) => {
        const status = JSON.parse(greeting.body).content;
        showGreeting(status);
        updateStatus(status);
        console.log(status);
    });
};

stompClient.onWebSocketError = (error) => {
    console.error('Error with websocket', error);
};

stompClient.onStompError = (frame) => {
    console.error('Broker reported error:', frame);
    console.error('Broker reported error: ' + frame.headers['message']);
    console.error('Additional details: ' + frame.body);
};

function connect() {
    stompClient.activate();
}

function sendName() {
    stompClient.publish({
        destination: "/app/hello",
        body: JSON.stringify({'name': $("#loggedInUserId").val()})
    });
}

$(function () {
    connect();
});

