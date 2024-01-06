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

    // 채팅방에 들어갔을 때 서버에 통지
$.ajax({
    url: "/chat/roomEntered",
    type: "post",
    contentType: "application/json",  // Content-Type을 명시
    data: JSON.stringify({ userId: username, roomNum: roomNum, receiver:receiver }),
    dataType: 'json',
    success: function (result) {
            alert('방 입장? ')
        if ($.isEmptyObject(result) || result.userId === null) {
            // alert('방 입장? ')
            // 성공적으로 업데이트된 경우
        }
    },
    error: function (xhr, status, error) {
        console.log("채팅방 상태 업데이트 실패", status, error);
    }
});

const stompClient = new StompJs.Client({
    brokerURL: 'wss://plikim.com/gs-guide-websocket'
    //brokerURL: 'ws://localhost:9090/gs-guide-websocket'
});
// 클라이언트 측 JavaScript 코드


// 사용자 이름(userId)을 사용하여 개별 주제에 구독
const userTopic = `/user/${userId}/topic/notification`;

stompClient.onmessage = function (event) {
    const roomStatus = JSON.parse(event.data);

    // 상대방이 채팅방에 들어왔을 때의 동작
    if (roomStatus.userId === receiver && roomStatus.roomNum === roomNum) {
        // 여기에 상대방이 채팅방에 들어왔을 때 실행되어야 할 동작 추가
        // 예를 들어, 알림 표시, 화면 갱신 등의 동작을 추가할 수 있습니다.
        showNotification("상대방이 채팅방에 들어왔습니다.");
    }
};

// 사용자가 구독하고 있는 주제에 대해 구독
stompClient.subscribe(userTopic, function (message) {
    // 메시지를 받아서 처리하는 코드
    console.log("Received message on topic:", userTopic, "Message:", message);
});

function showNotification(message) {
    // 브라우저 Notification API를 사용하여 알림을 표시하는 코드
    if (Notification.permission === "granted") {
        new Notification(message);
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(permission => {
            if (permission === "granted") {
                new Notification(message);
            }
        });
    }
}

// const roomStatusSubscription = stompClient.subscribe('/topic/roomStatus', function (message) {
//     const roomStatus = JSON.parse(message.body);
//
//     // 상대방이 채팅방에 들어왔을 때 실행되어야 하는 코드를 작성합니다.
//     if (roomStatus.userId === receiver && roomStatus.roomNum === roomNum) {
//         // 여기에 상대방이 채팅방에 들어왔을 때 실행되어야 하는 코드를 추가합니다.
//         showNotification("상대방이 채팅방에 들어왔습니다.");
//     }
// });

// const socket = new WebSocket("ws://localhost:9090/gs-guide-websocket");

stompClient.onmessage = function (event) {
    const roomStatus = JSON.parse(event.data);

    // 상대방이 채팅방에 들어왔을 때의 동작
    if (roomStatus.userId === receiver && roomStatus.roomNum === roomNum) {
        // 여기에 상대방이 채팅방에 들어왔을 때 실행되어야 할 동작 추가
        // 예를 들어, 알림 표시, 화면 갱신 등의 동작을 추가할 수 있습니다.
        showNotification("상대방이 채팅방에 들어왔습니다.");
    }
};






stompClient.onmessage = function (event) {
    const roomStatus = JSON.parse(event.data);

    // 상대방이 채팅방에 들어왔을 때의 동작
    if (roomStatus.userId === receiver && roomStatus.roomNum === roomNum) {
        // 여기에 상대방이 채팅방에 들어왔을 때 실행되어야 할 동작 추가
        // 예를 들어, 알림 표시, 화면 갱신 등의 동작을 추가할 수 있습니다.
        showNotification("상대방이 채팅방에 들어왔습니다.");
    }
};

function showNotification(message) {
    // 브라우저 Notification API를 사용하여 알림을 표시하는 코드
    if (Notification.permission === "granted") {
        new Notification(message);
    } else if (Notification.permission !== "denied") {
        Notification.requestPermission().then(permission => {
            if (permission === "granted") {
                new Notification(message);
            }
        });
    }
}


function showGreeting(message) {
    const data = JSON.parse(message.trim());

    // 중복을 방지하기 위해 이미 존재하는 사용자인지 확인
    if ($("#greetings").find("td:contains('" + data.userId + "')").length === 0) {
        // 존재하지 않으면 새로운 행 추가
        const newRow = $("<tr><td>" + data.userId + " 입장했습니다. <span style='color: green;'>●</span></td></tr>");
        $("#greetings").append(newRow);

        // 여기에서 갱신된 채팅방 상태에 따른 화면 업데이트를 수행할 수 있습니다.
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
                    // updateRead();
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
