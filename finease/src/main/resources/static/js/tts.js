const voiceList = document.getElementById("voiceList"),
      speechBtn = document.getElementById("speechBtn"), // Start/Pause button
      screenReaderBtn = document.getElementById("screenReaderBtn"); // Toggle button

let synth = window.speechSynthesis,
    isSpeaking = false,
    isScreenReaderActive = false;
    fKeyDownTime = null;

// Listen for the 'voiceschanged' event before calling the 'voices()' function
synth.addEventListener("voiceschanged", voices);

function voices() {
    // Check if synth.getVoices() is not null before iterating over it
    if (synth.getVoices() !== null) {
        for (let voice of synth.getVoices()) {
            let selected = voice.name === "Google US English" ? "selected" : "";
            let option = `<option value="${voice.name}" ${selected}>${voice.name} (${voice.lang})</option>`;
            voiceList.insertAdjacentHTML("beforeend", option);
        }
    }
}

function toggleScreenReader() {
    isScreenReaderActive = !isScreenReaderActive;
    if (isScreenReaderActive) {
        addHoverListenersToElements();
        addClickListenersToElements();
        screenReaderBtn.textContent = "Deactivate";
        speechBtn.style.display = "inline"; // Show the speech button
        localStorage.setItem("screenReaderState", "active"); // Save state in local storage
        speakPageName();
    } else {
        removeHoverListenersFromElements();
        removeClickListenersFromElements();
        screenReaderBtn.textContent = "Activate";
        speechBtn.style.display = "none"; // Hide the speech button
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
    let currentPage = document.title; // Get the title of the current page
    playaudio("/audio/pop.mp3");
    textToSpeech(`You are now at ${currentPage} page`);
}

// Function to handle mouseover event on screen reader button
screenReaderBtn.addEventListener("mouseover", () => {
    let hoverText = isScreenReaderActive ? "Click to Deactivate Screen Reader" : "Click to Activate Screen Reader";
    textToSpeech(hoverText);
});

function addHoverListenersToElements() {
    let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt]');
    elements.forEach(element => {
        element.addEventListener("mouseenter", hoverEventListener);
    });
}

function addClickListenersToElements() {
    let elements = document.querySelectorAll('a, input, textarea, button');
    elements.forEach(element => {
        element.addEventListener("click", clickEventListener);
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

    // Speak the click event if it's not empty
    if (clickText !== "" && !synth.speaking) {
        textToSpeech(clickText);
        isSpeaking = true;
    }
}

function removeHoverListenersFromElements() {
    let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt]');
    elements.forEach(element => {
        element.removeEventListener("mouseenter", hoverEventListener);
    });
}

function removeClickListenersFromElements() {
    let elements = document.querySelectorAll('a, input, textarea, button');
    elements.forEach(element => {
        element.removeEventListener("click", clickEventListener);
    });
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
        hoverText = `${itemText} ${this.name} input field`;
    } else if (elementType === "button") {
        hoverText = `${itemText} button`;
    } else {
        hoverText = `${itemText}`;
    }

    // Set the text to be spoken if it's not empty
    if (hoverText !== "" && !synth.speaking) {
        textToSpeech(hoverText);
        isSpeaking = true;
    }
}




speechBtn.addEventListener("click", () => {
    if (!isSpeaking) {
        let hoveredText = window.getSelection().toString().trim();
        if (hoveredText !== "") {
            textToSpeech(hoveredText);
            isSpeaking = true;
        }
    } else {
        synth.cancel();
        isSpeaking = false;
        playaudio("/audio/toggle_on.mp3");
    }
});

//JavaScript function to play sound  
function playaudio(audioPath) {   
    var sound = new Audio(audioPath);
    // sound.volume = 0.5;
    sound.play();   
}


// State
// Call toggleScreenReader when the screenReaderBtn is clicked
screenReaderBtn.addEventListener("click", () => {
    if (!isScreenReaderActive && !isSpeaking) {
        textToSpeech("Screen Reader Activated");
        playaudio("/audio/toggle_on.mp3");
    }
    if (isScreenReaderActive && isSpeaking) {
        textToSpeech("Screen Reader Deactivated");
        playaudio("/audio/toggle_off.mp3");
    }
    toggleScreenReader();
});

// Event listener to handle pressing of the F key
document.addEventListener("keydown", (event) => {
    if (event.code === "KeyF") {
        fKeyDownTime = Date.now(); // Record the current timestamp
    }
});

// Event listener to handle release of the F key after a long press
document.addEventListener("keyup", (event) => {
    if (event.code === "KeyF" && fKeyDownTime !== null && Date.now() - fKeyDownTime >= 1000) {
        toggleScreenReader(); // Toggle the screen reader
        fKeyDownTime = null; // Reset the timestamp
    }
});



// Function to check the screen reader state and activate it if necessary
function checkScreenReaderState() {
    if (localStorage.getItem("screenReaderState") === "active") {
        toggleScreenReader(); // Deactivate screen reader if it was active
    }else{
        speechBtn.style.display = "none"; // Hide the speech button
    }
}

// Call the function to check the screen reader state when the page loads
window.addEventListener("load", checkScreenReaderState);

document.getElementById("clearStateBtn").addEventListener("click", function() {
    if (localStorage.getItem("screenReaderState") === "active") {
        toggleScreenReader(); // Deactivate screen reader if it was active
    }
    localStorage.removeItem("screenReaderState");
    playaudio("/audio/munch.mp3");
    textToSpeech("Setting Resetted");
});