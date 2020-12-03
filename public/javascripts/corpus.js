
const getAllAnnotatedArticles = async () => {
    const result = await fetch('http://localhost:9000/corpus/articles');
    return result.json();
};

const createAnnotatedArticle = (articleInfo) => {
    const {id, longUrl, crawlTime, text, posAnnos} = articleInfo;
    const articleTemplate = document.getElementById("articleTemplate").content;
    const article = articleTemplate.cloneNode(true);
    const articleText = article.getElementById("articleText");
    // richtig?
    articleText.innerText = text
    //TODO add other attribs like longUrl
    document.getElementById("articleTab").appendChild(article)
}

const addArticlesToView = (articles) => {
    articles.forEach(article => createAnnotatedArticle(article))
}

const insertArticles = () => {
    getAllAnnotatedArticles().then(articles => addArticlesToView(articles))
}

console.log("running")
insertArticles()