
        // 댓글 목록을 불러오는 함수
        const loadComments = (page = 0, size = 10) => {
            // 게시글 ID를 가져옴
            const boardId = document.getElementById("boardId").value;

            // 페이지와 사이즈가 정의되지 않은 경우 기본값으로 설정
            if (page === undefined) {
                page = 0;
            }
            if (size === undefined) {
                size = 10;
            }

            // 서버에 댓글 목록 요청
            $.ajax({
                type: "get",
                url: `/comment/list/${boardId}?page=${page}&size=${size}`,
                success: function (commentsPage) {
                    // 댓글 목록을 화면에 표시
                    const commentListElement = document.getElementById("commentList");
                    commentListElement.innerHTML = "";

                    commentsPage.content.forEach(function (comment) {
                          const commentElement = document.createElement("div");
                        commentElement.className = "comment"; // 클래스 추가
                        commentElement.innerHTML = `
                            <div style="width: 80%; margin: 0 auto;">
                                <div style="float: left; width: 20%;">${comment.commentWriter}</div>
                                <div style="float: left; width: 40%; margin-left: 5%;">${comment.commentContents}</div>
                                <div style="float: right; width: 20%;">${comment.commentCreatedTime}</div>
                                  <div class="commentId" style="display: none;">${comment.id}</div>
                                <div style="clear: both;"></div>
                                <hr style="width: 100%;">
                            </div>
                        `;
                        commentListElement.appendChild(commentElement);
                    });

                    // 댓글 목록 하단에 페이징 버튼 생성
                    createPaginationButtons(commentsPage);
                },
                error: function (xhr, status, error) {
                    console.log("댓글 목록 불러오기 실패", status, error);
                }
            });
        };

        // 페이징 버튼을 생성하는 함수
        const createPaginationButtons = (commentsPage) => {
            const paginationElement = document.getElementById("pagination");

            // 페이징 컨테이너 초기화
            if (!paginationElement) {
                console.error("paginationElement가 존재하지 않습니다.");
                return;
            }
            paginationElement.innerHTML = "";

            // 페이징 컨테이너 및 버튼 생성
            const paginationContainer = document.createElement("ul");
            paginationContainer.classList.add("pagination", "justify-content-center");

            // 이전 버튼
            const prevButton = createPaginationButton("이전", !commentsPage.first, commentsPage.number - 1, commentsPage);
            paginationContainer.appendChild(prevButton);

            // 페이지 번호 버튼 생성
            for (var i = 0; i < commentsPage.totalPages; i++) {
                const pageButton = createPaginationButton(i + 1, i != commentsPage.number, i, commentsPage);
                paginationContainer.appendChild(pageButton);
            }

            // 다음 버튼
            const nextButton = createPaginationButton("다음", !commentsPage.last, commentsPage.number + 1, commentsPage);
            paginationContainer.appendChild(nextButton);

            // 페이징 컨테이너를 실제 페이지에 추가
            paginationElement.appendChild(paginationContainer);
        };

        // 페이지 번호 버튼을 생성하는 함수
        const createPaginationButton = (text, enabled, pageIndex, commentsPage) => {
            const button = document.createElement("li");
            button.classList.add("page-item");

            // 비활성화된 경우 'disabled' 클래스 추가
            if (!enabled) {
                button.classList.add("disabled");
            }

            const link = document.createElement("a");
            link.classList.add("page-link", "text-dark");

            // 비활성화된 경우 'text-primary' 클래스 추가
            if (!enabled) {
                link.classList.add("text-primary");
            }

            link.innerText = text;

            // 페이지 번호 버튼에 클릭 이벤트 추가
            link.addEventListener("click", () => {
                if (enabled) {
                    // 클릭된 경우 해당 페이지의 댓글 목록을 다시 불러옴
                    loadComments(pageIndex, commentsPage.size);
                }
            });

            // 현재 페이지와 일치하는 경우 파란색 스타일 적용
            if (pageIndex === commentsPage.number) {
                link.style.color = "blue"; // 또는 다른 파란색 스타일을 적용할 수 있습니다.
                link.classList.remove("text-dark");
            }

            button.appendChild(link);
            return button;
        };

                //입력창 엔터로 댓글 전송 가능
        document.getElementById('commentContents').addEventListener('keydown', function(event) {
            if (event.key === 'Enter') {
                event.preventDefault(); // 엔터 키의 기본 동작(개행 추가)을 막음
                commentWrite(); // commentWrite() 메서드 호출
            }
        });
        // 댓글 작성 함수
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

                        // 서버에 댓글 저장 요청
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
                        // 로그인되지 않은 경우, 알림창 표시
                        alert("로그인이 필요합니다.");
                    }
                },
                error: function (xhr, status, error) {
                    console.log("에러 발생", status, error);
                }
            });
        };

<!-- 페이지 로딩 완료 후 초기 댓글 목록 및 페이징 버튼 생성 -->
document.addEventListener("DOMContentLoaded", function () {
    const initialCommentsPage = { number: 0, size: 10 };
    createPaginationButtons(initialCommentsPage);
    loadComments();
});


// 전역 변수 초기화
let openReplyForm = null;


function attachCommentEventListeners() {

    // 댓글 클릭 시 대댓글 목록 가져오기
    $(document).on('click', '.comment', function () {
        // 새로운 코드: commentId 값을 가져오기

    const commentId = $(this).find(".commentId").text();

        // 대댓글을 추가할 부모 컨테이너를 찾거나 생성합니다.
        var replyCommentsContainer = $(this).find('.reply-comments');
        if (replyCommentsContainer.length === 0) {
            // 대댓글을 추가할 부모 컨테이너가 없으면 생성합니다.
            replyCommentsContainer = $('<div class="reply-comments"></div>');
            $(this).append(replyCommentsContainer);
        }


   // 대댓글 입력 폼이 열려있는 경우
        if (openReplyForm) {
            // 클릭된 댓글이 현재 열려 있는 대댓글 창의 대상 댓글과 일치하는지 확인
            if (openReplyForm.commentId === commentId) {
                // 일치하는 경우 대댓글 창을 닫기
                closeReplyForm();
                return;
            } else {
                // 일치하지 않는 경우 열려있는 대댓글 창을 닫기 1
                closeReplyForm();
            }
        }
  // 대댓글 입력 폼 생성
        var replyForm = $('<div>').html(`
            <textarea id="replyCommentContents-${commentId}" style="width: 70%; height: 100px; margin-bottom: 5px; display: inline-block;"></textarea>
            <button class="btn btn-primary" onclick="replyCommentWrite(${commentId})" style="margin-bottom: 5px;">대댓글작성</button>
        `);

        // 대댓글 입력 폼을 대댓글 컨테이너에 추가
        replyCommentsContainer.append(replyForm);

        // 대댓글 입력 폼이 추가되었음을 표시
        $(this).data('reply-form', true);

        // 현재 열린 대댓글 입력 폼 정보를 전역 변수에 저장
        openReplyForm = {
            commentId: commentId,
            replyForm: replyForm
        };
    });
        // 대댓글 목록을 이미 가져왔는지 확인
        if (!$(this).data('comments-loaded')) {
            // Ajax를 사용하여 서버에서 대댓글 목록 가져오기
            $.ajax({
                type: 'GET',
                url: '/getReplyComments?commentId=' + commentId,
                success: function (replyComments) {
                    // 서버로부터 받은 대댓글 목록을 표시
                    replyCommentsContainer.html('');

                    replyComments.forEach(function (replyComment) {
                        var replyCommentDiv = $('<div>').text(replyComment.replyCommentContents);
                        replyCommentsContainer.append(replyCommentDiv);
                    });

                    // 대댓글 목록을 가져왔음을 표시
                    $(this).data('comments-loaded', true);
                },
                error: function (error) {
                    console.log('대댓글 목록 가져오기 실패', error);
                }
            });
        }
}


// 대댓글 입력 폼 닫기 함수
function closeReplyForm() {
    // 열린 대댓글 입력 폼이 있다면 삭제
    if (openReplyForm) {
        openReplyForm.replyForm.remove();

        // 전역 변수 초기화
        openReplyForm = null;
    }
}
// 페이지 로딩 완료 후 초기 댓글 목록 및 페이징 버튼 생성
document.addEventListener("DOMContentLoaded", function () {
    const initialCommentsPage = { number: 0, size: 10 };
    createPaginationButtons(initialCommentsPage);
    loadComments();

    // 초기 댓글이 로드된 후에 이벤트 리스너를 추가
    attachCommentEventListeners();
});



// 대댓글 작성 함수
function replyCommentWrite(parentCommentId) {
    // 대댓글 내용 가져오기
    var replyContents = document.getElementById('replyCommentContents-' + parentCommentId).value;

    // 대댓글 작성자명 (임시로 고정)
    var replyWriter = "Alice";

    // 대댓글 객체 생성
    var replyComment = {
        replyCommentWriter: replyWriter,
        replyCommentContents: replyContents,
        boardId: 1,  // 게시글 ID (임시로 설정)
        parentComment: parentCommentId,
        commentCreatedTime: null,  // 서버에서 생성 시간 처리
        deleted: 0
    };

    // Ajax를 사용하여 서버로 데이터 전송
    $.ajax({
        type: 'POST',
        url: '/replySave?parentCommentId=' + parentCommentId,
        contentType: 'application/json',
        data: JSON.stringify(replyComment),
        success: function (response) {
            // 성공적으로 데이터를 서버로 전송한 경우
            console.log(response);

            // 필요한 추가 작업 수행 (예: 화면 갱신, 메시지 표시 등)

            // 성공 후 대댓글 목록 다시 불러오기
            loadReplyComments(parentCommentId);
        },
        error: function (error) {
            // 전송 실패 시 처리
            console.log(error);
        }
    });
}

// 대댓글 목록을 불러오는 함수
function loadReplyComments(parentCommentId) {
    // 서버에 대댓글 목록 요청
    $.ajax({
        type: "get",
        url: `/getReplyComments/${parentCommentId}`,
        success: function (replyComments) {
            // 대댓글 목록을 화면에 표시
            const replyCommentsContainer = document.getElementById(`reply-comments-${parentCommentId}`);
            replyCommentsContainer.innerHTML = "";

            replyComments.forEach(function (replyComment) {
                const replyCommentElement = document.createElement("div");
                replyCommentElement.innerHTML = `
                    <div style="width: 80%; margin: 0 auto;">
                        <div style="float: left; width: 20%;">${replyComment.replyCommentWriter}</div>
                        <div style="float: left; width: 40%; margin-left: 5%;">${replyComment.replyCommentContents}</div>
                        <div style="float: right; width: 20%;">${replyComment.commentCreatedTime}</div>
                        <div style="clear: both;"></div>
                        <hr style="width: 100%;">
                    </div>
                `;
                replyCommentsContainer.appendChild(replyCommentElement);
            });
        },
        error: function (xhr, status, error) {
            console.log("대댓글 목록 불러오기 실패", status, error);
        }
    });
}

  function replyCommentWrite() {
    // 대댓글 내용 가져오기
    var replyContents = document.getElementById('replyCommentContents').value;

    // 대댓글 작성자명 (임시로 고정)
    var replyWriter = "Alice";

    // 부모 댓글의 ID 가져오기 (임시로 설정, 실제로는 댓글 클릭 시 해당 댓글의 ID를 가져와야 함)
    var parentCommentId = 1;

    // 대댓글 객체 생성
    var replyComment = {
        replyCommentWriter: replyWriter,
        replyCommentContents: replyContents,
        boardId: 1,  // 게시글 ID (임시로 설정)
        parentComment: parentCommentId,
        commentCreatedTime: null,  // 서버에서 생성 시간 처리
        deleted: 0
    };

    // Ajax를 사용하여 서버로 데이터 전송
    $.ajax({
        type: 'POST',
        url: '/replySave?parentCommentId=' + parentCommentId,
        contentType: 'application/json',
        data: JSON.stringify(replyComment),
        success: function (response) {
            // 성공적으로 데이터를 서버로 전송한 경우
            console.log(response);

            // 필요한 추가 작업 수행 (예: 화면 갱신, 메시지 표시 등)
        },
        error: function (error) {
            // 전송 실패 시 처리
            console.log(error);
        }
    });
}