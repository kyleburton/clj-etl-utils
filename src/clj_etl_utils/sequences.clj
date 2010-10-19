(ns clj-etl-utils.sequences)


(def random-sample-seq
     (let [rnd (java.util.Random.)]
       (fn self [[item & population :as population-seq] population-size remaining-samples-needed]
         (if (or (zero? remaining-samples-needed) (empty? population-seq))
           nil
           (if (< (.nextInt rnd population-size) remaining-samples-needed)
             (lazy-cat
              [item]
              (self population
                    (dec population-size)
                    (dec remaining-samples-needed)))
             (self population
                   (dec population-size)
                   remaining-samples-needed))))))


(comment
 (= 1 (count (random-sample-seq
              (take 10 (iterate inc 1))
              10
              1)))

 (sort (apply concat (for [ii (range 0 10)]
                       (random-sample-seq
                        (take 100 (iterate inc 1))
                        100
                        10))))

 )