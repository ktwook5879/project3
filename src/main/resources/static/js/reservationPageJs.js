let content = ""; // 선택된 옵션의 값을 저장할 변수
let time= 0;
function getDataAndPopulateSelect() {
    // Ajax 요청
    $.ajax({
        url: '/treat/treatListBody', // 데이터를 받아올 엔드포인트 URL을 입력합니다.
        type: 'GET', // HTTP 요청 방식을 설정합니다. 여기서는 GET 방식을 사용합니다.
        success: function(data) { // Ajax 요청이 성공했을 때 실행될 함수
            populateSelect(data); // 받아온 데이터를 처리하여 select 요소를 생성하는 함수를 호출합니다.
        },
        error: function(xhr, status, error) { // Ajax 요청이 실패했을 때 실행될 함수
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
    data.forEach(function(item) {
        let optionText = '(' + item.gender + ')' + item.treatName + ' / ' + item.treatAmount + '원 / ' + item.treatTimeHour + '시간 ' + item.treatTimeMinute + '분';
        select.append($('<option>', {
            value: `(${item.gender})${item.treatName}(${item.treatAmount})`,
            text: optionText,
            time: parseInt(item.treatTimeHour)*60+parseInt(item.treatTimeMinute)
        }));
    });

    select.change(function() {
        updateSelecteContent()
    });
}
function updateSelecteContent() {
    // 선택된 옵션의 값을 content 변수에 할당
    content = $('#categorySelect').val();

    // 값이 없을 경우
    if (!content) {
        alert("시술을 선택해주세요.");
        return;
    }

    time = $('#categorySelect option:selected').attr('time');
    let parts = content.split(/\(|\)/).filter(Boolean);
    // 스타일과 가격 추출
    let style = `(${parts[0]})${parts[1]}`; // "(남성)커트"
    $('p#selected-content').text('시술내용 : ' + style);
}


$(document).ready(function () {
    getDataAndPopulateSelect()
    let userIdx = document.getElementById('sessionUserIdx').innerText;
    let sessionName = document.getElementById('sessionName').innerText;
    let sessionPhone = document.getElementById('sessionPhone').innerText;
    let reservationDate = localStorage.getItem('reservationDate');



    $('#saveButton').click(function () {
        // gihoTime에서 선택한 시간 불러오기
        let selectedTime = localStorage.getItem('selectedTime')

        // 선택한 시간이 없으면 alert 창 표시 및 두번째모달 이벤트 중지
        if (!selectedTime) {
            alert('시간을 선택해 주세요.');
            return false; // 두번째 모달로 안넘어가게 이벤트 멈추는 부분
        }

        let categorySelect = $('#categorySelect').val();
        if (categorySelect == "") {
            alert('시술을 선택해 주세요.');
            return false;
        }

        // 시간을 선택했을 경우 첫번째 모달 보이고 두번째 모달 안보이고
        if (selectedTime) {
            let startTime = localStorage.getItem('selectedTime');
            // startTime을 Date 객체로 변환
            let startTimeObj = new Date(`2000-01-01T${startTime}`);

            // 30분을 추가하여 endTime을 얻기
            startTimeObj.setMinutes(startTimeObj.getMinutes() + time);

            // endTime을 문자열로 변환하여 저장
            let hours = startTimeObj.getHours().toString().padStart(2, '0');
            let minutes = startTimeObj.getMinutes().toString().padStart(2, '0');
            let seconds = startTimeObj.getSeconds().toString().padStart(2, '0');
            let endTime = `${hours}:${minutes}:${seconds}`;
            let name = sessionName;
            let phoneNum=sessionPhone;

            $.ajax({
                type: 'POST',
                url: '/reservation/insert',
                contentType: 'application/json', // 요청의 컨텐츠 타입을 JSON으로 설정
                data: JSON.stringify({
                    userIdx: userIdx,
                    startTime: startTime,
                    endTime: endTime,
                    date: reservationDate,
                    name: name,
                    phoneNum: phoneNum,
                    content: content
                }),
                success: function (response) {
                    console.log('Reservation successfully added:', response);
                    $('#exampleModalToggle').modal('hide');
                    $('#exampleModalToggle2').modal('show');
                    $('#reservation-time').attr('src', '/reservation/timeTable');
                },
                error: function (xhr, status, error) {
                    console.error('Error adding reservation:', error);
                    // 오류 메시지 표시 등의 처리
                }
            });
        }

    });

    // 시간 변경 시 변경된 시간 업데이트 하는 부분
    function updateSelectedTime() {
        let storedTime = localStorage.getItem('selectedTime');
        if (storedTime) {
            $('p#selected-time').text('예약시간 : ' + storedTime);
        }
    }

    // 날짜 변경 시 변경된 날짜 업데이트 하는 부분
    function updateReservationDate() {
        let reservationDate = localStorage.getItem('reservationDate');
        if (reservationDate) {
            $('p#reservation-date').text('예약일 : ' + reservationDate);
        }
    }

    function updateReservationName() {
        if (sessionName !== null) {
            $('p#reservation-name').text('예약자 : ' + sessionName);
        }
    }

    function setUpEventHandlers() {
        // 첫번쨰 모달창에서 저장 버튼을 눌렀을때 이벤트 작동하게 하는 부분
        // $('#exampleModalToggle').on('click', 'button.btn-primary', handleModalButtonClick);

        // 두 번째 모달 창이 열릴 때마다 선택한 시간을 갱신 시키는 부분
        $('#exampleModalToggle2').on('show.bs.modal', function () {
            updateSelectedTime();
            updateReservationDate()
            updateReservationName()
        });

    }


    // 페이지 로드 시 초기 업데이트를 수행
    function initialUpdate() {
        updateSelectedTime();
        updateReservationDate();

        // 시간 및 날짜 변경 시 갱신하여 localStorage에 저장
        setInterval(function () {
            var updated = localStorage.getItem('selectedTimeUpdated');
            if (updated && updated === 'true') {
                updateSelectedTime();
                updateReservationDate();
                localStorage.removeItem('selectedTimeUpdated');
            }
        }, 1000);
    }

    // 초기 업데이트 및 이벤트 핸들러 설정
    initialUpdate();
    setUpEventHandlers();

    window.addEventListener('message', function(event) {
        // event.data를 통해 메시지 데이터에 접근
        if (event.data.reservationDate) {
            // reservationDate에 해당하는 데이터가 변경되었을 때 다시 렌더링 또는 작업 수행
            const newReservationDate = event.data.reservationDate;
            reservationDate = newReservationDate;
            console.log('mooncalendar에서 받아온 값', newReservationDate);
            const iframe = document.getElementById('reservation-time');
            // 자식 창의 함수 호출
            iframe.contentWindow.hideReservedTimes(newReservationDate);
            iframe.src = iframe.src;
        }
    });
});

