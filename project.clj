(defproject org.clojars.kyleburton/clj-etl-utils "1.0.17"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :warn-on-reflection true
  :aot [clj-etl-utils.io
        clj-etl-utils.lang-utils
        clj-etl-utils.regex
        clj-etl-utils.sequences
        clj-etl-utils.ref-data
        clj-etl-utils.text
        clj-etl-utils.landmark-parser
        clj-etl-utils.http
        clj-etl-utils.log]
  :jvm-opts ["-Xmx512M"]
  :dev-dependencies [[swank-clojure "1.2.1"]]
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [commons-io "2.0"]
                 [fleet "0.9.3"]
                 [log4j/log4j "1.2.14"]
                 [commons-httpclient "3.1"]
                 [commons-lang "2.5"]])
