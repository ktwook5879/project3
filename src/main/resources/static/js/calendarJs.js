//Date 객체 생성
let date = new Date(); // 현재 날짜로 초기화

const renderCalender = () => {
    const viewYear = date.getFullYear();
    const viewMonth = date.getMonth();

    // year-month 채우기
    document.querySelector('.year-month').textContent = `${viewYear}년 ${viewMonth + 1}월`;

    // 지난 달 마지막 Date, 이번 달 마지막 Date
    const prevLast = new Date(viewYear, viewMonth, 0);
    const thisLast = new Date(viewYear, viewMonth + 1, 0);

    const PLDate = prevLast.getDate();
    const PLDay = prevLast.getDay();

    const TLDate = thisLast.getDate();
    const TLDay = thisLast.getDay();

    // Dates 기본 배열들
    const prevDates = [];
    const thisDates = [...Array(TLDate + 1).keys()].slice(1);
    const nextDates = [];

    // prevDates 계산
    if (PLDay !== 6) {
        for (let i = 0; i < PLDay + 1; i++) {
            prevDates.unshift(PLDate - i);
        }
    }

    // nextDates 계산
    for (let i = 1; i < 7 - TLDay; i++) {
        nextDates.push(i);
    }

    // Dates 합치기
    const dates = prevDates.concat(thisDates, nextDates);

    //  Dates 정리
    // 지난달 알기 위해 첫날의 index(firstDateIndex)찾기
    const firstDateIndex = dates.indexOf(1);
    // 다음 달 알아내기 위해 마지막날 index(lastDateIndex)찾기
    const lastDateIndex = dates.lastIndexOf(TLDate);


    // // 삼항 연산자로 이번달 날짜는 this, 나머지는 other
    // // other로 처리된 일자는 이번달 제외한 지난달 다음달 날짜로 투명도 조절하여 표시
    // dates.forEach((date, i) => {
    //     const condition = i >= firstDateIndex && i < lastDateIndex + 1
    //         ? 'this'
    //         : 'other';
    //     // 날짜 onClick
    //     dates[i] = `<div class="date"><span class=${condition} id=${date} onclick="reservation(this.id)">${date}</span></div>`;
    // });

    // other로 처리된 일자를 표시하지 않는 함수
    // 오류 발생 시 아래 주석처리된 코드 사용
    dates.forEach((date, i) => {
        if (i >= firstDateIndex && i < lastDateIndex + 1) {
            dates[i] = `<div class="date"><span class="this" id=${date} onclick="reservation(this.id)">${date}</span></div>`;
        } else {
            dates[i] = `<div class="date"></div>`;
        }
    });

    document.querySelector('.dates').innerHTML = dates.join('');

    // 오늘 날짜 그리기
    const today = new Date();
    if (viewMonth === today.getMonth() && viewYear === today.getFullYear()) {
        for (let date of document.querySelectorAll('.this')) {
            if (+date.innerText === today.getDate()) {
                date.classList.add('today');
                break;
            }
        }
    }
};


renderCalender();
const prevMonth = () => {
    date.setMonth(date.getMonth() - 1);
    renderCalender();
};

const nextMonth = () => {
    date.setMonth(date.getMonth() + 1);
    renderCalender();
};

const goToday = () => {
    date = new Date();
    renderCalender();
};

// 날짜 onclick

// 날짜 onclick
const reservation = (selectedId) => {

    //   사용자가 클릭한 일자의 월, 일 객체
    var selectedYear = null;
    var selectedMonth = null;
    var selectedDate = null;

    //선택한 연 변수에 저장
    selectedYear = date.getFullYear();

    // 선택한 월 변수에 저장
    selectedMonth = date.getMonth() + 1;
    const paddedMonth = String(selectedMonth).padStart(2, '0');

    // 선택한 일 변수 저장
    selectedDate = selectedId;
    const paddedDay = String(selectedId).padStart(2, '0');

    // 선택한 날짜가 오늘 날짜 이전인 경우 클릭 이벤트를 막음
    const selectedTimestamp = new Date(selectedYear, selectedMonth - 1, selectedDate).getTime();
    const todayTimestamp = new Date().setHours(0, 0, 0, 0);
    if (selectedTimestamp < todayTimestamp) {
        console.log("이전 날짜는 선택할 수 없습니다.");

        return;
    }

    // 선택한 날짜를 다른 html에 넣기위한 변수 생성
    let reservationDate = `${selectedYear}-${paddedMonth}-${paddedDay}`;
    // 변수를 LocalStorage에 저장
    localStorage.setItem('reservationDate', reservationDate);
    console.log("mooncalendar의" + reservationDate);
    sendUpdateMessage(reservationDate);

    // 선택한 날짜의 요소에 클래스 추가
    const selectedElement = document.getElementById(selectedId);
    selectedElement.classList.add('selected');

    // 이전에 선택되어 있던 날짜와 today의 색깔 초기화
    const selectedElements = document.querySelectorAll('.selected, .today');
    selectedElements.forEach((element) => {
        if (element.id !== selectedId) {
            element.classList.remove('selected');
            element.classList.remove('today');
        }
    });
}


function sendUpdateMessage(reservationDate) {
    // 변경 사항에 따른 데이터 구성
    const data = {
        reservationDate: reservationDate,
    };
    // iframe를 통해 부모 창으로 메시지 전송
    window.parent.postMessage(data, '*');
}