(ns clj-etl-utils.indexer
  (:require [clojure.contrib.duck-streams :as ds]
            [clojure.contrib.shell-out    :as sh]
            [clj-etl-utils.sequences      :as sequences])
  (:import [java.io RandomAccessFile]))

;; index line oriented files

(defn line-index-seq [#^RandomAccessFile fp key-fn]
  (let [start-pos (.getFilePointer fp)
        line      (.readLine fp)
        end-pos   (.getFilePointer fp)
        key       (if (nil? line) nil (key-fn line))]
    (if (nil? line)
      (do (.close fp)
          nil)
      (lazy-cat
       [[key start-pos end-pos]]
       (line-index-seq fp key-fn)))))


;; returns a sequnce of [key line-start-byte-pos line-endbyte-pos]
;; given a key-fn that takes a line of text and returns a string key that represents the line.

(defn file-index-seq [#^String file #^IFn key-fn]
  (line-index-seq  (RandomAccessFile. file "r") key-fn))

(defn extract-range [#^RandomAccessFile fp start end]
  (.seek fp start)
  (let [data-bytes (byte-array (- end start))]
    (.read fp data-bytes)
    (String. data-bytes)))

;; NB: decide on sort behavior - string collation or numeric?  we're
;; going to shell out to GNU sort for this so that is a concern...
(defn create-index-file [#^String input-file #^String index-file #^IFn key-fn ]
  ;; run the indexer (seq), emit to index-file
  ;; sort index-file
  (with-open [outp (ds/writer index-file)]
    (loop [[[val start end] & vals] (file-index-seq input-file key-fn)]
      (if (nil? val)
        true
        (do
         (.println outp (format "%s\t%s\t%s" val start end))
         (recur vals))))))

;; NB: return value isn't taking into account error statuses
;; NB: will not work on platforms that don't have sort and mv, fix this...
(defn sort-index-file [#^String index-file]
  (let [tmp    (java.io.File/createTempFile "idx-srt" "tmp")
        tmpnam (.getName tmp)]
   (sh/sh "sort" "-o" tmpnam index-file)
   (sh/sh "mv" tmpnam index-file))
  true)

(defn index-file! [#^String input-file #^String index-file #^IFn key-fn]
  (create-index-file input-file index-file key-fn)
  (sort-index-file index-file))


(comment

  (index-file! "file.txt" ".file.txt.id-idx" #(first (.split % "\t")))

)

(defn index-blocks-seq [#^String index-file]
  (sequences/group-with (fn [l]
                          (first (.split l "\t")))
                        (ds/read-lines index-file)))

(comment
  (index-blocks-seq ".file.txt.id-idx")
)


;; TODO: Implement returning groups of records based on clustered
;; index values (remember, multiple records can have the same index
;; value).

(defn record-blocks-via-index [#^String inp-file #^String index-file]
  (raise "implement this..."))



;; TODO: Implement the binary search on the index

;; TODO: Implement mutiple index block streaming (grouping across
;; mutiple data files).

(comment

  (extract-range (RandomAccessFile. "file.txt" "r") 0 24)
  (file-index-seq "file.txt" #(first (.split % "\t")))
  (["99" 0 24] ["1" 24 48] ["2" 48 65] ["3" 65 88])


)
