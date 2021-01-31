const tagDescriptions = {ADJ: "Adjektiv", ADP: "Präposition", ADV: "Adverb", AUX: "Hilfsverb",
    CCONJ: "Konjunktion (koordinierend)", DET: "Artikel", INTJ: "Interjektion", NOUN: "Nomen", NUM: "Zahlwort",
    PART: "Partikel", PRON: "Pronomen", PROPN: "Eigenname", PUNCT: "Punctuation", SCONJ: "Konjunktion (subordinierend)",
    VERB: "Verb", X: "sonstige Wortart"};

const tagColors = {ADJ: "#A1CE5E", ADP: "#FACF63", ADV: "#969A52", AUX: "#FBAF5F",
    CCONJ: "#CCC1DB", DET: "#AEC6CC", INTJ: "#B38E50", NOUN: "#1A86A8", NUM: "#D2EFDB",
    PART: "#C5AB89", PRON: "#FFB6AD", PROPN: "#00919C", PUNCT: "white", SCONJ: "#CCC1DB",
    VERB: "#F68B69", X: "#C8C9D0"};

function removeTextLabel(){
    const labelList = document.getElementsByTagName("label");
    labelList[0].remove();
    const requiredEl = document.getElementsByClassName("info");
    requiredEl[0].remove();
}

function removeTextAreaMargin(){
    const ddList = document.getElementsByTagName("dd");
    ddList[0].style.marginLeft = "0em";
}

function start(arg) {
    return function (){
        removeTextLabel();
        removeTextAreaMargin();
        if(arg == "t"){
            jsRoutes.controllers.HomeController.annotatedText().ajax({
                success: function (result){
                    annotateText(result);
                },
                failure: function (err){
                    console.log("there was an error");
                }
            });
            console.log("t");
        } else {
            console.log("nothing to display");
            return;
        }
    };
}

function annotateText(annotated) {
    const heading = document.getElementById("resultHeading");
    heading.innerText = "Ergebnis";
    const spans = createAnnotatedTextSpans(annotated);
    const resultDiv = document.getElementById("spanDiv");
    spans.map(span => resultDiv.appendChild(span));
}

//TODO sometimes funny effects because of Normalizer (eg - is deleted )
function createAnnotatedTextSpans(annotated) {
    const {text, token, posAnnos, lemmas} = annotated;
    let spans = [];
    let subText = text;
    if(posAnnos.length != token.length){
        console.log("length of token and annos dont match");
    }
    for(let i = 0; i<token.length; i++){
        const indexOfToken = subText.indexOf(token[i]);
        if(indexOfToken > 0){
            const wordSpan = document.createElement("span");
            const spanText = subText.substr(0, indexOfToken);
            wordSpan.innerText = spanText;
            spans.push(wordSpan);
        }
        const wordSpan = document.createElement("span");
        const spanText = subText.substr(indexOfToken, token[i].length);
        wordSpan.innerText = spanText;
        wordSpan.onmouseover= function(ev) {showTagInfo(posAnnos[i], lemmas[i], ev);};
        wordSpan.onmouseout = function (ev) {hideTagInfo(ev);}
        wordSpan.style="background-color: "+tagColors[posAnnos[i]];
        spans.push(wordSpan);
        const add = indexOfToken+token[i].length;
        subText = subText.substr(add);
    }
    const wordSpan = document.createElement("span");
    wordSpan.innerText = subText;
    spans.push(wordSpan);
    return spans;
}

function showTagInfo(tag, lemma, event) {

    if(tagDescriptions[tag] == undefined){
        return;
    }

    const tagDescription = document.getElementById("tagDescription");
    tagDescription.innerText = tagDescriptions[tag];

    const tagName = document.getElementById("tagName");
    tagName.innerText = ", tag: "+tag;

    const lemmaDescription = document.getElementById("lemmaDescription");
    lemmaDescription.innerText = "Lemma: ";

    const lemmaSpan = document.getElementById("lemma");
    lemmaSpan.innerText = lemma;

    const tipDiv = document.getElementById("posTagInfoTipDiv");
    const offset = $(event.target).offset();
    const height = $(event.target).outerHeight();
    const color = $(event.target).css("background-color");
    $(tipDiv).css("display", "block");
    $(tipDiv).offset({
        'left': offset.left
    });
    $(tipDiv).offset({
        'top': offset.top + height
    });
    $(tipDiv).width('15em');
    $(tipDiv).css("background-color", color);
}

function hideTagInfo(ev){
    $(document.getElementById("posTagInfoTipDiv")).css("display", "none");
}

