$("#menu-toggle").click(function(e) {
    e.preventDefault();
    $("#wrapper").toggleClass("toggled");
});


var myPosition = null;
var options;
var attractionName;
var countryDict = {};
var countryIdDict = {};
var url = "/api/get-saved-attractions-for-user";
var routeInfo;

var deleteAttr;
var attrNameA;

function initMap() {

    options = document.getElementById("options");
    attractionName = document.getElementById("attraction-name");
    deleteAttr = document.getElementById("delete-attr");
    attrNameA = document.getElementById("attr-name-a");
    routeInfo = document.getElementById("routeInfo");

    options.innerHTML = "";
    countryDict = {};
    countryIdDict = {};



    $.get(url)
        .done(function (data) {
            data.forEach( i => {

                if(countryDict[i.city.country.name] === undefined){
                    countryDict[i.city.country.name] = [];
                    countryIdDict[i.city.country.name] = i.city.country.id;
                }
                countryDict[i.city.country.name].push(i);
            });
            if(Object.keys(countryDict).length === 0){
                attractionName.innerText = "Nincs elmentve látnivaló";
            }else{
                createOptions();
            }
        }).fail(function () {
            alert("Hiba a szerver kapcsolatban")
    });
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(setMyPosition);
    }
}

function createOptions(){

    var isFirst = false;

    Object.keys(countryDict).forEach(name => {
        var country = document.createElement("a");
        country.href = "#";
        country.classList.add("list-group-item", "optionlist", "country");
        country.innerText = name;
        country.onclick = function() {

            deleteAllSelectedClass();
            showCountry(name);
            this.classList.add("selected");
            deleteAttr.style.display = "none";

        };
        deleteAttr.style.display = "none";
        options.appendChild(country);

        countryDict[name].forEach(attraction => {
            createAttraction(attraction);
        });

        if(! isFirst){
            country.onclick(undefined);
            isFirst = true;
        }

    });
}

function createAttraction(j){
    var a = document.createElement("a");
    a.href = "#";
    a.classList.add("list-group-item", "optionlist");
    a.innerText = j.name;
    a.onclick = function() {
        deleteAllSelectedClass();
        showAttraction(j);
        this.classList.add("selected");

        deleteAttr.style.display = "block";
        deleteAttr.onclick = function () {
            removeAttr(j.id);
        }
    };
    options.appendChild(a);
}

function deleteAllSelectedClass() {
    $(".selected").each(function () {
        this.classList.remove("selected");
    });
}

function showAttraction(attraction){

    attractionName.innerText = attraction.name;
    attrNameA.href = "/attraction/" + attraction.id;
    createRoute(myPosition, attraction.city.country.name + " " + attraction.city.name + " " + attraction.name);
}

function showCountry(country){

    attractionName.innerText = country;
    attrNameA.href = "/country/" + countryIdDict[country];
    var attractionList = [];

    countryDict[country].forEach(a => {
        attractionList.push({
            location: a.city.country.name + " " + a.city.name + " " + a.name,
            stopover: true,
        });
    });

    var start = attractionList.pop();

    createRoute( myPosition, start.location, attractionList );

}

function setMyPosition( position ) {
    myPosition = { lat: position.coords.latitude, lng: position.coords.longitude };

}


function createRoute(from, to, waypts = []) {

    const directionsService = new google.maps.DirectionsService();
    const directionsRenderer = new google.maps.DirectionsRenderer();
    const map = new google.maps.Map(document.getElementById("map"));
    directionsRenderer.setMap(map);

    directionsService.route(
        {
            origin: from,
            destination: to,
            waypoints: waypts,
            optimizeWaypoints: true,
            avoidFerries: true,
            travelMode: google.maps.TravelMode.DRIVING,
        },
        (response, status) => {
            if (status === "OK") {
                directionsRenderer.setDirections(response);
                const route = response.routes[0];
                routeInfo.innerHTML = '<hr/>';
                for (let i = 0; i < route.legs.length; i++) {
                    var waypt = route.legs[i];
                    routeInfo.innerHTML += "<div class='row'>";
                    var address = waypt.end_address.substring(0, waypt.end_address.lastIndexOf(","));
                    routeInfo.innerHTML += "<div class='col-12'>" + address + "</div>";
                    routeInfo.innerHTML += "<div class='col-12'>" + waypt.distance.text + " : " + waypt.duration.text + "</div>";
                    routeInfo.innerHTML += "<hr/></div>";
                }
            } else {
                window.alert("Az adott országba nem lehet útvonalat tervezni.");
            }
        }
    );
}

function removeAttr(attrId) {
    var a = "/api/remove-attraction-for-user/" + attrId;

    $.ajax({method : "GET", url : a})
        .done(function (data) {
            initMap();

        });

}