(ns clj-etl-utils.log
  (:require
   [clojure.contrib.logging :as log]))

(defmacro tracef [& args]  `(if (log/enabled? :debug) (debug-fmt ~@args)))
(defmacro debugf [& args]  `(if (log/enabled? :debug) (debug-fmt ~@args)))
(defmacro infof  [& args]  `(if (log/enabled? :info)  (info-fmt  ~@args)))
(defmacro warnf  [& args]  `(if (log/enabled? :warn)  (warn-fmt  ~@args)))
(defmacro errorf [& args]  `(if (log/enabled? :error) (error-fmt ~@args)))
(defmacro fatalf [& args]  `(if (log/enabled? :fatal) (fatal-fmt ~@args)))


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

  (log-args-dispatcher "Herp a derp!")
  (log-args-dispatcher "Herp a %sp!" "flur")
  (log-args-dispatcher "Herp a %sp %sp!" "flur" "bler")
  (log-args-dispatcher (RuntimeException. "The caused-by") "Herp a derp!")
  (log-args-dispatcher (RuntimeException. "The caused-by") "Herp a %s!" "flur")

  )



(defn- log-args-dispatcher [& [frst scnd thrd & rst]]
  (cond
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

;; log/debug

(defmulti debug-fmt  log-args-dispatcher)

(defmethod debug-fmt [:throwable :message] [throwable message]
  (log/debug message throwable))

(defmethod debug-fmt [:throwable :fmt-and-args] [throwable fmt & args]
  (log/debug (apply format fmt args) throwable))

(defmethod debug-fmt [:fmt-and-args] [fmt & args]
  (log/debug (apply format fmt args)))

(defmethod debug-fmt :default [& stuff]
  (log/debug (apply format stuff)))


;; log/info

(defmulti info-fmt log-args-dispatcher)

(defmethod info-fmt [:throwable :message] [throwable message]
  (log/info message throwable))

(defmethod info-fmt [:throwable :fmt-and-args] [throwable fmt & args]
  (log/info (apply format fmt args) throwable))

(defmethod info-fmt [:fmt-and-args] [fmt & args]
  (log/info (apply format fmt args)))

(defmethod info-fmt :default [& stuff]
  (log/info (apply format stuff)))


;; log/warn

(defmulti warn-fmt log-args-dispatcher)

(defmethod warn-fmt [:throwable :message] [throwable message]
  (log/warn message throwable))

(defmethod warn-fmt [:throwable :fmt-and-args] [throwable fmt & args]
  (log/warn (apply format fmt args) throwable))

(defmethod warn-fmt [:fmt-and-args] [fmt & args]
  (log/warn (apply format fmt args)))

(defmethod warn-fmt :default [& stuff]
  (log/warn (apply format stuff)))


;; log/error

(defmulti error-fmt log-args-dispatcher)

(defmethod error-fmt [:throwable :message] [throwable message]
  (log/error message throwable))

(defmethod error-fmt [:throwable :fmt-and-args] [throwable fmt & args]
  (log/error (apply format fmt args) throwable))

(defmethod error-fmt [:fmt-and-args] [fmt & args]
  (log/error (apply format fmt args)))

(defmethod error-fmt :default [& stuff]
  (log/error (apply format stuff)))


;; log/fatal

(defmulti fatal-fmt log-args-dispatcher)

(defmethod fatal-fmt [:throwable :message] [throwable message]
  (log/fatal message throwable))

(defmethod fatal-fmt [:throwable :fmt-and-args] [throwable fmt & args]
  (log/fatal (apply format fmt args) throwable))

(defmethod fatal-fmt [:fmt-and-args] [fmt & args]
  (log/fatal (apply format fmt args)))

(defmethod fatal-fmt :default [& stuff]
  (log/fatal (apply format stuff)))



;; (fatal-fmt "this: %s" "that")

