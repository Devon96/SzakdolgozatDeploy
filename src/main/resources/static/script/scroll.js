const SCROLLEXTENT = 0.8;

function arrowHide(scrollfield, arrowLeft, arrowRight){
    var leftPos = scrollfield.scrollLeft();
    var maxScrollLeft = scrollfield.get(0).scrollWidth - scrollfield.get(0).clientWidth;
    if(leftPos > maxScrollLeft-10){
        arrowRight.hide();
    }else{
        arrowRight.show();
    }
    if(leftPos < 10){
        arrowLeft.hide();
    }else{
        arrowLeft.show();
    }
}

$('document').ready(function () {
    $('.arrowLeft').each(function () {
        $(this).hide();
    });

    $(".arrowLeft").click(function (event) {
        var classname = event.currentTarget.classList[0];
        var leftPos = $('.scrollfield_' + classname).scrollLeft();
        var width = $('.scrollfield_' + classname).get(0).clientWidth * SCROLLEXTENT;
        $(".scrollfield_" + classname).animate({scrollLeft: leftPos - width}, 200);
    });
    $(".arrowRight").click(function (event) {
        var classname = event.currentTarget.classList[0];
        var leftPos = $('.scrollfield_' + classname).scrollLeft();
        var width = $('.scrollfield_' + classname).get(0).clientWidth * SCROLLEXTENT;
        $(".scrollfield_" + classname).animate({scrollLeft: leftPos + width}, 200);
    });
    $('.scrollfield').scroll(function (event) {
        var classname = event.currentTarget.classList[0];
        arrowHide($('.'+event.target.classList[0]), $(".arrowLeft_" + classname), $(".arrowRight_" + classname));
    });

    var img=$("img");
    img.on("contextmenu",function(){return false;});
    img.on("dragstart",function(){return false;});

});
