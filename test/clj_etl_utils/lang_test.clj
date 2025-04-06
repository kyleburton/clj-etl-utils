(ns clj-etl-utils.lang-test
  (:require [clj-etl-utils.lang-utils :as lang-utils])
  (:use [clojure.test]
        [clj-etl-utils.lang-utils :only [nth-let]]))

(deftest test-make-periodic-invoker
  (let [stat    (atom [])
        trigger (lang-utils/make-periodic-invoker 10
                                                  (fn [action count val]
                                                    (swap! stat conj val)))]
    (dotimes [ii 100]
      (trigger ii))
    (is (= 10 (count @stat)))))

;; (test-make-periodic-invoker)

(comment
  (let [stat    (atom [])
        trigger (lang-utils/make-periodic-invoker 10
                                                  (fn [count val]
                                                    (swap! stat conj [count val])))]
    (dotimes [ii 100]
      (trigger :hit ii))
    @stat)


  ( def *timer* (lang-utils/make-periodic-invoker
                10
                (fn [val & args]
                  (printf "triggered: val=%s args=%s\n" val args))))

  (dotimes [ii 10] (*timer*))
  (*timer* :final)
  (*timer* :invoke 1 2 3)
  (*timer* :state)
  (*timer* :set 19)
  (*timer* :reset)

  (macroexpand-1
   '(lang-utils/with-hit-timer [timer 10]
      (dotimes [ii 100]
        (timer))))


  (lang-utils/with-hit-timer [timer 10]
    (dotimes [ii 109]
      (timer)))


  (let [total    1000
        period     100
        progress (lang-utils/make-periodic-invoker
                  period
                  (fn [val & [is-done]]
                    (if (= is-done :done)
                      (printf "All Done! %d\n" val)
                      (printf "So far we did %d, we are  %3.2f%% complete.\n" val (* 100.0 (/ val 1.0 total))))))]
    (dotimes [ii total]
      (progress))
    (progress :final :done))

  )

(deftest test-nth-let
  (let [rec (vec (.split "abcdefghijklmnopqrstuvwxyz" ""))]
    (nth-let [rec
              lstart   0
              lmiddle 12
              llast   25]
      (is (= "a" lstart))
      (is (= "m" lmiddle))
      (is (= "z" llast)))))
