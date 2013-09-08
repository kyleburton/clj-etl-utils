(defproject org.clojars.kyleburton/clj-etl-utils "1.3.11-SNAPSHOT"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license {:name         "Eclipse Public License - v 1.0"
            :url          "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments     "Same as Clojure"}
  :lein-release {:deploy-via :clojars}
  :java-source-path     "java"
  :repositories         {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}

  :profiles             {:dev {:dependencies [[swank-clojure "1.4.3"]
                                              [lein-marginalia "0.6.0"]]}
                         :1.2 {:dependencies [[org.clojure/clojure "1.2.0"]]}
                         :1.3 {:dependencies [[org.clojure/clojure "1.3.0"]]}
                         :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]]}
                         :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
                         :1.6 {:dependencies [[org.clojure/clojure "1.6.0-master-SNAPSHOT"]]}}
  :dependencies [[org.clojure/java.classpath "0.2.0"]
                 [org.clojure/core.incubator "0.1.0"]
                 [org.clojure/data.json      "0.2.0"]
                 [org.clojure/tools.logging  "0.2.3"]
                 [commons-io                 "2.0"]
                 [log4j/log4j                "1.2.17"]
                 [org.mindrot/jbcrypt        "0.3m"]
                 [commons-httpclient         "3.1"]
                 [commons-codec              "1.8"]
                 [commons-lang               "2.6"]
                 [joda-time/joda-time        "2.3"]]
  :autodoc {
    :name        "clj-etl-utils"
    :page-title  "clj-etl-utils: API Documentation"
    :description "ETL Utilites for Clojure"
    :web-home    "http://kyleburton.github.com/projects/clj-etl-utils/"
  })
