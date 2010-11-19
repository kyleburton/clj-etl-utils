(ns
    ^{:doc "Text manipulation utilities not yet in Clojure or Contrib."
      :author "Kyle Burton"}
    clj-etl-utils.text)

(defn uc [#^String s] (.toUpperCase s))
(defn lc [#^String s] (.toLowerCase s))
(defmacro with-tmp-file [[var & [prefix suffix]] & body]
  `(let [prefix# ~prefix
         suffix# ~suffix
         ~var (java.io.File/createTempFile (or prefix# "pfx") (or suffix# "sfx"))]
     ~@body))

(defn md5->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "MD5")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn sha1->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA1")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

;; (md5->string (.getBytes "foo bar\n"))
;; (sha1->string (.getBytes "foo bar\n"))

(defn string->sha1 [s]
  (sha1->string (.getBytes s)))

(defn string->md5 [s]
  (md5->string (.getBytes s)))

;; TODO this doesn't belong in text.clj, couldn't think of a better place for it
(defn now-milliseconds []
  (.getTime (java.util.Date.)))





