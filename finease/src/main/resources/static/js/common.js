document.addEventListener("DOMContentLoaded", function() {
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
        console.error("Header container not found.");
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
    // Get the current URL
    var currentUrl = window.location.href;
    
    // Get all the <a> elements in the navbar
    var navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    // Loop through each <a> element
    navLinks.forEach(function(navLink) {
        // Get the URL of the <a> element
        var href = navLink.getAttribute('href');
        
        // If the current URL matches the <a> element's URL, add the 'active' class to its parent <li> element
        if (currentUrl.endsWith(href)) {
            navLink.parentNode.classList.add('active');
            // Append the <span class="sr-only">(current)</span> to the <a> element
            navLink.innerHTML += '<span class="sr-only">(current)</span>';
        }
    });
}