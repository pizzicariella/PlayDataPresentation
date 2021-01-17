const tagDescriptions = {ADJ: "Adjektiv", ADP: "Präposition", ADV: "Adverb", AUX: "Hilfsverb",
    CCONJ: "Konjunktion (koordinierend)", DET: "Artikel", INTJ: "Interjektion", NOUN: "Nomen", NUM: "Zahlwort",
    PART: "Partikel", PRON: "Pronomen", PROPN: "Eigenname", PUNCT: "Punctuation", SCONJ: "Konjunktion (subordinierend)",
    VERB: "Verb", X: "sonstige Wortart"};

const tagColors = {ADJ: "#A1CE5E", ADP: "#FACF63", ADV: "#969A52", AUX: "#FBAF5F",
    CCONJ: "#CCC1DB", DET: "#AEC6CC", INTJ: "#B38E50", NOUN: "#1A86A8", NUM: "#D2EFDB",
    PART: "#C5AB89", PRON: "#FFB6AD", PROPN: "#00919C", PUNCT: "white", SCONJ: "#CCC1DB",
    VERB: "#F68B69", X: "#C8C9D0"};

function start(arg) {
    return function (){
        if(arg == "t"){
            jsRoutes.controllers.HomeController.annotatedText().ajax({
                success: function (result){
                    //loadedArticles = result
                    console.log(result)
                    //result.forEach(article => insertArticle(article))
                },
                failure: function (err){
                    console.log("there was an error")
                }
            });
        } else {
            console.log("nothing to display")
            return
        }
    }
}

//TODO test
function annotateText(annotated) {
    const spans = createAnnotatedTextSpans(annotated)
    const textField = document.getElementById("text");
    spans.map(span => textField.appendChild(span))
}

function createAnnotatedTextSpans(annotated) {
    const {text, annos, token} = annotated;
    let spans = []
    let subText = text
    if(annos.length != token.length){
        console.log("length of token and annos dont match")
    }
    for(let i = 0; i<token.length; i++){
        const indexOfToken = subText.indexOf(token[i])
        if(indexOfToken > 0){
            const wordSpan = document.createElement("span");
            wordSpan.innerText = subText.substr(0, indexOfToken+1)
            spans.push(wordSpan)
        }
        const wordSpan = document.createElement("span");
        wordSpan.innerText = subText.substr(indexOfToken, token[i].length)
        wordSpan.style="background-color: "+tagColors[annos[i]]
        spans.push(wordSpan)
        subText = subText.substr(indexOfToken.length)
    }
    return spans
}
