(ns
    ^{:doc "Text manipulation utilities."
      :author "Kyle Burton"}
  clj-etl-utils.text
  (:use [clj-etl-utils.lang-utils  :only [raise]])
  (:require    [clojure.string        :as str-utils])
  (:import [org.apache.commons.lang WordUtils]
           [java.text NumberFormat DecimalFormat]
           [org.apache.commons.codec.binary Base64]))

(defn
  ^{:doc "Convert string to upper case, null safe (returns empty string on null)."}
  uc [^String s]
  (if (nil? s)
    ""
    (.toUpperCase s)))

(defn
  ^{:doc "Convert string to lower case, null safe (returns empty string on null)."}
  lc [^String s]
  (if (nil? s)
    ""
    (.toLowerCase s)))

(defmacro
  ^{:doc "Binds a temporary file to the symbol indicated by var (java.io.File/createTempFile).
prefix and suffix default to \"pfx\" and \"sfx\" respectively.  Note that this macro does not
create or clean up the actual temporary file itself.
  "}
  with-tmp-file [[var & [prefix suffix]] & body]
  `(let [prefix# ~prefix
         suffix# ~suffix
         ~var (java.io.File/createTempFile (or prefix# "pfx") (or suffix# "sfx"))]
     ~@body))

(defn
  ^{:doc "Compute the MD5 sum of a byte buffer, returning it as a hex-encoded string."}
  md5->string [^bytes bytes]
  (let [digester (java.security.MessageDigest/getInstance "MD5")]
    (.update digester bytes)
    (.toString
     (java.math.BigInteger. 1 (.digest digester))
     16)))

(defn
  ^{:doc "Compute the SHA1 sum of a byte buffer, returning it as a hex-encoded string."}
  sha1->string [^bytes bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA1")]
    (.update digester bytes)
    (.toString
     (java.math.BigInteger. 1 (.digest digester))
     16)))

(defn
  ^{:doc "Returns a sequence of all the security providers available in the current JVM.
The sequence consists of pairs of [provider-type provider-algorithm]"}
  security-providers-type-algorithm-seq []
  (mapcat (fn [provider]
            (map (fn [^java.security.Provider$Service svc]
                   [(.getType svc) (.getAlgorithm svc)])
                 (.getServices ^java.security.Provider provider)))
          (java.security.Security/getProviders)))

(defn
  ^{:doc "Returns a seq of all of the provider types available in the current JVM."}
  security-providers-types []
  (vec (set (map first (security-providers-type-algorithm-seq)))))

(defn
  ^{:doc "Filters security-providers-type-algorithm-seq for those that match the given type.
  (security-providers-for-type \"MessageDigest\")
  "}
  security-providers-for-type [type]
  (filter #(= (first %) type)
          (security-providers-type-algorithm-seq)))

(defn
  ^{:doc "Sequence of all the MessageDigest providers available in the current JVM."}
  message-digest-algorithms []
  (security-providers-for-type "MessageDigest"))

(comment

  (security-providers-types)

  (message-digest-algorithms)

  )

(defn
  ^{:doc "Compute and return the SHA1 sum of the given string, returned as a hex-encoded string."}
  string->sha1 [^String s]
  (sha1->string (.getBytes s)))

(defn
  ^{:doc "Compute and return the MD5 sum of the given string, returned as a hex-encoded string."}
  string->md5 [^String s]
  (md5->string (.getBytes s)))


(defn
  ^{:doc "Compute and return the SHA256 sum of the given byte array, returned as a hex-encoded string."}
  sha256->string [^bytes bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-256")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn
  ^{:doc "Compute and return the SHA256 sum of the given string, returned as a hex-encoded string."}
  string->sha256 [^String s]
  (sha256->string (.getBytes s)))


(defn
  ^{:doc "Compute and return the SHA384 sum of the byte array, returned as a hex-encoded string."}
  sha384->string [^bytes bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-384")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn
  ^{:doc "Compute and return the SHA384 sum of the given string, returned as a hex-encoded string."}
  string->sha384 [^String s]
  (sha384->string (.getBytes s)))

(defn
  ^{:doc "Compute and return the SHA512 sum of the byte array, returned as a hex-encoded string."}
  sha512->string [^bytes bytes]
  (let [digester (java.security.MessageDigest/getInstance "SHA-512")]
    (.update digester bytes)
    (apply str (map (fn [byte]
                      (Integer/toHexString (bit-and 0xFF byte)))
                    (.digest digester)))))

(defn
  ^{:doc "Compute and return the SHA512 sum of the given string, returned as a hex-encoded string."}
  string->sha512 [^String s]
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
(defn
  ^{:doc "Current time in milliseconds."}
  now-milliseconds []
  (.getTime (java.util.Date.)))


(defn
  ^{:doc "Substring that supports negative starting positions (negative takes the last N'th characters from the right-hand side of the string).

  (substr \"the quick brown fox\" 10) => \"brown fox\"
  (substr \"the quick brown fox\" -3) => \"fox\"

  "}
  substr [^String s start & [end]]
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

(defn
  ^{:doc "Prodcues a human-readable (friendly unit sizes) count of the number of bytes provided (as a string).

  (human-readable-byte-count 1023)                                    \"1023B\"
  (human-readable-byte-count 1024)                                    \"1.00KiB\"
  (human-readable-byte-count (* 1024 1024))                           \"1.00MiB\"
  (human-readable-byte-count (* 1024 1024 1024))                      \"1.00GiB\"
  (human-readable-byte-count (+ 1 (* 1024 1024 1024)))                \"1.00GiB\"
  (human-readable-byte-count (* 1024 1024 1024 1024))                 \"1.00TiB\"
  (human-readable-byte-count (* 1024 1024 1024 1024 1024))            \"1.00PiB\"
  (human-readable-byte-count (* 1024 1024 1024 1024 1024 1024))       \"1.00EiB\"
  (human-readable-byte-count (* 1024 1024 1024 1024 1024 1024 1024)) => Error, no Si prefix for this size


Taken from: http://stackoverflow.com/questions/3758606/how-to-convert-byte-size-into-human-readable-format-in-java

"}
  human-readable-byte-count
  ([nbytes]
     (human-readable-byte-count nbytes false))
  ([nbytes use-si]
     (let [unit (if use-si 1000 1024)
           exp (int (/ (Math/log nbytes) (Math/log unit)))]
       (if (< nbytes unit)
         (str nbytes "B")
         (format "%.2f%sB"
                 (/ nbytes (Math/pow unit exp))
                 (str
                  (.charAt
                   (if use-si
                     "kMGTPE"
                     "KMGTPE")
                   (dec exp))
                  (if use-si
                    ""
                    "i")))))))

(comment


  )


(defn
  ^{:doc "Wrap a string (sentence or paragraph) at a maximum length.

  (word-split \"This is a long sentence, if it were documentation someone would be happy and someone would be unsatisified.  That is the way of things.\" 50)
    =>
    (\"This is a long sentence, if it were documentation\" \"someone would be happy and someone would be\" \"unsatisified.  That is the way of things.\")

"}
  word-split
  ([^String str size]
     (word-split str size "\0"))
  ([^String str size ^String delim]
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


(comment


  )


(def formatter-setters
     {:negative-prefix (fn [^DecimalFormat nf ^String x] (.setNegativePrefix nf x))
      :negative-suffix (fn [^DecimalFormat nf ^String x] (.setNegativeSuffix nf x))
      :positive-prefix (fn [^DecimalFormat nf ^String x] (.setPositivePrefix nf x))
      :positive-suffix (fn [^DecimalFormat nf ^String x] (.setPositiveSuffix nf x))})

(defn
  ^{:doc ""}
  apply-format-setter [^NumberFormat nf k v]
  (if-not (contains? formatter-setters k)
    (raise "set-formatter-option: option not yet implemented: %s" k))
  ((get formatter-setters k) nf v)
  nf)

(declare default-formatters)

(defn get-currency-formatter [opts-or-keyword]
  (cond
    (map? opts-or-keyword)
    (reduce (fn [formatter [k v]]
              (apply-format-setter formatter k v))
            (java.text.NumberFormat/getCurrencyInstance)
            opts-or-keyword)
    (keyword? opts-or-keyword)
    (or (get @default-formatters opts-or-keyword)
        (raise "Error: formatter not found for keyword: %s" opts-or-keyword))
    :else
    (raise "Error: unrecognized formatter spec (not a map or keyword): [%s] %s"
           (class opts-or-keyword) opts-or-keyword)))

(def currency-with-negative (get-currency-formatter {:negative-prefix "-$" :negative-suffix ""}))

(def default-formatters
     (atom
      {:currency-with-negative currency-with-negative
       :default                (get-currency-formatter {})}))




(defn format-as-currency
  ([num]
     (format-as-currency num :default))
  ([num opts]
     (.format ^java.text.Format (get-currency-formatter opts)
              num)))

(defonce rx-clean-phone-number #"\D+")


(defn canonical-phone-number [^String mobile-number]
  (if (nil? mobile-number)
    ""
    (let [num (str-utils/replace  mobile-number rx-clean-phone-number "")]
      (if (= 10 (count num))
        (str 1 num)
        num))))


(defn uncanonicalize-phone-number [^String mobile-number]
  (let [phone-number (canonical-phone-number mobile-number)
        [_ area-code central-office subscriber-number] (re-find #"\d{1}(\d{3})(\d{3})(\d{4})" phone-number)]
    (format "%s-%s-%s" area-code central-office subscriber-number)))

(defn snake-case [^String s]
  (.toString
   ^StringBuilder
   (reduce
    (fn [b c]
      (if (Character/isUpperCase c)
        (do
          (.append ^StringBuilder b "-")
          (.append ^StringBuilder b ^CharSequence (clojure.string/lower-case c)))
        (.append ^StringBuilder b c)))
    (StringBuilder.)
    (name s))))

(defn camel->snake [^java.util.Map params]
  (reduce
   (fn [accum [k v]]
     (assoc accum (keyword (snake-case k)) v))
   {}
   params))

(defn camel->underscore [^java.util.Map params]
  (reduce
   (fn [accum [k v]]
     (assoc accum (keyword (.replaceAll ^String (snake-case k) "-" "_")) v))
   {}
   params))

(defn snake->underscore [^java.util.Map params]
  (reduce
   (fn [accum [k v]]
     (assoc accum (keyword (.replaceAll (name k) "-" "_")) v))
   {}
   params))

(defn underscore->snake [^java.util.Map params]
  (reduce
   (fn [accum [k v]]
     (assoc accum (keyword (.replaceAll (name k) "_" "-")) v))
   {}
   params))

(defn camelize-keyword [k]
  (let [[res & parts] (.split (name k) "[-_]")]
    (loop [res         res
           [n & parts] parts]
      (if-not n
        (keyword res)
        (recur (str res (org.apache.commons.lang.WordUtils/capitalize n))
               parts)))))

(defn camelize-map-keys [m]
  (reduce
   (fn [accum [k v]]
     (assoc accum
       (camelize-keyword k) v))
   {}
   m))

(def encode-base64
     (let [b (Base64.)]
       (fn encode-base64 [raw]
         (.encode b raw))))

(def decode-base64
     (let [b (Base64.)]
       (fn decode-base64 [coded]
         (.decode b coded))))


(defn summarize-message
  ([msg len]
     (summarize-message msg len "'" "..."))
  ([msg len delimiter summary-marker]
     (if (> (count msg) len)
       (str delimiter (first (word-split msg len)) summary-marker delimiter)
       (str delimiter msg delimiter))))


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


(defn trim-and-truncate
  ([^String value ^Number max-len]
     (trim-and-truncate value max-len nil))
  ([^String value ^Number max-len ^String default-value]
     (cond
       (nil? value)
       default-value

       :trim-and-truncate
       (let [value (.trim value)]
         (if (empty? value)
           default-value
           (.trim (substr value 0 max-len)))))))
