(ns clj-etl-utils.test-text
  (:use
   clojure.test)
  (:require
   [clj-etl-utils.text :as text]))


(comment



)


(deftest nil-and-emtpy-default
  (is (= nil (text/trim-and-truncate nil 100)))
  (is (= nil (text/trim-and-truncate "" 100)))
  (is (= "banana" (text/trim-and-truncate "" 100 "banana"))))

(deftest blanks-are-trimmed
  (is (= nil          (text/trim-and-truncate "  " 100)))
  (is (= "First Name" (text/trim-and-truncate " First Name " 100))))

(deftest truncates-long-strings
  (is (=
       "First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  Firs"
       (text/trim-and-truncate " First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name  First Name " 100)))
  (is (=
       "First Name"
       (text/trim-and-truncate "First Name                                                                                                                                                                                                          xxx I should be removed." 100))))