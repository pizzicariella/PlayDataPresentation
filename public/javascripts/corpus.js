$(document).ready(function () {
    jsRoutes.controllers.AnnotatedArticleController.articleList().ajax({
        success: function (result){
            result.forEach(article => createAnnotatedArticle(article))
        },
        failure: function (err){
            console.log("there was an error")
        }
    });
});

const convertPosAnnotationToWordSpan = (annotation, text) => {
    const {begin, end, tag} = annotation;
    const wordSpan = document.createElement("span")
    wordSpan.innerText = text.substring(begin, end+1)
    switch (tag){
        case "ADJ": wordSpan.style="background-color: green"; break;
        case "ADP": wordSpan.style="background-color: yellow"; break;
        case "ADV": wordSpan.style="background-color: darkgreen"; break;
        case "AUX": wordSpan.style="background-color: orange"; break;
        case "CCONJ": wordSpan.style="background-color: purple"; break;
        case "DET": wordSpan.style="background-color: lightblue"; break;
        case "INTJ": wordSpan.style="background-color: brown"; break;
        case "NOUN": wordSpan.style="background-color: blue"; break;
        case "NUM": wordSpan.style="background-color: darkblue"; break;
        case "PART": wordSpan.style="background-color: grey"; break;
        case "PROPN": wordSpan.style="background-color: pink"; break;
        case "PUNCT": wordSpan.style="background-color: white"; break;
        case "SCONJ": wordSpan.style="background-color: purple"; break;
        case "VERB": wordSpan.style="background-color: red"; break;
        case "X": wordSpan.style="background-color: darkgrey"; break;
        case "empty": break;
    }
    return wordSpan;
}

function completeAnnotations(annotations) {
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
        }
    }
    return completeAnnos
}

const createAnnotatedArticle = (articleInfo) => {
    const {_id, longUrl, crawlTime, text, annotationsPos} = articleInfo;
    //const completeTextAnnotated = completeAnnotations(annotationsPos)
    //const wordSpans = completeTextAnnotated.map(pos => convertPosAnnotationToWordSpan(pos, text));
    const textAttribs = text.split("$§$");

    const articleTemplate = document.getElementById("articleTemplate").content;
    const article = articleTemplate.cloneNode(true);

    const articleTitle = article.getElementById("articleTitle");
    articleTitle.innerText = textAttribs[0];

    const articleIntro = article.getElementById("articleIntro");
    articleIntro.innerText = textAttribs[1];

    const articleText = article.getElementById("articleText");
    for(let i = 0; i<wordSpans.length; i++){
        articleText.appendChild(wordSpans[i])
    }
    //articleText.innerText = textAttribs[2];

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

    document.getElementById("articleTab").appendChild(article);
}

console.log("running");