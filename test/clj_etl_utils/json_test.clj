(ns clj-etl-utils.json-test
  (:require clj-etl-utils.json
            [clojure.data.json :as json])
  (:use clojure.test))


(deftest test-json-date-produces-iso8601-format
  (is (= (json/json-str (java.util.Date. 1379264104884))
         "\"2013-09-15T12:55:04.884-04:00\"")))






