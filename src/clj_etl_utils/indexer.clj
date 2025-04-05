(ns ^{:doc "Indexing functions for working with delimited and fixed
    width files in situ, allowing them to be searched and iterated
    through in other than natural order, without having to load the
    data into a database."
      :author "Kyle Burton"}
  clj-etl-utils.indexer
  (:require
   [clojure.java.shell           :as sh]
   [clojure.string               :as string]
   [clj-etl-utils.sequences      :as sequences]
   [clj-etl-utils.io             :as io]
   [clojure.java.io              :as cljio])
  (:import
   [java.io RandomAccessFile FileInputStream InputStreamReader BufferedReader]
   [org.apache.commons.io.input BoundedInputStream]))

;; index line oriented files


;; TODO: convention for escaping the key (URI? - it could contain a
;; tab character...), handling null or empty values, when the key fn
;; throws an exception, etc.

;; TODO: consider updating or refreshing - incrementally, the index files

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

(defn line-index-seq
  "Given a random access file (need not be positioned at the start)
and a key function (run on the line to compute the keys for the line)
this will return a sequence of:

   ([[key-value ...] line-start-pos line-end-pos] ...)

For all the lines in the file.

"
  [^RandomAccessFile fp key-fn]
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

(defn file-index-seq [^String file key-fn]
  (line-index-seq  (RandomAccessFile. file "r") key-fn))

(defn extract-range [^RandomAccessFile fp start end]
  (.seek fp start)
  (let [data-bytes (byte-array (- end start))]
    (.read fp data-bytes)
    (String. data-bytes)))

;; NB: decide on sort behavior - string collation or numeric?  we're
;; going to shell out to GNU sort for this so that is a concern...
(defn create-index-file [^String input-file ^String index-file key-fn]
  ;; run the indexer (seq), emit to index-file
  ;; sort index-file
  (with-open [^java.io.Writer outp (cljio/writer index-file)]
    (loop [[[kvals start end] & vals] (file-index-seq input-file key-fn)]
      (if (or (nil? kvals)
              (empty? kvals))
        true
        (do
          (doseq [val kvals]
            (.write outp (format "%s\t%s\t%s" val start end))
            (.write outp "\n"))
          (recur vals))))))

;; NB: return value isn't taking into account error statuses
;; NB: will not work on platforms that don't have sort and mv, fix this...
(defn sort-index-file [^String index-file]
  (let [tmp    (java.io.File/createTempFile "idx-srt" "tmp")
        tmpnam (.getName tmp)]
    (sh/sh "sort" "-o" tmpnam index-file
           :env {"LANG" "C"})
    (sh/sh "mv" tmpnam index-file))
  true)

(defn index-file! [^String input-file ^String index-file key-fn]
  (create-index-file input-file index-file key-fn)
  (sort-index-file index-file))


(comment

  (defn rand-elt [s]
    (let [idx (mod (.nextInt (java.util.Random.))
                   (count s))]
      (nth s idx)))

  (require 'clj-etl-utils.ref-data)
  (let [rnd (java.util.Random.)
        states (vec (map first clj-etl-utils.ref-data/*us-states*))]
    (with-open [wtr (java.io.PrintWriter. "file.txt")]
      (dotimes [ii 100]
        (.println wtr
                  (str
                   (rand-elt states)
                   "\t"
                   (.nextInt rnd))))))

  (index-file! "file.txt" ".file.txt.id-idx"
               (fn [l]
                 [(.toLowerCase (first (.split l "\t")))]))

  )

;; TODO: this is splitting multiple times, rework to only split 1x
(defn index-blocks-seq [^String index-file]
  (map (fn [grp]
         (map (fn [^String l]
                (let [[val spos epos] (.split l "\t")]
                  [val (Integer/parseInt spos) (Integer/parseInt epos)]))
              grp))
       (sequences/group-with (fn [^String l]
                               (first (.split l "\t")))
                             (io/lazy-read-lines index-file))))

;; This is the new form of above (only call split 1x), needs to be tested
#_(defn index-blocks-seq [^String index-file]
    (sequences/group-with
     first
     (map
      (fn [l]
        (let [[val spos epos] (.split l "\t")]
          [val (Long/parseLong spos) (Long/parseLong epos)]))
      (io/lazy-read-lines index-file))))



(comment
  (index-blocks-seq ".file.txt.id-idx")
  ((["1" 24 48]) (["2" 48 65]) (["3" 65 88]) (["99" 0 24] ["99" 88 115]))
  )


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

(defn record-blocks-via-index
  "Given an data file and an index file, this stream through the distinct
index values returning records from the data file."
  [^String inp-file ^String index-file]
  (map (partial records-for-idx-block inp-file)
       (index-blocks-seq index-file)))

;;   ((["1" 24 48]) (["2" 48 65]) (["3" 65 88]) (["99" 0 24] ["99" 88 115]))

(comment

  (record-blocks-via-index "file.txt" ".file.txt.id-idx")

  )

;; 1 MB
(def min-streaming-threshold (* 1024 1024 1))

;; matcher = (fn [index-val term]) => bool
(defn index-search
  ([idx-file term]
     (index-search idx-file term =))
  ([idx-file term matcher]
     (with-open [rdr (cljio/reader idx-file)]
       (index-search idx-file term matcher rdr)))
  ([idx-file term matcher ^java.io.BufferedReader rdr]
     (loop [line (.readLine rdr)
            res  []]
       (if (nil? line)
         res
         (let [[v s e] (.split line "\t" 3)
               direction (compare v term)]
           (cond
             (matcher v term)
             (recur (.readLine rdr)
                    (conj res [v (Long/parseLong s) (Long/parseLong e)]))

             (pos? direction)
             (do
               #_(println (format "direction was positive, indicating we've gone past: (compare \"%s\" \"%s\") %d"
                                  v term direction))
               res)

             :continue
             (recur (.readLine rdr) res)))))))

;; NB: this only works with \n (byte value 10) as a line separator
(defn rewind-to-newline [^RandomAccessFile fp min-pos]
  #_(println "rewind-to-newline: remove the max iters")
  (loop [] ;; max-iters 10000
    (let [b  (.readByte fp)
          ch (Byte/toString b)]
      (cond
        ;; (<= max-iters 0)
        ;; (raise "Too much recursion")

        ;; (int (aget (.getBytes "a") 0))
        (= (int b) 10)
        (do
          #_(println (format "rewind-to-newline: Found newline at %d" (.getFilePointer fp)))
          true)

        (<= (.getFilePointer fp) min-pos)
        (do
          #_(println (format "rewind-to-newline: Rewound to start"))
          (.seek fp 0)
          true)

        :rewind
        (do
          #_(println (format "rewind-to-newline: [%d/%s] Did not find newline at %d, going back to %d"
                             (.intValue b)
                             ch
                             (.getFilePointer fp)
                             (- (.getFilePointer fp) 2)))
          (.seek fp (- (.getFilePointer fp) 2))
          ;; (recur (dec max-iters))
          (recur))))))


;; spos must point at either the start of the file, or the beginning of a line
;; epos must point at either the end of the file, or a newline
(defn index-search-prefix-impl [^String idx-file ^String term spos epos]
  #_(println (format "index-search-prefix-impl %s %s %d %d"
                     idx-file term spos epos))
  (if (<= (- epos spos) min-streaming-threshold)
    (with-open [rdr (BufferedReader.
                     (InputStreamReader.
                      (BoundedInputStream.
                       (doto (FileInputStream. idx-file)
                         (.skip spos))
                       (- epos spos))))]
      #_(println (format "before binary search, spos=%d to epos=%d under THRESH, falling back to streaming search" spos epos))
      (index-search idx-file
                    term
                    (fn [^String idx-val ^String term]
                      (.startsWith idx-val term))
                    rdr))
    (with-open [fp (RandomAccessFile. idx-file "r")]
      (loop [;;max-iters 25
             spos   spos
             epos   epos
             middle (long (/ (- epos spos) 2))]
        #_(println (format "loop: spos=%d epos=%d middle=%d" spos epos middle))
        ;; (when (<= max-iters 0)
        ;;   (raise "too much recursion"))
        (.seek fp middle)
        (rewind-to-newline fp spos)
        (let [middle  (.getFilePointer fp)
              line    (.readLine fp)
              [iterm bstart bend] (.split line "\t" 3)
              order   (compare term iterm)]
          #_(println (format "Looking at[%d] line=%s"
                             (.getFilePointer fp)
                             line))
          (cond
            (<= (- epos spos) min-streaming-threshold)
            (with-open [rdr (BufferedReader.
                             (InputStreamReader.
                              (BoundedInputStream.
                               (doto (FileInputStream. idx-file)
                                 (.skip spos))
                               (- epos spos))))]
              #_(println (format "in binary search, spos=%d to epos=%d under THRESH, falling back to streaming search" spos epos))
              (index-search
               idx-file
               term
               (fn [^String idx-val ^String term]
                 #_(println (format "(.startsWith \"%s\" \"%s\") => %s"
                                    idx-val term (.startsWith idx-val term)))
                 (.startsWith idx-val term))
               rdr))

            (neg? order)
            (do
              #_(println (format "order was: %d, go left" order))
              (recur ;; (dec max-iters)
               spos
               middle
               (long (- middle  (/ (- middle spos) 2)))))

            (zero? order)
            (do
              #_(println (format "order was: %d, we're in the block, need to find the start" order)))

            (pos? order)
            (do
              #_(println (format "order was: %d, go right" order))
              (recur
               ;; (dec max-iters)
               middle
               epos
               (long (- epos  (/ (- epos middle) 2)))))))))))


(defn index-search-prefix [^String idx-file ^String term]
  ;; binary search the file
  ;; open a RandomAccessFile
  ;; start=0 end=LEN
  ;; if (- end len) < THRESH, just stream through the section
  ;; jump to the middle, rewind to '\n'
  (let [epos (.length (java.io.File. idx-file))]
    (if (<= epos min-streaming-threshold)
      (do
        #_(println (format "file size %d < thresh %d, falling back to streaming"
                           epos min-streaming-threshold))
        (index-search idx-file term #(= %1 %2)))
      (index-search-prefix-impl idx-file term 0 epos))))


(defn index-search-file
  ([^String input-file ^String index-file term]
     (index-search-file input-file index-file term =))
  ([^String input-file ^String index-file term matcher]
     (map
      (fn [[v s e]]
        (first (vec (io/read-lines-from-file-segment input-file s e))))
      (filter
       (fn [[v s e]]
         (matcher v term))
       (index-search-prefix index-file term)))))

(comment


  (index-search-file "file.txt" ".file.txt.id-idx" "IA")


  (index-file! "file.txt" ".file.txt.id-idx" (fn [line] [(first (.split line "\t"))]))
  (record-blocks-via-index "file.txt" ".file.txt.id-idx")


  )


;; (defn csv-parse [^String s]
;;   (with-in-str s
;;     (first (csv/read-csv *in*))))

(defn index-file-path ^String [src idx-name]
  (let [^String fname (-> src :config :file)
        src-file      (java.io.File. fname)]
    (format "%s/.%s.%s-idx"
            (.getParent src-file)
            (.getName src-file)
            (name idx-name))))

(defn ensure-indexes [src]
  (doseq [[idx-name idx] (-> src :config :indexes)]
    (println (format "idx:%s" idx))
    (let [src-path (-> src :config :file)
          src-file (java.io.File. ^String src-path)
          idx-path (index-file-path src (:name idx))
          idx-file (java.io.File. idx-path)]
      ;; only if the idx-file doesn't exist or the src file is newer
      (println (format "src-path:%s idx-path:%s" src-path idx-path))
      (when (or (not (.exists idx-file))
                (> (.lastModified src-file)
                   (.lastModified idx-file)))
        (index-file!
         src-path
         idx-path
         (:fn idx))))))

(defn make-candidate-keyfile [sources index-name ^String candfile]
  ;; combine the index values, sort and count them
  (with-open [wtr (java.io.PrintWriter. candfile)]
    (doseq [src sources]
      (let [idx (-> src :config :indexes index-name)
            idx-file (index-file-path src index-name)]
        (with-open [rdr (java.io.BufferedReader. (java.io.FileReader. idx-file))]
          (doall
           (for [^String line (line-seq rdr)]
             (.println wtr (first (.split line "\t" 2)))))))))
  (let [tmp    (java.io.File/createTempFile "cand-srt" "tmp")
        tmpnam (.getName tmp)]
    (sh/sh "sort" "-u" "-o" tmpnam candfile
           :env {"LANG" "C"})
    (println (format "mv %s %s" tmpnam candfile))
    (sh/sh "mv" tmpnam candfile)))

(defn ensure-candidate-keyfile [sources index-name ^String candfile]
  (let [f (java.io.File. candfile)]
    (when-not (.exists f)
      (make-candidate-keyfile sources index-name candfile))))

(defn process-candidte-clusters [sources index-name f]
  (let [candfile (format "%s-by-%s.candidates"
                         (string/join "" (map #(name (:name %1)) sources))
                         (name index-name))]
    ;; ensure indexes, then the candidate file
    (ensure-candidate-keyfile sources index-name candfile)
    (with-open [rdr (java.io.BufferedReader. (java.io.FileReader. candfile))]
      (doseq [term (line-seq rdr)]
        (let [cluster (vec (map (fn [src]
                                  (let [input-file (-> src :config :file)
                                        index-file (index-file-path src index-name)]
                                    {:source     (-> src :name)
                                     :index-file index-file
                                     :recs       (vec (index-search-file input-file index-file term))}))
                                sources))]
          (f term cluster))))))
