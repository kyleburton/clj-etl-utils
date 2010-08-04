;; Based off of nu.xom.xinclude.EncodingHeuristics

(ns clj-etl-utils.io
  (:require [clojure.contrib.logging :as log])
  (:import [java.io FileInputStream File InputStreamReader]))


;; (BufferedReader. (InputStreamReader. (FileInputStream. file) (or encoding *file-encoding*)))

;; TODO: can fail for streams that don't support marking
(defn first-n-bytes-available [stream n-bytes]
  (let [res (atom [])]
    (try
     (if (.markSupported stream)
       (.mark stream 1024))
     (dotimes [nn n-bytes]
       (let [next-byte (.read stream)]
         (if (not (= -1 next-byte))
           (reset! res (conj @res next-byte)))))
     (finally
      ;; NB: this is no good for already created streams IOW one's
      ;; that can be recreated, b/c the goal is to advance past the
      ;; BOM and no further, so we need to consume at most 1 byte at a
      ;; time
      (if (.markSupported stream)
        (.reset stream))))
    @res))

;; from: http://unicode.org/faq/utf_bom.html
(def *utf-16be*
     {:encoding  "UTF-16BE"
      :name      :utf-16be
      :marker    "\u00FE\u00FF"
      :marker-bytes [0xFE 0xFF]})

(def *utf-16le*
     {:encoding  "UTF-16LE"
      :name      :utf-16le
      :marker    "\u00FF\u00FE"
      :marker-bytes [0xFF 0xFE]})

(def *utf-32be*
     {:encoding  "UTF-32BE"
      :name       :utf-32be
      :marker    "\u0000\u0000\u00FE\u00FF"
      :marker-bytes [0x00 0x00 0xFE 0xFF]})

(def *utf-32le*
     {:encoding  "UTF-32LE"
      :name      :utf-32le
      :marker    "\u00FF\u00FE\u0000\u0000"
      :marker-bytes [0xFF 0xFE 0x00 0x00]})

(def *utf-8*
     {:encoding  "UTF-8"
      :name      :utf-8
      :marker    "\u00EF\u00BB\u00BF"
      :marker-bytes [0xEF 0xBB 0xBF]})

(def *iso-8851-1*
     {:encoding  "ISO-8859-1"
      :name      :iso-8859-1
      :marker    ""
      :marker-bytes []})

(def *us-ascii*
     {:encoding  "US-ASCII"
      :name      :us-ascii
      :marker    ""
      :marker-bytes []})

(def *bom-markers*
     [*utf-32be*
      *utf-32le*
      *utf-16be*
      *utf-16le*
      *utf-8*])

(def *default-encoding* *iso-8851-1*)


(defn byte-marker-matches? [marker-bytes file-bytes]
  (cond
    (empty? marker-bytes) false
    (empty? file-bytes)   false
    :else
    (loop [[marker & marker-bytes] marker-bytes
           [byte & file-bytes]     file-bytes]
      (cond
        (or (not marker) (not byte))      true
        (= marker byte)                   (recur marker-bytes file-bytes)
        :else                             false))))

;; TODO: what if the stream doesn't support mark?
;; TODO: may return a false positive on arbitrary binary data
(defn detect-stream-encoding-via-bom [stream & [default-encoding]]
  (let [file-bytes (first-n-bytes-available stream 4)]
    (log/error (format "detect-stream-encoding-via-bom: stream=%s %s" stream file-bytes))
    (loop [[encoding & encodings] *bom-markers*]
      (cond
        (not encoding)
        ;; TODO: return the default encoding here
 	(or default-encoding *default-encoding*)
        (byte-marker-matches? (:marker-bytes encoding) file-bytes)
        encoding
        :else
        (recur encodings)))))




(defmulti detect-file-encoding-via-bom (fn [x & [default-encoding]] (class x)))

(defmethod detect-file-encoding-via-bom String [file & [default-encoding]]
  (detect-file-encoding-via-bom (File. file) default-encoding))

(defmethod detect-file-encoding-via-bom File [file & [default-encoding]]
  (with-open [inp (FileInputStream. file)]
    (detect-stream-encoding-via-bom inp default-encoding)))

(defmethod detect-file-encoding-via-bom :default [file & [default-encoding]]
  (throw (format "Error: fell through to :default for detect-stream-encoding-via-bom file=%s" file)))


(defn unicode-input-stream [#^String path]
  (InputStreamReader.
   (FileInputStream. path)
   (:encoding (detect-file-encoding-via-bom path))))


