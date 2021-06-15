# NEWRELIC_ENV is directory where newrelic.yml you want to use resides (e.g. "/path/to/yml/directory")
# NEWRELIC_JAR is full path to the newrelic.jar (e.g. "/path/to/file.jar")
java -Dnewrelic.environment=$NEWRELIC_ENV -javaagent:$NEWRELIC_JAR -jar build/libs/*-SNAPSHOT.jar
