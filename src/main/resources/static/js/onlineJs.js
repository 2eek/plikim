//롤백함수
//1.로그인상황 테이블에 이름 넣는 메서드 + 회원 테이블에 온라인 여부 표시 동작
function showGreeting(message) {
    const dataUsername = message.trim(); // 웹소켓을 통해 수신된 메시지의 공백 제거
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

//회원의 디테일 정보 조회+ 채팅  동적으로 생성된 요소에 대한 이벤트 리스너 등록
// 테이블 내의 tr 클릭 이벤트 리스너
$(document).on('click', 'tr.user-row', function () {
    // 세션 값 확인
    var loggedInUserId = $('#loggedInUserId').val();
    if (!loggedInUserId) {
        // 세션 값이 없는 경우 로그인 페이지로 리다이렉트
        alert('로그인 후 이용 가능')
        // window.location.href = '/user/loginForm';
    } else {
        // 세션 값이 있는 경우 사용자 상세 페이지로 이동
        var id = $(this).attr('id').replace('user-row-', ''); // 유저 아이디 추출
        window.location.href = '/user/userdetail?id=' + id;
    }
});

$(document).on('click', 'a.user-link', function (e) {
    // 세션 값 확인
    var loggedInUserId = $('#loggedInUserId').val();
    if (!loggedInUserId) {
        e.preventDefault(); // 기본 동작 중단
    }
});

const stompClient = new StompJs.Client({
    //  brokerURL: 'wss://plikim.com/gs-guide-websocket'
    brokerURL: 'ws://localhost:9090/gs-guide-websocket'
});

// 웹 소켓을 통해 '/topic/greetings' 토픽을 구독
stompClient.onConnect = (frame) => {
    console.log('Connected: ' + frame);
    // 연결이 성공하면 메시지를 보냄
    sendName();
    //로그아웃 상황
    stompClient.subscribe('/topic/userLogout', (username) => {
        const dataUsername = username.body;
        // alert('이까지 와야됨' + dataUsername);
        removeUserRow(dataUsername);
     // 연결 종료 후 2초 뒤에 다시 연결
    disconnect();
    setTimeout(() => {
        connect();
    }, 1000);
    });

             // connect();
    //로그인 상황
    // 웹 소켓을 통해 '/topic/greetings' 토픽을 구독
    stompClient.subscribe('/topic/greetings', (greeting) => {
        const status = JSON.parse(greeting.body).content;
        showGreeting(status);
        updateStatus(status);
    });
};
function disconnect() {
    stompClient.deactivate();
}


function updateStatus(username, status) {
    // 사용자 이름(username)을 기반으로 해당하는 span 요소를 찾아 상태를 변경
    const statusElement = $(".status-" + username);
    // const statusElement = $("#status-" + username);
    statusElement.text(status).css("color", "green");
    // 사용자의 아이디를 읽어오기 위해 data-username을 사용
    // const dataUsername = statusElement.attr('data-username');
}
function deleteStatus(username, status) {
    // 사용자 이름(username)을 기반으로 해당하는 span 요소를 찾아 상태를 변경
    const statusElement = $(".status-" + username);
    // const statusElement = $("#status-" + username);
    statusElement.text(status).css("color", "#808080");
}
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
    const loggedInUserId = $("#loggedInUserId").val();
    // 사용자가 로그인한 경우에만 아이디를 서버로 전송 GreetingController
    if (loggedInUserId !== null && loggedInUserId !== undefined) {
        stompClient.publish({
            //서버로 보냄
            destination: "/app/hello",
            body: JSON.stringify({'name': loggedInUserId})
        });
    }
}

$(function () {
    connect();
    setInterval(function () {
        sendName();
    }, 1000); // 10000 milliseconds = 10 seconds
});

//로그아웃 상황
function removeUserRow(message) {
    const dataUsername = message.trim(); //// 웹소켓을 통해 수신된 메시지의 공백 제거
        deleteStatus(dataUsername, '오프라인');
     const existingRow = $("#greetings").find("td:contains('" +dataUsername+ "')").closest('tr');
     existingRow.remove();
}
