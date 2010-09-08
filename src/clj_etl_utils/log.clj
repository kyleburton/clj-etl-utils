(ns clj-etl-utils.log
  (:require
   [clojure.contrib.logging :as log]))

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

  (log-formatter "foo")
  (print (log-formatter (RuntimeException. "herp!") "foo"))
  (print (log-formatter (RuntimeException. "herp!")))

  (fatalf "foo")
  (fatalf (RuntimeException. "herp!"))
  (fatalf (RuntimeException. "herp!") "foo: %s" "derp.")

  (isa? (class (RuntimeException. "herp!")) Throwable)
  )


(defn- log-args-dispatcher [& [frst scnd thrd & rst]]
  (cond
    (and (isa? (class frst) Throwable)
         (not scnd))
    [:throwable]

    (and (isa? (class frst) Throwable)
         (isa? (class scnd) String)
         thrd)
    [:throwable :fmt-and-args]

    (and (isa? (class frst) Throwable)
         (isa? (class scnd) String)
         (not thrd))
    [:throwable :message]

    (and (isa? (class frst) String)
         scnd)
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


