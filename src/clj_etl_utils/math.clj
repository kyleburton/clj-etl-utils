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

(def base62-digits "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ")


(defn base62-encode [n & [base]]
  (let [#^BigInteger base (BigInteger/valueOf (or base 62))
        #^BigInteger n (if (isa? (class n) BigInteger)
                         n
                         (BigInteger/valueOf n))]
    (if (= -1 (.compareTo n BigInteger/ZERO))
      (raise "Error: negative numbers are not supported, sorry: %s" n))
    (loop [res                 (StringBuilder.)
           [number remainder]  (.divideAndRemainder n base)]
      (if (= 1 (.compareTo number BigInteger/ZERO))
        (recur
         (.insert res 0 (.charAt base62-digits (.intValue remainder)))
         (.divideAndRemainder number base))
        (let [res (if (= 1 (.compareTo remainder BigInteger/ZERO))
                    (str (.insert res 0 (.charAt base62-digits (.intValue remainder))))
                    (str res))]
          (if (empty? res)
            "0"
            res))))))


(defn base62-decode [#^String s & [base]]
  (let [#^BigInteger base (BigInteger/valueOf (or base 62))
        digits (count s)]
    (if (empty? s)
      BigInteger/ZERO
      (loop [res #^BigInteger BigInteger/ZERO
             idx 0]
        (if (>= idx digits)
          res
          (let [c     (.charAt s (- digits idx 1))
                digit (BigInteger/valueOf (.indexOf base62-digits (str c)))]
            (recur (.add res (.multiply digit (.pow base idx)))
                   (inc idx))))))))



