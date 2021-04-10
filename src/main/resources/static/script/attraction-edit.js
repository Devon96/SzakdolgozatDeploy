

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

    $('#solution-type option[selected]').each(function (i, x) {

        var str = "div.item[data-value='" + x.value + "']";
        $(str).click();

    });

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
            if(attraction.city.id === city.id){
                option.selected = true;
            }
            option.innerText = city.name;
            citySelect.appendChild(option);

        });

    });
    select.value


}