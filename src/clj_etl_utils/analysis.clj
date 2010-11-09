(ns clj-etl-utils.analysis
  (:require
   [clojure.contrib.duck-streams :as ds]
   [fleet :as fleet]))

(defn max-col-lens [rec-seq]
  (letfn [(track-counts
           [m rec]
           (reduce (fn [m idx]
                     (assoc m idx
                            (max (count (rec idx))
                                 (get m idx -1))))
                   m
                   (range 0 (count rec))))]
    (reduce track-counts {} rec-seq)))

(comment

  (def *example-data* "field1\tfield2\tfield3
This\ttaht\tother
\t\t
some more stuff\tand yet more\tfinal field
the quick brown\t fox jumped over\t the lazy\t toad
\tguns\t germs\t steel")

  (def *example-recs* (map (fn [l] (vec (.split l "\t"))) (.split *example-data* "\\n")))

  (max-col-lens *example-recs*)

)

(defn translate-to-header-names-vec [counts-map fields]
  (map (fn [idx]
         [(fields idx)
          (counts-map idx)])
       (range 0 (count fields))))

(comment

  (translate-to-header-names-vec (max-col-lens (drop 1 *example-recs*))
                                 (first *example-recs*))

)

(defn analyze-column-data [[hdr & recs]]
  "Takes a sequences, returns a seq of column info, one per column.
The column info will contain the following:

  {:name field-name
   :type field-type
   :length max-size}

Where the field-name is defined by the first record in the stream - by
assuming this first record is the column names (a header row).

field-type is the type detectec by the analyzer (currently hard-coded
to 'character varying').

max-size is the maximum detected width of data values from the
remainder of the stream (not the header)."
  (for [[field-name max-size]
        (translate-to-header-names-vec
         (max-col-lens recs)
         hdr)]
    {:name field-name
     :type "character varying"
     :length max-size}))

(comment


  (analyze-column-data *example-recs*)

)