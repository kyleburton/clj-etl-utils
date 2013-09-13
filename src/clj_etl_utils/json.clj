(ns clj-etl-utils.json
  (:import
   [java.sql Timestamp]
   [java.io PrintWriter]
   [org.joda.time DateTime  ]
   [org.joda.time.format ISODateTimeFormat])
  (:use
   [clojure.data.json  :only [JSONWriter]]))

(extend java.sql.Timestamp JSONWriter
        {:-write (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out
                               (.print (ISODateTimeFormat/dateTime )
                                       (org.joda.time.DateTime. x)))
                       (.print out "\""))})

(extend org.joda.time.DateTime JSONWriter
        {:-write (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})

(extend java.util.Date JSONWriter
        {:-write (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString (org.joda.time.DateTime. x)))
                       (.print out "\""))})

(extend clojure.lang.Fn JSONWriter
        {:-write (fn [x #^PrintWriter out]
                       (.print out "\"")
                       (.print out (.toString x))
                       (.print out "\""))})



(comment
  (org.joda.time.DateTime. (java.util.Date.))
  (clojure.data.json/json-str (java.util.Date.))
  (clojure.data.json/json-str (java.sql.Timestamp. (.getTime (java.util.Date.))))

  (.print
   (ISODateTimeFormat/dateTime)
   (org.joda.time.DateTime. "2013-09-12"))
  )
