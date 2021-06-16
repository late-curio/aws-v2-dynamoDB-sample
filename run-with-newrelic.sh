# NEWRELIC_ENV is directory where newrelic.yml you want to use resides (e.g. "/path/to/yml/directory")
# NEWRELIC_JAR is full path to the newrelic.jar (e.g. "/path/to/file.jar")
# with ability to attach remote debugger
java -Dnewrelic.environment=$NEWRELIC_ENV -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address:5005 -javaagent:$NEWRELIC_JAR -jar build/libs/*-SNAPSHOT.jar
