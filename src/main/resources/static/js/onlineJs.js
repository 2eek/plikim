// function showGreeting(message) {
//     const dataUsername = message.trim(); // 웹소켓을 통해 수신된 메시지의 공백 제거
//
//     // 중복을 방지하기 위해 이미 존재하는 사용자인지 확인
//     if ($("#greetings").find("td:contains('" + dataUsername + "')").length === 0) {
//         // 존재하지 않으면 새로운 행 추가
//         const newRow = $("<tr><td>" + dataUsername + " <span style='color: green;'>●</span></td></tr>");
//         $("#greetings").append(newRow);
//
//         // 여기서 JSON.parse(greeting.body).content 값이 사용자 아이디와 일치하면
//         // 해당하는 span 요소의 내용을 '온라인'으로 변경
//         updateStatus(dataUsername, '온라인');
//     }
// }
//
// // 사용자 행 제거 함수
// function removeUserRow(username) {
//     const cleanedUsername = username.trim().toLowerCase();
//     const existingRow = $("#greetings").find("td").filter(function () {
//         return $(this).text().trim().toLowerCase() === cleanedUsername;
//     }).closest("tr");
//     existingRow.remove();
// }
//
// //회원의 디테일 정보 조회+ 채팅  동적으로 생성된 요소에 대한 이벤트 리스너 등록
// // 테이블 내의 tr 클릭 이벤트 리스너
// $(document).on('click', 'tr.user-row', function () {
//     // 세션 값 확인
//     var loggedInUserId = $('#loggedInUserId').val();
//     if (!loggedInUserId) {
//         // 세션 값이 없는 경우 로그인 페이지로 리다이렉트
//         window.location.href = '/member/loginForm';
//     } else {
//         // 세션 값이 있는 경우 사용자 상세 페이지로 이동
//         var id = $(this).attr('id').replace('user-row-', ''); // 유저 아이디 추출
//         window.location.href = '/user/userdetail?id=' + id;
//     }
// });
//
//  // 사용자 아이디 클릭 시 로그인 페이지로 리다이렉트 (세션 값이 없는 경우)
//     $(document).on('click', 'a.user-link', function (e) {
//         // 세션 값 확인
//         var loggedInUserId = $('#loggedInUserId').val();
//
//         // 세션 값이 없는 경우 로그인 페이지로 리다이렉트
//         if (!loggedInUserId) {
//             e.preventDefault(); // 기본 동작 중단
//             window.location.href = '/member/loginForm';
//         }
//     });
//
//
// function updateStatus(username, status) {
//     // 사용자 이름(username)을 기반으로 해당하는 span 요소를 찾아 상태를 변경
//     const statusElement = $("#status-" + username);
//     statusElement.text(status).css("color", "green");
//
//     // 사용자의 아이디를 읽어오기 위해 data-username을 사용
//     const dataUsername = statusElement.attr('data-username');
//     console.log('User ID !!:', dataUsername);
// }
//
// // 예시: 세션 업데이트 함수
// function updateLoginStatus(username, status) {
//     console.log('Logout Event Received:', username);
//     removeUserRow(username); // 로그아웃 시 해당 사용자 행 제거
// }
//
// const stompClient = new StompJs.Client({
//    brokerURL: 'wss://plikim.com/gs-guide-websocket'
//     //brokerURL: 'ws://localhost:9090/gs-guide-websocket'
// });
//
// stompClient.onConnect = (frame) => {
//     // setConnected(true);
//     console.log('Connected: ' + frame);
//
//     // 연결이 성공하면 메시지를 보냄
//     sendName();
//
//     //웹 소켓을 통해 '/topic/greetings' 토픽을 구독하고, 해당 토픽으로부터 메시지를 받았을 때 실행되는 콜백 함수
//     stompClient.subscribe('/topic/greetings', (greeting) => {
//         const status = JSON.parse(greeting.body).content;
//         showGreeting(status);
//         updateStatus(status);
//         console.log('로그인한 회원:'+status);
//     });
//
//
//     // WebSocket을 통한 세션 종료 이벤트 수신
//     stompClient.subscribe('/topic/session-disconnect', (username) => {
//         // 세션 종료 이벤트가 발생하면 index 페이지의 로그인 상태를 로그아웃으로 업데이트
//         updateLoginStatus(username, '로그아웃');
//     });
//
// };
//
// stompClient.onWebSocketError = (error) => {
//     console.error('Error with websocket', error);
// };
//
// stompClient.onStompError = (frame) => {
//     console.error('Broker reported error:', frame);
//     console.error('Broker reported error: ' + frame.headers['message']);
//     console.error('Additional details: ' + frame.body);
// };
//
// function connect() {
//     stompClient.activate();
// }
//
// function sendName() {
//     const loggedInUserId = $("#loggedInUserId").val();
//
//     // 사용자가 로그인한 경우에만 아이디를 서버로 전송
//     if (loggedInUserId !== null && loggedInUserId !== undefined) {
//         stompClient.publish({
//             destination: "/app/hello",
//             body: JSON.stringify({'name': loggedInUserId})
//         });
//     }
// }
//
// $(function () {
//     connect();
//     setInterval(function () {
//         sendName();
//     }, 10000); // 10000 milliseconds = 10 seconds
// });
