set -eu
test -f pom.xml && rm pom.xml
for f in *.jar; do
  test -f $f && rm $f
done
lein clean
lein deps
lein test
lein jar
lein pom
