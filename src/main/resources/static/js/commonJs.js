// const stompClientRead = new StompJs.Client({
//      brokerURL: 'wss://plikim.com/gs-guide-websocket'
//     //brokerURL: 'ws://localhost:9090/gs-guide-websocket'
// });
// stompClientRead.onConnect = (frame) => {
//      console.log('Connected: ' + frame);
//     // 연결이 성공하면 메시지를 보냄
//     sendName();
//
//     stompClientRead.subscribe('/topic/totalRead', (username) => {
//         const dataUsername = username.body;
//         // alert('이까지 와야됨' + dataUsername);
//         removeUserRow(dataUsername);
//      // 연결 종료 후 2초 뒤에 다시 연결
//     disconnect();
//     setTimeout(() => {
//         connect();
//     }, 1000);
//     });
// };
// function disconnect() {
//     stompClient.deactivate();
// }