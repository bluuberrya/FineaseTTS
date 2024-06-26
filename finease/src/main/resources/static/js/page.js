if (document.getElementById("editProfileBtn")) {
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
}

if (document.querySelector("[id^='viewPdfBtn1_']")) {
    document.querySelectorAll("[id^='viewPdfBtn1_']").forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault();
            const pdfUrl = this.getAttribute('data-pdf-url');
            const pdfViewer = document.getElementById('pdf-viewer1');
            pdfViewer.setAttribute('src', pdfUrl);
            history.pushState({}, '', '/history?pdf=True');
        });
    });
}

if (document.querySelector("[id^='viewPdfBtn2_']")) {
    document.querySelectorAll("[id^='viewPdfBtn2_']").forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault();
            const pdfUrl = this.getAttribute('data-pdf-url');
            const pdfViewer = document.getElementById('pdf-viewer2');
            pdfViewer.setAttribute('src', pdfUrl);
            history.pushState({}, '', '/history?pdf=True');
        });
    });
}

function enableEditMode() {
    var inputs = document.querySelectorAll("input");
    inputs.forEach(function (input) {
        input.removeAttribute("readonly");
    });

    document.getElementById("password").setAttribute("type", "text");
    document.getElementById("editProfileBtn").innerText = "Save";
}

function disableEditMode() {
    var inputs = document.querySelectorAll("input");
    inputs.forEach(function (input) {
        input.setAttribute("readonly", true);
    });

    document.getElementById("password").setAttribute("type", "password");
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
