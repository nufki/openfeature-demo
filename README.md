# Switch Java version
`export JAVA_HOME=`/usr/libexec/java_home -v 21``

# Howto Run

## Run flagd as a docker container
`docker run -p 8013:8013 -v $(pwd)/:/etc/flagd/ -it ghcr.io/open-feature/flagd:latest start --uri file:/etc/flagd/flags.json`

## compile
`mvn clean install`

## Run the application
`java -jar target/openfeature-demo-0.0.1-SNAPSHOT.jar`

## Tests
```
curl -X GET  http://localhost:8080/hello 
Hello!
curl -X GET  http://localhost:8080/retries
Medium retries configured (5 attempts).%
curl -X GET http://localhost:8080/isColorYellow
Feature enabled: false
curl -X GET http://localhost:8080/isNewFeatureEnabled
Feature enabled: false
curl -X GET http://localhost:8080/isSpecialFeatureEnabled -H "Authentication: 59333-0006"
Feature enabled: true
curl -X GET http://localhost:8080/isSpecialFeatureEnabled -H "Authentication: 59333-0007"
Feature enabled: false
```

## Change flag to on
- 1st Call: "defaultVariant": "on"
- 2nd Call: "defaultVariant": "high"

## Test again...
```
curl -X GET http://localhost:8080/hello
Hello, welcome to this OpenFeature-enabled website!
curl -X GET http://localhost:8080/retries
High retries configured (10 attempts).
curl -X GET "http://localhost:8080/isColorYellow?color=yellow"
Feature enabled: true
curl -X GET http://localhost:8080/isNewFeatureEnabled -H "group: Mitarbeiter"
Feature enabled: true
```


