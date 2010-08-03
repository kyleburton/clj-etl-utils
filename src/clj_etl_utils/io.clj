;; Based off of nu.xom.xinclude.EncodingHeuristics

(ns clj-etl-utils.io
  (:import [java.io FileInputStream]))


;; (BufferedReader. (InputStreamReader. (FileInputStream. file) (or encoding *file-encoding*)))

;; TODO: can fail for streams that don't support marking
(defn first-n-bytes-available [stream n-bytes]
  (let [res (atom [])]
    (try
     (.mark stream 1024)
     (dotimes [nn n-bytes]
       (let [next-byte (.read stream)]
         (if (not (= -1 next-byte))
           (reset! res (conj @res next-byte)))))
     (finally
      (.reset stream)))
    @res))


;; TODO: can fail for streams that have too few bytes available for reading
;; TODO: may return a false positive on arbitrary binary data
(defn open-input-stream [#^String file]
  (let [is (FileInputStream. file)]
    (.mark is 1024)
    (let [[first-byte second-byte third-byte fourth-byte] (first-n-bytes-available is 4)])))





