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

function initializeTTS() {
    const voiceList = document.getElementById("voiceList"),
        speechBtn = document.getElementById("speechBtn"), // Start/Pause button
        screenReaderBtn = document.getElementById("screenReaderBtn"); // Toggle button

    let synth = window.speechSynthesis,
        isSpeaking = false,
        isScreenReaderActive = false;

    checkScreenReaderState();

    synth.addEventListener("voiceschanged", voices);

    voiceList.value = localStorage.getItem("selectedVoice") || "Google US English";

    voiceList.addEventListener("change", function () {
        localStorage.setItem("selectedVoice", this.value);
        textToSpeech(`Selected ${this.value}`);
    });

    // Function to handle mouseover event on screen reader button
    screenReaderBtn.addEventListener("mouseover", () => {
        let hoverText = isScreenReaderActive ? "Click to Deactivate Screen Reader" : "Click or Press Shift to Activate Screen Reader";
        textToSpeech(hoverText);
    });

    speechBtn.addEventListener("click", () => {
        playaudio("/audio/toggle_on.mp3");
        pause();
    });

    // Call toggleScreenReader when the screenReaderBtn is clicked
    screenReaderBtn.addEventListener("click", () => {
        if (!isScreenReaderActive && !isSpeaking) {
            playaudio("/audio/toggle_on.mp3");
            setTimeout(function () {
                textToSpeech("Screen Reader Activated");
            }, 400);
        }
        if (isScreenReaderActive && !isSpeaking) {
            playaudio("/audio/toggle_off.mp3");
            setTimeout(function () {
                textToSpeech("Screen Reader Deactivated");
            }, 400);
        }
        toggleScreenReader();
    });

    document.addEventListener("keydown", function (event) {
        if (event.key === "Shift" && localStorage.getItem("IOBusy") !== "active") {
            if (!isScreenReaderActive && !isSpeaking) {
                playaudio("/audio/toggle_on.mp3");
                setTimeout(function () {
                    textToSpeech("Screen Reader Activated");
                }, 400);
            }
            if (isScreenReaderActive && !isSpeaking) {
                playaudio("/audio/toggle_off.mp3");
                setTimeout(function () {
                    textToSpeech("Screen Reader Deactivated");
                }, 400);
            }
            toggleScreenReader();
        }
    });

    if (localStorage.getItem("screenReaderState") === "active") {
        document.addEventListener("keydown", function (event) {
            if (localStorage.getItem("IOBusy") !== "active" && !synth.speaking) {
                switch (event.key) {
                    case "z":
                        checkCursorPosition();
                        isSpeaking = true;
                        break;
                    case "x":
                        speakPageStructure();
                        break;
                    case " ":
                        event.preventDefault();
                        speakPageContent();
                        break;
                    case "`":
                        readNavigationOptions();
                        break;
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                    case "5":
                        navigateToLink(event.key);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    function speakPageContent() {
        const metaDescription = document.querySelector('meta[name="description"]');
        const descriptionContent = metaDescription ? metaDescription.getAttribute('content') : '';
        textToSpeech(descriptionContent);
    }

    function speakPageStructure() {
        let pageStructure = generatePageStructure();
        textToSpeech(pageStructure);
    }

    function generatePageStructure() {
        let pageStructure = "The page structure is:\n";
        let navbarElements = Array.from(document.querySelectorAll('.navbar-nav .nav-link')).map(link => link.textContent.trim());
        let contentElements = [];
        let accessibilityWidgetElements = [];
        let elements = document.querySelectorAll('select, input, button, a, .fa-chevron-left');

        function isScrollable() {
            return document.body.scrollHeight > document.documentElement.clientHeight ||
                document.documentElement.scrollHeight > document.documentElement.clientHeight;
        }
    
        if (isScrollable()) {
            pageStructure += "The page is scrollable.\n";
        } else {
            pageStructure += "The page is not scrollable.\n";
        }

        elements.forEach(element => {
            let tagName = element.tagName.toLowerCase();
            let textContent = element.textContent.trim();
            let name = element.getAttribute('name');

            if (tagName === 'select') {
                if (element.id === 'voiceList') {
                    accessibilityWidgetElements.push("voice selection");
                } else {
                    if (textContent !== "") {
                        contentElements.push(textContent + " (select)");
                    }
                }
            } else if (tagName === 'input') {
                contentElements.push(name + " (input)");
            } else if (tagName === 'button') {
                if (!element.closest('#accessibility-widget')) {
                    if (textContent !== "") {
                        contentElements.push(textContent + " (button)");
                    }
                } else {
                    if (textContent !== "") {
                        accessibilityWidgetElements.push(textContent + " (button)");
                    }
                }
            } else if (tagName === 'a') {
                if (!element.closest('.navbar-nav') && !element.closest('#accessibility-widget')) {
                    if (textContent !== "") {
                        contentElements.push(textContent + " (link)");
                    }
                } else if (element.closest('#accessibility-widget')) {
                    if (textContent !== "") {
                        accessibilityWidgetElements.push(textContent + " (link)");
                    }
                }
            }
        });

        if (navbarElements.length > 0) {
            pageStructure += "Top navigation bar consists of: " + navbarElements.join(". ") + ".\n";
        }

        if (contentElements.length > 0) {
            pageStructure += "Center content section consists of: " + contentElements.join(". ") + ".\n";
        }

        if (accessibilityWidgetElements.length > 0) {
            pageStructure += "Bottom left Accessibility widget consists of: navigation guide. " + accessibilityWidgetElements.join(". ") + ".\n";
        }

        return pageStructure;
    }


    function navigateToLink(key) {
        let navLinks = document.querySelectorAll('.navbar-nav .nav-link');
        const index = parseInt(key);
        if (index === 1) {
            window.history.back(); // Go back in browser history
        } else {
            const linkIndex = index - 2; // Adjust for "1" going back and "2" being the first link
            if (navLinks.length > linkIndex && linkIndex >= 0) {
                navLinks[linkIndex].click();
            }
        }
    }

    function readNavigationOptions() {
        let optionsText = "Menu options: (1) Go back.";
        let navLinks = document.querySelectorAll('.navbar-nav .nav-link');
        navLinks.forEach((link, index) => {
            optionsText += `(${index + 2}) ${link.textContent} `;
        });
        textToSpeech(optionsText);
    }

    document.getElementById("clearStateBtn").addEventListener("click", function () {
        if (localStorage.getItem("screenReaderState") === "active") {
            toggleScreenReader(); // Deactivate screen reader if it was active
        }
        localStorage.setItem("selectedVoice", "Google US English");
        localStorage.removeItem("screenReaderState");
        localStorage.removeItem("IOBusy");
        playaudio("/audio/munch.mp3");
        textToSpeech("Settings Reset");
    });


    function voices() {
        if (synth.getVoices() !== null) {
            voiceList.innerHTML = "";
            for (let voice of synth.getVoices()) {
                let selected = voice.name === localStorage.getItem("selectedVoice") ? "selected" : "";
                let option = `<option value="${voice.name}" ${selected}>${voice.name} (${voice.lang})</option>`;
                voiceList.insertAdjacentHTML("beforeend", option);
            }
        }
    }

    // Function to check the screen reader state and activate it if necessary
    function checkScreenReaderState() {
        if (localStorage.getItem("screenReaderState") === "active") {
            toggleScreenReader();
        } else {
            speechBtn.style.display = "none";
        }
    }

    function toggleScreenReader() {
        isScreenReaderActive = !isScreenReaderActive;
        if (isScreenReaderActive) {
            addHoverListenersToElements();
            addClickListenersToElements();
            addFocusListenersToElements();
            addInputEventListener();
            screenReaderBtn.textContent = "Deactivate";
            speechBtn.style.display = "none";
            voiceList.style.display = "inline";
            localStorage.setItem("screenReaderState", "active");
            playaudio("/audio/pop.mp3");
            handlePageUpdate();
        } else {
            removeHoverListenersFromElements();
            removeClickListenersFromElements();
            removeFocusListenersFromElements();
            removeInputEventListener();
            screenReaderBtn.textContent = "Activate";
            voiceList.style.display = "none";
            localStorage.removeItem("screenReaderState");
            localStorage.removeItem("IOBusy");
        }
    }

    function textToSpeech(text) {
        console.log(text);
        let utterance = new SpeechSynthesisUtterance(text);

        for (let voice of synth.getVoices()) {
            if (voice.name === voiceList.value) {
                utterance.voice = voice;
                break;
            }
        }

        // Speak the utterance
        synth.speak(utterance);
    }

    function handlePageUpdate() {
        const urlParams = new URLSearchParams(window.location.search);
        const error = urlParams.get('error');
        const transaction = urlParams.get('transaction');
        const action = urlParams.get('action');
        const pdf = urlParams.get('pdf');
        let message = "";
    
        const playAudioAndSpeak = (audio, text) => {
            playaudio(audio);
            setTimeout(() => {
                if (!synth.speaking) {
                    textToSpeech(text);
                    isSpeaking = true;
                }
            }, 800);
        };
    
        if (error === "InvalidCredentials") {
            playAudioAndSpeak("/audio/error.mp3", "Input invalid, please try again");
        } else if (error === "UserExist") {
            playAudioAndSpeak("/audio/error.mp3", "Username or Email Address exist, please try again");
        } else if (error === "InsufficientBal") {
            playAudioAndSpeak("/audio/error.mp3", "Insufficient Balance, please try again");
        } else if (transaction === "Success") {
            playAudioAndSpeak("/audio/success.mp3", "Transaction successful");
        } else if (transaction === "Failed") {
            playAudioAndSpeak("/audio/error.mp3", "Transaction failed, please try again");
        } else if (transaction === "SameAcc") {
            playAudioAndSpeak("/audio/error.mp3", "Unable to transfer to same user, please try again");
        } else if (action === "Success") {
            playAudioAndSpeak("/audio/success.mp3", "Action successful");
        } else if (pdf === "True") {
            playAudioAndSpeak("/audio/success.mp3", "PDF is now open below");
        }
    }
    

    function convertDigitsToWords(number) {
        const digitsToWords = {
            '0': 'zero',
            '1': 'one',
            '2': 'two',
            '3': 'three',
            '4': 'four',
            '5': 'five',
            '6': 'six',
            '7': 'seven',
            '8': 'eight',
            '9': 'nine'
        };
        return number.toString().split('').map(digit => digitsToWords[digit]).join(' ');
    }

    function addHoverListenersToElements() {
        let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt], select, label,tr, embed');
        elements.forEach(element => {
            element.addEventListener("mouseenter", hoverEventListener);
            element.addEventListener("mouseleave", pause);
        });
    }

    function removeHoverListenersFromElements() {
        let elements = document.querySelectorAll('h1, h2, h3, h4, h5, p, a, input, textarea, button, img[alt], select, label, tr, embed');
        elements.forEach(element => {
            element.removeEventListener("mouseenter", hoverEventListener);
            element.removeEventListener("mouseleave", pause);
        });
    }

    function addClickListenersToElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.addEventListener("click", clickEventListener);
        });
    }

    function removeClickListenersFromElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.removeEventListener("click", clickEventListener);
        });
    }

    function addFocusListenersToElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.addEventListener("focus", focusEventListener);
        });
    }

    function removeFocusListenersFromElements() {
        let elements = document.querySelectorAll('a, input, textarea, button');
        elements.forEach(element => {
            element.removeEventListener("click", clickEventListener);
            element.removeEventListener("focus", focusEventListener);
        });
    }

    function addInputEventListener() {
        let elements = document.querySelectorAll('input, textarea');
        elements.forEach(element => {
            element.addEventListener("input", function(event) {
                if (event.isTrusted) {
                    localStorage.setItem("IOBusy", "active");
                    inputEventListener(event);
                }
            });
            element.addEventListener("blur", () => {
                localStorage.removeItem("IOBusy");
            });
        });
    }    

    function removeInputEventListener() {
        let elements = document.querySelectorAll('input, textarea');
        elements.forEach(element => {
            element.removeEventListener("input", inputEventListener);
        });
        localStorage.removeItem("IOBusy");
    }

    function inputEventListener(event) {
        const inputContent = event.target.value;
        if (inputContent && !synth.speaking) {
            textToSpeech(inputContent);
            isSpeaking = true;
        }
    }

    function clickEventListener() {
        const itemText = this.textContent;
        const elementType = this.tagName.toLowerCase();
        let clickText = `click ${itemText}`;

        switch (elementType) {
            case "textarea":
                clickText = `click ${itemText} text area`;
                break;
            case "input":
                clickText = `click ${itemText} ${this.name || "input"} field`;
                break;
            case "button":
                clickText = `click ${itemText} button`;
                break;
        }
        pause();

        if (clickText !== "" && !synth.speaking) {
            textToSpeech(clickText);
            isSpeaking = true;
        }
    }

    function focusEventListener() {
        const itemText = this.textContent;
        const elementType = this.tagName.toLowerCase();
        let focusText = "";

        switch (elementType) {
            case "a":
                focusText = `${itemText} link`;
                break;
            case "textarea":
                focusText = `${itemText} text area`;
                break;
            case "input":
                focusText = `${itemText} ${this.name || "input"} field`;
                break;
            case "button":
                focusText = `${itemText} button`;
                break;
            default:
                focusText = `${itemText}`;
                break;
        }

        if (focusText !== "" && !synth.speaking) {
            textToSpeech(focusText);
            isSpeaking = true;
        }
    }

    async function hoverEventListener() {
        let itemText = this.textContent;
        let elementType = this.tagName.toLowerCase();
        let hoverText = "";

        if (elementType === "tr") {
            let rowCells = this.cells;
            let rowText = Array.from(rowCells).map(cell => cell.textContent.trim()).join(', ');
            hoverText = `Row details: ${rowText}`;
        } else {
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
                let options = Array.from(this.options).map(option => option.text).join(", ");
                hoverText = `${this.name}, currently selected${this.value}. The available options are ${options}`;
            } else if (elementType === "embed") {
                hoverText = "pdf";
            } else {
                hoverText = `${itemText}`;
            }
        }
        if (hoverText.includes("Account Number:")) {
            let digits = hoverText.match(/\d+/g).join("");
            hoverText = hoverText.replace(/\d+/g, convertDigitsToWords(digits));
        }
        pause();
        if (hoverText !== "pdf" && hoverText !== "" && !synth.speaking) {
            textToSpeech(hoverText);
            isSpeaking = true;
        } else if (hoverText === "pdf") {
            const pdfLink = this.getAttribute('src').replace("http://localhost:8080/", "");
            displayPDF(pdfLink);
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

    function displayPDF(pdfLink) {
        pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.11.338/pdf.worker.min.js';
        return new Promise((resolve, reject) => {
            if (pdfLink === "/pdf/finease.pdf") {
                textToSpeech("no transaction pdf showing");
            } else {
                const pdfName = decodeURIComponent(pdfLink.substring(pdfLink.lastIndexOf('/') + 1))
                    .replace(/-/g, ' ')
                    .replace(/pdf/g, ' ')
                    .replace(/\\/g, ' ')
                    .replace(/\./g, ' ')
                    .trim();
                textToSpeech("reading " + pdfName + " pdf");

                fetch(pdfLink)
                    .then(response => response.blob())
                    .then(blob => {
                        var reader = new FileReader();
                        reader.onload = function () {
                            var arrayBuffer = this.result;
                            var pdfData = new Uint8Array(arrayBuffer);
                            pdfjsLib.getDocument({ data: pdfData }).promise.then(pdf => {
                                var text = "";
                                for (let pageNum = 1; pageNum <= pdf.numPages; pageNum++) {
                                    pdf.getPage(pageNum).then(page => {
                                        page.getTextContent().then(pageText => {
                                            pageText.items.forEach(item => {
                                                text += item.str.replace(/-/g, "") + " "; // Replace "-" with an empty string
                                            });
                                            if (pageNum === pdf.numPages) {
                                                textToSpeech(text);
                                            }
                                        });
                                    });
                                }
                            });
                        };
                        reader.readAsArrayBuffer(blob);
                    })
                    .catch(error => {
                        console.error('Error fetching PDF:', error);
                        reject(error); // Reject the promise if there's an error
                    });
            }
        });
    }

    function checkCursorPosition() {
        const mouseMoveHandler = function (event) {
            const cursorX = event.clientX;
            const cursorY = event.clientY;
            const windowWidth = window.innerWidth || document.documentElement.clientWidth;
            const windowHeight = window.innerHeight || document.documentElement.clientHeight;

            let sectionX = "";
            if (cursorX < windowWidth / 3) {
                sectionX = "Left ";
            } else if (cursorX < (windowWidth / 3) * 2) {
                sectionX = "Center ";
            } else {
                sectionX = "Right ";
            }

            let sectionY = "";
            if (cursorY < windowHeight / 3) {
                sectionY = "Top ";
            } else if (cursorY < (windowHeight / 3) * 2) {
                sectionY = "Center ";
            } else {
                sectionY = "Bottom ";
            }
            if (sectionX === "Center " && sectionY === "Center ") {
                textToSpeech("Cursor is currently at Center section.");
            } else {
                textToSpeech("Cursor is currently at " + sectionY + sectionX + "section.");
            }
            document.removeEventListener("mousemove", mouseMoveHandler);
        };
        document.addEventListener("mousemove", mouseMoveHandler);
    }
}