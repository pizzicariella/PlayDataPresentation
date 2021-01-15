
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