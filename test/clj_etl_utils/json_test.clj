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
       json/read-str
       tformat/parse)
      (->
       "2013-09-15T12:55:04.884-04:00"
       tformat/parse))))




