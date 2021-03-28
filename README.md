# PlayDataPresentation

This web application build in scala with play framework visualizes the results of the Spark NLP pipeline build in projekt https://github.com/pizzicariella/POSPipelineGerman.

## Data 

The application uses `ReactiveMongo` to access annotated articles in MongoDB. Alternatively you can find a test file containing some annotated articles in 
`conf/resources` directory. 

## HTTP API

It provides an HTTP API to make annotated articles, annotations and other resources accessible. Routes are configured in `conf/routes`.
The following endpoints are available in the API:


| Method | Route                               | Description            |
| :-----:|:------------------------------------| :--------------------- |
| `GET`  | `/api/corpus/articles`              | Get annotated articles articles |
| `GET`  | `/api/test/corpus/articles`         | Get articles provided by test file |
| `POST`  | `/api/analyze/text`             | Post raw text |
| `GET`  | `/api/analyze/annotated`         | Get raw text and it's annotations    |

## Views

The application provides four views, two of which (corpus and analyze) are interactive. 
The following endpoints render the views:

| Method | Route                               | Description            |
| :-----:|:------------------------------------| :--------------------- |
| `GET`  | `/`              | Render Home Page |
| `GET`  | `/corpus`         | Render Corpus Page|
| `GET`  | `/analyze`             | Render Analyse Page |
| `GET`  | `/info`         | Render Info Page    |

## Javascript Routes

Javascript Routes can be configured in `controllers/AnnotatedArticleController` and are accessible on endpoint

`GET /javascriptRoutes`

## Build

Before build an `application.conf` file has to be added in `conf` directory. Here parameters to run the application have to be configured. 
`play.modules.enabled += "play.modules.reactivemongo.ReactiveMongoModule"` has to be set. Additionally `mongodb.uri` and `mongodb.collection` have to be set to 
configure access to mongoDB. Furthermore it's necessary to set `play.http.secret.key` and `play.filters.hosts.allowed`. Other parameters can be edited as desired. 

Executing `sbt dist` in project directory will create `.zip` file in `target/universal` directory. 

## Run

Unzip `.zip` file in desired location. To run application simply execute `bin/playdatapresentation` or create systemd service.
