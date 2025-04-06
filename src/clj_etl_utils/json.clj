(ns clj-etl-utils.json
  (:import
   [java.sql Timestamp]
   [java.io Writer]
   [org.joda.time DateTime]
   [org.joda.time.format ISODateTimeFormat])
  (:require
   [clojure.data.json :as json]))

(defn -write-quoted-string [^String s #^Writer out options]
  (.write out "\"")
  (.write out s)
  (.write out "\""))

(defn -write-as-string [obj #^Writer out options]
  (-write-quoted-string (str obj) out options))

(def ^java.text.SimpleDateFormat java-util-time-iso8601-formatter
  (let [tz (java.util.TimeZone/getTimeZone "UTC")
        df (java.text.SimpleDateFormat. "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")]
    (.setTimeZone df tz)
    df))

(defn -write-java-sql-timestamp [^java.sql.Timestamp x #^Writer out options]
  (-write-as-string (.format java-util-time-iso8601-formatter x) out options))
(extend java.sql.Timestamp json/JSONWriter
        {:-write -write-java-sql-timestamp})

(extend org.joda.time.DateTime json/JSONWriter
        {:-write -write-as-string})

(defn -write-java-util-date [^java.util.Date x #^Writer out options]
  (-write-as-string (.format java-util-time-iso8601-formatter x) out options))
(extend java.util.Date json/JSONWriter
        {:-write -write-java-util-date})

(extend clojure.lang.Fn json/JSONWriter
        {:-write -write-as-string})

(defn java-time-local-date-time-to-iso8601-str [^java.time.LocalDateTime dtime]
  (str (.format dtime java.time.format.DateTimeFormatter/ISO_DATE_TIME) "Z"))

(comment

  (java-time-local-date-time-to-iso8601-str
   (java.time.LocalDateTime/ofInstant
    (java.time.Instant/ofEpochMilli 1379264104884)
    (.toZoneId (java.util.TimeZone/getTimeZone "UTC"))))

  )

(defn -write-java-time-local-date-time [^java.time.LocalDateTime x #^Writer out options]
  (-write-quoted-string
   (java-time-local-date-time-to-iso8601-str x)
   out
   options))
(extend
    java.time.LocalDateTime json/JSONWriter
    {:-write -write-java-time-local-date-time})

(defn java-time-zoned-date-time-to-iso8601-str [^java.time.ZonedDateTime dtime]
  (.format (.toOffsetDateTime dtime) java.time.format.DateTimeFormatter/ISO_ZONED_DATE_TIME))

(comment
  (java-time-zoned-date-time-to-iso8601-str
   (java.time.ZonedDateTime/ofInstant
    (java.time.Instant/ofEpochMilli 1379264104884)
    (.toZoneId (java.util.TimeZone/getTimeZone "UTC"))))
  )

(defn -write-java-time-zoned-date-time [^java.time.ZonedDateTime x #^Writer out options]
  (-write-quoted-string
   (java-time-zoned-date-time-to-iso8601-str x)
   out
   options))
(extend java.time.ZonedDateTime json/JSONWriter
        {:-write -write-java-time-zoned-date-time})

(comment
  (org.joda.time.DateTime. (java.util.Date.))
  (clojure.data.json/json-str (java.util.Date.))
  (clojure.data.json/json-str (java.sql.Timestamp. (.getTime (java.util.Date.))))
  java.time.DateTime

  (clojure.data.json/json-str
   (java.time.LocalDateTime/ofInstant
    (java.time.Instant/ofEpochMilli 1379264104884)
    (.toZoneId (java.util.TimeZone/getTimeZone "UTC"))))

  (clojure.data.json/json-str
   (java.time.ZonedDateTime/ofInstant
    (java.time.Instant/ofEpochMilli 1379264104884)
    (.toZoneId (java.util.TimeZone/getTimeZone "UTC"))))

  (.print
   (ISODateTimeFormat/dateTime)
   (org.joda.time.DateTime. "2013-09-12"))
  )
