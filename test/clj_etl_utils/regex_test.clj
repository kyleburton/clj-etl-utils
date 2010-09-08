(ns clj-etl-utils.regex-test
  (:require [clj-etl-utils.regex :as rx])
  (:use [clojure.test]
        [clj-etl-utils.test-helper]))


(defn us-state-match-found [data]
  (let [regex (:us-states rx/*common-regexes*)]
    (.matches (.matcher regex data))))


(deftest test-us-state-regex
  (is (not (us-state-match-found "xx")))
  (is (us-state-match-found "NJ"))
  (is (us-state-match-found "nJ"))
  (is (not (us-state-match-found "")))
  (is (us-state-match-found "pa"))
  (is (us-state-match-found "NJ"))
  (is (us-state-match-found "AE"))
  (is (not (us-state-match-found "not"))))

;; (test-us-state-regex)




