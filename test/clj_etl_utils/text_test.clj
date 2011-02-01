(ns clj-etl-utils.text-test
  (:use
   [clojure.test]
   [clj-etl-utils.lang-utils :only [raise]])
  (:require
   [clj-etl-utils.text  :as text]))


(deftest test-split-message
  (let [msg "This is a message that will get split."]

    ;; Sanity check
    (is (= 38 (count msg)))

    ;; 100-char blocks. No splitting should be done.
    (is (= '("This is a message that will get split.")
           (text/word-split msg 100)))

    ;; Msg is exactly 38 chars long. Test the boundary.
    (is (= '("This is a message that will get split.")
           (text/word-split msg 38)))


    ;; Split into two approximately 20-char blocks.
    ;; Should result in:
    ;;   "This is a message" and "that will get split"
    (is (= '("This is a message" "that will get split.")
           (text/word-split msg 20)))

    ;; Split into approximately 10-char blocks.
    ;; Should result in:
    ;;   "This is a", "message", "that will", "get split."
    (is (= '("This is a" "message" "that will" "get split.")
           (text/word-split msg 10)))

    ))




(comment

  (run-tests)
)




