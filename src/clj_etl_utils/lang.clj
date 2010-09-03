(ns clj-etl-utils.lang
  (:import [org.apache.commons.io IOUtils]))

(defmulti raise
  (fn [& [fst snd thrd & rst]]
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
  (throw (RuntimeException. (apply format args) caused-by)))

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


;; (raise "foof!")
;; (raise "foof!: %s" "blarf")
;; (raise (Exception. "root") "another")
;; (raise (Exception. "root") "another: %s" "thing!")

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