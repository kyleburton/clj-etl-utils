(ns clj-etl-utils.test-helper
  (:require [clojure.string :as str-utils]
            [clojure.tools.logging :as log]))

(defonce fixture-registry (atom {}))

(defn mm-get [m k1 k2]
  (and (contains? m k1)
       ((m k1) k2)))

(defn mm-put [m k1 k2 v]
  (assoc m k1
         (assoc (m k1 {})
           k2 v)))

(defn mm-contains? [m k1 k2]
  (and (contains? m k1)
       (contains? (m k1) k2)))

(defn clear-fixture-registry []
  (reset! fixture-registry (atom {})))

(defn register-fixture [type k v]
  (reset! fixture-registry
          (mm-put @fixture-registry type k v)))

;; is this strategy good enough?
(defn project-root []
  (if-let [location (.getLocation (.getCodeSource (.getProtectionDomain (.getClass project-root))))]
    (do
      (log/debug (format "project-root: from class location: %s" location))
      location)
    (do
      (log/debug (format "project-root: falling back to user.dir"))
      (System/getProperty "user.dir"))))

(defn fixture-file [file]
  (format "%s/test/fixtures/files/%s" (project-root) file))

(defn fixture-file-contents [file]
  (let [fixture-path (fixture-file file)]
    (if (not (.exists (java.io.File. fixture-path)))
      (throw (format "Error: no such fixture file '%s' => '%s'" file fixture-path)))
    (slurp fixture-path)))

;; load/access a fixture
(defn fixture [type key]
  (cond (= :file type)                                (fixture-file-contents key)
        (mm-contains? @fixture-registry type key  )   (mm-get @fixture-registry type key)
        :else                                         (throw (RuntimeException. (format "Error: unknown fixture type: %s / %s  registry(%s)" type key (keys @fixture-registry))))))



(comment

  (fixture-file-contents "sample.in.txt")

  (let [props (System/getProperties)
        keys  (.stringPropertyNames props)]
    (doseq [key keys]
      (printf "%s: %s\n" key (.getProperty props key))))

  ;; getClass().getProtectionDomain().getCodeSource().getLocation()


  (foo)


)
