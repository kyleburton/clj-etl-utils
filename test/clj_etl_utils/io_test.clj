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
    (is (= [97 97 97 97]  (io/first-n-bytes-available rdr 4))))
  (with-open [rdr (java.io.FileReader. (fixture-file "sample.in.txt"))]
    (is (= [76 111 114 101] (io/first-n-bytes-available rdr 4)))))

;; (test-first-n-bytes-available)

(deftest test-byte-marker-matches?
  (is (not (io/byte-marker-matches? []    [])))
  (is (not (io/byte-marker-matches? [1]   [0])))
  (is      (io/byte-marker-matches? [1]   [1]))
  (is      (io/byte-marker-matches? [1 2] [1 2 3])))

;; (test-byte-marker-matches?)

(deftest test-detect-file-encoding-via-bom
  (is
   (= "ISO-8859-1"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.in.txt")))))
  (is
   (= "US-ASCII"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.in.txt") io/*us-ascii*))))
  (is
   (= "UTF-8"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-8.txt")))))
  (is
   (= "UTF-8"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-8.txt") io/*us-ascii*))))
  (is
   (= "UTF-16LE"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-16le.txt")))))
  (is
   (= "UTF-16BE"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-16be.txt")))))
  (is
   (= "UTF-32LE"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-32le.txt")))))
  (is
   (= "UTF-32BE"
      (:encoding (io/detect-file-encoding-via-bom
                  (fixture-file "sample.utf-32be.txt"))))))

;; (io/detect-file-encoding-via-bom (fixture-file "sample.utf-32be.txt"))

;; (test-detect-file-encoding-via-bom)

;; (deftest read-files
;;   (let [ascii-txt (fixture-file-contents "sample.in.txt")
;;         utf8-txt  (fixture-file-contents "sample.utf-8.txt")]
;;     (is (= ascii-txt utf8-txt))))
