(ns clj-etl-utils.linguistics
  (:require [clojure.contrib.duck-streams :as ds])
  (:use
   [clj-etl-utils.lang-utils :only [raise]])
  (:import [com.rn.codec Nysiis]
           [org.apache.commons.codec.language DoubleMetaphone]
           [org.apache.commons.codec.language Soundex]))



;; see: http://norvig.com/spell-correct.html
(def *dict-file* "/usr/share/dict/words")

(defn load-dictionary [file]
  (reduce
   (fn [s l]
     (conj s (.toLowerCase l)))
   #{}
   (filter
    #(not (empty? %1)) (ds/read-lines file))))


(def *dict* (atom nil))

(defn in-dictionary? [word]
  (if-not @*dict*
    (reset! *dict* (load-dictionary *dict-file*)))
  (not (nil? (get @*dict* (.toLowerCase word)))))

(def *alphabet* (vec (drop 1 (.split "abcdefghijklmnopqrstuvwxyz" ""))))

(defn edist1 [word]
  (let [splits     (for [idx (range 0 (inc (count word)))]
                     [(.substring word 0 idx)
                      (.substring word idx)])
        deletes    (for [[a b] splits :when (not (empty? b))]
                     (str a (.substring b 1)))
        transposes (for [[a b] splits :when (> (count b) 1)]
                     (str a
                          (.substring b 1 2)
                          (.substring b 0 1)
                          (.substring b 2)))
        replaces   (for [[a b] splits :when (not (empty? b))
                         c     *alphabet*]
                     (str a c (.substring b 1)))
        inserts    (for [[a b] splits
                         c     *alphabet*]
                     (str a c b))]
    (set (concat deletes transposes replaces inserts))))



(defn edist2 [word]
  (for [d1 (edist1 word)
        d2 (edist1 d1)]
    d2))

(def double-metaphone
     (let [encoder (DoubleMetaphone.)]
       (fn [a]
         (.encode encoder a))))

(def double-metaphone-match?
     (let [encoder (DoubleMetaphone.)]
       (fn [a b]
         (.isDoubleMetaphoneEqual encoder a b))))

(def nysiis
     (let [encoder (Nysiis.)]
       (fn [a]
         (.encode encoder a))))

(def nysiis-match?
     (let [encoder (Nysiis.)]
       (fn [a b]
         (=
          (.encode encoder a)
          (.encode encoder b)))))

(def soundex
     (let [encoder (Soundex.)]
       (fn [a]
         (.encode encoder a))))

(def soundex-match?
     (let [encoder (Soundex.)]
       (fn [a b]
         (= (.encode encoder a)
            (.encode encoder b)))))

(defn filter-for-phonetic-equivalence [word permutations]
  (let [dmeta        (DoubleMetaphone.)
        sdex         (Soundex.)
        target-sdex  (.encode sdex word)]
   (filter (fn [permutation]
             (and (.isDoubleMetaphoneEqual dmeta word permutation)
                  (Nysiis/isEncodeEqual word permutation)
                  (= (.encode sdex permutation) target-sdex )))
           permutations)))


(defn permutations [word]
  (set (concat [word]
               (filter-for-phonetic-equivalence word (edist1 word))
               (filter-for-phonetic-equivalence word (edist2 word)))))

(defn permutations-with-encodings [word]
  (for [permutation (vec (permutations word))]
    [word
     (soundex word)
     (double-metaphone word)
     (nysiis word)
     permutation
     (soundex permutation)
     (double-metaphone permutation)
     (nysiis permutation)]))

