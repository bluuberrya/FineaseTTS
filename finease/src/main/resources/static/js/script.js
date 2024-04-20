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

document.addEventListener("DOMContentLoaded", function () {
    // get fetch accessibility widget
    fetch('accesswidget')
        .then(response => response.text())
        .then(widgetHtml => {
            // Append the accessibility widget HTML to the specified container
            document.getElementById('accessibility-widget').innerHTML = widgetHtml;
            initializeTTS();
        });
});


function highlightCurrentPage() {
    // Get the current URL
    var currentUrl = window.location.href;

    // Get all the <a> elements in the navbar
    var navLinks = document.querySelectorAll('.navbar-nav .nav-link');

    // Loop through each <a> element
    navLinks.forEach(function (navLink) {
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


function initializeTTS() {
    const voiceList = document.getElementById("voiceList"),
        speechBtn = document.getElementById("speechBtn"), // Start/Pause button
        screenReaderBtn = document.getElementById("screenReaderBtn"); // Toggle button

    let synth = window.speechSynthesis,
        isSpeaking = false,
        isScreenReaderActive = false;

    checkScreenReaderState();

    // Listen for the 'voiceschanged' event before calling the 'voices()' function
    synth.addEventListener("voiceschanged", voices);

    // Set the selected voice from localStorage or default to Google US English
    voiceList.value = localStorage.getItem("selectedVoice") || "Google US English";

    // Update the selected voice in localStorage when the user changes it
    voiceList.addEventListener("change", function () {
        localStorage.setItem("selectedVoice", this.value);
        textToSpeech(`Selected ${this.value}`);
    });

    // Function to handle mouseover event on screen reader button
    screenReaderBtn.addEventListener("mouseover", () => {
        let hoverText = isScreenReaderActive ? "Click to Deactivate Screen Reader" : "Click to Activate Screen Reader";
        textToSpeech(hoverText);
    });

    speechBtn.addEventListener("click", () => {
        playaudio("/audio/toggle_on.mp3");
        pause();

    });

    // State
    // Call toggleScreenReader when the screenReaderBtn is clicked
    screenReaderBtn.addEventListener("click", () => {
        if (!isScreenReaderActive && !isSpeaking) {
            playaudio("/audio/toggle_on.mp3");
            setTimeout(function() {
                textToSpeech("Screen Reader Activated");
            }, 300);
        }
        if (isScreenReaderActive && isSpeaking) {
            playaudio("/audio/toggle_off.mp3");
            setTimeout(function() {
                textToSpeech("Screen Reader Deactivated");
            }, 300);
        }
        toggleScreenReader();
    });

    // // Event listener to handle pressing of the F key
    // document.addEventListener("keydown", (event) => {
    //     if (event.code === "KeyF") {
    //         fKeyDownTime = Date.now(); // Record the current timestamp
    //     }
    // });

    // // Event listener to handle release of the F key after a long press
    // document.addEventListener("keyup", (event) => {
    //     if (event.code === "KeyF" && fKeyDownTime !== null && Date.now() - fKeyDownTime >= 1000) {
    //         toggleScreenReader(); // Toggle the screen reader
    //         fKeyDownTime = null; // Reset the timestamp
    //     }
    // });

    document.getElementById("clearStateBtn").addEventListener("click", function () {
        if (localStorage.getItem("screenReaderState") === "active") {
            toggleScreenReader(); // Deactivate screen reader if it was active
        }
        localStorage.setItem("selectedVoice", "Google US English");
        localStorage.removeItem("screenReaderState");
        playaudio("/audio/munch.mp3");
        textToSpeech("Settings Reset");
    });


    function voices() {
        // Check if synth.getVoices() is not null before iterating over it
        if (synth.getVoices() !== null) {
            // Clear the options in the voiceList dropdown
            voiceList.innerHTML = "";
            for (let voice of synth.getVoices()) {
                //let selected = voice.name === "Google US English" ? "selected" : "";
                let selected = voice.name === localStorage.getItem("selectedVoice") ? "selected" : "";
                let option = `<option value="${voice.name}" ${selected}>${voice.name} (${voice.lang})</option>`;
                voiceList.insertAdjacentHTML("beforeend", option);
            }
        }
    }

    // Function to check the screen reader state and activate it if necessary
    function checkScreenReaderState() {
        if (localStorage.getItem("screenReaderState") === "active") {
            toggleScreenReader(); // Activate screen reader if it was active
        } else {
            speechBtn.style.display = "none"; // Hide the speech button
        }
    }

    function toggleScreenReader() {
        isScreenReaderActive = !isScreenReaderActive;
        if (isScreenReaderActive) {
            addHoverListenersToElements();
            addClickListenersToElements();
            screenReaderBtn.textContent = "Deactivate";
            speechBtn.style.display = "inline"; // Show the speech button
            voiceList.style.display = "inline";
            localStorage.setItem("screenReaderState", "active"); // Save state in local storage
            // speakPageName();
            handleErrorMessage();
        } else {
            removeHoverListenersFromElements();
            removeClickListenersFromElements();
            screenReaderBtn.textContent = "Activate";
            speechBtn.style.display = "none"; // Hide the speech button
            voiceList.style.display = "none";
            localStorage.removeItem("screenReaderState"); // Remove state from local storage
        }
    }

    function textToSpeech(text) {
        let utterance = new SpeechSynthesisUtterance(text);
        for (let voice of synth.getVoices()) {
            if (voice.name === voiceList.value) {
                utterance.voice = voice;
                break;
            }
        }
        synth.speak(utterance);
    }

    // Function to speak the current page name
    function speakPageName() {
        let pageText = "";
        playaudio("/audio/pop.mp3");
        setTimeout(function() {
            pageText = `You are now at ${document.title} page`;
    
            if (pageText !== "" && !synth.speaking) {
                textToSpeech(pageText);
                isSpeaking = true;
            }
        }, 800);
    }

    function handleErrorMessage() {
        // Check if there's an error parameter in the URL
        const urlParams = new URLSearchParams(window.location.search);
        const error = urlParams.get('error');
        
        if (error) {
            playaudio("/audio/error.mp3");
            setTimeout(function() {
                if (error !== "" && !synth.speaking) {
                    textToSpeech("Input invalid, please try again");
                    isSpeaking = true;
                }
            }, 800);
        }else{
            speakPageName();
        }
    }

    function addHoverListenersToElements() {
        let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt], select');
        elements.forEach(element => {
            element.addEventListener("mouseenter", hoverEventListener);
            element.addEventListener("mouseleave", pause);
        });
    }

    function addClickListenersToElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.addEventListener("click", clickEventListener);
        });
    }

    function removeHoverListenersFromElements() {
        let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt], select');
        elements.forEach(element => {
            element.removeEventListener("mouseenter", hoverEventListener);
            element.removeEventListener("mouseleave", pause);
        });
    }

    function removeClickListenersFromElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.removeEventListener("click", clickEventListener);
        });
    }

    function clickEventListener() {
        let itemText = this.textContent;
        let elementType = this.tagName.toLowerCase();
        let clickText = "";

        // Determine the appropriate click text based on the element type
        if (elementType === "a") {
            clickText = `click ${itemText}`;
        } else if (elementType === "textarea") {
            clickText = `click ${itemText} text area`;
        } else if (elementType === "input") {
            clickText = `click ${itemText} ${this.name} input field`;
        } else if (elementType === "button") {
            clickText = `click ${itemText} button`;
        } else {
            clickText = `click ${itemText}`;
        }
        pause();
        // Speak the click event if it's not empty
        if (clickText !== "" && !synth.speaking) {
            textToSpeech(clickText);
            isSpeaking = true;
        }
    }

    function hoverEventListener() {
        let itemText = this.textContent;
        let elementType = this.tagName.toLowerCase();
        let hoverText = "";

        // Determine the appropriate hover text based on the element type
        if (elementType === "img") {
            hoverText = this.alt;
        } else if (elementType === "textarea") {
            hoverText = `${itemText} text area`;
        } else if (elementType === "input") {
            if (this.readOnly) {
                hoverText = `${this.name}, ${this.value}`;
            } else {
                hoverText = `${itemText} ${this.name} input field`;
            }
        } else if (elementType === "button") {
            hoverText = `${itemText} button`;
        } else if (elementType === "select") {
            hoverText = `${this.name}, currently selected${this.value}`;

        } else {
            hoverText = `${itemText}`;
        }

        // Set the text to be spoken if it's not empty
        if (hoverText !== "" && !synth.speaking) {
            textToSpeech(hoverText);
            isSpeaking = true;
        }
    }
    
    function pause() {
        if (!isSpeaking) {
            let hoveredText = window.getSelection().toString().trim();
            if (hoveredText !== "") {
                textToSpeech(hoveredText);
                isSpeaking = true;
            }
        } else {
            synth.cancel();
            isSpeaking = false;
        }
    }

    function playaudio(audioPath) {
        var sound = new Audio(audioPath);
        sound.volume = 0.8;
        sound.play();
    }
}