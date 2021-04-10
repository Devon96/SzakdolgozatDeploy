let map;
let service;
let infowindow;
var querySTR
var mapResults;
var opening;


$('document').ready(function(){
    getRating();
});

function beforeInitMap() {
    querySTR = $('#country-name').text();
    querySTR += ' ';
    querySTR += $('#city-name').text();
    querySTR += ' ';
    querySTR += $('#attraction-name').text();
    initMap();
}
function initMap() {

    infowindow = new google.maps.InfoWindow();
    map = new google.maps.Map(document.getElementById("map"), {
        zoom: 17,
    });
    const request = {
        query: querySTR,
        fields: ["place_id", "name", "geometry"],
    };

    service = new google.maps.places.PlacesService(map);
    service.findPlaceFromQuery(request, (results, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK) {
                mapResults = results[0];
                createMarker(mapResults);
            map.setCenter(results[0].geometry.location);
            initOpening();

        }
    });


}

function initOpening() {

    const request2 = {
        placeId: mapResults.place_id,
        fields: ["opening_hours"],
    };
    var table = document.getElementById("opening_hours");
    table.innerHTML = "";
    service.getDetails(request2, (place, status) => {
        if (status === google.maps.places.PlacesServiceStatus.OK && place.opening_hours !== undefined) {
            place.opening_hours.weekday_text.forEach(data => {
                var line = data.split(" ");

                table.innerHTML += '<tr><th>' + line[0] + '</th><th>' + ((line[1] !== undefined)? line[1] : '' ) + '</th></tr>';

            });

            if(place.opening_hours.open_now){
                document.getElementById("open-now").innerText = "Nyitva";
                document.getElementById("open-now").style.color = "green"
            }else {
                document.getElementById("open-now").innerText = "Zárva";
                document.getElementById("open-now").style.color = "red"
            }



        }else{
            document.getElementById("opening").style.display = 'none';
        }
    });

}

function createMarker(place) {
    const marker = new google.maps.Marker({
        map,
        position: place.geometry.location,
    });
    google.maps.event.addListener(marker, "click", () => {
        infowindow.setContent(place.name);
        infowindow.open(map);
    });
}

function addRating(rating) {
    var url = document.getElementById("url").getAttribute("href");
    url += "/" + rating;
    $.ajax({method : "GET", url})
        .done(function (data) {
            getRating();
            if(data !== true){
                alert("Az értékeléshez be kell jelentkezni");

                $(".radio-button").each(function () {
                    this.checked = false;
                })
            }
        });

}



function getRating() {
    var url = document.getElementById("url-to-get-ratings").getAttribute("href");
    $(".rating-td").each(function () {
        this.innerText = '0';
    })
    let ratingsum = 0;
    let ratingcount = 0;

    $.ajax({method : "GET", url})
        .done(function (data) {
            data.forEach(i => {
                ratingsum += ( i[0] * i[1] );
                ratingcount += i[1];
                if(i[0] === 1){
                    $("#ratings1").text(i[1]);
                }else if (i[0] === 2){
                    $("#ratings2").text(i[1]);
                }else if (i[0] === 3){
                    $("#ratings3").text(i[1]);
                }else if (i[0] === 4){
                    $("#ratings4").text(i[1]);
                }else if (i[0] === 5){
                    $("#ratings5").text(i[1]);
                }
            });

            if(ratingcount !== 0){
                $("#rating-avg").text((ratingsum/ratingcount).toFixed(2));
            }else{
                $("#rating-avg").text(0);
            }
        })

}

