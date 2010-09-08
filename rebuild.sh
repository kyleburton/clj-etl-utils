set -eu
test -f pom.xml && rm pom.xml
test -f *.jar && rm *.jar
lein clean
lein deps
lein test
lein jar
lein pom
