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

    window.onload = function () {
        // 페이지가 완전히 로드되면 실행될 코드 작성
        userEnter();
    };
// SSE 연결하기. 객체 생성. 크로스 오리진 자바스크립트 요청은 서버쪽에서 봉쇄하고 있다. -> 서버에서 처리함
  //  const eventSource = new EventSource(`https://plikim.com/chat/roomNum/${roomNum}`);
     const eventSource = new EventSource(`http://localhost:9090/chat/roomNum/${roomNum}`);


    eventSource.onmessage = (event) => {
        //console.log(1,event);

        //1.데이터를 가져옴
        const data = JSON.parse(event.data);

        if (data.sender !== username) {

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
    function readValueBoxSend(data) {

return `<div class="readValue" style="float: left; margin-left: 0; padding-left: 555px; font-weight: bold; font-size: 10px;">${data.read}</div>`;


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


    function getReceiveMsgBox(data) {

        let md = data.createdAt.substring(5, 10)
        let tm = data.createdAt.substring(11, 16)
        convertTime = tm + " | " + md

        return `<div class="received_withd_msg" style="float: left; margin-top: 0;"">

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

//db에 채팅내용이 들어감
    async function addMessage() {
        let msgInput = document.querySelector("#chat-outgoing-msg");

        //chat객체에 담아서 서버로 전달
        let chat = {
            sender: username,
            receiver: receiver,
            msg: msgInput.value,
            roomNum: roomNum.toString() // roomNum을 문자열로 변환
        };

        userEnter(function (checkResult) {
            //상대방이 채팅방에 있는 경우
            if (checkResult === true) {
                // readValue

                // alert('코드가 인증되었습니다.')
                // //휴대폰 번호 업데이트
                // updatePhoneNumber();

            } else {
                // alert('코드가 맞지 않습니다. ');
            }
        });

        try {
           // const response = await fetch("https://plikim.com/chat", {
                 const response = await fetch("http://localhost:9090/chat", {
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
                // console.error("Fetch 요청 실패");
            }
        } catch (error) {
            // 오류 처리
            // console.error("오류 발생: " + error);
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

    //화면을 나가기 전에 메서드 동작
    // $(window).on("beforeunload", function(e) {
    //         leaveChatRoom();
    // });
    //   window.addEventListener("pagehide", function(e) {
    //        leaveChatRoom();
    // });


// window.addEventListener('pagehide', function (e) {
//           alert('hi')
//     });

    function userEnter() {
        $.ajax({
            type: "post",
            url: "/chat/userEnter",
            contentType: "application/json",
            data: JSON.stringify({
                receiver: receiver,
                sender: username,
                roomNum: roomNum
            }),
            dataType: 'json',
            success: function (chatEnterRecord) {
                if (chatEnterRecord.state) {
                    // document.querySelector('.readValue').textContent = '';

                }
                // console.log('입장기록1')
                // console.log(response.roomNum)

                // console.log('입장기록start');
                //         console.log('chatEnterRecord:', chatEnterRecord);
                //         console.log('RoomNum:', chatEnterRecord.roomNum);
                //   console.log('입장기록end');

            },
            error: function (xhr, status, error) {
                // console.log("에러 발생", status, error);
            }
        });

    }


    function userLeave() {
        $.ajax({
            type: "post",
            url: "/chat/userLeave",
            contentType: "application/json",
            data: JSON.stringify({
                receiver: receiver,
                sender: username,
                roomNum: roomNum
            }),
            dataType: 'json',
            success: function (chatEnterRecord) {
                if (chatEnterRecord.state) {
                    // callback(true)
                }
                // console.log('퇴장기록1')
                // console.log(response.roomNum)
                //
                // console.log('퇴장기록start');
                //         console.log('chatEnterRecord:', chatEnterRecord);
                //         console.log('RoomNum:', chatEnterRecord.roomNum);
                //   console.log('퇴장기록end');

            },
            error: function (xhr, status, error) {
                // console.log("에러 발생", status, error);
            }
        });

    }

    window.addEventListener('beforeunload', function (e) {
        userLeave();
    });

});

