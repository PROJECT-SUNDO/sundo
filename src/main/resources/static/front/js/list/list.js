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
    }


})