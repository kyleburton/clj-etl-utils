(ns
    ^{:doc "Sequences helpers and extension functions."
      :author "Kyle Burton"}
    clj-etl-utils.sequences)

(defn make-stream-sampler
  "(make-stream-sampler rand-int)

  Creates a stream sampling function from a given random integer source (defaults to rand-int).
  The returned sampling function takes the following arguments:

  - population sequnce
  - total-population-size
  - remaining-samples-needed
  - update-notification-callback [optional]
  "
  
  ([]
   (make-stream-sampler rand-int))
  ([rand-int-fn]
   (fn sampler-fn [[item & population :as population-seq]
                   population-size
                   remaining-samples-needed
                   & [update-fn]]
     (if (or (zero? remaining-samples-needed) (empty? population-seq))
       nil
       (if (< (rand-int-fn population-size) remaining-samples-needed)
         (do
           (when update-fn
             (update-fn))
           (lazy-cat
            [item]
            (sampler-fn population
                        (dec population-size)
                        (dec remaining-samples-needed)
                        update-fn)))
         (recur population
                (dec population-size)
                remaining-samples-needed
                update-fn))))))

;; TODO: factor out the random, allow it to be passed in
(def
  #^{:doc "random-sample-seq
(population-seq population-size num-samples-needed & [update-fn])

Filters a sequence taking a random sample of the elements from the
population sequence.  The random sample will be evenly distributed
over the given population-size.  The sample will terminate when the
sequence runs out or the requested sample size has been reached.  NB:
Given the probabalistic nature of the random sampling process the
sample size may not been precisely met.  If an update-fn is supplied,
it will be invoked every time an element is selected by the random
sampling process."}
  random-sample-seq (make-stream-sampler))


(comment

  (= 1 (count (random-sample-seq
               (take 10 (iterate inc 1))
               10
               1
               (fn [] (printf "foof\n")))))

  (let [x (fn thing [a b & [c]]
            [a b c])]
    (x 1 2 (fn [] 2)))

  (sort (apply concat (for [ii (range 0 10)]
                        (random-sample-seq
                         (take 100 (iterate inc 1))
                         100
                         10))))

  )

(defn make-reservoir-sampler
  "(make-reservoir-sampler reservoir-size
  (make-reservoir-sampler reservoir-size rand-int-fn)

Returns a function that will take a reservoir sample from a sequence (see: https://en.wikipedia.org/wiki/Reservoir_sampling)."
  ([reservoir-size]
   (make-reservoir-sampler reservoir-size rand-int))
  ([reservoir-size rand-int-fn]
   (fn reservoir-sampler [elements]
     ;; fill the reservoir w/the first reservoir-size elements
     (loop [elements elements
            reservoir []
            ii       0]
       (cond
         ;; completed?
         (empty? elements)
         reservoir

         ;; need to fill the reservoir?
         (< (count reservoir) reservoir-size)
         (recur
          (rest elements)
          (conj reservoir (first elements))
          (inc ii))

         :attempt-sampling
         (let [jj (rand-int-fn (inc ii))]
           (if (< jj reservoir-size)
             (recur
              (rest elements)
              (assoc reservoir jj (first elements))
              (inc ii))
             (recur
              (rest elements)
              reservoir
              (inc ii)))))))))

(defn reservoir-sample-seq [reservoir-size elements]
  ((make-reservoir-sampler reservoir-size) elements))


(comment

  ((make-reservoir-sampler 10) (range 10))
  [0 1 2 3 4 5 6 7 8 9]

  ((make-reservoir-sampler 10) (range 100))
  [53 83 73 70 91 78 49 7 52 9]

  ((make-reservoir-sampler 10) (range 10000))
  [5388 3861 8622 9700 4658 5334 1517 8222 8591 6114]

  (let [sampler (make-reservoir-sampler 1)]
    (->>
     (range 99)
     (mapv (fn [trial] (sampler [1 2 3])))
     (reduce (fn [acc choice]
               (assoc acc choice
                      (inc (acc choice 0))))
             {})))

  


)



;; TODO: remove this, it is a re-implementation of partition-by which
;; is in the core in clojure 1.2
(defn
  ^{:doc  "
  (group-seq identity [1 1 2 3 4 5 5 5 6 1 1])
  ;; => [[1 1] [2] [3] [4] [5 5 5] [6] [1 1]]
  "
    :added "1.0.0"}
  group-with [f s]

  (if (empty? s)
    nil
    (let [k            (f (first s))
          pred         #(= k (f %))
          [grp rst]  (split-with pred s)]
      (lazy-cat
       [grp]
       (group-with f rst)))))

(comment

  (group-with identity [1 1 2 3 3 4 4 4 5 6 7 8 9 9 9 9 9 9 9])
  ([1 1] [2] [3 3] [4 4 4] [5] [6] [7] [8] [9 9 9 9 9 9 9])

  (group-with
   (fn [#^String s]
     (.charAt s 0))
   ["this" "that" "other" "othello" "flub" "flubber" "flugelhorn" "potatoe"])

  )

(defn ^{:doc "Given a comparator function (-1, 0, 1) and a set of
sequences, this function will return the minimal head value across all
of the given sequences, and the set of sequences with the minimal
value dropped from the sequence it was identified within."
        :added "1.0.0"}
  minval-from-seqs [cmpfn sequences]
  (let [sqs (sort #(cmpfn (first %1) (first %2)) (filter (complement empty?) sequences))]
    [(first (first sqs))
     (filter (complement empty?) (conj (drop 1 sqs) (drop 1 (first sqs))))]))

(comment

  (minval-from-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   [[2 2 4 6 8 10 12 14 16 18]])


  (minval-from-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   [[2 2 4 6 8 10 12 14 16 18]
    [1 2 3 3 3 9 9 9 14 15 16 20 20 20]
    [-5 0 0 0 99 999]])

  )


(defn ^{:doc "Given a comparator function and one or more sequences
  this function will merge them taking the next most minimal value
  from each of the given sequences.  A good way to think about this
  is: if you have a set of already sorted sequences, this function
  will produce a merged, sorted sequence that combines the given
  sequences.

Example:

  (merge-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   [2 2 4 6 8 10 12 14 16 18]
   [1 2 3 3 3 9 9 9 14 15 16 20 20 20]
   [-5 0 0 0 99 999])

  (-5 0 0 0 1 2 2 2 3 3 3 4 6 8 9 9 9 10 12 14 14 15 16 16 18 20 20 20 99 999)

"
        :added "1.0.0"}
  merge-seqs [cmpfn & sequences]
  (if (or (empty? sequences)
          (every? empty? sequences))
    nil
    (let [[minval rest-seqs] (minval-from-seqs cmpfn sequences)]
      (lazy-cat
       [minval]
       (apply merge-seqs cmpfn rest-seqs)))))

(comment

  (merge-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   [2 2 4 6 8 10 12 14 16 18]
   [1 2 3 3 3 9 9 9 14 15 16 20 20 20]
   [-5 0 0 0 99 999])

  (-5 0 0 0 1 2 2 2 3 3 3 4 6 8 9 9 9 10 12 14 14 15 16 16 18 20 20 20 99 999)

  (merge-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   [2 2 4 6 8 10 12 14 16 18]
   [1 2 3 3 3 9 9 9 14 15 16 20 20 20])

  (merge-seqs
   (fn [a b]
     (cond (< a b) -1
           (= a b)  0
           :else    1))
   []
   []
   [1])


  )

(defn
  ^{:doc "Enumerates all pairs of items.

"
    :added "1.0.0"}
  all-pairs [things]
  (for [this things]
    (for [that (remove #(= this %1) things)]
      [this that])))

(defn n-choose-2 [n]
  (apply + (range 1 n)))

(comment

  (all-pairs [1 2 3])

  )


(defn
  ^{:doc "Given a sequence of numeric values, results in a lazy
  sequence of the running averge of those values.
  (running-avg-seq [1 2 3 4 5 6 7 8 9 9 8 7 6 5 4 3 2 1])
    => (1 3/2 2 5/2 3 7/2 4 9/2 5 27/5 62/11 23/4
        75/13 40/7 28/5 87/16 89/17 5)

  (running-avg-seq [1.0 2 3 4 5 6 7 8 9 9 8 7 6 5 4 3 2 1])
    => (1.0 1.5 2.0 2.5 3.0 3.5 4.0 4.5 5.0 5.4
        5.636363636363637 5.75 5.769230769230769
        5.714285714285714 5.6 5.4375 5.235294117647059
        5.0)
"
    :added "1.0.20"}
  running-avg-seq [s]
  (letfn [(averager
            [s items-seen total]
            (if (empty? s)
              nil
              (lazy-cat
               [(/ (+ total (first s)) (inc items-seen))]
               (averager
                (drop 1 s)
                (inc items-seen)
                (+ total (first s))))))]
    (averager s 0 0)))

(comment

  (running-avg-seq [])
  (running-avg-seq [1])
  (running-avg-seq [1 2 3])
  (running-avg-seq [1 2 3 4 5 6 7 8 9 9 8 7 6 5 4 3 2 1])

  )

(defn
  ^{:doc "Given a numeric sequence, results in a lazy sequence
of the average of the `n' elements from the sequence - averaging over
a window of size `n'.

    (windowed-avg-seq 3 [1 1 1 1 2 2 2 2 2])
      => (1 1 4/3 5/3 2 2)

    (map #(* 1.0 %) (windowed-avg-seq 3 [1 1 1 1 2 2 2 2 2]))
      => (1.0 1.0 1.333333333333333 1.666666666666667 2.0 2.0)

"
    :added "1.0.20"}
  windowed-avg-seq [n s]
  (letfn [(averager
            [s buffer]
            (if (empty? s)
              nil
              (lazy-cat
               [(/ (apply + buffer)
                   (count buffer))]
               (averager (drop 1 s)
                         (concat (drop 1 buffer)
                                 [(first s)])))))]
    (averager (drop n s)
              (take n s))))

(comment
  (windowed-avg-seq 3 [1 1 1 1 2 2 2 2 2])

  )

