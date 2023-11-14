var sender = document.getElementById('loggedInUserId').value;
var receiver = document.getElementById('userId').value;

//const sortedUsers = [sender, receiver].sort();//777, 666



const chatRoomURI = `https://plikim.com/chat/sender/${sender}/receiver/${receiver}`;
const chatRoomURI1 = `https://plikim.com/chat/sender/${receiver}/receiver/${sender}`;
const eventSource = new EventSource(chatRoomURI);
const eventSource1 = new EventSource(chatRoomURI1);

eventSource.onmessage = (event) => {
	//console.log(1,event);
	const data = JSON.parse(event.data);
		//console.log(2,data);
if (data.sender === sender) { // 로그인한 유저가 보낸 메시지
    // 파란박스(오른쪽)
    initMyMessage(data);
}
}

eventSource1.onmessage = (event) => {
	//console.log(1,event);
	const data = JSON.parse(event.data);
		//console.log(2,data);
if (data.receiver === receiver) { // 로그인한 유저가 보낸 메시지
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

// 최초 초기화될 때 1번방 3건이 있으면 3건을 다 가져옴
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
	console.log("test")
    let msgInput = document.querySelector("#chat-outgoing-msg");
    let chat = {
        sender: sender,
        // roomNum: roomNum,
		receiver: receiver,
        msg: msgInput.value
    };

  try {

const chatRoomURI = "https://plikim.com/chat/sender/${sender}/receiver/${receiver}"; //666 777
    const response = await fetch(chatRoomURI, { // chatRoomURI 변수를 사용하도록 수정
        method: "POST", // POST 메소드로 수정
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