(ns clj-etl-utils.json.schema
  (:use
   [clj-etl-utils.lang-utils :only [raise]]))

(defn validate [schema json-object]
  {:ok true})

(defn validate! [schema json-object]
  (let [res (validate schema json-object)]
    (if-not (:ok res)
      (raise "Validation Errors: %s" res))
    res))

(defn make-validator! [schema]
  (fn curryd-validator [json-object]
    (validate! schema json-object)))

