(ns
    ^{:doc "Text manipulation utilities not yet in Clojure or Contrib."
      :author "Kyle Burton"}
  clj-etl-utils.text
  (:use [clj-etl-utils.lang-utils  :only [raise]])
  (:import [org.apache.commons.lang WordUtils]
           [java.text NumberFormat]))

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

(defn security-providers-type-algorithm-seq []
  (mapcat (fn [provider]
            (map (fn [svc]
                   [(.getType svc) (.getAlgorithm svc)])
                 (.getServices provider)))
          (java.security.Security/getProviders)))

(defn security-providers-types []
  (vec (set (map first (security-providers-type-algorithm-seq)))))

(defn security-providers-for-type [type]
  (filter #(= (first %) type)
          (security-providers-type-algorithm-seq)))

(defn message-digest-algorithms []
  (security-providers-for-type "MessageDigest"))

(comment

  (security-providers-types)

  (message-digest-algorithms)

  )

(defn string->sha1 [s]
  (sha1->string (.getBytes s)))

(defn string->md5 [s]
  (md5->string (.getBytes s)))


(defn sha256->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-256")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn string->sha256 [s]
  (sha256->string (.getBytes s)))


(defn sha384->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-384")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn string->sha384 [s]
  (sha384->string (.getBytes s)))

(defn sha512->string [bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-512")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn string->sha512 [s]
  (sha512->string (.getBytes s)))

(comment

  (count (string->sha1 "foof"))   ;;  40
  (count (string->sha256 "foof")) ;;  63
  (count (string->sha384 "foof")) ;;  90
  (count (string->sha512 "foof")) ;; 126

  (time
   (dotimes [ii 10000]
     (string->sha1 "foof")))

  (time
   (dotimes [ii 10000]
     (string->sha256 "foof")))

  (time
   (dotimes [ii 10000]
     (string->sha512 "foof")))


  )



;; TODO this doesn't belong in text.clj, couldn't think of a better place for it
(defn now-milliseconds []
  (.getTime (java.util.Date.)))


(defn substr [^String s start & [end]]
  (cond
    (and (< start 0)
         (not end))
    (let [start (+ (count s) start)]
      (if (< start 0)
        s
        (.substring s start)))

    (> start (count s))
    ""
    (or (not end)
        (> end (count s)))
    (.substring s start)

    :else
    (.substring s start end)))


(comment

  (= ""   (substr ""     0     0))
  (= ""   (substr "a"    0     0))
  (= "a"  (substr "a"    0))
  (= "a"  (substr "a"    0     1))
  (= "a"  (substr "a"    0    99))
  (= ""   (substr "a"    99))
  (= ""   (substr "a"    99 199))
  (= "a"  (substr "a"    -1))
  (= "bc" (clj-etl-utils.text/substr "abc"  -2))
  (= ""   (substr "abc"  -9)))



;; "public static String humanReadableByteCount(long bytes, boolean si) {
;;     int unit = si ? 1000 : 1024;
;;     if (bytes < unit) return bytes + " B ";
;;     int exp = (int) (Math.log(bytes) / Math.log(unit));
;;     String pre = (si ? "kMGTPE " : "KMGTPE ").charAt(exp-1) + (si ? " " : "i ");
;;     return String.format("%.1f %sB ", bytes / Math.pow(unit, exp), pre);
;; }"

(defn human-readable-byte-count
  ([bytes]
     (human-readable-byte-count bytes false))
  ([bytes use-si]
     "Taken from: http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java"
     (let [unit (if use-si 1000 1024)
           exp (int (/ (Math/log bytes) (Math/log unit)))]
       (if (< bytes unit)
         (str bytes "B")
         (format "%.2f%sB"
                 (/ bytes (Math/pow unit exp))
                 (str
                  (.charAt
                   (if use-si
                     "kMGTPE"
                     "KMGTPE")
                   (dec exp))
                  (if use-si
                    ""
                    "i")))))))


(defn word-split
  ([str size]
     (word-split str size "\0"))
  ([str size delim]
     (if (>= (.indexOf str delim) 0)
       (raise "Input string must not contain delimiter string (%s). Unable to split (input string=%s" delim str)
       (seq
        (.split
         (WordUtils/wrap
          str
          size
          delim
          false)
         delim)))))


(def *formatter-setters*
     {:negative-prefix (fn [#^NumberFormat nf x] (.setNegativePrefix nf x))
      :negative-suffix (fn [#^NumberFormat nf x] (.setNegativeSuffix nf x))
      :positive-prefix (fn [#^NumberFormat nf x] (.setPositivePrefix nf x))
      :positive-suffix (fn [#^NumberFormat nf x] (.setPositiveSuffix nf x))})

(defn apply-format-setter [#^NumberFormat nf k v]
  (if-not (contains? *formatter-setters* k)
    (raise "set-formatter-option: option not yet implemented: %s" k))
  ((get *formatter-setters* k) nf v)
  nf)

(declare *default-formatters*)


(defn get-currency-formatter [opts-or-keyword]
  (cond
    (map? opts-or-keyword)
    (reduce (fn [formatter [k v]]
              (apply-format-setter formatter k v))
            (java.text.NumberFormat/getCurrencyInstance)
            opts-or-keyword)
    (keyword? opts-or-keyword)
    (or (get @*default-formatters* opts-or-keyword)
        (raise "Error: formatter not found for keyword: %s" opts-or-keyword))
    :else
    (raise "Error: unrecognized formatter spec (not a map or keyword): [%s] %s"
           (class opts-or-keyword) opts-or-keyword)))

(def *currency-with-negative* (get-currency-formatter {:negative-prefix "-$" :negative-suffix ""}))

(def *default-formatters*
     (atom
      {:currency-with-negative *currency-with-negative*
       :default                (get-currency-formatter {})}))




(defn format-as-currency
  ([num]
     (format-as-currency num :default))
  ([num opts]
     (.format (get-currency-formatter opts)
              num)))


(comment
  (.format (java.text.NumberFormat/getCurrencyInstance) -1234)
  (format-as-currency -1234 :currency-with-negative)
  (format-as-currency -1234 :default)
  (format-as-currency -1234)
  (format-as-currency 1234  :currency-with-negative)

  (human-readable-byte-count 1024)
  (human-readable-byte-count 1024 true)
  (human-readable-byte-count (* 3 1024 1024))
  (human-readable-byte-count (* 3 1024 1024) true)
  )