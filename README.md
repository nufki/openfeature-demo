# Switch Java version
`export JAVA_HOME=`/usr/libexec/java_home -v 21``

# Howto Run

## Run flagd as a docker container or start docker-compose
`docker run -p 8013:8013 -v $(pwd)/:/etc/flagd/ -it ghcr.io/open-feature/flagd:latest start --uri file:/etc/flagd/flags.json`

## compile
`mvn clean install`

## Run the application
`java -jar target/openfeature-demo-0.0.1-SNAPSHOT.jar`

## Tests
```
curl -X GET http://localhost:8080/hello 
Hello!
curl -X GET http://localhost:8080/retries
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




# JWT
To test the feature flag within a JWT Token with basic Role based authentication. Create a new user:

## Signup new Client user: 
```
   curl -X POST http://localhost:8080/auth/signup \
   -H "Content-Type: application/json" \
   -d '{
   "email": "max.muster@gmail.ch",
   "password": "1234",
   "fullName": "Max Muster"
   }'
```

## Login with the new user and store the issued JWT Token
```
   curl -X POST http://localhost:8080/auth/login \
   -H "Content-Type: application/json" \
   -d '{
   "email": "max.muster@gmail.ch",
   "password": "1234"
   }'
```

## Call the secured API to test the feature
```
curl -X GET http://localhost:8080/api/isNewFeatureEnabled \
-H "Authorization: Bearer yourtoken"
```
Feature enabled: true

## Change the flags.json file content and change from CLIENT to EMPLOYEE and retry
Feature enabled: false


## More apis
Next to the user api's there are admin API's that allow you to:
- Create a new administrator user
- Switch the role of an existing user (here the existing token will become invalid and the user needs to re-authenticate)

Also, in the user API's you can further
- List all users (only for ADMIN and EMPLOYEE roles)
- List you as logged in user



## Call flagd via REST
welcome-message flag:
```
curl -X POST "http://localhost:8013/flagd.evaluation.v1.Service/ResolveBoolean" \
     -d '{"flagKey":"welcome-message","context":{}}' \
     -H "Content-Type: application/json"
```

newFeature flag:
```
curl -X POST "http://localhost:8013/flagd.evaluation.v1.Service/ResolveBoolean" \
     -d '{"flagKey":"newFeature","context":{ "role": "CLIENT"}}' \
     -H "Content-Type: application/json"
```