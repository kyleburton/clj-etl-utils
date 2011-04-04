(ns ^{:doc "Collection of commonly used regular expressions."
      :author "Kyle Burton"} clj-etl-utils.regex
      (:import
       [java.util.regex Pattern Matcher])
      (:require
       [clojure.contrib.str-utils :as str]
       [clj-etl-utils.ref-data :as ref-data]))

;; *ns*

;; regexes, initial set pulled from Regex::Common CPAN module
(def *common-regexes*
     {:numeric
      {:real #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[0123456789]+))|)))"
       :int #"(?xism:(?:(?:[+-]?)(?:[0123456789]+)))"
       :dec     #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[0123456789]+))|)))"
       :decimal #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)))"
       :hex #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789ABCDEF]|[.])(?:[0123456789ABCDEF]*)(?:(?:[.])(?:[0123456789ABCDEF]{0,}))?)(?:(?:[G])(?:(?:[+-]?)(?:[0123456789ABCDEF]+))|)))"
       :oct #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[01234567]|[.])(?:[01234567]*)(?:(?:[.])(?:[01234567]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[01234567]+))|)))"
       :bin #"(?xism:(?:(?i)(?:[+-]?)(?:(?=[01]|[.])(?:[01]*)(?:(?:[.])(?:[01]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[01]+))|)))"
       :roman #"(?xism:(?xi)(?=[MDCLXVI])
                         (?:M{0,3}
                            (D?C{0,3}|CD|CM)?
                            (L?X{0,3}|XL|XC)?
                            (V?I{0,3}|IV|IX)?))"}
      :geographic
      {:iso
       {:country-3 (Pattern/compile
                    (format
                     "(?xism:%s)"
                     (str/str-join
                      "|"
                      (map first ref-data/*iso-3-country-codes*))))

        :country-2 (Pattern/compile
                    (format
                     "(?xism:%s)"
                     (str/str-join
                      "|"
                      (map first ref-data/*iso-2-country-codes*))))}
       :usa
       {:zip #"(?xism:(?:(?:(?:USA?)-){0,1}(?:(?:(?:[0-9]{3})(?:[0-9]{2}))(?:(?:-)(?:(?:[0-9]{2})(?:[0-9]{2}))){0,1})))"
        ;; NB: same as zip, just using a consistent name
        :postal-code #"(?xism:(?:(?:(?:USA?)-){0,1}(?:(?:(?:[0-9]{3})(?:[0-9]{2}))(?:(?:-)(?:(?:[0-9]{2})(?:[0-9]{2}))){0,1})))"
        :state        (Pattern/compile (format "(?xism:%s)" (str/str-join "|" (map first ref-data/*us-states*))))
        :state-name   (Pattern/compile (format "(?xism:%s)" (str/str-join "|" (map second ref-data/*us-states*))))
        :airport-code (Pattern/compile (format "(?xism:%s)" (str/str-join "|" (map #(nth % 2) ref-data/*us-airport-codes*))))
        :area-code    (Pattern/compile (format "(?xism:%s)" (str/str-join "|" ref-data/*us-area-codes*)))
        :phone #"(?:1[- ]?)?\(?[2-9]\d{2}\)?[-\. ]?\d{3}[-\. ]?\d{4}(?:\s*(?:e|ex|ext|x|xtn|extension)?\s*\d*)"}

       :can
       {:postal-code #"[ABCEGHJKLMNPRSTVXY]\d[ABCEGHJKLMNPRSTVWXYZ]( )?\d[ABCEGHJKLMNPRSTVWXYZ]\d"}}

      :internet
      {:ipv4 #"(?xism:(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})))"
       :mac #"(?xism:(?:(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2})))"
       :net-domain #"(?xism:(?: |(?:[A-Za-z](?:(?:[-A-Za-z0-9]){0,61}[A-Za-z0-9])?(?:\.[A-Za-z](?:(?:[-A-Za-z0-9]){0,61}[A-Za-z0-9])?)*)))"}
      :general
      {:word           #"(?:[\w-]+)"
       :punctuation    #"(?:[\.,\?/'\";:\\`~!\(\)]+)"}})


(comment
  (:zip *common-regexes*)

  )

(defn std-regex [& path]
  (get-in *common-regexes* path))

(defn std-regex-compose [& paths]
  (Pattern/compile (format "(?:%s)"
                           (str/str-join
                            "|"
                            (map (fn [path]
                                   (apply std-regex path))
                                 paths)))))

(defn all-groups
  "Extracts all the groups from a java.util.regex.Matcher into a seq."
  [#^java.util.regex.Matcher m]
  (for [grp (range 1 (+ 1 (.groupCount m)))]
    (.group m grp)))


(defn re-find-all
  "Retreive all of the matches for a regex in a given string."
  [re str]
  (doall
   (loop [m (re-matcher (if (isa? (class re) String) (re-pattern re) re) str)
          res []]
     (if (.find m)
       (recur m (conj res (vec (all-groups m))))
       res))))

(defn re-find-first
  "Retreive the first set of match groups for a regex in a given string."
  [re str]
  (first
   (doall
    (loop [m (re-matcher (if (isa? (class re) String) (re-pattern re) re) str)
           res []]
      (if (.find m)
        (recur m (conj res (vec (all-groups m))))
        res)))))

