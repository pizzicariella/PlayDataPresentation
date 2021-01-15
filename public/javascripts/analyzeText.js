console.log("testscript")

$("#submitButton").click(function () {
    console.log("test")
    jsRoutes.controllers.HomeController.annotatedText().ajax({
        success: function (result){
            //loadedArticles = result
            console.log("seems to be empty")
            console.log(result)
            //result.forEach(article => insertArticle(article))
        },
        failure: function (err){
            console.log("there was an error")
        }
    });
});