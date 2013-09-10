(ns clj-etl-utils.json
  (:import
   [java.sql Timestamp]
   [java.io PrintWriter]
   [org.joda.time DateTime  ]
   [org.joda.time.format ISODateTimeFormat])
  (:use
   [clojure.contrib.json  :only [Write-JSON]]))

(extend java.sql.Timestamp Write-JSON
        {:write-json (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out
                               (.print (ISODateTimeFormat/dateTime )
                                       (org.joda.time.DateTime. x)))
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



(comment
  (.print (ISODateTimeFormat/dateTime ) (org.joda.time.DateTime. ))

  "2012-04-18T14:33:02.068-04:00"


  (rn.clorine.core/with-connection :wall_ro
    (.print
     (ISODateTimeFormat/dateTime )
     (org.joda.time.DateTime. (:created_at (rn-db.core/find-first-by :wall.actions {:id 5664})))))
  )
