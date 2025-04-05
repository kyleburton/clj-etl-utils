;; # Caching Utilities
;;
;; Basic memoization produces a function with a cache wrapped around it.
;; This type of cache will have unbounded growth.  The functions
;; in this module are for maintaining similar behavior while allowing
;; caches to have various flushing policies associated with them.
;;
(ns clj-etl-utils.cache-utils
  (:require
   [clojure.tools.logging    :as log])
  (:use
   [clj-etl-utils.lang-utils :only [raise aprog1]])
  (:import
   [org.joda.time DateTime]))


;; ## Cache Registry
;;
;; The intent is for static caches to be registered so they can be
;; managed by code outside of the memoize wrapper.
;;
;; Best practice for naming caches is the namespace and the wrapped
;; function name.  Eg:
;;
;;   `(register-cache :clj-etl-utils.cache-utils.my-function #{:standard} (atom {}))`
;;
(defonce cache-registry (atom {}))

;; ### Cache Tags
;;
;; Allows caches to be operated on based on a 'type'.
;; The first supported type for this module is :standard
;; which represents a standard memoize based cache.

(defn register-cache [name #^java.util.Set tags cache-ref & [cache-reset-fn]]
  (swap! cache-registry assoc name {:name name :tags tags :cache cache-ref :reset-fn (or cache-reset-fn
                                                                                           (fn [entry] (reset! (:cache entry) {})))}))

(defn lookup-cache-by-name [name]
  (get @cache-registry name))

(defn purge-cache-named [n]
  (reset! (:cache (lookup-cache-by-name n))
          {}))

(defn lookup-caches-by-tag [tag]
  (filter (fn [entry]
            (contains? (:tags entry) tag))
          (vals @cache-registry)))

(defn purge-caches-with-tag [tag]
  (doseq [entry (lookup-caches-by-tag tag)]
    ((:reset-fn entry) entry)))

(defn purge-standard-caches []
  (purge-caches-with-tag :standard))

(comment

  (map :cache (lookup-caches-by-tag :standard))
  )

(defn wrap-standard-cache [name tags the-fn args-ser-fn]
  (let [cache (atom {})]
    (register-cache name tags cache)
    (fn [& args]
      (let [k    (args-ser-fn args)
            cmap @cache]
        (if (contains? cmap k)
          (get cmap k)
          (aprog1
              (apply the-fn args)
            (swap! cache assoc k it)))))))

(defn simple-cache [name the-fn]
  (wrap-standard-cache name #{:standard} the-fn identity))

(defmacro def-simple-cached [name arg-spec & body]
  `(def ~name
        (simple-cache ~(keyword (str *ns* "." name))
                      (fn ~arg-spec
                        ~@body))))

(defn wrap-countdown-cache [name tags the-fn config]
  (let [cache       (atom {})
        args-ser-fn (:args-ser-fn config)
        max-hits    (:max-hits    config 100)
        nhits       (java.util.concurrent.atomic.AtomicLong. 0)]
    (register-cache name tags cache)
    (fn [& args]
      (let [k    (args-ser-fn args)
            cmap @cache]
        (when (>= (.incrementAndGet nhits) max-hits)
          (.set nhits 0)
          (reset! cache {}))
        (if (contains? cmap k)
          (get cmap k)
          (aprog1
              (apply the-fn args)
            (swap! cache assoc k it)))))))

(defmacro def-countdown-cached [name max-hits arg-spec & body]
  `(def ~name
        (wrap-countdown-cache
         ~(keyword (str *ns* "." name))
         #{:countdown}
         (fn ~arg-spec
           ~@body)
         {:max-hits    ~max-hits
          :args-ser-fn identity})))

(defn wrap-timeout-cache [name tags the-fn config]
  (let [cache       (atom {})
        args-ser-fn (:args-ser-fn config)
        duration    (long  (:duration config (* 1000 60 60)))
        exp-time    (atom  (.plusMillis  (DateTime.) duration))]
    (register-cache name tags cache)
    (fn [& args]
      (let [k    (args-ser-fn args)
            cmap @cache]
        (when (.isBeforeNow ^DateTime @exp-time)
          (reset! exp-time (.plusMillis  (DateTime.) duration))
          (reset! cache {}))
        (if (contains? cmap k)
          (get cmap k)
          (aprog1
              (apply the-fn args)
            (swap! cache assoc k it)))))))


(defmacro def-timeout-cached [name duration arg-spec & body]
  `(def ~name
        (wrap-timeout-cache
         ~(keyword (str *ns* "." name))
         #{:timeout}
         (fn ~arg-spec
           ~@body)
         {:duration    ~duration
          :args-ser-fn identity})))

(defn timeout-with-fallback-cache [name tags timeout-ms the-fn]
  (let [cache           (atom {})
        now-ms          (fn [] (.getTime (java.util.Date.)))
        store-in-cache! (fn store-in-cache [cache-key res]
                          (swap! cache assoc cache-key {:res res :time (now-ms)})
                          res)
        in-cache-and-not-expired? (fn in-cache-and-not-expired [cache-key]
                                    (if-let [entry (get @cache cache-key)]
                                      (< (- (now-ms) (:time entry)) timeout-ms)
                                      false))]
    (register-cache name tags cache)
    (fn timeout-with-fallback-cache-inner [& args]
      (cond
        (not (contains? @cache args))
        (store-in-cache! args (apply the-fn args))

        (in-cache-and-not-expired? args)
        (:res (get @cache args))

        :in-cache-but-expired
        (try
         (log/infof "in-cache-and-not-expired: refetching: %s" args)
         (store-in-cache! args (apply the-fn args))
         (catch Exception ex
           (log/errorf ex "Error executing wrapped function! (will return old cached value) %s" ex)
           (:res (get @cache args))))))))

(defmacro def-timeout-with-fallback-cache [fn-name timeout-ms args-spec & body]
  `(def ~fn-name
        (timeout-with-fallback-cache ~(keyword (str *ns* "." fn-name)) #{:timeout :fallback} ~timeout-ms
          (fn ~args-spec
            ~@body))))



(defn invalidate-standard-cache [cache-name args]
  (let [cache
        (->
         @cache-registry
         (get cache-name)
         :cache)]
    (swap! cache
           dissoc args)))
