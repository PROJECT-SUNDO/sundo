window.addEventListener("DOMContentLoaded", function(){
    const pageBtns = document.querySelectorAll(".page-link button")

    for(const el of pageBtns){
        el.addEventListener("click", function(){
            const url = this.dataset.url;
            location.replace(url);
        })

        if(window.location.pathname === el.dataset.url){
            el.classList.add("on");
        }
    }



});