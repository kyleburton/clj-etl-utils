(defproject com.github.kyleburton/clj-etl-utils "1.0.99"
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

  :global-vars          {*warn-on-reflection* true}
  :profiles {:dev {:resource-paths ["dev-resources"]
                   :dependencies   [
                                    [org.clojure/clojure          "1.12.0"]
                                    [prismatic/schema             "1.1.3"]
                                    [nrepl/nrepl                  "1.0.0"]
                                    [cider/cider-nrepl            "0.52.1"]
                                    ]}}
  :dependencies         [[commons-io/commons-io                  "2.5"]
                         [org.clojure/tools.logging              "1.3.0"]
                         [ch.qos.logback/logback-classic         "1.2.11"]
                         [org.mindrot/jbcrypt                    "0.4"]
                         [commons-codec/commons-codec            "1.10"]
                         [commons-lang/commons-lang              "2.6"]
                         [org.clojure/data.csv                   "0.1.4"]
                         [org.clojure/data.json                  "2.5.1"]
                         [clj-time                               "0.14.0"]])
