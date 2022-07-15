# demo-hibernate-search
Demo Project to test hibernate search with a standard lucene index

## How to use

- Start the application
- Open http://localhost:8080 and you will be redirected to the swagger-ui
- Interact with the api to search for Movies

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