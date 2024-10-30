# Switch Java version
export JAVA_HOME=`/usr/libexec/java_home -v 21`

# Howto Run

## compile
mvn clean install

## Run flagd as a docker container
docker run -p 8013:8013 -v $(pwd)/:/etc/flagd/ -it ghcr.io/open-feature/flagd:latest start --uri file:/etc/flagd/flags.flagd.json

## Run the application
java -jar target/openfeature-demo-0.0.1-SNAPSHOT.jar

## Test
http:/ /localhost:8080/hello

## Change flag to on
"defaultVariant": "on"

## Refresh browser
http:/ /localhost:8080/hello

