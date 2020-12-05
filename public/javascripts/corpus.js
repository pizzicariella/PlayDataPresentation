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


const createAnnotatedArticle = (articleInfo) => {
    const {_id, longUrl, crawlTime, text, annotationsPos} = articleInfo;
    const articleTemplate = document.getElementById("articleTemplate").content;
    const article = articleTemplate.cloneNode(true);
    const articleText = article.getElementById("articleText");
    articleText.innerText = text
    //TODO add other attribs like longUrl
    document.getElementById("articleTab").appendChild(article)
}

console.log("running");