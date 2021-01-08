let loadedArticles = [];
const tagDescriptions = {ADJ: "Adjektiv", ADP: "Präposition", ADV: "Adverb", AUX: "Hilfsverb",
    CCONJ: "Konjunktion (koordinierend)", DET: "Artikel", INTJ: "Interjektion", NOUN: "Nomen", NUM: "Zahlwort",
    PART: "Partikel", PRON: "Pronomen", PROPN: "Eigenname", PUNCT: "Punctuation", SCONJ: "Konjunktion (subordinierend)",
    VERB: "Verb", X: "sonstige Wortart"};

const tagColors = {ADJ: "#A1CE5E", ADP: "#FACF63", ADV: "#969A52", AUX: "#FBAF5F",
    CCONJ: "#CCC1DB", DET: "#AEC6CC", INTJ: "#B38E50", NOUN: "#1A86A8", NUM: "#D2EFDB",
    PART: "#C5AB89", PRON: "#FFB6AD", PROPN: "#00919C", PUNCT: "white", SCONJ: "#CCC1DB",
    VERB: "#F68B69", X: "#C8C9D0"};

$(document).ready(function () {
    jsRoutes.controllers.AnnotatedArticleController.inMemoryArticleList().ajax({
        success: function (result){
            loadedArticles = result
            result.forEach(article => insertArticle(article))
        },
        failure: function (err){
            console.log("there was an error")
        }
    });
});


function showPosAnnotations(articleId) {
    const articleInfo = loadedArticles.find(article => article.id === articleId);
    const {_id, longUrl, crawlTime, text, annotationsPos} = articleInfo;
    const completeTextAnnotated = completeAnnotations(annotationsPos, text.length);
    const wordSpans = completeTextAnnotated.map(pos => convertPosAnnotationToWordSpan(pos, text, articleId));
    const article = document.getElementById(articleId);
    const articleTitle = article.querySelector("#articleTitle");
    const articleIntro = article.querySelector("#articleIntro");
    const articleText = article.querySelector("#articleText");
    articleTitle.innerText = "";
    articleIntro.innerText = "";
    articleText.innerText = "";
    articleTitle.setAttribute("class", "w3-main")
    let i = 0;
    let nextP = false;
    while (i<wordSpans.length && !nextP){
        if(wordSpans[i].innerText.includes("$§$")) {
            const subIndex = wordSpans[i].innerText.indexOf("$§$");
            wordSpans[i].innerText = wordSpans[i].innerText.substring(0, subIndex);
            nextP = true;
        }
        articleTitle.appendChild(wordSpans[i]);
        i++;
    }
    nextP = false;
    while (i<wordSpans.length && !nextP){
        if(wordSpans[i].innerText.includes("$§$")) {
            const subIndex = wordSpans[i].innerText.indexOf("$§$");
            wordSpans[i].innerText = wordSpans[i].innerText.substring(0, subIndex);
            nextP = true;
        }
        articleIntro.appendChild(wordSpans[i]);
        i++;
    }
    while (i<wordSpans.length){
        articleText.appendChild(wordSpans[i]);
        i++;
    }
}

function showTagInfo(tag, articleId, event) {

    if(tagDescriptions[tag] == undefined){
        return ;
    }
    const currentArticle = document.getElementById(articleId);

    const tagDescription = currentArticle.querySelector("#tagDescription")
    tagDescription.innerText = tagDescriptions[tag]

    const tagName = currentArticle.querySelector("#tagName")
    tagName.innerText = ", tag: "+tag

    const tipDiv = currentArticle.querySelector("#posTagInfoTipDiv")
    const offset = $(event.target).offset();
    const height = $(event.target).outerHeight();
    const color = $(event.target).css("background-color");
    $(tipDiv).show()
    $(tipDiv).offset({
        'left': offset.left
    });
    $(tipDiv).offset({
        'top': offset.top + height
    });
    $(tipDiv).width('10em')
    $(tipDiv).css("background-color", color);
}

function hideTagInfo(articleId, ev){
    const currentArticle = document.getElementById(articleId);
    $(currentArticle.querySelector("#posTagInfoTipDiv")).css("display", "none");
}

const convertPosAnnotationToWordSpan = (annotation, text, articleId) => {
    const {begin, end, tag} = annotation;
    const wordSpan = document.createElement("span");
    wordSpan.innerText = text.substring(begin, end+1);
    wordSpan.onmouseover= function(ev) {showTagInfo(tag, articleId, ev)};
    wordSpan.onmouseout = function (ev) {hideTagInfo(articleId, ev)}
    wordSpan.style="background-color: "+tagColors[tag]
    return wordSpan;
}

function completeAnnotations(annotations, textLength) {
    let completeAnnos = [];
    for(let i=0; i<annotations.length; i++){
        completeAnnos.push(annotations[i])
        const {begin, end, tag} = annotations[i]
        const endPrev = end
        if(i != annotations.length-1){
            let {begin, end, tag} = annotations[i+1]
            if(begin > endPrev+1){
                const empyAnno = {begin: endPrev+1, end: begin-1, tag: "empy"}
                completeAnnos.push(empyAnno)
            }
        } else {
            if(textLength-1 > endPrev){
                const empyAnno = {begin: endPrev+1, end: textLength-1, tag: "empy"}
                completeAnnos.push(empyAnno)
            }
        }
    }
    return completeAnnos
}

const insertArticle = (articleInfo) => {
    const {id, longUrl, crawlTime, text, annotationsPos, tagPercentage} = articleInfo;

    const textAttribs = text.split("$§$");

    const articleTemplate = document.getElementById("articleTemplate").content;
    const article = articleTemplate.cloneNode(true);
    const articleElement = article.getElementById("setToArticleId")
    articleElement.id = id

    const articleTitle = article.getElementById("articleTitle");
    articleTitle.innerText = textAttribs[0];

    const articleIntro = article.getElementById("articleIntro");
    articleIntro.innerText = textAttribs[1];

    const articleText = article.getElementById("articleText");
    articleText.innerText = textAttribs[2];

    const articleReference = article.getElementById("articleReference");
    const link = document.createElement('a');
    const linkText = document.createTextNode(longUrl);
    const href = longUrl;
    link.appendChild(linkText);
    link.href = href;
    const sourceSpan = document.createElement("span");
    sourceSpan.innerText = "Quelle: ";
    const dateSpan = document.createElement("span");
    dateSpan.innerText = " aufgerufen am: ".concat(new Date(parseInt(crawlTime)).toString());
    articleReference.appendChild(sourceSpan);
    articleReference.appendChild(link);
    articleReference.appendChild(dateSpan);

    const button = article.getElementById("showAnnotationsButton");
    button.onclick = function () {showPosAnnotations(id)}

    createArticleInformation(tagPercentage, article);

    document.getElementById("articleTab").appendChild(article);
}

function createArticleInformation(tagPercentage, article) {
    const list = article.querySelector("#posTagList");
    for(let i=0; i<tagPercentage.length; i++){
        const {tag, percentage} = tagPercentage[i];
        const dt = document.createElement("DT");
        const tagSpan = document.createElement("SPAN");
        const percentageSpan = document.createElement("SPAN");
        let color = tagColors[tag];
        let description = tagDescriptions[tag];
        if(color == undefined){
            color = "darkgrey";
        }
        tagSpan.style = "background-color: "+color;
        if(description == undefined){
            description = "unbekannt";
        }
        tagSpan.innerText = tagDescriptions[tag]+" ("+tag+")";
        const percentageReadable = percentage*100
        percentageSpan.innerText = ": "+percentageReadable.toFixed(2)+"%";
        dt.appendChild(tagSpan);
        dt.appendChild(percentageSpan);
        list.appendChild(dt);
    }
}





