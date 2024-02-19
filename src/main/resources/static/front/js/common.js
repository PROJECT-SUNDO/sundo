window.addEventListener("DOMContentLoaded",function() {
/* 양식 공통 처리 S*/
    const formActions = document.getElementsByClassName("form_action");
    for(const el of formActions) {
        el.addEventListener("click"),function() {
            const dataset = this.dataset;
            const mode = dataset.mode || "update";
            const formName = dataset.formName;
            if (!formName || !document[formName]) {
                return;
            }
            const formEl = document[formName];
            formEl._method.value = mode == 'delete' ? 'DELETE' : 'PATCH';

            cost modeTitle = mode == 'delete' ? '삭제' : '수정';

            const chks = formEl.querySelectorAll("input[name='chk']:checked");

            if (chks.length == 0) {
                alert(`${modeTitle}할 항목을 체크하세요.`);
                return;
            }

            if (confirm(`정말 ${modeTitle} 하겠습니까?`)) {
                formEl.submit();
            }
        });
    }
    /* 양식 공통 처리 E */
});
