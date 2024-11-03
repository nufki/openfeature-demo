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

# Use Flapt (https://www.flipt.io/) instead of flagd
1. configure service to use 'flipt' FeatureProvider
    ```application.properties
    #demo.featureProvider=flagd
    demo.featureProvider=flipt
    ```
1. Run Flapt (on Port 8081!): https://docs.flipt.io/introduction

    ```
    docker run -d \
        -p 8081:8080 \
        -p 9000:9000 \
        -v $HOME/flipt:/var/opt/flipt \
        docker.flipt.io/flipt/flipt:latest
    ```
1. create a segement with 'MAs':
    - "Match Type=All"
    - Constraint: Type=String, Property=contract, Operator='IS ONE OF', Values='59333-0006,59444-0006'
1. create a Flag
    - Key=`special_feature`
    - Type=Boolean
    - Default Rollout: Value=false
    - new Rollout: Segement='MAs', Value=true
1. try
    ```
    curl -X GET http://localhost:8080/isSpecialFeatureEnabled -H "Authentication: 59333-0006"
    Feature enabled: true
    curl -X GET http://localhost:8080/isSpecialFeatureEnabled -H "Authentication: 59333-0007"
    Feature enabled: false
    ```






