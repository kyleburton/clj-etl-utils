(ns clj-etl-utils.math
  (:use
   [clj-etl-utils.lang-utils :only [raise]])
  (:import [java.lang Math]))

(defn log-b [b x]
  (/ (Math/log x)
     (Math/log b)))

(defn log2 [x]
  (log-b 2 x))

(comment

  (log2 1)
  (log2 2)
  (log2 4)
  (log2 8)

  (log2 (Math/pow 10 10))

  (log2 (Math/pow 2 32))
  (log2 4200000000)

  )

