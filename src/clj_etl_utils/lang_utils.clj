(ns
    ^{:doc "Core, shared utility functions that aid in development with Clojure, or with development on the JVM."
      :author "Kyle Burton"}
  clj-etl-utils.lang-utils
  (:import [org.apache.commons.io IOUtils]
           [java.net InetAddress]))

(defn- raise-dispatch-fn [& [fst snd thrd & rst]]
  (cond
    ;;       (and (isa? (class fst) Exception)
    ;;            (isa? (class snd) Class)
    ;;            (isa? (class thrd) String))
    ;;       [:type-to-throw :caused-by :fmt-and-args]

    ;;       (and (isa? (class fst) Class)
    ;;            (isa? (class snd) Exception)
    ;;            (isa? (class thrd) String))
    ;;       [:caused-by :type-to-throw :fmt-and-args]

    (and
     (isa? (class fst) Throwable)
     (isa? (class snd) String)
     thrd)
    [:caused-by :fmt-and-args]

    (and
     (isa? (class fst) Throwable)
     (isa? (class snd) String)
     (not thrd))
    [:caused-by :msg]

    (and
     (isa? (class fst) String)
     snd)
    [:fmt-and-args]

    :else
    :default))


(defmulti raise raise-dispatch-fn
  #_(fn [& [fst snd thrd & rst]]
    (cond
      ;;       (and (isa? (class fst) Exception)
      ;;            (isa? (class snd) Class)
      ;;            (isa? (class thrd) String))
      ;;       [:type-to-throw :caused-by :fmt-and-args]

      ;;       (and (isa? (class fst) Class)
      ;;            (isa? (class snd) Exception)
      ;;            (isa? (class thrd) String))
      ;;       [:caused-by :type-to-throw :fmt-and-args]

      (and
       (isa? (class fst) Throwable)
       (isa? (class snd) String)
       thrd)
      [:caused-by :fmt-and-args]

      (and
       (isa? (class fst) Throwable)
       (isa? (class snd) String)
       (not thrd))
      [:caused-by :msg]

      (and
       (isa? (class fst) String)
       snd)
      [:fmt-and-args]

      :else
      :default)))


(defmethod raise
  [:caused-by :fmt-and-args]
  [#^Throwable caused-by #^String fmt & args]
  (throw (RuntimeException. (apply format fmt args) caused-by)))

(defmethod raise
  [:caused-by :msg]
  [#^Throwable caused-by #^String msg]
  (throw (RuntimeException. msg caused-by)))

(defmethod raise
  [:fmt-and-args]
  [#^String fmt & args]
  (throw (RuntimeException. (apply format fmt args))))

(defmethod raise
  :default
  [& stuff]
  (throw (RuntimeException. (apply str stuff))))


;; TODO: get rid of 'log'
(defn log [& args]
  (.println System/err (apply format args)))

(defn seq-like? [thing]
  (or (seq? thing)
      (vector? thing)))

(defn resource-as-stream [res-url]
  (.getResourceAsStream (.getClass *ns*) res-url))

(defn resource-as-string [res-url]
  (let [strm (resource-as-stream res-url)]
    (if (not strm)
      nil
      (with-open [istr strm]
        (IOUtils/toString istr)))))

(defn rest-params->map [params]
  (reduce
   (fn [m pair]
     (apply assoc m pair))
   {}
   (partition 2 params)))

;; (rest-params->map [:follow-redirects true :basic-auth {:user "bob" :pass "sekret"}])

(defn assert-allowed-keys! [m allowed-keys]
  (let [allowed-keys (apply hash-set allowed-keys)]
    (doseq [k (keys m)]
      (if (not (allowed-keys k))
        (raise "Error: disallowed key: %s not in %s" k allowed-keys)))))

;; (assert-allowed-keys! {:a 1 :b 2} [:a :b])
;; (assert-allowed-keys! {:a 1 :b 2 :c 3} [:a :b])

(defn make-periodic-invoker
  "Takes a count `count' and a function `f'.  Returns a function
that takes an optional 'action' and number of arguments.  After
`count' invocations it will invoke the originally supplied function
`f'.  `f' will be invoked with the current 'count' value and will be
passed any arguments passed into the returned function.

Useful for 'long' running processes where you would like to
periodically see a progress update.  For example:

  (let [progress-bar (make-periodic-invoker 10000 (fn [count chr] (.print System/err chr))]
    (doseq [line (clojure.contrib.duck-streams/read-lines \"some/file\")]
      (progress-bar \".\")
      (process-line line)))

Actions: actions are used to interact with the returned function.  The
following actions are supported:

 :final or :invoke
   Causes the wrapped function `f' to be invoked immediately.  Does not
   modify the value of the counter.

 :state
   Returns the current value of the counter.

 :reset
   Sets the counter back to zero.  Allows the periodic function to be re-used.

 :set
   Set the counter to the supplied value.

"
  [count f]
  (let [ctr (java.util.concurrent.atomic.AtomicLong.)]
    (fn [& args]
      (let [action (first args)]
        (cond
          (or (= :final action)
              (= :invoke action))
          (apply f action (.get ctr) (rest args))

          (= :state action)
          (.get ctr)

          (= :reset action)
          (.set ctr 0)

          (= :set action)
          (.set ctr (second args))

          :else
          (let [nextval (.incrementAndGet ctr)]
            (if (= 0 (mod nextval count))
              (apply f action nextval args))))))))




(defmacro prog1 [res & body]
  `(let [res# ~res]
     ~@body
     res#))


(defmacro aprog1 [res & body]
  `(let [~'it ~res]
     ~@body
     ~'it))



(defmacro prog2 [fst res & body]
  `(do
     ~fst
     (let [res# ~res]
       ~@body
       res#)))


(defmacro aprog2 [fst res & body]
  `(do
     ~fst
     (let [~'it ~res]
       ~@body
       ~'it)))


(defmacro with-hit-timer [[sym-name block-size] & body]
  `(let [start-time# (- (.getTime (java.util.Date.)) 1.0)
         ~sym-name (clj-etl-utils.lang-utils/make-periodic-invoker
                    ~block-size
                    (fn [action# val# & args#]
                      (let [elapsed# (- (.getTime (java.util.Date.))
                                        start-time#)
                            elapsed-secs# (/ elapsed# 1000.0)
                            rate#     (/ val# elapsed-secs#)]
                        (if (= action# :final)
                          (printf "COMPLETED: %d in %ss @ %s/s\n" val# elapsed-secs# rate#)
                          (printf "%d in %ss @ %s/s\n" val# elapsed-secs# rate#)))))]
     (prog1
         (do ~@body)
       (~sym-name :final))))




(def rec-bean
     (let [primitive? #{Class
                        String
                        clojure.lang.Keyword
                        clojure.lang.Symbol
                        Number
                        java.util.Map
                        clojure.lang.IFn}]
       (fn rec-bean [thing]
         (if (or (nil? thing)
                 (seq? thing)
                 (primitive? (class thing)))
           thing
           (let [bn (dissoc (bean thing) :class)]
             (reduce (fn [res k]
                       (assoc res k (rec-bean (get bn k))))
                     {}
                     (keys bn)))))))

(defn get-stack-trace [ex]
  (format "Exception Message: %s, Stack Trace: %s"
          (.getMessage ex)
          (with-out-str
            (.printStackTrace
             ex
             (java.io.PrintWriter. *out*)))))

(defn caused-by-seq [th]
  (loop [res []
         next th]
    (if next
      (recur (conj res next)
             (.getCause next))
      res)))

(defmacro ..?
  ([x form] `(if (nil? ~x) nil (. ~x ~form)))
  ([x form & more] `(..? (if (nil? ~x) nil (. ~x ~form)) ~@more)))

(defmacro restructure-map [& vars]
  (reduce (fn [accum var]
              (assoc accum (keyword var) var))
            {}
            vars))


(defmacro defn! [fn-name arg-spec & body]
  `(defn ~fn-name ~arg-spec
     ~@(map
        (fn [arg]
          `(if-not (isa? ~(:tag (meta arg)) (class ~arg))
             (raise "Error: type-mismatch: expected:'%s' to be a '%s', it was a '%s'"
                    '~arg ~(:tag (meta arg)) (class ~arg))))
        arg-spec)
     ~@body))


(defn hostname []
  (-> (java.net.InetAddress/getLocalHost) (.getHostName)))

(defn string->int? [s]
  (try
   (Integer/parseInt s)
   (catch Exception e
     nil)))

(defn string->long? [s]
  (try
   (Long/parseLong s)
   (catch Exception e
     nil)))


