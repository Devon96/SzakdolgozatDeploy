
function updateCities(){
    var search = document.getElementById('search');
    var input_options = document.getElementById('input_options');

    input_options.hidden = search.value === '';

    $.get('/api/get-cities-and-countries-by-name/' + search.value, function (data) {

        input_options.innerHTML = '';

        if(data.length === 0){
            input_options.hidden = true;
            return;
        }

        $.each(data, function (i, hely) {

            let a = document.createElement('a');
            console.log(hely);

            if(hely.orszag){
                a.setAttribute('href', '/country/' + hely.countryid);
            }else{
                a.setAttribute('href', '/city/' + hely.id);
            }

            a.classList.add('searchoption');

            let h6 = document.createElement('h6');

            if(hely.orszag){
                h6.innerText = hely.name;
            }else{
                h6.innerText = hely.countryname + " - " + hely.name;
            }

            a.appendChild(h6);
            input_options.appendChild(a);

        });

    });
}

function goCity() {

    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(successFunction, errorFunction);
    }

    function successFunction(position) {
        var lat = position.coords.latitude;
        var lng = position.coords.longitude;
        let str = 'https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=' + lat + '&longitude=' + lng + '&localityLanguage=hu'
        $.get(str, function (data) {
            console.log(data);
            var url = document.getElementById("go-to-city").getAttribute("href");

            url = url + "?countryName=" + data.countryName + "&" + "cityName=" + data.city;

            location.replace(url);
        });

    }

    function errorFunction(){
        alert("Nincs helymeghatározás");
    }




}




