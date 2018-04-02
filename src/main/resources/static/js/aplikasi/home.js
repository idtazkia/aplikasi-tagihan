function displayAppVersion(){
    axios.get('/actuator/info')
        .then(function (response) {
            console.log(response);
            console.log("App Version : "+response.data.git.build.version);
            $("#appversion").html(response.data.git.build.version);
        })
        .catch(function (error) {
            console.log(error);
        });
}

$(document).ready(function() {
    displayAppVersion();
});