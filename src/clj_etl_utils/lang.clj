(ns clj-etl-utils.lang)

(defn throwf
  "Simple wrapper around throw."
  [& args]
  (throw (RuntimeException. (apply format args))))

;; TODO: get rid of 'log'
(defn log [& args]
  (.println System/err (apply format args)))






