document.getElementById("editProfileBtn").addEventListener("click", function (event) {
    event.preventDefault(); // Prevent default form submission behavior

    var isEditMode = this.innerText === "Edit";

    if (isEditMode) {
        enableEditMode(); // Enable edit mode
    } else {
        var formData = getFormDataAsJson(); // Get form data as JSON object

        // Send the JSON data to the server using AJAX
        sendJsonData("/profile/update", formData);
    }
});

function enableEditMode() {
    var inputs = document.querySelectorAll("input");
    inputs.forEach(function (input) {
        input.removeAttribute("readonly");
    });

    // Change password input type to 'text'
    document.getElementById("password").setAttribute("type", "text");

    // Change button text to 'Save'
    document.getElementById("editProfileBtn").innerText = "Save";
}

function disableEditMode() {
    var inputs = document.querySelectorAll("input");
    inputs.forEach(function (input) {
        input.setAttribute("readonly", true);
    });

    // Change password input type back to 'password'
    document.getElementById("password").setAttribute("type", "password");

    // Change button text back to 'Edit'
    document.getElementById("editProfileBtn").innerText = "Edit";
}

function getFormDataAsJson() {
    var formData = {};
    var inputs = document.querySelectorAll("input");
    inputs.forEach(function (input) {
        formData[input.name] = input.value;
    });
    return formData;
}

function sendJsonData(url, data) {
    console.log("Sending JSON data to server:", data);
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                console.log("Profile updated successfully");
                disableEditMode();
            } else {
                console.error("Failed to update profile");
            }
        }
    };
    xhr.send(JSON.stringify(data));
}