
// 1)댓글 목록을 불러오는 함수
const loadComments = (page = 0, size = 10) => {
    //1-1-1) 게시글 ID를 가져옴
    const boardId = document.getElementById("boardId").value;

    //1-1-2) 페이지와 사이즈가 정의되지 않은 경우 기본값으로 설정
    if (page === undefined) {
        page = 0;
    }
    if (size === undefined) {
        size = 10;
    }

    //1-2) 서버에 댓글 목록 요청
    $.ajax({
        type: "get",
        url: `/comment/list/${boardId}?page=${page}&size=${size}`,
        success: function (commentsPage) {

            //1-2-1) 댓글 목록을 화면에 표시 commentList ->이걸 댓글리스트의 컨테이너로 쓴다
            const commentListElement = document.getElementById("commentList");
            commentListElement.innerHTML = "";

            commentsPage.content.forEach(function (comment) {
                  const commentElement = document.createElement("div");
                commentElement.className = "comment"; // 클래스 추가
                commentElement.id = "comment-id-" + `${comment.id}`;
                commentElement.innerHTML = `
                    <div style="width: 80%; margin: 0 auto;">
                        <div style="float: left; width: 20%;">${comment.commentWriter}</div>
                        <div style="float: left; width: 40%; margin-left: 5%;">${comment.commentContents}</div>
                        <div style="float: right; width: 20%;">${comment.commentCreatedTime}</div>
                        <div class="commentId" style="display: none;">${comment.id}</div>
                         <button class="btn btn-primary open-reply-form-button" data-comment-id="${comment.id}" style="float: right;">답글</button>
                        <div style="clear: both;"></div>
                        <hr style="width: 100%;">
                    </div>
                `;
                commentListElement.appendChild(commentElement);
            });

            //1-2-2) 댓글 목록 하단에 페이징 버튼 생성
            createPaginationButtons(commentsPage);
        },
        error: function (xhr, status, error) {
            console.log("댓글 목록 불러오기 실패", status, error);
        }
    });
};



//2) 페이징 버튼을 생성하는 함수
const createPaginationButtons = (commentsPage) => {
    const paginationElement = document.getElementById("pagination");

    //2-1) 페이징 컨테이너 초기화
    if (!paginationElement) {
        console.error("paginationElement가 존재하지 않음.");
        return;
    }
    paginationElement.innerHTML = "";

    //2-2) 페이징 컨테이너 및 버튼 생성
    const paginationContainer = document.createElement("ul");
    paginationContainer.classList.add("pagination", "justify-content-center");

    //2-3) 이전 버튼
    const prevButton = createPaginationButton("이전", !commentsPage.first, commentsPage.number - 1, commentsPage);
    paginationContainer.appendChild(prevButton);

    //2-4) 페이지 번호 버튼 생성
    for (var i = 0; i < commentsPage.totalPages; i++) {
        const pageButton = createPaginationButton(i + 1, i != commentsPage.number, i, commentsPage);
        paginationContainer.appendChild(pageButton);
    }

    //2-5) 다음 버튼
    const nextButton = createPaginationButton("다음", !commentsPage.last, commentsPage.number + 1, commentsPage);
    paginationContainer.appendChild(nextButton);

    //2-6) 페이징 컨테이너를 실제 페이지에 추가
    paginationElement.appendChild(paginationContainer);
};



//3) 페이지 번호 버튼 스타일 생성하는 함수
const createPaginationButton = (text, enabled, pageIndex, commentsPage) => {
    const button = document.createElement("li");
    button.classList.add("page-item");

    //3-1) 비활성화된 경우 'disabled' 클래스 추가
    if (!enabled) {
        button.classList.add("disabled");
    }

    const link = document.createElement("a");
    link.classList.add("page-link", "text-dark");

    //3-2) 비활성화된 경우 'text-primary' 클래스 추가
    if (!enabled) {
        link.classList.add("text-primary");
    }

    link.innerText = text;


    //3-3) 페이지 번호 버튼에 클릭 이벤트 추가
    link.addEventListener("click", () => {
        if (enabled) {
            // 클릭된 경우 해당 페이지의 댓글 목록을 다시 불러옴
            loadComments(pageIndex, commentsPage.size);
        }
    });

    //3-4) 현재 페이지와 일치하는 경우 파란색 스타일 적용
    if (pageIndex === commentsPage.number) {
        link.style.color = "blue"; // 또는 다른 파란색 스타일을 적용할 수 있음.
        link.classList.remove("text-dark");
    }

    button.appendChild(link);
    return button;
};



//4)입력창 엔터로 댓글, 대댓글 전송 가능
document.getElementById('commentContents').addEventListener('keydown', function(event) {
    if (event.key === 'Enter') {
        event.preventDefault(); // 엔터 키의 기본 동작(개행 추가)을 막음
        commentWrite(); // commentWrite() 메서드 호출터

    }
});


//5) 댓글 작성 함수
const commentWrite = () => {
    // 사용자 로그인 상태 확인
    $.ajax({
        type: "get",
        url: "/api/check-login",
        success: function (response) {
            if (response.isLoggedIn) {
                // 로그인된 경우, 댓글 작성 로직 수행
                const contents = document.getElementById("commentContents").value;
                const boardId = document.getElementById("boardId").value;

                //5-1) 서버에 댓글 저장 요청
                $.ajax({
                    type: "post",
                    url: "/comment/save",
                    data: {
                        commentContents: contents,
                        boardId: boardId
                    },
                    success: function (response) {
                        if (response === "success") {
                            console.log("댓글 작성 성공");
                            document.getElementById("commentContents").value = "";
                            // 작성된 댓글 목록을 다시 불러옴
                            loadComments();
                        } else {
                            console.log("댓글 작성 실패");
                        }
                    },
                    error: function (xhr, status, error) {
                        console.log("에러 발생", status, error);
                    }
                });
            } else {
                //5-2) 로그인되지 않은 경우, 알림창 표시
                alert("로그인이 필요.");
            }
        },
        error: function (xhr, status, error) {
            console.log("에러 발생", status, error);
        }
    });
};



// 6)페이지 로딩 완료 후 초기 댓글 목록 및 페이징 버튼 생성
document.addEventListener("DOMContentLoaded", function () {
    const initialCommentsPage = { number: 0, size: 10 };
    createPaginationButtons(initialCommentsPage);
    loadComments();

    //6-1) 초기 댓글이 로드된 후에 이벤트 리스너를 추가
    attachCommentEventListeners();
});




// 전역 변수 초기화
let openReplyForm = null;

//7)대댓글 이벤트리스너
// 대댓글 이벤트리스너
function attachCommentEventListeners() {
    $(document).off('click', '.open-reply-form-button').on('click', '.open-reply-form-button', function () {
        console.log('hello');

        // 새로운 코드: 형제요소의 commentId 값을 가져오기
        const commentId = $(this).siblings(".commentId").text();

        // 대댓글을 추가할 부모 컨테이너를 찾거나 생성.
        var replyCommentsContainer = $(this).closest('.comment').find('.reply-comments');
        if (replyCommentsContainer.length === 0) {
            // 대댓글을 추가할 부모 컨테이너가 없으면 생성.
            replyCommentsContainer = $('<div class="reply-comments"></div>');
            console.log(replyCommentsContainer)
            $(this).closest('.comment').append(replyCommentsContainer);

            // 대댓글이 없는 경우에도 대댓글 입력 폼을 생성.
            // 이 부분을 추가하였음.
            var replyForm = $('<div>').html(`
                <textarea id="replyCommentContents-${commentId}" style="width: 70%; height: 100px; margin-left: 200px; margin-bottom: 5px; display: inline-block;"></textarea>
                <button class="btn btn-primary" onclick="replyCommentWriteForm(${commentId})" style="margin-bottom: 5px;">대댓글작성</button>
                <hr style="width: 100%;">      
            `);
            console.log("댓글폼+ 번호?" + replyForm)

            // 대댓글 입력 폼을 대댓글 컨테이너에 추가
            replyCommentsContainer.append(replyForm);

            // 대댓글 입력 폼이 추가되었음을 표시
            $(this).data('reply-form', true);
        } else {
            $(this).closest('.comment').find('.reply-comments').remove();

        }
    });
}

// 8) 대댓글 입력 폼 닫기 함수
// 대댓글 입력 폼 닫기 함수
function closeReplyForm(commentId) {
}










//9) 대댓글 작성 함수



function replyCommentWriteForm(commentId) {
    console.log('콘솔아이디테스트있냐'+commentId)

    // 대댓글 내용 가져오기
    var replyContents = document.getElementById('replyCommentContents-' + commentId).value;


    // 대댓글 작성자명 (임시로 고정)
    // var replyWriter = "Alice";

    // 대댓글 객체 생성
    var replyComment = {
        //객체의 속성(프로퍼티)은 {키,밸류}로 이루어져있다.
        replyCommentWriter: null,
        replyCommentContents: replyContents,
        boardId: null,  // 게시글 ID (임시로 설정)
        parentComment: commentId,
        commentCreatedTime: null,  // 서버에서 생성 시간 처리
        deleted: 0
    };

    // Ajax를 사용하여 서버로 데이터 전송
$.ajax({
    type: 'POST',
    url: '/comment/replySave?commentId=' + commentId,
    contentType: 'application/json',
    data: JSON.stringify(replyComment),
    success: function (response) {
        // 성공적으로 데이터를 서버로 전송한 경우
        console.log(response);

        // 필요한 추가 작업 수행 (예: 화면 갱신, 메시지 표시 등)

        // 성공 후 대댓글 목록 다시 불러오기
        loadReplyComments(commentId);
        console.log('부모댓글 id?'+commentId)
    },
    error: function (error) {
        // 전송 실패 시 처리
        console.log(error);
    }
});
}
    // 스크립트 코드 작성



//10) 대댓글 목록을 불러오는 함수
function loadReplyComments(commentId) {
    // 서버에 대댓글 목록 요청
    fetch(`/comment/getReplyComments/${commentId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`대댓글 목록 불러오기 실패: ${response.status}`);
            }
            return response.json(); // JSON 데이터로 변환
        })
        .then(commentWithReplies => {
            const replyComments = commentWithReplies.childComments;

            // 대댓글 목록을 화면에 표시
// const replyCommentsContainer = document.querySelector(`.reply-comments-${commentId}`);
// const replyCommentsContainer = document.querySelector('.reply-comments');
const parentCommentElement = document.querySelector(`#comment-id-${commentId}`);

// 새로운 <div> 엘리먼트를 생성하고 추가
const newDiv = document.createElement("div");
newDiv.className = "second-child"; // 클래스 추가
parentCommentElement.appendChild(newDiv);

// 할당
const replyCommentsContainer = document.querySelector(`#comment-id-${commentId} .second-child`);


            if (!replyCommentsContainer) {
                console.error(`대댓글 컨테이너를 찾을 수 없음: .reply-comments-${commentId}`);
                return;
            }

            replyCommentsContainer.innerHTML = "";

         replyComments.forEach(replyComment => {
    const replyCommentElement = document.createElement("div");
    replyCommentElement.innerHTML = `
        <div class="reply-comments" style="width: 80%; margin: 0 auto;">
            <div style="float: left; width: 20%;">${replyComment.replyCommentWriter || ''}</div>
            <div style="float: left; width: 40%; margin-left: 5%;">${replyComment.replyCommentContents}</div>
            <div style="float: right; width: 20%;">${replyComment.replyCommentCreatedTime}</div>
            <div style="clear: both;"></div>
            <hr style="width: 100%;">
        </div>
    `;
    replyCommentsContainer.appendChild(replyCommentElement);
});
        })
        .catch(error => {
            console.error("대댓글 목록 불러오기 실패", error);
        });
}






