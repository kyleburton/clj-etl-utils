(defproject
    org.clojars.kyleburton/clj-etl-utils
    "1.0.7"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :warn-on-reflection true
  :jvm-opts ["-Xmx512M"]
  :dev-dependencies [[swank-clojure "1.2.1"]]
  :dependencies [[org.clojure/clojure "1.1.0"]
                 [org.clojure/clojure-contrib "1.1.0"]
                 [commons-io "1.4"]
                 [log4j/log4j "1.2.14"]])
