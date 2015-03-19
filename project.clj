(defproject com.github.kyleburton/clj-etl-utils "1.0.94"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :lein-release {:deploy-via :clojars :scm :git}
  :license {:name         "Eclipse Public License - v 1.0"
            :url          "http://www.eclipse.org/legal/epl-v10.html"
            :distribution :repo
            :comments     "Same as Clojure"}
  :repositories         {"sonatype-oss-public" "https://oss.sonatype.org/content/groups/public/"}
  :java-source-path     "java"
  :local-repo-classpath true
  :autodoc {
    :name        "clj-etl-utils"
    :page-title  "clj-etl-utils: API Documentation"
    :description "ETL Utilites for Clojure"
    :web-home    "http://kyleburton.github.com/projects/clj-etl-utils/"
  }

  :profiles             {:dev {:dependencies [[swank-clojure "1.4.3"]
                                              [lein-marginalia "0.7.1"]]}
                         :1.3 {:dependencies [[org.clojure/clojure "1.3.0"]
                                              [org.clojure/data.json      "0.2.3"]]}
                         :1.4 {:dependencies [[org.clojure/clojure "1.4.0"]
                                              [org.clojure/data.json      "0.2.3"]]}
                         :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]
                                              [org.clojure/data.json      "0.2.3"]]}
                         :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]
                                              [org.clojure/data.json      "0.2.3"]]}}
  :aliases              {"all" ["with-profile" "dev,1.2:dev,1.3:dev,1.4:dev,1.5:dev,1.6"]}
  :global-vars          {*warn-on-reflection* true}
  :dependencies         [[commons-io                 "2.0"]
                         [log4j/log4j                "1.2.14"]
                         [org.mindrot/jbcrypt        "0.3m"]
                         [commons-codec              "1.4"]
                         [commons-lang               "2.5"]
                         [org.clojure/data.csv       "0.1.2"]
                         [org.clojure/tools.logging  "0.2.6"]
                         [clj-time                   "0.7.0"
                           :exclusions [org.clojure/clojure]]])
