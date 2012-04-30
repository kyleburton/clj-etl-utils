(ns
    ^{:doc "Wrapper functions for clojure.contrib.logging that allow for printf style format strings."
      :author "Kyle Burton"}
  clj-etl-utils.log
  (:require
   [clojure.tools.logging  :as log]))


(defmacro tracef [& args]  `(if (log/enabled? :debug) (log/debug (log-formatter ~@args))))
(defmacro debugf [& args]  `(if (log/enabled? :debug) (log/debug (log-formatter ~@args))))
(defmacro infof  [& args]  `(if (log/enabled? :info)  (log/info  (log-formatter ~@args))))
(defmacro warnf  [& args]  `(if (log/enabled? :warn)  (log/warn  (log-formatter ~@args))))
(defmacro errorf [& args]  `(if (log/enabled? :error) (log/error (log-formatter ~@args))))
(defmacro fatalf [& args]  `(if (log/enabled? :fatal) (log/fatal (log-formatter ~@args))))


(comment

  (macroexpand '(debugf "this: %s %d" "that" 25))

  (do
    (debugf "this: %s %d" "that" 25)
    (infof  "this: %s %d" "that" 25)
    (warnf  "this: %s %d" "that" 25)
    (errorf "this: %s %d" "that" 25)
    (fatalf "this: %s %d" "that" 25))

  (let [ex (RuntimeException. "the caused by and by and by")]
    (debugf ex "this: %s %d" "that" 25)
    (infof  ex "this: %s %d" "that" 25)
    (warnf  ex "this: %s %d" "that" 25)
    (errorf ex "this: %s %d" "that" 25)
    (fatalf ex "this: %s %d" "that" 25))

  (isa? (class (RuntimeException. "The caused-by")) Throwable)



  (log-args-dispatcher (RuntimeException. "The caused-by") "foof")
  (log-args-dispatcher (RuntimeException. "The caused-by"))

  (log-args-dispatcher "Herp a derp!")
  (log-args-dispatcher "Herp a %sp!" "flur")
  (log-args-dispatcher "Herp a %sp %sp!" "flur" "bler")
  (log-args-dispatcher (RuntimeException. "The caused-by") "Herp a derp!")

  (log-args-dispatcher (RuntimeException. "The caused-by") "Herp a %s!" "flur")

  (log-args-dispatcher (RuntimeException. "The caused-by") "Herp a %s!" nil)

  (log-formatter "foo")
  (print (log-formatter (RuntimeException. "herp!") "foo"))
  (print (log-formatter (RuntimeException. "herp!")))

  (fatalf "foo")
  (fatalf (RuntimeException. "herp!"))
  (fatalf (RuntimeException. "herp!") "foo: %s" "derp.")

  (isa? (class (RuntimeException. "herp!")) Throwable)
  )


(defn- log-args-dispatcher [& args]
  (cond
    (and (isa? (class (first args)) Throwable)
         (= 1 (count args)))
    [:throwable]

    (and (isa? (class (first args)) Throwable)
         (isa? (class (second args)) String)
         (> (count args) 2))
    [:throwable :fmt-and-args]

    (and (isa? (class (first args)) Throwable)
         (isa? (class (second args)) String)
         (= 2 (count args)))
    [:throwable :message]

    (and (isa? (class (first args)) String)
         (> (count args) 1))
    [:fmt-and-args]

    :else
    :default))

(defn exception->string [#^Throwable th]
  (let [sw (java.io.StringWriter.)]
    (.printStackTrace th (java.io.PrintWriter. sw))
    (.toString sw)))


;; (print (exception->string (RuntimeException. "foo")))

(defmulti log-formatter log-args-dispatcher)

(defmethod log-formatter [:throwable] [throwable]
  (exception->string throwable))

(defmethod log-formatter [:throwable :message] [throwable message]
  (str message " " (exception->string throwable)))

(defmethod log-formatter [:throwable :fmt-and-args] [throwable fmt & args]
  (str (apply format fmt args) " " (exception->string throwable)))

(defmethod log-formatter [:fmt-and-args] [fmt & args]
  (str (apply format fmt args)))

(defmethod log-formatter :default [& stuff]
  (str (apply format stuff)))

(defn load-log4j-file [log4j-prop-file]
  (infof "Configuring log4j from %s\n" log4j-prop-file)
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

(defmacro time-nano [name & body]
  `(let [start-nano# (System/nanoTime)
         res# (do
                ~@body)
         end-nano# (System/nanoTime)]
     (infof "%s:: %s elapsed (from %s to %s)" ~name (- end-nano# start-nano#) start-nano# end-nano#)
     res#))
