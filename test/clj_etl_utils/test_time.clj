(ns clj-etl-utils.test-time
  (:use
   clojure.test)
  (:require
   [clj-etl-utils.time :as time]))


(deftest date-seq
  (is (= 1 (count (time/date-seq
                   (org.joda.time.DateTime. "2014-05-06T12:59:59Z")
                   (org.joda.time.DateTime. "2014-05-06T12:59:59Z")))))
  (is (= 3 (count (time/date-seq
                   (org.joda.time.DateTime. "2014-05-06T12:59:59Z")
                   (org.joda.time.DateTime. "2014-05-08T12:59:59Z"))))))

(deftest test-mins-between-end-and-start
  (is (zero?   (time/mins-between "13:00" "13:00")))
  (is (= 1     (time/mins-between "13:00" "13:01")))
  (is (= 361   (time/mins-between "13:00" "19:01")))
  (is (= 360   (time/mins-between "23:00" "05:00"))))

(deftest test-business-hours
  ;; 1 min before 9am
  (is (not (time/during-business-hours?
            (org.joda.time.DateTime. "2014-05-06T12:59:59Z")
            "09:00" "22:00" "EDT")))
  ;; 9am
  (is (time/during-business-hours?
       (org.joda.time.DateTime. "2014-05-06T13:00:00Z")
       "09:00" "22:00" "EDT"))
  ;; 1 min before 10pm
  (is (time/during-business-hours?
       (org.joda.time.DateTime. "2014-05-06T01:59:59Z")
       "09:00" "22:00" "EDT"))
  ;; at 10pm
  (is (not (time/during-business-hours?
            (org.joda.time.DateTime. "2014-05-06T02:00:00Z")
            "09:00" "22:00" "EDT"))))
