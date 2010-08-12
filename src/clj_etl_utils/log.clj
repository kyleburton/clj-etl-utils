(ns clj-etl-utils.log
  (:require
   [clojure.contrib.logging :as log]))

(defmacro tracef [fmt & args]  `(if (log/enabled? :debug) (log/debug (format ~fmt ~@args))))
(defmacro debugf [fmt & args]  `(if (log/enabled? :debug) (log/debug (format ~fmt ~@args))))
(defmacro infof  [fmt & args]  `(if (log/enabled? :info)  (log/info  (format ~fmt ~@args))))
(defmacro warnf  [fmt & args]  `(if (log/enabled? :warn)  (log/warn  (format ~fmt ~@args))))
(defmacro errorf [fmt & args]  `(if (log/enabled? :error) (log/error (format ~fmt ~@args))))
(defmacro fatalf [fmt & args]  `(if (log/enabled? :fatal) (log/fatal (format ~fmt ~@args))))


(comment

  (macroexpand '(debugf "this: %s %d" "that" 25))

  (debugf "this: %s %d" "that" 25)
  (errorf "this: %s %d" "that" 25)
)