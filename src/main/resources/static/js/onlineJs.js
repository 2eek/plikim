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

// 사용자 행 제거 함수
function removeUserRow(username) {
    const cleanedUsername = username.trim().toLowerCase();
    const existingRow = $("#greetings").find("td").filter(function () {
        return $(this).text().trim().toLowerCase() === cleanedUsername;
    }).closest("tr");
    existingRow.remove();
}

// 동적으로 생성된 요소에 대한 이벤트 리스너 등록
$(document).on('click', '[id^="user-row-"]', function () {
    var userId = this.id.replace('user-row-', '');
    window.location.href = '/user/userdetail?id=' + userId;
});

function updateStatus(username, status) {
    // 사용자 이름(username)을 기반으로 해당하는 span 요소를 찾아 상태를 변경
    const statusElement = $("#status-" + username);
    statusElement.text(status).css("color", "green");

    // 사용자의 아이디를 읽어오기 위해 data-username을 사용
    const dataUsername = statusElement.attr('data-username');
    console.log('User ID:', dataUsername);
}

// 예시: 세션 업데이트 함수
function updateLoginStatus(username, status) {
    console.log('Logout Event Received:', username);
    removeUserRow(username); // 로그아웃 시 해당 사용자 행 제거
}

const stompClient = new StompJs.Client({
    brokerURL: 'wss://plikim.com/gs-guide-websocket'
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
        console.log('로그인한 회원:'+status);
    });
    // WebSocket을 통한 세션 종료 이벤트 수신
    stompClient.subscribe('/topic/session-disconnect', (username) => {
        // 세션 종료 이벤트가 발생하면 index 페이지의 로그인 상태를 로그아웃으로 업데이트
        updateLoginStatus(username, '로그아웃');
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
    setInterval(function () {
        sendName();
    }, 10000); // 5000 milliseconds = 5 seconds
});
