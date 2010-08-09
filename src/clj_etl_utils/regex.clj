(ns clj-etl-utils.regex)

;; regexes, initial set pulled from Regex::Common CPAN module
(def *common-regexes*
  {:num-real #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[0123456789]+))|)))"
   :num-int #"(?-xism:(?:(?:[+-]?)(?:[0123456789]+)))"
   :num-decimal #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)))"
   :num-hex #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789ABCDEF]|[.])(?:[0123456789ABCDEF]*)(?:(?:[.])(?:[0123456789ABCDEF]{0,}))?)(?:(?:[G])(?:(?:[+-]?)(?:[0123456789ABCDEF]+))|)))"
   :num-dec #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[0123456789]|[.])(?:[0123456789]*)(?:(?:[.])(?:[0123456789]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[0123456789]+))|)))"
   :num-oct #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[01234567]|[.])(?:[01234567]*)(?:(?:[.])(?:[01234567]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[01234567]+))|)))"
   :num-bin #"(?-xism:(?:(?i)(?:[+-]?)(?:(?=[01]|[.])(?:[01]*)(?:(?:[.])(?:[01]{0,}))?)(?:(?:[E])(?:(?:[+-]?)(?:[01]+))|)))"
   :num-roman #"(?-xism:(?xi)(?=[MDCLXVI])
                         (?:M{0,3}
                            (D?C{0,3}|CD|CM)?
                            (L?X{0,3}|XL|XC)?
                            (V?I{0,3}|IV|IX)?))"
   :zip #"(?-xism:(?:(?:(?:USA?)-){0,1}(?:(?:(?:[0-9]{3})(?:[0-9]{2}))(?:(?:-)(?:(?:[0-9]{2})(?:[0-9]{2}))){0,1})))"
   :net-ipv4 #"(?-xism:(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[.](?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})))"
   :net-mac #"(?-xism:(?:(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2}):(?:[0-9a-fA-F]{1,2})))"
   :net-domain #"(?-xism:(?: |(?:[A-Za-z](?:(?:[-A-Za-z0-9]){0,61}[A-Za-z0-9])?(?:\.[A-Za-z](?:(?:[-A-Za-z0-9]){0,61}[A-Za-z0-9])?)*)))"
   :phone #"(?:1[- ]?)?\(?[2-9]\d{2}\)?[-\. ]?\d{3}[-\. ]?\d{4}(?:\s*(?:e|ex|ext|x|xtn|extension)?\s*\d*)"
   :us-states        (Pattern/compile (format "(?-xism:%s)" (str/str-join "|" (keys ref-data/*us-states*))))
   :us-state-names   (Pattern/compile (format "(?-xism:%s)" (str/str-join "|" (vals ref-data/*us-states*))))
   :us-airport-codes (Pattern/compile (format "(?-xism:%s)" (str/str-join "|" (map #(nth % 2) ref-data/*us-airport-codes*))))
   :us-area-codes    (Pattern/compile (format "(?-xism:%s)" (str/str-join "|" ref-data/*us-area-codes*)))


   :word           #"(?:[\w-]+)"
   :punctuation    #"(?:[\.,\?/'\";:\\`~!\(\)]+)"
   })




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

