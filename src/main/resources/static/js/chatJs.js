
  // 로그인 시스템 대신 임시 방편
let username = prompt("아이디를 입력하세요");
//let roomNum = prompt("채팅방 번호를 입력하세요");
  let roomNumString = prompt("채팅방 번호를 입력하세요");
 roomNum = parseInt(roomNumString);

document.querySelector("#username").innerHTML = username;

// SSE 연결하기. 객체 생성. 크로스 오리진 자바스크립트 요청은 서버쪽에서 봉쇄하고 있다. -> 서버에서 처리함
//const eventSource = new EventSource(`http://localhost:9090/chat/roomNum/${roomNum}`);
//const eventSource = new EventSource(`https://plikim.com/chat/roomNum/${roomNum}`);
//const eventSource = new EventSource(`http://localhost:9090/sender/ssar/receiver/cos`);
const eventSource = new EventSource(`https://plikim.com/chat/roomNum/${roomNum}`);


eventSource.onmessage = (event) => {
	//console.log(1,event);
	const data = JSON.parse(event.data);
		//console.log(2,data);
	if (data.sender === username) { // 로그인한 유저가 보낸 메시지
		// 파란박스(오른쪽)
		initMyMessage(data);
	} else {
		// 회색박스(왼쪽)
		initYourMessage(data);
	}
}

// 파란박스 만들기. 보내는 대화박스
function getSendMsgBox(data) {
//new Date()
	let md = data.createdAt.substring(5, 10)
	let tm = data.createdAt.substring(11, 16)
	convertTime = tm + " | " + md

	return `<div class="sent_msg">
	<p>${data.msg}</p>
	<span class="time_date"> ${convertTime} / <b>${data.sender}</b> </span>
</div>`;
}

// 회색박스 만들기
function getReceiveMsgBox(data) {

	let md = data.createdAt.substring(5, 10)
	let tm = data.createdAt.substring(11, 16)
	convertTime = tm + " | " + md

	return `<div class="received_withd_msg">
	<p>${data.msg}</p>
	<span class="time_date"> ${convertTime} / <b>${data.sender}</b> </span>
</div>`;
}

// 최초 초기화될 때 1번방 3건이 있으면 3건을 다 가져와요
// addMessage() 함수 호출시 DB에 insert 되고, 그 데이터가 자동으로 흘러들어온다(SSE)
// 파란박스 초기화하기
function initMyMessage(data) {
	let chatBox = document.querySelector("#chat-box");

	let sendBox = document.createElement("div");
	sendBox.className = "outgoing_msg";

	sendBox.innerHTML = getSendMsgBox(data);
	chatBox.append(sendBox);

	document.documentElement.scrollTop = document.body.scrollHeight;
}

// 회색박스 초기화하기
function initYourMessage(data) {
	let chatBox = document.querySelector("#chat-box");

	let receivedBox = document.createElement("div");
	receivedBox.className = "received_msg";

	receivedBox.innerHTML = getReceiveMsgBox(data);
	chatBox.append(receivedBox);

	document.documentElement.scrollTop = document.body.scrollHeight;
}


async function addMessage() {
    let msgInput = document.querySelector("#chat-outgoing-msg");
    let chat = {
        sender: username,
        roomNum: roomNum,
        msg: msgInput.value
    };

    try {//"http://localhost:9090/chat" "https://plikim.com/chat"
        const response = await fetch( "https://plikim.com/chat", {
            method: "post",
            body: JSON.stringify(chat),
            headers: {
                "Content-Type": "application/json; charset=utf-8"
            }
        });

        if (response.ok) {
            // fetch 요청이 성공하면 추가 작업 수행
            msgInput.value = "";
        } else {
            // 요청이 실패한 경우에 대한 처리
            console.error("Fetch 요청 실패");
        }
    } catch (error) {
        // 오류 처리
        console.error("오류 발생: " + error);
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