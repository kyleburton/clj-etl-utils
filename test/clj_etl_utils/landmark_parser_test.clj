(ns landmark-parser-test
  (:require [clj-etl-utils.landmark-parser :as lp])
  (:use [clojure.test]
        [clj-etl-utils.test-helper]))


(register-fixture :text-doc :simple-text "This is some text.  There is some in the middle.\nThere is some towards the end, but it is not this.\nA few sentences in all.")

(defn- test-doc [k]
  (lp/make-parser (*docs* k (format "Error: invalid key: %s" k))))

(deftest test-extract
  (is (= "This is some text."
         (lp/extract
          (lp/make-parser (fixture :text-doc :simple-text))
          [[:start nil]]
          [[:forward-past "."]])))
  (is (= "There is some in the middle."
         (lp/extract
          (lp/make-parser (fixture :text-doc :simple-text))
          [[:forward-past "."]
           [:forward-past "  "]]
          [:forward-past "."])))
  (is (= "A few sentences in all."
         (lp/extract
          (lp/make-parser (fixture :text-doc :simple-text))
          [[:end nil]
           [:rewind-to "\n"]]
          [:end nil]))))

;; (test-extract)

