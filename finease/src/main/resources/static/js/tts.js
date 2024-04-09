const voiceList = document.getElementById("voiceList"),
      speechBtn = document.getElementById("speechBtn");

let synth = window.speechSynthesis,
    isSpeaking = false;

voices();

function voices() {
    for (let voice of synth.getVoices()) {
        let selected = voice.name === "Google US English" ? "selected" : "";
        let option = `<option value="${voice.name}" ${selected}>${voice.name} (${voice.lang})</option>`;
        voiceList.insertAdjacentHTML("beforeend", option);
    }
}

synth.addEventListener("voiceschanged", voices);

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

function addHoverListenersToElements() {
    // let elements = document.querySelectorAll('h1, h2, h3, p, input[type="text"], textarea, button');
    let elements = document.querySelectorAll('h1, h2, h3, p, input, textarea, button');
    elements.forEach(element => {
        element.addEventListener("mouseenter", () => {
            let text = element.textContent;
            if (!synth.speaking) {
                textToSpeech(text);
                isSpeaking = true;
            }
        });
    });
}

function addClickListenersToElements() {
    document.getElementById("textField").addEventListener("click", () => {
        if (!synth.speaking) {
            textToSpeech("click text field");
            isSpeaking = true;
        }
    });

    document.getElementById("textarea").addEventListener("click", () => {
        if (!synth.speaking) {
            textToSpeech("click text area");
            isSpeaking = true;
        }
    });

    document.getElementById("button1").addEventListener("click", () => {
        if (!synth.speaking) {
            textToSpeech("click button");
            isSpeaking = true;
        }
    });
}

addHoverListenersToElements();
addClickListenersToElements();

document.getElementById("textField").addEventListener("input", (event) => {
    let text = event.target.value;
    if (!synth.speaking && text.trim() !== "") {
        textToSpeech(text);
        isSpeaking = true;
    }
});

document.getElementById("textarea").addEventListener("input", (event) => {
    let text = event.target.value;
    if (!synth.speaking && text.trim() !== "") {
        textToSpeech(text);
        isSpeaking = true;
    }
});

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
    }
});
