(ns clj-etl-utils.io-test
  (:require [clj-etl-utils.io :as io])
  (:use [clojure.test]
        [clj-etl-utils.test-helper]))

(deftest test-first-n-bytes-available
  (let [rdr (java.io.StringReader. "")]
    (is (= [] (io/first-n-bytes-available rdr 0)))
    (is (= [] (io/first-n-bytes-available rdr 1)))
    (is (= [] (io/first-n-bytes-available rdr 2)))
    (is (= [] (io/first-n-bytes-available rdr 3))))
  (let [rdr (java.io.StringReader. "a")]
    (is (= []   (io/first-n-bytes-available rdr 0)))
    (is (= [97] (io/first-n-bytes-available rdr 1)))
    (is (= [97] (io/first-n-bytes-available rdr 2)))
    (is (= [97] (io/first-n-bytes-available rdr 3))))
  (let [rdr (java.io.StringReader. "aaaaaaaaaaa")]
    (is (= []             (io/first-n-bytes-available rdr 0)))
    (is (= [97]           (io/first-n-bytes-available rdr 1)))
    (is (= [97 97]        (io/first-n-bytes-available rdr 2)))
    (is (= [97 97 97]     (io/first-n-bytes-available rdr 3)))
    (is (= [97 97 97 97]  (io/first-n-bytes-available rdr 4)))))


'(deftest read-files
  (let [ascii-txt (fixture-file-contents "sample.in.txt")
        utf8-txt  (fixture-file-contents "sample.utf-8.txt")]
    (is (= ascii-txt utf8-txt))))
