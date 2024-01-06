document.addEventListener('DOMContentLoaded', function () {
    var username = document.getElementById('loggedInUserId').value;
    var receiver = document.getElementById('userId').value;
//화면 로드 후 포커스
document.querySelector('#chat-outgoing-msg').focus();

//채팅 알림
    let notificationBadge = document.getElementById('notificationBadge');
    let unreadCount = 0;

// 채팅방이름 만들기
    var roomNum = [username, receiver].sort().join('');

// 상대방 아이디 표시
    document.querySelector("#username").innerHTML = receiver;


//     const stompClientChat = new StompJs.Client({
//     brokerURL: 'ws://localhost:9090/gs-guide-websocket'
// });

    //onlinJs.js에서 소켓연결 하고 있으므로 자동 연결 가능.
stompClient.onConnect = (frame) => {
    console.log('Connected chat : ' + frame);
     sendName();

        stompClient.subscribe('/topic/chatEnter', (chatEnter) => {

             const chatMessage = JSON.parse(chatEnter.body);
    console.log('chatMessage', chatMessage);
    console.log('Sender:', chatMessage.sender);
    console.log('Receiver:', chatMessage.receiver);
    console.log('RoomNum:', chatMessage.roomNum);
    }
    );


//             stompClient.subscribe('/topic/greetings', (greeting) => {
//         const status = JSON.parse(greeting.body).content;
//         showGreeting(status);
//         updateStatus(status);
//     });
// };

    // 연결이 성공하면 사용자 정보를 서버로 전송
    // sendName();

    // 사용자 접속 이벤트를 구독
    // stompClient.subscribe('/topic/userConnected', (username) => {
    //     alert(username + '님이 채팅에 참여했습니다.');
    // });
    //
    // // 사용자 퇴장 이벤트를 구독
    // stompClient.subscribe('/topic/userDisconnected', (username) => {
    //     alert(username + '님이 채팅에서 나갔습니다.');
    // });
};



// stompClient.onConnect = (frame) => {
//     console.log('Connected: ' + frame);
//     // 연결이 성공하면 메시지를 보냄
//     sendName();
//     //로그아웃 상황
//     stompClient.subscribe('/topic/userLogout', (username) => {
//         const dataUsername = username.body;
//         // alert('이까지 와야됨' + dataUsername);
//         removeUserRow(dataUsername);
//      // 연결 종료 후 2초 뒤에 다시 연결
//     disconnect();
//     setTimeout(() => {
//         connect();
//     }, 1000);
//     });
//
//              // connect();
//     //로그인 상황
    // 웹 소켓을 통해 '/topic/greetings' 토픽을 구독
//     stompClient.subscribe('/topic/greetings', (greeting) => {
//         const status = JSON.parse(greeting.body).content;
//         showGreeting(status);
//         updateStatus(status);
//     });
// };










function sendName() {

    const loggedInUserId = $("#loggedInUserId").val();
    // 사용자가 로그인한 경우에만 아이디를 서버로 전송 GreetingController
    if (loggedInUserId !== null && loggedInUserId !== undefined) {
        stompClient.publish({
            destination: "/app/chatEnter",
            body: JSON.stringify({'sender': loggedInUserId,'receiver':receiver, 'roomNum':roomNum})
        });


    }
}


// SSE 연결하기. 객체 생성. 크로스 오리진 자바스크립트 요청은 서버쪽에서 봉쇄하고 있다. -> 서버에서 처리함
  // const eventSource = new EventSource(`https://plikim.com/chat/roomNum/${roomNum}`);
 const eventSource = new EventSource(`http://localhost:9090/chat/roomNum/${roomNum}`);
 // const eventSource = new EventSource(`http://localhost:9090/chat/roomNum/${roomNum}/${receiver}`);

    eventSource.onmessage = (event) => {
        //console.log(1,event);

        //1.데이터를 가져옴
        const data = JSON.parse(event.data);
        // console.log(2,data);

             // 상대방이 보낸 메시지만 처리
            //내가 대화에 참여중인데 '상대방이 채팅을 보내고 있는 경우'
                if (data.sender !== username) {

                    // unreadCount++; // 새로운 메시지 도착 시 카운트 증가
                    // updateNotificationBadge();

                    //2.상대방이 읽었으면 0으로 만듦
                    //  updateRead();
                    //-> 업데이트의 결과물을 data로 받아야한다.read가 1-> 0 된 걸 화면에 뿌려줘야됨
                }

                //3.데이터를 보여줌
        if (data.sender === username) { // 로그인한 유저가 보낸 메시지
            // 파란박스(오른쪽)
            initMyMessage(data);
        } else {
            // 회색박스(왼쪽) 상대방이 보내는 메세지 처리
            initYourMessage(data);
        }


    }
// function updateRead() {
//
//     $.ajax({
//         url: "/chat/updateRead",
//         type: "post",
//         data: {myId: username, roomNum: roomNum},
//         dataType: 'json',
//         success: function (result) {
//             if ($.isEmptyObject(result) || result.userId === null) {
//             }
//
//         },
//         error: function (xhr, status, error) {
//             console.log("목록 불러오기 실패", status, error);
//         }
//
//     });
// }


    function updateNotificationBadge() {
        notificationBadge.innerText = unreadCount;
    }

    function markAsRead() {
        unreadCount = 0;
        updateNotificationBadge();
    }


// 파란박스 만들기. 보내는 대화박스
        function readValueBoxSend(data){

               return `<div class="readValue" style="float: left;margin-left: 0;padding-left: 315px; font-size:10px" >${data.read}</div>`;


}
    function getSendMsgBox(data) {
//new Date()
        let md = data.createdAt.substring(5, 10)
        let tm = data.createdAt.substring(11, 16)
        convertTime = tm + " | " + md

        return `<div class="sent_msg" >
	<p>${data.msg}</p>
	<span class="time_date" style="float:right"> ${convertTime} / <b>${data.sender}</b> </span>
</div>`;
    }

// 회색박스 만들기. 받는 대화박스
    function readValueBoxReceive(data){

               return `<div class="readValue" style="float: left;margin-left: 0;padding-left: 315px; font-size:10px" >${data.read}</div>`;


}

    function getReceiveMsgBox(data) {

        let md = data.createdAt.substring(5, 10)
        let tm = data.createdAt.substring(11, 16)
        convertTime = tm + " | " + md

        return `<div class="received_withd_msg" style="float: left; margin-top: 0px;"">

	<p>${data.msg}</p>
	<span class="time_date"> ${convertTime} / <b>${data.sender}</b> </span>
</div>`;
    }

// 최초 초기화될 때 1번방 3건이 있으면 3건을 다 가져옴
// addMessage() 함수 호출시 DB에 insert 되고, 그 데이터가 자동으로 흘러들어온다(SSE)
// 파란박스 초기화하기
    function initMyMessage(data) {
        let chatBox = document.querySelector("#chat-box");

        let sendBox = document.createElement("div");
        sendBox.className = "outgoing_msg";

    sendBox.innerHTML = `${getSendMsgBox(data)} ${readValueBoxSend(data)} `;
        chatBox.append(sendBox);

        document.documentElement.scrollTop = document.body.scrollHeight;
    }

// 회색박스 초기화하기
    function initYourMessage(data) {
        let chatBox = document.querySelector("#chat-box");


// 받은 메시지를 감싸는 상자 생성
        let receivedBox = document.createElement("div");
        receivedBox.className = "received_msg";

// 이미지를 복제하여 새로운 div 엘리먼트에 추가
        let profileImageElement = document.querySelector(".profile_name img");
        let clonedImageElement = profileImageElement.cloneNode(true);
        let newDivElement = document.createElement("div");
            newDivElement.style.float = "left";
            newDivElement.style.marginRight = "8px";


        let messageBox = document.createElement("div");

        newDivElement.appendChild(clonedImageElement);

// 대화 상자 엘리먼트 생성
        let receiveMsgBoxContent = getReceiveMsgBox(data);

// newDivElement를 messageBox에 추가
        messageBox.appendChild(newDivElement);

// getReceiveMsgBox(data)의 결과를 messageBox에 추가
        messageBox.innerHTML += receiveMsgBoxContent;

        // 새로운 div 엘리먼트와 대화 상자를 받은 상자에 추가
        // receivedBox.appendChild(newDivElement);//이 div안에 이미지 있음
        receivedBox.appendChild(messageBox);//이 div안에 대화내용 있음

        // 채팅 박스에 받은 메시지 상자 추가
        chatBox.appendChild(receivedBox);

        // 스크롤 조정
        document.documentElement.scrollTop = document.body.scrollHeight;
    }

//db에 채팅이 들어감
    async function addMessage() {
        let msgInput = document.querySelector("#chat-outgoing-msg");

        let chat = {
            sender: username,
            receiver: receiver,
            msg: msgInput.value,
            roomNum: roomNum.toString() // roomNum을 문자열로 변환
        };

        try {
           // const response = await fetch("https://plikim.com/chat", {
                const response = await fetch( "http://localhost:9090/chat", {
                method: "post",
                body: JSON.stringify(chat),
                headers: {
                    "Content-Type": "application/json; charset=utf-8"
                }
            });


            if (response.ok) {
                // fetch 요청이 성공하면 추가 작업 수행
                //인풋창 초기화
                msgInput.value = "";
            } else {
                // 요청이 실패한 경우에 대한 처리
                console.error("Fetch 요청 실패");
            }
        } catch (error) {
            // 오류 처리
            console.error("오류 발생: " + error);
            //새로고침
            location.reload();
        }
    }


// 버튼 클릭시 메시지 전송
    document.querySelector("#chat-outgoing-button").addEventListener("click", () => {
        //alert('hi');
        addMessage();
    });

// 엔터를 치면 메시지 전송
    document.querySelector("#chat-outgoing-msg").addEventListener("keydown", (e) => {
        if (e.keyCode === 13) {
            addMessage();
        }
    });

});



