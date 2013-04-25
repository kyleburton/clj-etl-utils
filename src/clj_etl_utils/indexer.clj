(ns ^{:doc "Indexing functions for working with delimited and fixed
    width files in situ, allowing them to be searched and iterated
    through in other than natural order, without having to load the
    data into a database."
      :author "Kyle Burton"}
  clj-etl-utils.indexer
  (:require [clojure.java.io              :as jio]
            [clojure.java.shell           :as sh]
            [clj-etl-utils.sequences      :as sequences]
            [clj-etl-utils.io             :as io])
  (:import [java.io RandomAccessFile]))


;; index line oriented files

;; TODO: convention for escaping the key (URI? - it could contain a
;; tab character...), handling null or empty values, when the key fn
;; throws an exception, etc.

;; TODO: consider updating or refreshing - incrementally, the index files

(defn read-lines [file-name]
  (with-open [rdr (clojure.java.io/reader file-name)]
    (file-seq rdr)))

(defn line-position-seq [^RandomAccessFile fp]
  (let [start-pos (.getFilePointer fp)
        line      (.readLine fp)
        end-pos   (.getFilePointer fp)]
    (if (nil? line)
      (do (.close fp)
          nil)
      (lazy-cat
       [[line start-pos end-pos]]
       (line-position-seq fp)))))

(defn line-index-seq [^RandomAccessFile fp key-fn]
  "Given a random access file (need not be positioned at the start)
and a key function (run on the line to compute the keys for the line)
this will return a sequence of:

   ([[key-value ...] line-start-pos line-end-pos] ...)

For all the lines in the file.

"
  (pmap (fn [[line start-pos end-pos]]
          [(key-fn line) start-pos end-pos])
        (line-position-seq fp)))

(comment

  (take 10 (line-index-seq
            (RandomAccessFile. "/home/superg/data/citi/relay_incremental_9_2010-10k-sample.rpt.fix" "r")
            (fn [line]
              [(str (.charAt line 0))])))

)

;; returns a sequnce of [key line-start-byte-pos line-endbyte-pos]
;; given a key-fn that takes a line of text and returns a string key that represents the line.

(defn file-index-seq [^String file ^clojure.lang.IFn key-fn]
  (line-index-seq  (RandomAccessFile. file "r") key-fn))


(defn extract-range [^RandomAccessFile fp start end]
  (.seek fp start)
  (let [data-bytes (byte-array (- end start))]
    (.read fp data-bytes)
    (String. data-bytes)))

;; NB: decide on sort behavior - string collation or numeric?  we're
;; going to shell out to GNU sort for this so that is a concern...
(defn create-index-file [^String input-file ^String index-file ^clojure.lang.IFn key-fn]
  ;; run the indexer (seq), emit to index-file
  ;; sort index-file
  (with-open [outp (jio/writer index-file)]
    (loop [[[kvals start end] & vals] (file-index-seq input-file key-fn)]
      (if (or (nil? kvals)
              (empty? kvals))
        true
        (do
          (doseq [val kvals]
           (.println outp (format "%s\t%s\t%s" val start end)))
          (recur vals))))))

;; NB: return value isn't taking into account error statuses
;; NB: will not work on platforms that don't have sort and mv, fix this...
(defn sort-index-file [^String index-file]
  (let [tmp    (java.io.File/createTempFile "idx-srt" "tmp")
        tmpnam (.getName tmp)]
   (sh/sh "sort" "-o" tmpnam index-file)
   (sh/sh "mv" tmpnam index-file))
  true)

(defn index-file! [^String input-file ^String index-file ^clojure.lang.IFn key-fn]
  (create-index-file input-file index-file key-fn)
  (sort-index-file index-file))


(comment

  (index-file! "file.txt" ".file.txt.id-idx" #(first (.split % "\t")))

)

;; TODO: this is splitting multiple times, rework to only split 1x
(defn index-blocks-seq [^String index-file]
  (map (fn [grp]
         (map (fn [l]
                (let [[val spos epos] (.split l "\t")]
                  [val (Integer/parseInt spos) (Integer/parseInt epos)]))
              grp))
       (sequences/group-with (fn [l]
                               (first (.split l "\t")))
                             (read-lines index-file))))
(comment
  (with-open [rdr  (clojure.java.io/reader "Changes" )]
    (vec (line-seq rdr)))



  )
;; This is the new form of above (only call split 1x), needs to be tested
#_(defn index-blocks-seq [^String index-file]
    (sequences/group-with
     first
     (map
      (fn [l]
        (let [[val spos epos] (.split l "\t")]
          [val (Long/parseLong spos) (Long/parseLong epos)]))
      (read-lines index-file))))

(defn records-for-idx-block ^String [inp-file idx-block]
  (loop [recs []
         [[k start-pos end-pos] & idx-block] idx-block]
    (if (not k)
      recs
      (recur
       ;; NB: range should be 1 and only 1 line/record
       (conj recs (first (vec (io/read-lines-from-file-segment inp-file start-pos end-pos))))
       idx-block))))

(comment

  (records-for-idx-block "file.txt" [["99" 0 24] ["99" 88 115]])

)

;; TODO: building an index has to stream the records, if we are to
;; build N indicies we will have to stream the records N times, modify
;; the implementation such that we can create multiple indicies by
;; streaming only one time...

(defn record-blocks-via-index [^String inp-file ^String index-file]
  "Given an data file and an index file, this stream through the distinct
index values returning records from the data file."
  (map (partial records-for-idx-block inp-file)
       (index-blocks-seq index-file)))

;;   ((["1" 24 48]) (["2" 48 65]) (["3" 65 88]) (["99" 0 24] ["99" 88 115]))

(comment

  (record-blocks-via-index "file.txt" ".file.txt.id-idx")

)

;; TODO: Implement mutiple index block streaming (grouping across
;; mutiple data files).

(comment


  (index-file! "file.txt" ".file.txt.id-idx" #(first (.split % "\t")))
  (record-blocks-via-index "file.txt" ".file.txt.id-idx")

  )

;; TODO: Implement the binary search on the index

(comment

  (index-search "file.txt" ".file.txt.id-idx" "99")

)

