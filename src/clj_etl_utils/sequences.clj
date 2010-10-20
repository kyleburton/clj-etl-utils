(ns clj-etl-utils.sequences)


(def
 #^{:doc "random-sample-seq
(population-seq population-size num-samples-needed & [update-fn])

Filters a sequence taking a random sample of the elements from the
initial population sequence.  The random sample will be evenly
distributed over the given population-size.  The sample will terminate
when the sequence runs out or the requested sample size has been
reached.  NB: Given the probabalistic nature of the random sampling
process the sample size may not been precisely met.  If an `update-fn'
is supplied, it will be invoked every time an element is selected by
the random sampling process.

"}
 random-sample-seq
     (let [rnd (java.util.Random.)]
       (fn myself [[item & population :as population-seq]
                   population-size
                   remaining-samples-needed
                   & [f :as f-arg]]
         (if (or (zero? remaining-samples-needed) (empty? population-seq))
           nil
           (if (< (.nextInt rnd population-size) remaining-samples-needed)
             (do
               (if f (f))
               (lazy-cat
                [item]
                (myself population
                        (dec population-size)
                        (dec remaining-samples-needed)
                        f)))
             (recur population
                    (dec population-size)
                    remaining-samples-needed
                    f-arg))))))


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

