(ns clj-etl-utils.scratch
  (:require
   [clj-etl-utils.indexer        :as indexer]
   [clojure.data.csv             :as csv]
   [clojure.java.sh              :as sh]
   [clojure.string               :as string]
   [clj-etl-utils.indexer        :as indexer]
   [clj-etl-utils.lang-utils     :refer [raise]])
  (:import
   [java.io RandomAccessFile FileInputStream InputStreamReader BufferedReader]
   [org.apache.commons.io.input BoundedInputStream]))

(comment

  (process-candidte-clusters
   (vals sources)
   :city
   (fn [term cluster]
     (doseq [src-results cluster]
       (println (format "%s: %s : %d %s" term (:source src-results) (count (:recs src-results)) (:recs src-results))))))
  (make-candidate-keyfile
   (vals sources)
   :city

   "candidates.tab")

  (def sources
       {:boutell
        {:name   :boutell
         :config {:file "tmp/zipcode.csv"
                  :has-header true
                  :indexes {:city {:name :city
                                   :fn   (fn [l]
                                           (let [rec (csv-parse l)]
                                             ;; 4th col is City
                                             (when-not (empty? rec)
                                               [(.toLowerCase (nth rec 1))])))}
                            :state {:name :state
                                    :fn   (fn [l]
                                            (let [rec (csv-parse l)]
                                              ;; 4th col is City
                                              (when-not (empty? rec)
                                                [(.toLowerCase (nth rec 2))])))}}}}
        :free-zipcode-database
        {:name :free-zipcode-database
         :config {:file "tmp/free-zipcode-database.csv"
                  :has-header true
                  :indexes {:city {:name :city
                                   :fn   (fn [l]
                                           (let [rec (csv-parse l)]
                                             [(.toLowerCase (nth rec 3))]))}
                            :state
                            {:name :state
                             :fn   (fn [l]
                                     (let [rec (csv-parse l)]
                                       [(.toLowerCase (nth rec 4))]))}}}}})

  (time
   (do
     (ensure-indexes (sources :boutell))
     (ensure-indexes (sources :free-zipcode-database))))


  (vec
   (index-search-file
    "tmp/free-zipcode-database.csv"
    "tmp/.free-zipcode-database.csv.city-idx"
    "philade"
    (fn [a b]
      (.startsWith
       (.toLowerCase a)
       (.toLowerCase b)))))


  (vec
   (index-search-file
    "tmp/zipcode.csv"
    "tmp/.zipcode.csv.city-idx"
    "philade"
    (fn [a b]
      (.startsWith
       (.toLowerCase a)
       (.toLowerCase b)))))

  )
