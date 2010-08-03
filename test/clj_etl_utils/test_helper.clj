(ns clj-etl-utils.test-helper
  (:use [clojure.contrib.str-utils :as str-utils]
        [clojure.contrib.logging :as log]))

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

;; load a fixture
(defn fixture [type key]
  (cond (= :file type)
        (fixture-file-contents key)
        :else
        (throw (format "Error: unknown fixture type: %s / %s" type key))))





(comment

  (fixture-file-contents "sample.in.txt")

  (let [props (System/getProperties)
        keys  (.stringPropertyNames props)]
    (doseq [key keys]
      (printf "%s: %s\n" key (.getProperty props key))))

  ;; getClass().getProtectionDomain().getCodeSource().getLocation()


  (foo)


)