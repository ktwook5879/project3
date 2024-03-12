function goKeyDown(event) {
    if (event.key === 'Enter') {
        var userName = document.getElementById("userName").value;
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

                var modalBody = $('.modal-body .form-group');
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
                $('.modal').modal('show');

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

function handlePaymentSelection(radio) {
    var paymentInput = radio.parentElement.nextElementSibling.querySelector('input[type="number"]');

    // 모든 payment 입력 필드를 비활성화
    document.querySelectorAll('.pricePayment input[type="number"]').forEach(function (input) {
        input.disabled = true;
    });

    // 선택된 payment 입력 필드만 활성화
    paymentInput.disabled = false;

    // 선택된 payment 입력 필드에 포커스
    paymentInput.focus();
}

$(document).ready(function () {
    let userInfo = "";
    let totalSelectedPrice = 0;

    // 모달창 닫기버튼 클릭
    $('.modal .close').on('click', function () {
        $('.modal').modal('hide');
    });

    // 모달창에서 데이터보내기
    $('.modal-body .form-group').on('click', 'a', function () {
        // 모달창의 input데이터
        userInfo = $(this).find('input').val();

        // 원래 html에 모달창에서 클릭되어진 회원값을 넣기
        $('#modalUserValue').val(userInfo);
        // 회원값  visibility: hidden;=>visible
        // $('.salesBoxCustom .salesUserValue').css('visibility','visible');
        $('.salesBoxCustom .salesUserValue').css('display', 'inline-block');
        // 원래 html에서 회원 검색 창 비워주기
        $('#userName').val("");

        //모달닫기
        $('.modal').modal('hide');

        /*비회원 체크란 숨기기*/
        $('.salesCustom').css("visibility", "hidden");

    });

    //     modal에서 받아온 user값의 x버튼 클릭 시 삭제
    $('.salesBoxCustom .salesUserValue .btnDelUser').on('click', function () {
        //    modal에서 받아온 user데이터 삭제
        $('#modalUserValue').val("");
        $('.salesBoxCustom .salesUserValue').css('display', 'none');
        // $('.salesBoxCustom .salesUserValue').css('visibility','hidden');

        // 비회원 체크란 표시
        $('.salesCustom').css("visibility", "visible");


    })

    //     비회원클릭 시 이벤트
    $('#chkCustom').change(function () {
        if ($("#chkCustom").is(":checked")) {
            $("#userName").attr("disabled", true);
            userInfo = "비회원";
            $("#")
        } else {
            $("#userName").attr("disabled", false);
            $("#userName").val("");
        }

    })

    // 회원명 검색하면서 input text에서 엔터키 누르면 form submit 되는 것 방지
    $("input[type='text']").keydown(function (e) {
        if (e.keyCode === 13)
            e.preventDefault();
    });


    // 선택된 성별과 시술 목록을 저장할 배열
    let selectedServices = []; // 선택된 서비스를 저장하는 배열

    // selectContent가 변경될 때마다 선택된 값을 배열에 추가
    $('#selectContent').change(function () {
        let gender = $('#genderSelect').val();
        let treatName = $('#selectContent').val();
        addServiceRow(gender, treatName);
    });


    function addServiceRow(gender, treatName,price) {
        let selectedTreatPrice = "";
        if(price !=null){
            selectedTreatPrice = price;
        }
        else {
            selectedTreatPrice = $('#selectContent option:selected').data('price');
        }
        if (gender && treatName) {
            let selectedTreatPriceNumber = parseFloat($('#selectContent option:selected').data('price')); // 가격을 숫자로 변환
            let newRow = '<tr class="row">' +
                '<td class="column remove">' +
                '<svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-trash" viewBox="0 0 16 16">' +
                '<path d="M5.5 5.5A.5.5 0 0 1 6 6v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m2.5 0a.5.5 0 0 1 .5.5v6a.5.5 0 0 1-1 0V6a.5.5 0 0 1 .5-.5m3 .5a.5.5 0 0 0-1 0v6a.5.5 0 0 0 1 0z"/>' +
                '<path d="M14.5 3a1 1 0 0 1-1 1H13v9a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V4h-.5a1 1 0 0 1-1-1V2a1 1 0 0 1 1-1H6a1 1 0 0 1 1-1h2a1 1 0 0 1 1 1h3.5a1 1 0 0 1 1 1zM4.118 4 4 4.059V13a1 1 0 0 0 1 1h6a1 1 0 0 0 1-1V4.059L11.882 4zM2.5 3h11V2h-11z"/>' +
                '</svg>' +
                '</td>' +
                '<td class="column gender">' + gender + '</td>' +
                '<td class="column name">' + treatName + '</td>' +
                '<td class="column price">' +
                '<input type="number" id="treatPrice" class="treatPrice" value="' + selectedTreatPrice + '">' +
                '<span>원</span>' +
                '</td>' +
                '</tr>';
            $('#serviceList tbody').append(newRow);
            $('.remove').off().on('click', function () {
                // 클릭된 삭제 버튼이 속한 행의 값을 가져옵니다.
                let deletedRow = $(this).closest('tr');
                let deletedRowValue = deletedRow.find('.name').text();
                let deletedRowPrice = parseFloat(deletedRow.find('.treatPrice').val());

                // 선택된 서비스 배열에서 해당 값을 찾아 제거합니다.
                let index = selectedServices.indexOf(`(${gender})${deletedRowValue}`);
                if (index !== -1) {
                    selectedServices.splice(index, 1);
                }

                // 가격을 총 가격에서 빼줍니다.
                totalSelectedPrice -= deletedRowPrice;
                console.log("Total Selected Price: ", totalSelectedPrice);
                // 가장 가까운 <tr>을 찾아서 삭제합니다.
                deletedRow.remove();
            });

            // 선택된 서비스 배열에 추가
            selectedServices.push(`(${gender})${treatName}`);

            // 모든 서비스의 가격을 더하기
            totalSelectedPrice += selectedTreatPriceNumber;

            // totalSelectedPrice 값을 HTML에 출력하거나 다른 곳에 활용할 수 있습니다.
            console.log("Total Selected Price: ", totalSelectedPrice);


        }
    }

    // totalSelectedPrice 업데이트 함수
    function updateTotalPrice() {
        // 선택된 시술의 총 가격을 다시 계산합니다.
        let total = 0;
        $('.treatPrice').each(function() {
            total += parseFloat($(this).val());
        });
        totalSelectedPrice = total;
        console.log("Total Selected Price Updated: ", totalSelectedPrice);
    }


    let saleDateInput = document.getElementById('saleDate');

    // 현재 날짜를 가져옴
    let currentDate = new Date();

    // 초기값을 설정할 때 원하는 형식으로 날짜를 포맷합니다.
    let year = currentDate.getFullYear();
    let month = (currentDate.getMonth() + 1).toString().padStart(2, '0'); // 월은 0부터 시작하므로 1을 더하고, 두 자리로 포맷합니다.
    let day = currentDate.getDate().toString().padStart(2, '0'); // 날짜를 두 자리로 포맷합니다.

    // 초기값 설정
    let initialDate = year + '-' + month + '-' + day;
    saleDateInput.value = initialDate;

    let saleTimeHourSelect = document.getElementById('saleTimeHour');
    let saleTimeMinuteSelect = document.getElementById('saleTimeMinute');

    // 시간 초기값 설정
    let initialHour = '10';
    // 분 초기값 설정
    let initialMinute = '30';

    // 시간 선택 요소의 초기값 설정
    saleTimeHourSelect.value = initialHour;
    // 분 선택 요소의 초기값 설정
    saleTimeMinuteSelect.value = initialMinute;

    if (window.location.search) {
        let params = new URLSearchParams(window.location.search);
        if (params.has('content') && params.has('name') && params.has('phoneNum') && params.has('date') && params.has('end') && params.has('userIdx')&& params.has('price')) {
            let content = params.get('content');
            let name = params.get('name');
            let phoneNum = params.get('phoneNum');
            let date = params.get('date');
            let end = params.get('end');
            let userIdx = params.get('userIdx');
            let price = params.get('price');

            userInfo="[" + userIdx + "] " + name + " (" + phoneNum + ")";

            // 값 사용 예시
            // console.log("Content: " + content);
            console.log(userInfo);

            // 예약페이지->매출등록 시 param값 받아오기
            $('#modalUserValue').val(userInfo);
            $('.salesBoxCustom .salesUserValue').css('display', 'inline-block');
            $('.salesBoxCustom .salesUserValue').css('right', '10px');

            $('#userName').val("");

            // $('#userName').val(name);
            $('#contentInput').val(content);
            $('#saleDate').val(date);
            let endTime = end.split(':')[0];
            $('#saleTimeHour').val(endTime);
            let endMinute = end.split(':')[1];
            $('#saleTimeMinute').val(endMinute);
            $('#selectedUserIdx').val(userIdx);
            $('.salesCustom').hide();

            // content 값에서 "(gender)treatName" 형식의 데이터를 가져와 처리
            let gender = content.substring(content.indexOf('(') + 1, content.indexOf(')'));
            let treatName = content.substring(content.indexOf(')') + 1);
            addServiceRow(gender, treatName,price);
        }
    }


    $('#genderSelect').change(function () {
        let gender = $(this).val();
        if (gender) {
            $.ajax({
                url: '/treat/treatListByGender', // 성별에 따른 옵션을 가져오는 API 엔드포인트
                method: 'GET',
                data: {gender: gender},
                success: function (response) {
                    // 새로운 옵션을 추가하기 전에 이전에 선택된 옵션을 보존합니다.
                    let selectedTreat = $('#selectContent').val();

                    // selectContent를 비우지 않고 새로운 옵션만 추가합니다.
                    $('#selectContent').empty(); // 기존의 옵션을 제거
                    let defualtOption= `<option value="" selected disabled hidden>선택</option>`;
                    $('#selectContent').append(defualtOption);
                    // 가져온 옵션을 추가 드롭다운 메뉴로 생성하여 추가합니다.
                    $.each(response, function (index, treat) {
                        let option = $('<option>', {
                            value: treat.treatName,
                            text: treat.treatName,
                            'data-price': treat.treatAmount // 데이터 속성으로 가격을 저장합니다.
                        });
                        $('#selectContent').append(option);
                    });
                    $('#selectContent').val(selectedTreat);
                },
                error: function (xhr, status, error) {
                    console.error('Ajax request failed:', error);
                    // 오류 처리를 할 수 있습니다.
                }
            });
        } else {
            let options = '<option>선택</option>';
            $('#selectContent').html(options); // 성별이 선택되지 않은 경우 selectContent를 비웁니다.
        }
    });

    $("#btnSales").on("click", function () {
        const frm = document.addSalesFrm;

        // 입력여부 검사
        if (userInfo === "") {
            alert("회원을 선택해주세요");
            frm.userName.focus();
            return false;
        }

        if (selectedServices.length == 0) {
            alert("시술을 선택해주세요");
            $('#selectContent').focus();
            return false;
        }

        // 결제 방법 선택 확인
        var paymentMethod = document.querySelector('input[name="paymentMethod"]:checked');
        if (!paymentMethod) {
            alert("결제 방법을 선택해주세요");
            return false;
        }

        // 선택된 결제 방법에 따라 결제 금액 입력 필드 확인
        var paymentInput = null;
        if (paymentMethod.value === 'cash') {
            paymentInput = document.getElementById('salesCash');
        } else if (paymentMethod.value === 'credit') {
            paymentInput = document.getElementById('salesCredit');
        }

        if (!paymentInput || !paymentInput.value) {
            alert("결제 금액을 입력해주세요");
            paymentInput.focus();
            return false;
        }

        // 선택된 결제 방법 가져오기
        var paymentMethod = $('input[name="paymentMethod"]:checked').val();

        // 선택된 결제 방법에 따라 해당 입력 필드 가져오기
        var paymentInput = null;
        if (paymentMethod === 'cash') {
            paymentInput = $('#salesCash');
        } else if (paymentMethod === 'credit') {
            paymentInput = $('#salesCredit');
        }

        // 입력된 결제 금액 가져오기
        var enteredAmount = parseFloat(paymentInput.val());

        updateTotalPrice(); // totalSelectedPrice 업데이트
        // 입력된 금액과 totalSelectedPrice 비교
        if (enteredAmount !== totalSelectedPrice) {
            // 입력된 금액과 totalSelectedPrice가 다를 경우 폼 제출 막음
            alert("입력된 결제 금액과 선택된 서비스의 총 가격이 일치하지 않습니다.");
            return false;
        }

        // 폼 제출
        frm.submit();
    });


    $('#addSalesFrm').submit(function (event) {

        // 폼 제출을 막음
        event.preventDefault();

        let userName ="";
        let userIdx=""
        if (userInfo === "비회원") {
            $('#selectedUserIdx').val(0);
            userName="비회원"
        } else {
            $('#selectedUserIdx').val(userIdx);
            let matches = userInfo.match(/\[(\d+)\]\s*(\S+)/);

            // let matches = userInfo.match(/\[(\d+)\](\S+)\((\d+)\)/);
            // userIdx와 userName 변수에 추출된 값 할당
            userIdx = matches[1]; // "[6]"에서 "6" 추출
            $('#selectedUserIdx').val(userIdx);
            userName = matches[2]; // "이기호" 추출
        }


        // 결과 출력
        // 선택된 값 배열을 폼 데이터에 추가xc
        let selectedServicesString = selectedServices.join(', ');

        $(this).append('<input type="hidden" name="content" value="' + selectedServicesString + '">');


        // 시간과 분을 선택한 select 요소에서 값을 추출
        let selectedHour = $('#saleTimeHour').val();
        let selectedMinute = $('#saleTimeMinute').val();

        // 시간과 분을 조합하여 "09:00:00" 형식으로 만듦
        let formattedTime = selectedHour + ':' + selectedMinute + ':00';

        // 폼에 hidden input을 추가하여 시간 데이터를 전송
        $(this).append('<input type="hidden" name="paymentTime" value="' + formattedTime + '">');

        $(this).append('<input type="hidden" name="userName" value="' + userName + '">');



        // 폼 제출
        this.submit();
    });


// 현금 또는 카드 선택 시 이벤트 핸들러
    $('input[name="paymentMethod"]').change(function () {
        // 선택된 결제 방법 가져오기
        var paymentMethod = $(this).val();

        // 선택된 결제 방법에 따라 해당 입력 필드 가져오기
        var paymentInput = null;
        if (paymentMethod === 'cash') {
            paymentInput = $('#salesCash');
            $('#salesCredit').val(""); // 카드 입력 필드 값 제거
        } else if (paymentMethod === 'credit') {
            paymentInput = $('#salesCredit');
            $('#salesCash').val(""); // 현금 입력 필드 값 제거
        }

        updateTotalPrice(); // totalSelectedPrice 업데이트

        // 입력 필드에 totalSelectedPrice 기본값 설정
        paymentInput.val(totalSelectedPrice).trigger('input');

    });

    $('input[name="paymentMethod"]').click(function() {

        // 선택된 결제 방법 가져오기
        var paymentMethod = $(this).val();

        // 선택된 결제 방법에 따라 해당 입력 필드 가져오기
        var paymentInput = null;
        if (paymentMethod === 'cash') {
            paymentInput = $('#salesCash');
            $('#salesCredit').val(""); // 카드 입력 필드 값 제거
        } else if (paymentMethod === 'credit') {
            paymentInput = $('#salesCredit');
            $('#salesCash').val(""); // 현금 입력 필드 값 제거
        }

        updateTotalPrice(); // totalSelectedPrice 업데이트

        paymentInput.val(totalSelectedPrice).trigger('input');
    });

// 입력된 금액이 totalSelectedPrice를 초과하는 경우
    $('input[name="payment"]').on('input', function () {
        var enteredAmount = parseFloat($(this).val()); // 입력된 금액 가져오기

        // 입력된 금액이 totalSelectedPrice를 초과하는 경우
        if (enteredAmount > totalSelectedPrice) {
            // totalSelectedPrice로 입력된 금액 대체
            $(this).val(totalSelectedPrice);
        }
    });
});
