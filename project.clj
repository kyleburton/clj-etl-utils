(defproject org.clojars.kyleburton/clj-etl-utils "1.0.25"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license {:name "Eclipse Public License - v 1.0"
            :url "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments "same as Clojure"}
  :warn-on-reflection true
  :jvm-opts ["-Xmx512M"]
  :dev-dependencies [[swank-clojure "1.3.0"]
                     ;;[autodoc "0.7.1"]
                     ]
  :dependencies [[org.clojure/clojure "1.2.0"]
                 [org.clojure/clojure-contrib "1.2.0"]
                 [commons-io "2.0"]
                 [fleet "0.9.3"]
                 [log4j/log4j "1.2.14"]
                 [commons-httpclient "3.1"]
                 [commons-lang "2.5"]]
  :autodoc {
    :name "clj-etl-utils"
    :page-title "clj-etl-utils: API Documentation"
    :description "ETL Utilites for Clojure"
    :web-home "http://kyleburton.github.com/projects/clj-etl-utils/"
  })
