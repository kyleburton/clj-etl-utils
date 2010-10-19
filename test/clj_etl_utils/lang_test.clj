(ns clj-etl-utils.lang-test
  (:require [clj-etl-utils.lang :as lang])
  (:use [clojure.test]))

(deftest test-make-periodic-invoker
  (let [stat    (atom [])
        trigger (lang/make-periodic-invoker 10
                                            (fn [count val]
                                              (swap! stat conj val)))]
    (dotimes [ii 100]
      (trigger ii))
    (is (= 10 (count @stat)))))

;; (test-make-periodic-invoker)

(comment
  (let [stat    (atom [])
        trigger (lang/make-periodic-invoker 10
                                            (fn [count val]
                                              (swap! stat conj [count val])))]
    (dotimes [ii 100]
      (trigger ii))
    @stat)


  (take 100 (iterate inc 1))

  )
