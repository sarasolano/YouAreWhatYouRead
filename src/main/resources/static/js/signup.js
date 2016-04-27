document.addEventListener("DOMContentLoaded", function() {
        var form = document.getElementById('sign-in');
    form.addEventListener('submit', function(e) {
         e.preventDefault(); 
         var pw = this.pwd.value;

        if (this.username.value.length == 0) {
             document.getElementById("username-err").style.display = "block";
         } else if (pw.length < 8 || pw.length > 36) {
            document.getElementById("pwd-err").style.display = "block";
        } else {
            var user = JSON.stringify(this.username.value);
            var pass = JSON.stringify(pw);
            var postParameters = {username: user, password: pass};

            $.post("/signup", postParameters); 
        }
    });
});