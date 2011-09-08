(ns clj-etl-utils.time
  (:use
   [clj-etl-utils.lang-utils :only [raise]])
  (:import
   [org.joda.time LocalDate DateTime Days]))

(defn date-seq [start-date end-date]
  (take-while
   #(or (.isBefore %1 end-date)
        (.isEqual  %1 end-date))
   (iterate (fn [d]
              (.plusDays d 1))
            start-date)))

