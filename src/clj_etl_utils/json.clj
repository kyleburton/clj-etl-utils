(ns clj-etl-utils.json
  (:import
   [java.sql Timestamp]
   [java.io PrintWriter])
  (:use
   [clojure.contrib.json  :only [Write-JSON]]))

(extend java.sql.Timestamp Write-JSON
        {:write-json (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})

(extend org.joda.time.DateTime Write-JSON
        {:write-json (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})

(extend java.util.Date Write-JSON
        {:write-json (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})

(extend clojure.lang.Fn Write-JSON
        {:write-json (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})
