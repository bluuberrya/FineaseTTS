document.addEventListener("DOMContentLoaded", function () {
    var headerType;
    var headerContainerId;

    // Determine header type based on the existence of element with id "main-header-container", "user-header-container", or "admin-header-container"
    if (document.getElementById("main-header-container")) {
        headerType = 'main';
        headerContainerId = 'main-header-container';
    } else if (document.getElementById("user-header-container")) {
        headerType = 'user';
        headerContainerId = 'user-header-container';
    } else if (document.getElementById("admin-header-container")) {
        headerType = 'admin';
        headerContainerId = 'admin-header-container';
    } else {
        console.log("Header container not found.");
        return; // Stop execution if header container is not found
    }

    // Fetch the corresponding header based on the determined header type
    fetch(headerType + 'header')
        .then(response => response.text())
        .then(html => {
            document.getElementById(headerContainerId).innerHTML = html;
            highlightCurrentPage();
        });
});


function highlightCurrentPage() {
    var currentUrl = window.location.href;
    var navBarLinks = document.querySelectorAll('.navbar-nav .nav-link');

    navBarLinks.forEach(function (navLink) {
        var href = navLink.getAttribute('href');

        if (currentUrl.endsWith(href)) {
            navLink.parentNode.classList.add('active');
            navLink.innerHTML += '<span class="sr-only">(current)</span>';
        }
    });
}

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

// For current transactions pdf
if (document.querySelector("[id^='viewPdfBtn1_']")) {
    document.querySelectorAll("[id^='viewPdfBtn1_']").forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault(); // Prevent default form submission behavior
            const pdfUrl = this.getAttribute('data-pdf-url');
            const pdfViewer = document.getElementById('pdf-viewer1');
            pdfViewer.setAttribute('src', pdfUrl);
            history.pushState({}, '', '/history?pdf=True');
        });
    });
}

// For savings transactions pdf
if (document.querySelector("[id^='viewPdfBtn2_']")) {
    document.querySelectorAll("[id^='viewPdfBtn2_']").forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault(); // Prevent default form submission behavior
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
