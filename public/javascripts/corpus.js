let loadedArticles = [];
let visibleArticles = [];
const tagDescriptions = {ADJ: "Adjektiv", ADP: "Präposition", ADV: "Adverb", AUX: "Hilfsverb",
    CCONJ: "Konjunktion (koordinierend)", DET: "Artikel", INTJ: "Interjektion", NOUN: "Nomen", NUM: "Zahlwort",
    PART: "Partikel", PRON: "Pronomen", PROPN: "Eigenname", SCONJ: "Konjunktion (subordinierend)",
    VERB: "Verb", X: "sonstige Wortart"};

const tagColors = {ADJ: "#A1CE5E", ADP: "#FACF63", ADV: "#969A52", AUX: "#FBAF5F",
    CCONJ: "#CCC1DB", DET: "#AEC6CC", INTJ: "#B38E50", NOUN: "#1A86A8", NUM: "#D2EFDB",
    PART: "#C5AB89", PRON: "#FFB6AD", PROPN: "#00919C", SCONJ: "#CCC1DB",
    VERB: "#F68B69", X: "#C8C9D0"};

$(document).ready(function () {
    jsRoutes.controllers.AnnotatedArticleController.articleList().ajax({
        success: function (result){
            loadedArticles = result;
            const numArticles = loadedArticles.length;
            const numPages = Math.floor(numArticles / 10);
            const paginationElems = ((numPages > 5) ? 5 : numPages);
            const paginationBar = document.getElementById("paginationBar");
            paginationBar.removeChild(paginationBar.childNodes[0]);
            paginationBar.removeChild(paginationBar.childNodes[1]);
            for(let i = 0; i<paginationElems; i++){
                const aEle =  document.createElement("A");
                aEle.innerText = i+2;
                aEle.classList.add("w3-button");
                aEle.classList.add("w3-hover-black");
                aEle.href = "javascript:loadNextPage("+(i+1)+");";
                paginationBar.appendChild(aEle);
            }
            if(numPages > 5){
                const followUpA = document.createElement("A");
                followUpA.innerText = "»";
                followUpA.classList.add("w3-button");
                followUpA.classList.add("w3-hover-black");
                followUpA.href = "javascript:loadNextPagination();";
                paginationBar.appendChild(followUpA)
            }
            const articlesToInsert = result.slice(0,10);
            articlesToInsert.forEach(article => insertArticle(article));
            visibleArticles = articlesToInsert;
        },
        failure: function (err){
            console.log("there was an error: "+err);
        }
    });
});

function loadNextPagination(){
    const paginationBar = document.getElementById("paginationBar");
    const paginationChildren = paginationBar.childNodes;
    const biggestChild = ((paginationChildren[0].innerText == "«") ? parseInt(paginationChildren[6].innerText) :
        parseInt(paginationChildren[5].innerText));
    while(paginationBar.childNodes[0]){
        paginationBar.removeChild(paginationBar.childNodes[0]);
    }
    const numPages = Math.floor((loadedArticles.length - biggestChild*10) / 10);
    const paginationElems = ((numPages > 6) ? 6 : numPages);
    const followBackA = document.createElement("A");
    followBackA.innerText = "«";
    followBackA.classList.add("w3-button");
    followBackA.classList.add("w3-hover-black");
    followBackA.href = "javascript:loadPreviousPagination();";
    paginationBar.appendChild(followBackA)
    for(let i = 0; i<paginationElems; i++){
        const aEle =  document.createElement("A");
        aEle.innerText = i+1+biggestChild;
        aEle.classList.add("w3-button");
        if(i+1+biggestChild == biggestChild+1){
            aEle.classList.add("w3-black");
        } else {
            aEle.classList.add("w3-hover-black");
        }
        aEle.href = "javascript:loadNextPage("+(i+biggestChild)+");";
        paginationBar.appendChild(aEle);
    }
    if(numPages > 6){
        const followUpA = document.createElement("A");
        followUpA.innerText = "»";
        followUpA.classList.add("w3-button");
        followUpA.classList.add("w3-hover-black");
        followUpA.href = "javascript:loadNextPagination();";
        paginationBar.appendChild(followUpA)
    }
    loadNextPage(parseInt(paginationBar.childNodes[1].innerText)-1);
}

function loadPreviousPagination(){
    const paginationBar = document.getElementById("paginationBar");
    const paginationChildren = paginationBar.childNodes;
    const smallestChild = parseInt(paginationChildren[1].innerText);
    while(paginationBar.childNodes[0]){
        paginationBar.removeChild(paginationBar.childNodes[0]);
    }
    if(smallestChild > 7){
        const followBackA = document.createElement("A");
        followBackA.innerText = "«";
        followBackA.classList.add("w3-button");
        followBackA.classList.add("w3-hover-black");
        followBackA.href = "javascript:loadPreviousPagination();";
        paginationBar.appendChild(followBackA)
    }
    for(let i = 6; i>0; i--){
        const aEle =  document.createElement("A");
        aEle.innerText = smallestChild-i;
        aEle.classList.add("w3-button");
        if(i == 6){
            aEle.classList.add("w3-black");
        } else {
            aEle.classList.add("w3-hover-black");
        }
        aEle.href = "javascript:loadNextPage("+(smallestChild-i-1)+");";
        paginationBar.appendChild(aEle);
    }
    const followUpA = document.createElement("A");
    followUpA.innerText = "»";
    followUpA.classList.add("w3-button");
    followUpA.classList.add("w3-hover-black");
    followUpA.href = "javascript:loadNextPagination();";
    paginationBar.appendChild(followUpA);
    loadNextPage(smallestChild-2);
}

function loadNextPage(pageNumber){
    for(let i=0; i<visibleArticles.length; i++){
        const article = document.getElementById(visibleArticles[i]._id["$oid"]);
        article.remove();
    }
    const nextArticles = loadedArticles.slice(pageNumber*10, (pageNumber*10)+10);
    nextArticles.forEach(article => insertArticle(article));
    visibleArticles = nextArticles;
    const paginationBar = document.getElementById("paginationBar");
    const paginationChildren = paginationBar.childNodes;
    for(let i=0; i<paginationChildren.length; i++){
        if(paginationChildren[i].innerText == pageNumber+1){
            paginationChildren[i].classList.remove("w3-hover-black");
            paginationChildren[i].classList.add("w3-black");
        } else {
            paginationChildren[i].className = "w3-button";
            paginationChildren[i].classList.add("w3-hover-black");
        }
    }
    $('html, body').scrollTop(0);
}


function showPosAnnotations(articleId) {
    const articleInfo = loadedArticles.find(article => article._id["$oid"] === articleId);
    const {text, lemma, pos} = articleInfo
    const completeTextAnnotated = completeAnnotations(pos, lemma, text.length);
    const wordSpans = completeTextAnnotated.map(posAndLemma =>
        convertPosAnnotationToWordSpan(posAndLemma, text, articleId));
    const article = document.getElementById(articleId);
    const articleTitle = article.querySelector("#articleTitle");
    const articleIntro = article.querySelector("#articleIntro");
    const articleText = article.querySelector("#articleText");
    articleTitle.innerText = "";
    articleIntro.innerText = "";
    articleText.innerText = "";
    articleTitle.setAttribute("class", "w3-main");
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

function showTagInfo(tag, lemma, articleId) {

    if(tagDescriptions[tag] === undefined){
        return ;
    }
    const currentArticle = document.getElementById(articleId);

    const tagDescription = currentArticle.querySelector("#tagDescription");
    tagDescription.innerText = tagDescriptions[tag];

    const tagName = currentArticle.querySelector("#tagName");
    tagName.innerText = ", tag: "+tag;

    const lemmaDescription = currentArticle.querySelector("#lemmaDescription");
    lemmaDescription.innerText = "Lemma: ";

    const lemmaSpan = currentArticle.querySelector("#lemma");
    lemmaSpan.innerText = lemma;

    const tipDiv = currentArticle.querySelector("#posTagInfoTipDiv");
    const offset = $(event.target).offset();
    const height = $(event.target).outerHeight();
    const color = $(event.target).css("background-color");

    $(tipDiv).show();
    $(tipDiv).offset({
        'left': offset.left
    });
    $(tipDiv).offset({
        'top': offset.top + height
    });
    $(tipDiv).width('15em');
    $(tipDiv).css("background-color", color);
}

function hideTagInfo(articleId, ev){
    const currentArticle = document.getElementById(articleId);
    $(currentArticle.querySelector("#posTagInfoTipDiv")).css("display", "none");
}

const convertPosAnnotationToWordSpan = (posAndLemma, text, articleId) => {
    //const {begin, end, tag} = annotation;
    const {pos, lemma} = posAndLemma;
    const {begin, end, tag} = pos;
    const {result} = lemma;
    const wordSpan = document.createElement("span");
    wordSpan.innerText = text.substring(begin, end+1);
    wordSpan.onmouseover= function() {showTagInfo(tag, result, articleId)};
    wordSpan.onmouseout = function() {hideTagInfo(articleId)};
    wordSpan.style="background-color: "+tagColors[tag];
    return wordSpan;
}

function completeAnnotations(annotations, lemmas, textLength) {
    let completeAnnos = [];
    for(let i=0; i<annotations.length; i++){
        completeAnnos.push({pos: annotations[i], lemma: lemmas[i]});
        const {end} = annotations[i];
        const endPrev = end;
        if(i !== annotations.length-1){
            let {begin} = annotations[i+1];
            if(begin > endPrev+1){
                const emptyAnno = {begin: endPrev+1, end: begin-1, tag: "empty"};
                const emptyLemma = {beginToken: endPrev+1, endToken: begin-1, result: "empty"};
                completeAnnos.push({pos: emptyAnno, lemma: emptyLemma});
            }
        } else {
            if(textLength-1 > endPrev){
                const emptyAnno = {begin: endPrev+1, end: textLength-1, tag: "empty"};
                const emptyLemma = {beginToken: endPrev+1, endToken: textLength-1, result: "empty"}
                completeAnnos.push({pos: emptyAnno, lemma:emptyLemma});
            }
        }
    }
    return completeAnnos;
}

const insertArticle = (articleInfo) => {
    const {_id, long_url, crawl_time, text, posPercentage} = articleInfo;

    const textAttribs = text.split("$§$");

    const articleTemplate = document.getElementById("articleTemplate").content;
    const article = articleTemplate.cloneNode(true);
    const articleElement = article.getElementById("setToArticleId");
    articleElement.id = _id["$oid"];

    const articleTitle = article.getElementById("articleTitle");
    articleTitle.innerText = textAttribs[0];

    const articleIntro = article.getElementById("articleIntro");
    articleIntro.innerText = textAttribs[1];

    const articleText = article.getElementById("articleText");
    articleText.innerText = textAttribs[2];

    const articleReference = article.getElementById("articleReference");
    const link = document.createElement('a');
    const linkText = document.createTextNode(long_url+";");
    const href = long_url;
    link.appendChild(linkText);
    link.href = href;
    const sourceSpan = document.createElement("span");
    sourceSpan.innerText = "Quelle: ";
    const dateSpan = document.createElement("span");
    const date = new Date(crawl_time["$date"])
    dateSpan.innerText = " aufgerufen am: ".concat(date.getDate() + "." + (date.getMonth()+1) + "." +
        date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + " Uhr"); //.toString());
    articleReference.appendChild(sourceSpan);
    articleReference.appendChild(link);
    articleReference.appendChild(dateSpan);

    const button = article.getElementById("showAnnotationsButton");
    button.onclick = function () {showPosAnnotations(_id["$oid"]);};


    createArticleInformation(posPercentage, article);

    document.getElementById("articleTab").appendChild(article);
}

function createArticleInformation(posPercentage, article) {
    const list = article.querySelector("#posTagList");
    posPercentage.sort(compareTP);
    for(let i=0; i<posPercentage.length; i++){
        const {tag, percentage} = posPercentage[i];
        const dt = document.createElement("DT");
        const tagSpan = document.createElement("SPAN");
        const percentageSpan = document.createElement("SPAN");
        let color = tagColors[tag];
        let description = tagDescriptions[tag];
        if(color === undefined){
            color = "darkgrey";
        }
        tagSpan.style = "background-color: "+color;
        if(description === undefined){
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

function compareTP(tp1, tp2){
    if (tp1.percentage < tp2.percentage){
        return 1;
    } else {
        return -1;
    }
}





