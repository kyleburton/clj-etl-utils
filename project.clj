(defproject com.github.kyleburton/clj-etl-utils "1.0.99-SNAPSHOT"
  :description "ETL Utilities"
  :url         "http://github.com/kyleburton/clj-etl-utils"
  :license      {:name         "Eclipse Public License - v 1.0"
                 :url          "http://www.eclipse.org/legal/epl-v10.html"
                 :distribution :repo
                 :comments     "Same as Clojure"}
  :deploy-repositories [
                 ["releases"  {:url "https://clojars.org/repo" :creds :gpg}]
                 ["snapshots" {:url "https://clojars.org/repo" :creds :gpg}]]
  :java-source-path     "java"
  :local-repo-classpath true
  :autodoc {
    :name        "clj-etl-utils"
    :page-title  "clj-etl-utils: API Documentation"
    :description "ETL Utilites for Clojure"
    :web-home    "http://kyleburton.github.com/projects/clj-etl-utils/"
  }
  :main ^:skip-aot clj-etl-utils.repl

  :global-vars          {*warn-on-reflection* true}
  :profiles {:dev {:resource-paths ["dev-resources"]
                   :dependencies   [
                                    [org.clojure/clojure          "1.8.0"]
                                    [org.clojure/tools.nrepl      "0.2.12"]
                                    [cider/cider-nrepl            "0.13.0"]
                                    [prismatic/schema             "1.1.3"]
                                    ]}}
  :dependencies         [[commons-io/commons-io                  "2.5"]
                         [org.clojure/tools.logging              "0.3.1"]
                         [ch.qos.logback/logback-classic         "1.0.13"]
                         [org.mindrot/jbcrypt                    "0.4"]
                         [commons-codec/commons-codec            "1.10"]
                         [commons-lang/commons-lang              "2.6"]
                         [org.clojure/data.csv                   "0.1.4"]
                         [org.clojure/data.json                  "0.2.6"]
                         [clj-time                               "0.14.0"]])
