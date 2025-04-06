(ns clj-etl-utils.json-test
  (:require
   clj-etl-utils.json
   [clojure.data.json :as json]
   [clj-time.core     :as time]
   [clj-time.format   :as tformat])
  (:use clojure.test))


(deftest test-json-date-produces-iso8601-format
  (is
   (= (->
       (java.util.Date. 1379264104884)
       json/json-str
       json/read-str)
      (->
       "2013-09-15T16:55:04.884Z")))
  (is
   (= (->
       (java.sql.Timestamp. 1379264104884)
       json/json-str
       json/read-str)
      (->
       "2013-09-15T16:55:04.884Z")))
  (is
   (= (->
       (org.joda.time.DateTime. 1379264104884)
       json/json-str
       json/read-str
       tformat/parse
       str)
      "2013-09-15T16:55:04.884Z"))
  (is
   (= (->
       (java.time.LocalDateTime/ofInstant
        (java.time.Instant/ofEpochMilli 1379264104884)
        (.toZoneId (java.util.TimeZone/getTimeZone "UTC")))
       json/json-str
       json/read-str
       tformat/parse
       str)
      "2013-09-15T16:55:04.884Z"))
  (is
   (= (->
       (java.time.ZonedDateTime/ofInstant
        (java.time.Instant/ofEpochMilli 1379264104884)
        (.toZoneId (java.util.TimeZone/getTimeZone "UTC")))
       json/json-str
       json/read-str
       tformat/parse
       str)
      "2013-09-15T16:55:04.884Z")))
