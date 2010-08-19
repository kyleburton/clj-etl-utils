(ns clj-etl-utils.lang
  (:import [org.apache.commons.io IOUtils]))

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

(defn resource-as-stream [res-url]
  (.getResourceAsStream (.getClass *ns*) res-url))

(defn resource-as-string [res-url]
  (with-open [istr (resource-as-stream res-url)]
    (IOUtils/toString istr)))
