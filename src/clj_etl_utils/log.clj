(ns
    ^{:doc "This namespace is only here for  backwards compatability, it is deprecated now that tools.logging has identical functionality."
      :author "Kyle Burton"}
  clj-etl-utils.log
  (:require
   [clojure.tools.logging :as log]))

(defn load-log4j-file [^String log4j-prop-file]
  (if (.exists (java.io.File. log4j-prop-file))
    (org.apache.log4j.PropertyConfigurator/configure
     (doto (java.util.Properties.)
       (.load (java.io.FileReader. log4j-prop-file))))
    (if-let [res (.getResourceAsStream (class "") log4j-prop-file)]
      (try
       (let [p (doto (java.util.Properties.) (.load res))]
         (org.apache.log4j.PropertyConfigurator/configure p))
       (finally
        (.close res)))
      (throw (RuntimeException. (format "Error: log4j configuration not found as file nor as a resource: '%s'" log4j-prop-file))))))

;; (load-log4j-file "config/log4j.properties")

;; NB: these are only here for backwards compatability, they should be
;; considered deprecated now that tools.logging has identical
;; functionality...
(defmacro tracef [& body]
  `(log/tracef ~@body))

(defmacro debugf [& body]
  `(log/debugf ~@body))

(defmacro infof [& body]
  `(log/infof ~@body))

(defmacro warnf [& body]
  `(log/warnf ~@body))

(defmacro errorf [& body]
  `(log/errorf ~@body))

(defmacro fatalf [& body]
  `(log/fatalf ~@body))


;; (fatalf (RuntimeException. "test" (RuntimeException. "my cause")) "foof%s" 1234)

