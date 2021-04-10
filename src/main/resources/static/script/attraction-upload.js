$(".addImage").click(function(){
    $(this).closest(".row").find('.addImage').before('<div class="col-sm-3 imgUp"><div class="imagePreview"></div><label class="btn btn-primary">Feltöltés<input name="attractionImage" type="file" class="uploadFile img" value="Upload Photo" style="width:0px;height:0px;overflow:hidden;"></label><b class="fa fa-times removeImage">X</b></div>');
});
$(document).on("click", "b.removeImage" , function() {
    $(this).parent().remove();
});
$(function() {
    $(document).on("change",".uploadFile", function()
    {
        var uploadFile = $(this);
        var files = !!this.files ? this.files : [];
        if (!files.length || !window.FileReader) return; // no file selected, or no FileReader support

        if (/^image/.test( files[0].type)){ // only image file
            var reader = new FileReader(); // instance of the FileReader
            reader.readAsDataURL(files[0]); // read the local file

            reader.onloadend = function(){ // set image data as background of div
                uploadFile.closest(".imgUp").find('.imagePreview').css("background-image", "url("+this.result+")");
            }
        }

    });
});



var categori;
function myFunction() {
    var checkBox = document.getElementById("newCategory");
    var newCategoryInput = document.getElementById("newCategoryInput");
    var categories = document.getElementById("categories");
    var categori = document.getElementById("category");
    var categoryTextField = document.getElementById("categoryTextField");
    if (checkBox.checked === true){
        newCategoryInput.style.display = "block";
        categories.style.display = "none";
        categoryTextField.value = "";
        categoryTextField.required = false;
        categori.required = true;


    } else {
        newCategoryInput.style.display = "none";
        categories.style.display = "block";
        categori.value = "";
        categoryTextField.required = true;
        categori.required = false;
    }
}
$(document).ready(function () {
    categori = document.getElementById("category");
    categori.value = "";
});


$('select.dropdown').dropdown({
    label: {
        duration: 0,
    },
    debug: true,
    performance: true,
});
$('.ui.checkbox')
    .checkbox({
        onChecked() {
            const options = $('#members_dropdown > option').toArray().map(
                (obj) => obj.value
            );
            $('#members_dropdown').dropdown('set exactly', options);
        },
        onUnchecked() {
            $('#members_dropdown').dropdown('clear');
        },
    });

$(document).ready(function () {
    $('.ui.dropdown')
        .dropdown({
            allowAdditions: true
        })
    ;

    updateCities();
});



//Városok merő feltöltése

function updateCities(){
    var select = document.getElementById('country');
    var citySelect = document.getElementById('city');

    $.get('/api/get-cities-by-country-id/' + select.value, function (data) {

        citySelect.innerHTML = '';

        $.each(data, function (i, city) {

            let option = document.createElement('option');
            option.setAttribute('value', city.id);
            option.innerText = city.name;
            citySelect.appendChild(option);

        });

    });
    select.value


}