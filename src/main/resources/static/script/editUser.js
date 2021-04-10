var password = document.getElementById("password")
    , confirm_password = document.getElementById("confirm_password");

function validatePassword(){
    if(password.value !== confirm_password.value && document.getElementById("pwrd-check").checked) {
        confirm_password.setCustomValidity("A két jelszó nem egyezik");
    } else {
        confirm_password.setCustomValidity('');
    }
}

password.onchange = validatePassword;
confirm_password.onkeyup = validatePassword;

function hiddenPassword() {
    if(document.getElementById("pwrd-check").checked){
        document.getElementById("password-div").style.display = 'block';
        password.required = true;
        confirm_password.required = true;

    }else{
        document.getElementById("password-div").style.display = 'none';
        password.value = "";
        confirm_password.value = "";
        password.required = false;
        confirm_password.required = false;
    }
}