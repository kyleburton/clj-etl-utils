(defproject org.clojars.kyleburton/clj-etl-utils "1.0.66"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license {:name         "Eclipse Public License - v 1.0"
            :url          "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments     "Same as Clojure"}
  :java-source-path     "java"
  :warn-on-reflection   true
  :local-repo-classpath true
  :jvm-opts             ["-Xmx512M"]
  :lein-release {:deploy-via :clojars :scm :git}
  :plugins [[lein-release/lein-release "1.0.0"]]
  :dev-dependencies [[swank-clojure   "1.4.0-SNAPSHOT"]
                   ;;[autodoc       "0.7.1"]
                     [lein-marginalia "0.6.0"]]
  :dependencies [[org.clojure/clojure         "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [commons-io          "2.0"]
                 [log4j/log4j         "1.2.14"]
                 [org.mindrot/jbcrypt "0.3m"]
                 [commons-httpclient  "3.1"]
                 [commons-codec       "1.4"]
                 [commons-lang        "2.5"]
                 [joda-time/joda-time                   "1.6.2"]]
  :autodoc {
    :name        "clj-etl-utils"
    :page-title  "clj-etl-utils: API Documentation"
    :description "ETL Utilites for Clojure"
    :web-home    "http://kyleburton.github.com/projects/clj-etl-utils/"
  })
