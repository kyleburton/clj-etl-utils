(ns clj-etl-utils.regex-test
  (:require [clj-etl-utils.regex :as rx])
  (:use [clojure.test]
        [clj-etl-utils.test-helper]))


(defn us-state-match-found [data]
  (let [regex (rx/std-regex :geographic :usa :state)]
    (.matches (.matcher regex data))))


(deftest test-us-state-regex
  (is (not (us-state-match-found "xx")))
  (is (us-state-match-found "NJ"))
  (is (us-state-match-found "nJ"))
  (is (not (us-state-match-found "")))
  (is (us-state-match-found "pa"))
  (is (us-state-match-found "NJ"))
  (is (us-state-match-found "AE"))
  (is (not (us-state-match-found "not"))))

;; (test-us-state-regex)

(def *sample-postal-codes*
     {:can
      ["R2K 3X4"
       "R6W 4B7"
       "L1T 2W2"
       "M2R 1L7"
       "R5G 0M3"
       "R2V4J1"
       "H9S2R9"
       "H1T 2R6"
       "H1T 2R6"
       "N0G 2H0"
       "H1P 1K5"
       "L1V 6L4"
       "T2H 1K2"
       "L8H 3B2"
       "H1T 2R6"
       "L8E0C7"
       "J7R 6W2"
       "J3H 0A9"
       "R0G 2B0"
       "L4Z 2X2"
       "N7T 8B2"
       "V2X 2R4"
       "T6K 3P8"
       "V0G 1M0"
       "N8H 3P2"
       "G6E 1A2"
       "J3X 2B8"]
      :usa
      ["93306"
       "94568"
       "26547"
       "12440"
       "43333"
       "16692"
       "98392"
       "26038"
       "90815"
       "67484"
       "63366"
       "60946"
       "61048"
       "24950"
       "07036"
       "65764"
       "12538"
       "30296"
       "41601"
       "75228"
       "26267"
       "84320"
       "61743"
       "37402"
       "94523"
       "20164 1131"
       "20164-1131"
       "48044 2335"
       "48044-2335"
       "37214 2928"
       "37214-2928"
       "48340 1205"
       "48340-1205"
       "18054 2531"
       "18054-2531"
       "30224 5330"
       "30224-5330"
       "49426 7317"
       "49426-7317"
       "30340 4294"
       "30340-4294"
       "08201 2813"
       "08201-2813"]})

(deftest test-usa-zip-matcher
  (doseq [zip5 (:usa *sample-postal-codes*)]
    (is (re-matches (rx/std-regex :geographic :usa :zip)
                    zip5))))

(deftest test-can-postal-code-matcher
  (doseq [postal-code (:can *sample-postal-codes*)]
    (is (re-matches (rx/std-regex :geographic :can :postal-code)
                    postal-code))))

(deftest test-north-america-postal-code-matcher
  (let [regex (rx/std-regex-compose [:geographic :usa :postal-code]
                                    [:geographic :can :postal-code])]
    (doseq [postal-code (concat
                         (:can *sample-postal-codes*)
                         (:usa *sample-postal-codes*))]
      (is (re-matches regex postal-code)))))

(deftest test-north-america-postal-code-matcher?
  (let [regex (rx/std-regex-compose [:geographic :usa :postal-code?]
                                    [:geographic :can :postal-code?])]
    (doseq [postal-code (concat
                         (:can *sample-postal-codes*)
                         (:usa *sample-postal-codes*))]
      (is (re-matches regex postal-code)))))

(comment

  (apply rx/std-regex [:geographic :can :postal-code])

  (test-usa-zip-matcher)
  (test-can-postal-code-matcher)
  (test-north-america-postal-code-matcher)

  )