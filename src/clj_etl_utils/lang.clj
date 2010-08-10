(ns clj-etl-utils.lang)

(defn raise
  "Simple wrapper around throw."
  [& args]
  (throw (RuntimeException. (apply format args))))

;; TODO: get rid of 'log'
(defn log [& args]
  (.println System/err (apply format args)))

(defn seq-like? [thing]
  (or (seq? thing)
      (vector? thing)))