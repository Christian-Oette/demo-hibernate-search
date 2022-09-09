# demo-hibernate-search
Demo Project to test hibernate search with a standard lucene index

## How to use

- Start the application
- Open http://localhost:8080
- Type in your search

![Application Screenshot](docs/screenshot.png?raw=true "Application")

## Use the swagger api

- Alternatively to the start page you can use http://localhost:8080/swagger-ui/index.html

```
    GET /api/search
```

## Example search for "Indiana"

```
[
  {
    "id": 69,
    "title": "Indiana Jones and the Last Crusade",
    "storyLine": "An art collector appeals to Jones to embark on a search for the Holy Grail..."
  },
  {
    "id": 28,
    "title": "Raiders of the Lost Ark",
    "storyLine": "The year is 1936. An archeology professor named Indiana Jones is venturing in the jungles of South America ..."
  }
]
```