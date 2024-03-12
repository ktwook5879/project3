let contents = ""; // 선택된 옵션의 값을 저장할 변수
function getDataAndPopulateSelect() {
    // Ajax 요청
    $.ajax({
        url: '/treat/treatListBody', // 데이터를 받아올 엔드포인트 URL을 입력합니다.
        type: 'GET', // HTTP 요청 방식을 설정합니다. 여기서는 GET 방식을 사용합니다.
        success: function (data) { // Ajax 요청이 성공했을 때 실행될 함수
            populateSelect(data); // 받아온 데이터를 처리하여 select 요소를 생성하는 함수를 호출합니다.
        },
        error: function (xhr, status, error) { // Ajax 요청이 실패했을 때 실행될 함수
            console.error('Ajax request failed:', error); // 에러를 콘솔에 출력합니다.
        }
    });
}

// 받아온 데이터를 처리하여 select 요소를 생성하는 함수
function populateSelect(data) {
    let select = $('#categorySelect'); // select 요소를 jQuery로 선택합니다.

    // select 요소의 내용을 초기화합니다.
    select.empty();

    // 기본 옵션을 추가합니다.
    select.append($('<option>', {
        value: '',
        text: '시술을 선택해 주세요',
        selected: true
    }));

    // 받아온 데이터를 순회하면서 option 요소를 생성하고 select 요소에 추가합니다.
    data.forEach(function (item) {
        let optionText = '(' + item.gender + ')' + item.treatName;
        select.append($('<option>', {
            value: `(${item.gender})${item.treatName}(${item.treatAmount})`,
            text: optionText,
        }));
    });

    select.change(function () {
        updateSelecteContent()
    });
}

function updateSelecteContent() {
    // 선택된 옵션의 값을 content 변수에 할당
    contents = $('#categorySelect').val();
}

// 예약등록 모달에서 회원명 조회  시작
function goKeyDown(event) {
    if (event.key === 'Enter') {
        var userName = document.getElementById("userSales").value;
        searchUser(userName);
        // console.log(userName);
    }
}

function searchUser(userName) {
    // console.log(typeof(userName));
    $.ajax({
        type: 'POST',
        url: '/sale/saleMemberSearch',
        data: {userName: userName},
        success: function (data) {
            // var firstResult = data[0];
            // console.log(firstResult.userPhone);

            if (data.length > 0) {
                // let opt = "<input class='form-control' type='text' id='modalUserInfo' name='modalUserInfo' value=''>";

                var modalBody = $('#searchUser .modal-body .form-group');
                modalBody.empty();

                for (let i = 0; i < data.length; i++) {
                    // var firstResult = data[0];

                    var userInfoInput = $('<a>', {
                        class: 'form-control',
                        style: 'text-decoration: none; border : none;  '
                    }).append($('<input>', {
                        class: 'form-control',
                        type: 'text',
                        readonly: 'readonly',
                        style: ' cursor: pointer; text-align : center ',
                        value: '[' + data[i].userIdx + '] ' + data[i].userName + ' (' + data[i].userPhone + ')'
                    }));

                    modalBody.append(userInfoInput);
                    // $('#modalUserInfo').val("[" + data[i].userIdx + "]" + data[i].userName + "(" + data[i].userPhone + ")");
                }
                $('#searchUser').modal('show');
                $('#myModal').modal('hide');

            } else {
                // console.log("데이터 없음");
                alert("조회된 회원정보가 없습니다.");
            }
        },
        error: function (error) {
            console.log(error);
        }
    });
}


// 예약등록 모달에서 회원명 조회  끝

// 회원가입 모달 창에서 비밀번호 일치 메세지
function pwCheck() {
    if ($('#joinModal #userPwd1').val() == $('#joinModal #userPwd2').val()) {
        // alert("비밀번호 일치");
        $('#joinModal #pwdMsg').css('visibility', "hidden");
        $('#joinModal #userPwd2').css('border-color', '#a8a4a4');
    } else {
        // alert("비밀번호 불일치")
        $('#joinModal #pwdMsg').css('visibility', "visible");
        $('#joinModal #pwdMsg').text("비밀번호가 일치하지 않습니다.").css('color', 'red').css('font-size', '14px').css('margin-left', '120px').css('margin-top', '3px');
        $('#userPwd2').css('border-color', 'red');
    }
};


// 시간 반환 함수
function formatTime(inputTime) {
    // Date 객체 생성
    var date = new Date(inputTime);

    // 시, 분, 초를 추출하여 문자열로 조합
    var hours = date.getHours().toString().padStart(2, '0');
    var minutes = date.getMinutes().toString().padStart(2, '0');
    var seconds = date.getSeconds().toString().padStart(2, '0');

    var formattedTime = hours + ':' + minutes + ':' + seconds;

    return formattedTime;
}

let currentViewType = 'dayGridMonth'; // 현재 보고 있는 뷰 타입을 저장할 변수

document.addEventListener('DOMContentLoaded', function () {
    getDataAndPopulateSelect();
    // 모달창에서 회원 검색 후 받아오는 변수
    let userInfo = "";
    let currentViewType = 'dayGridMonth'; // 현재 보고 있는 뷰 타입을 저장할 변수
    let calendarEl = document.getElementById('calendar');
    let calendar = new FullCalendar.Calendar(calendarEl, {
        headerToolbar: {
            left: 'dayGridMonth,timeGridWeek',
            center: 'title',
            right: 'prev,today,next'
        },
        initialView: 'dayGridMonth',
        height: '95vh',
        expandRows: true,
        locale: 'ko', // 한국어 로케일 설정
        selectable: true, // 전체 선택 기능 비활성화
        editable: false, // 일정 편집 가능 여부
        slotMinTime: '08:00:00', // 시작 시간 설정 (오전 8시)
        slotMaxTime: '21:00:00', // 오후 8시까지 슬롯을 생성
        allDaySlot: false, // all-day 슬롯 제거
        select: function (info) {
            // 주간 뷰인 경우에만 select 이벤트를 설정
            if (currentViewType === 'timeGridWeek') {
                const startDate = info.startStr;
                const endDate = info.endStr;
                console.log('Selected time:', startDate, 'to', endDate);

                const startDates = formatTime(info.startStr); // 시작 시간 가져오기
                const endDates = formatTime(info.endStr);
                const date = info.endStr.toString().split('T')[0];
                console.log('시간 time:', startDates, 'to', endDates, date);

                const startTimeElement = document.getElementById('startTime');
                startTimeElement.textContent = startDates;
                const endTimeElement = document.getElementById('endTime');
                endTimeElement.textContent = endDates;

                if (startDate !== null) {
                    $('#myModal').modal('show'); // 모달 창 열기

                    $('#myModal .modal-body #modalUserValue').val("");
                    $('#myModal .modal-body #userSales').val("");
                    $('.salesBoxCustom .salesUserValue').css('display', 'none');

                    $('#myModal .modal-body #categorySelect').val("");
                }


                $('#myModal #saveButton').off('click').on('click', function (event) {
                    const startDate = formatTime(info.startStr); // 시작 시간 가져오기
                    const endDate = formatTime(info.endStr);
                    const date = info.endStr.toString().split('T')[0];

                    if(userInfo ===""){

                        alert("고객명을 선택해주세요.");
                        event.preventDefault();
                    }
                    else {
                        // 대괄호 사이의 숫자 추출
                        var userIdx = userInfo.match(/\[(\d+)\]/)[1];
                        // 괄호 사이의 이름 추출
                        var name = userInfo.match(/\] (.+?) \(/)[1];
                        // 괄호 안의 전화번호 추출
                        var phoneNum = userInfo.match(/\((\d+)\)/)[1];
                        if(contents===""){
                            alert("시술명을 선택해주세요.");
                            event.preventDefault();
                        }
                        else {
                            $.ajax({
                                type: 'POST',
                                url: '/reservation/insert',
                                contentType: 'application/json', // 요청의 컨텐츠 타입을 JSON으로 설정
                                data: JSON.stringify({
                                    startTime: startDate,
                                    endTime: endDate,
                                    date: date,
                                    userIdx : userIdx,
                                    name: name,
                                    phoneNum: phoneNum,
                                    content: contents
                                }),
                                success: function (response) {
                                    console.log('Reservation successfully added:', response);
                                    // 모달창 닫기
                                    $('#myModal').modal('hide');
                                    // 모달창이 닫힐 때 폼 초기화
                                    $('#reservationForm').trigger('reset');
                                    // 캘린더 리로드
                                    calendar.refetchEvents();
                                },
                                error: function (xhr, status, error) {
                                    console.error('Error adding reservation:', error);
                                    // 오류 메시지 표시 등의 처리
                                }
                            });
                        }
                    }
                });

                //     모달 창에서 회원등록 버튼 클릭 시
                $('#addMember').on("click", function () {
                    // console.log("클릭");
                    $('#joinModal').modal('show');
                    $('#myModal').modal('hide');


                    // 아이디 중복확인

                    // 중복확인 버튼
                    var userIdChk = false;

                    $("#joinModal #userId").on("change", function (e) {
                        $('#userIdMsg').css('visibility', "hidden");
                        userIdChk = false;
                    });

                    $("#joinModal #userIdChk").off("click").on("click", function (e) {
                        if ($("#joinModal #userId").val() == "") {
                            $('#joinModal #userIdMsg').text("아이디를 입력해주세요.");
                            $('#joinModal #userIdMsg').css('visibility', "visible").css("color", "red").css('margin-left', '120px').css('margin-top', '3px').css('font-size', '14px');
                        } else {
                            $('#joinModal #userIdMsg').css('visibility', "hidden");
                            e.preventDefault();
                            $.ajax({
                                url: "/member/join/modalIdCheck",
                                data: { "userId": $("#joinModal #userId").val() },
                                success: function (data) {
                                    alert(data);
                                    if (data == "사용가능한 아이디입니다.") {
                                        userIdChk = true;
                                    }
                                },
                                error: function (req, status, err) {

                                    console.log(req);
                                }
                            });
                        }
                    });

                    /*전화번호 형식 확인*/

                    //  휴대폰 정규식 설정
                    var regPhone = /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/;

                    $("#joinModal #userPhone").on("blur", function () {
                        if (!regPhone.test($(this).val())) {
                            return alert("전화번호 형식이 맞지 않습니다.");
                        } else {
                            return true;
                        }
                    });


                    $("#joinModal #join").on("click", function () {

                        if (userIdChk) {
                            // 입력여부 검사
                            if (!$("#joinModal #userName").val()) {
                                alert("이름을 입력해주세요.");
                                return false;
                            }

                            if (!$("input[name=gender]").is(":checked")) {
                                alert("성별을 입력해주세요.")
                                return false;
                            }

                            if (!$("#joinModal #userPhone").val()) {
                                alert("전화번호를 입력해주세요.");
                                return false;
                            }


                            if (!$("#joinModal #userPwd1").val()) {
                                alert("비밀번호를 입력해주세요.");
                                return false;
                            }

                            if (!$("#joinModal #userPwd2").val()) {
                                alert("비밀번호를 확인해주세요.");
                                return false;
                            }

                            // alert("회원가입 성공!");

                            $.ajax({
                                type:'post',
                                url: "/member/join/joinModal"
                                , data: {
                                    "userName": $("#joinModal #userName").val(),
                                    "gender": $("input[name=gender]:checked").val(),
                                    "userPhone": $("#joinModal #userPhone").val(),
                                    "userId": $("#joinModal #userId").val(),
                                    "userPwd1": $("#joinModal #userPwd1").val(),
                                    "userPwd2": $("#joinModal #userPwd2").val(),
                                }
                                , success: function (data) {
                                    // console.log($("#joinModal #userId").val())
                                    if (data == "회원가입 성공")
                                        alert(data);

                                },
                                error: function (req, status, err) {
                                    console.log(req);
                                }
                            });

                            $('#joinModal').modal('hide');
                            $('#myModal').modal('show');

                        } else {
                            alert("아이디 중복확인 해주세요");
                            return false;
                        }

                    });

                    $("#joinModal #cancel").on("click", function () {

                        $('#joinModal').modal('hide');
                        $('#myModal').modal('show');

                    });
                });
                //     모달 창에서 회원등록 끝

            //    회원명 검색 후 뜨는 모달 창 이벤트 시작
                $('#searchUser .close').on('click', function () {
                    $('#searchUser').modal('hide');
                    $('#myModal').modal('show');
                });

                // 모달창에서 데이터보내기
                $('#searchUser .modal-body .form-group').on('click', 'a', function () {
                    // 모달창의 input데이터
                    userInfo = $(this).find('input').val();

                    // 이전 페이지에 모달창에서 클릭되어진 회원값을 넣기
                    $('#myModal .modal-body #modalUserValue').val(userInfo);
                    $('#myModal .modal-body .salesBoxCustom .salesUserValue').css('display', 'inline-block');

                    // 회원 검색했던 페이지에서 회원 입력된 태그 비워주기
                    $('#userSales').val("");

                    //모달닫기
                    $('#searchUser').modal('hide');
                    $('#myModal').modal('show');
                });

                //     modal에서 받아온 user값의 x버튼 클릭 시 삭제
                $('#myModal .salesBoxCustom .salesUserValue .btnDelUser').on('click', function () {
                    //    modal에서 받아온 user데이터 삭제
                    $('#myModal .modal-body #modalUserValue').val("");
                    $('.salesBoxCustom .salesUserValue').css('display', 'none');
                    // $('.salesBoxCustom .salesUserValue').css('visibility','hidden');
                })

                // 회원명 검색하면서 input text에서 엔터키 누르면 form submit 되는 것 방지
                $("#userSales").keydown(function (e) {
                    if (e.keyCode === 13)
                        e.preventDefault();
                });

                //    회원명 검색 후 뜨는 모달 창 이벤트 끝


            } else {
                // 다른 뷰인 경우에는 select 이벤트 해제
                $('#myModal').modal('hide'); // 모달 닫기
                $('#saveButton').off('click'); // 클릭 이벤트 제거


            }
        },
        events: function (info, successCallback, failureCallback) {
            // // 월간 뷰인 경우에만 카운트 받아옴
            const startDate = info.startStr; // 시작날짜
            const endDate = info.endStr; // 끝날짜
            const start = startDate.split('T')[0];
            const end = endDate.split('T')[0];

            console.log('시작 날짜:', start);
            console.log('끝 날짜:', end);

            if (currentViewType == 'dayGridMonth') {
                $.ajax({
                    type: 'GET',
                    url: '/reservation/count',
                    data: {
                        start: start,
                        end: end
                    },
                    success: function (response) {
                        const eventsData = [];
                        response.forEach(function (item) {
                            const eventData = {
                                title: '예약 ' + item[1] + '건',
                                start: item[0]
                            };
                            eventsData.push(eventData);
                        });
                        successCallback(eventsData);
                    },
                    error: function (xhr, status, error) {
                        console.error('에러 발생:', error);
                        failureCallback(error); // 오류 콜백 호출
                    }
                });
            } else if (currentViewType == 'timeGridWeek') {
                // 주간 뷰인 경우에만 이벤트를 받아옴
                $.ajax({
                    url: '/reservation/events', // 데이터를 가져올 엔드포인트
                    type: 'GET',
                    success: function (response) {
                        // 이벤트 데이터 배열 초기화
                        const eventData = [];

                        // 응답 데이터를 순회하면서 이벤트 객체로 변환하여 배열에 추가
                        response.forEach(function (item) {
                            let name = item.name;
                            let content = item.content;

                            // "( )" 기준으로 문자열 분할
                            let parts = content.split(/\(|\)/).filter(Boolean);
                            // 스타일과 가격 추출
                            let style = `(${parts[0]})${parts[1]}`; // "(남성)커트"
                            let price = parts[2]; // "70000"

                            let phoneNum = item.phoneNum;
                            let userIdx = item.userIdx;
                            console.log("userIdx" + userIdx);

                            const title = `${name}/${content}/${phoneNum}`;

                            // 이벤트 객체 생성
                            const event = {
                                title: item.name, // 이벤트 제목 (이름 / 내용 형식)
                                start: item.date + 'T' + item.startTime, // 시작 시간 (ISO 8601 형식)
                                end: item.date + 'T' + item.endTime, // 종료 시간 (ISO 8601 형식)
                                id: item.idx,
                                // 기타 필요한 속성 추가 가능
                                extendedProps: {
                                    content: style,
                                    price:price,
                                    phoneNum: phoneNum,
                                    userIdx: userIdx,
                                }
                            };

                            // 배열에 이벤트 객체 추가
                            eventData.push(event);
                        });

                        // 성공 콜백 함수 호출하여 이벤트 배열 전달
                        successCallback(eventData);
                    },
                    error: function (xhr, status, error) {
                        console.error('에러 발생:', error);
                        failureCallback(error); // 오류 콜백 호출
                    }
                });
            }
        },
        eventClick: function (info) {
            // 이벤트 클릭 시 모달 창 열기
            if (currentViewType === 'timeGridWeek') {
                $('#editModal').modal('show');

                // 클릭한 이벤트 정보 가져오기
                const event = info.event;
                const idx = event.id;
                const name = event.title.split('/')[0];
                const content = event.extendedProps.content;
                const price = event.extendedProps.price;
                const phoneNum = event.extendedProps.phoneNum;
                const userIdx = event.extendedProps.userIdx;
                const start = event.startStr.split('T')[1].split('+')[0];
                const end = event.endStr.split('T')[1].split('+')[0];
                const date = event.startStr.split('T')[0];

                // treatSale DB에서 name, content, phoneNum, date 있는지 확인 후 매출등록버튼 비활성화
                $.ajax({
                    url:"/"
                })

                // 모달에 이벤트 정보 표시
                $('#editEventName').val(name);
                $('#editEventContent').val(content);
                $('#editEventPhoneNum').val(phoneNum);
                $('#editEventStart').val(start);
                $('#editEventEnd').val(end);



                // 수정 버튼 클릭 시
                $('#saveChangesBtn').off('click').on('click', function () {
                    // 수정된 정보 가져오기
                    const newName = $('#editEventName').val();
                    const newContent = $('#editEventContent').val();
                    const newPhoneNum = $('#editEventPhoneNum').val();
                    const newStart = $('#editEventStart').val();
                    const newEnd = $('#editEventEnd').val();

                    // 이벤트 업데이트 요청 보내기 (예: AJAX 요청)
                    if (confirm("정말로 수정하시겠습니까?")) {
                        $.ajax({
                            type: 'PUT', // PUT 요청으로 수정
                            url: '/reservation/' + idx, // 해당 이벤트의 ID에 맞춰 업데이트할 엔드포인트로 보내기
                            contentType: 'application/json', // JSON 형식으로 데이터 전송
                            data: JSON.stringify({ // 수정할 데이터를 JSON 형식으로 전송
                                idx: idx,
                                name: newName,
                                content: newContent,
                                phoneNum: newPhoneNum,
                                startTime: newStart,
                                endTime: newEnd,
                                date: date
                            }),
                            success: function (response) {
                                // 업데이트 성공 시 캘린더 다시 렌더링
                                calendar.refetchEvents();
                            },
                            error: function (xhr, status, error) {
                                console.error('Error updating event:', error);
                            }
                        });
                        // 모달 닫기
                        $('#editModal').modal('hide');
                    }
                });

                // 삭제 버튼 클릭 시
                $('#deleteEventBtn').off('click').on('click', function () {
                    // 확인창 표시
                    if (confirm("정말로 이벤트를 삭제하시겠습니까?")) {
                        // 사용자가 확인을 누를 경우 삭제 요청 보내기 (예: AJAX 요청)
                        $.ajax({
                            type: 'DELETE',
                            url: '/reservation/' + event.id, // 해당 이벤트의 ID에 맞춰 삭제할 엔드포인트로 보내기
                            success: function (response) {
                                // 삭제 성공 시 캘린더 다시 렌더링
                                calendar.refetchEvents();
                            },
                            error: function (xhr, status, error) {
                                console.error('Error deleting event:', error);
                            }
                        });
                    }
                    $('#editModal').modal('hide');
                });


                $('#registerEventBtn').off('click').on('click', function () {
                    window.location.href = '/sale/addSales?content='
                        + encodeURIComponent(content)
                        + '&userIdx=' + encodeURIComponent(userIdx)
                        + '&name=' + encodeURIComponent(name)
                        + '&phoneNum=' + encodeURIComponent(phoneNum)
                        + '&date=' + encodeURIComponent(date)
                        + '&end=' + encodeURIComponent(end)
                        + '&price=' + encodeURIComponent(price);
                });


            }
        },
        dateClick: function (info) {
            // 날짜 클릭 시 timeGridWeek 뷰로 변경
            currentViewType = 'timeGridWeek';
            calendar.refetchEvents();
            calendar.changeView('timeGridWeek', info.dateStr);
        },
        eventContent: function (arg) {
            var eventHtml = '<div class="fc-content">' +
                '<div>' + arg.event.title + '</div>';
            if (currentViewType === 'timeGridWeek') {
                eventHtml = '<div class="fc-content">' +
                    '<div>' + arg.event.title + '</div>' +
                    '<div>' + arg.event.extendedProps.content + '</div>' +
                    '<div>' + arg.event.extendedProps.phoneNum + '</div>' +
                    '</div>';
            }
            return {html: eventHtml};
        }
    });

    // 월간 뷰 버튼 클릭 시 페이지 새로고침
    $(document).ready(function () {
        $('.fc-dayGridMonth-button').on('click', function () {
            currentViewType = 'dayGridMonth';
            calendar.refetchEvents();
        });

        $('.fc-timeGridWeek-button').on('click', function () {
            currentViewType = 'timeGridWeek';
            calendar.refetchEvents();
        });


    });
    calendar.render();
});