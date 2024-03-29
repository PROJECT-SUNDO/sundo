window.addEventListener("DOMContentLoaded", function(){

    // setting 버튼 클릭시 해당 관측소 환경설정으로 이동
    const settingBtns = document.querySelectorAll(".settingBtn");

    for(const settingBtn of settingBtns){
        settingBtn.addEventListener("click", function(){
            const seq = settingBtn.dataset.seq;
            const url = settingBtn.dataset.url + seq;
            const type = document.getElementById('type_' + settingBtn.dataset.seq);
            const obscd = document.getElementById('obscd_' + settingBtn.dataset.seq);

            const queryString = '?type=' + type.value + "&&obscd=" + obscd.value;
            location.href=url+queryString;
        });
        /* modal */
        function openObDetailModal() {
            alert("실행좀");
            $('#obDetailModal .modal-content').load("/list/list");
            $('#obDetailModal').modal();
        }
    }

    // info 버튼 클릭시 해당 관측소 정보 확인페이지로 이동
    const infoBtns = document.querySelectorAll(".infoBtn");
    for(const infoBtn of infoBtns) {
        infoBtn.addEventListener("click", function(){
            const seq = infoBtn.dataset.seq;
            const url = infoBtn.dataset.url + seq;
            const type = document.getElementById('type_' + seq);
            const obscd = document.getElementById('obscd_' + seq);

            const queryString = '?type=' + type.value + '&&obscd=' + obscd.value;
            location.href = url + queryString;
        });

    }





})