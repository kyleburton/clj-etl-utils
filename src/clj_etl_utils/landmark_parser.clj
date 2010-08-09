(ns clj-etl-utils.landmark-parser
  (:import [java.util.regex Pattern Matcher])
  (:use [com.github.kyleburton.sandbox.utils :as kutils])
  (:use [com.github.kyleburton.sandbox.regex :as regex-util])
  (:use [clojure.contrib.str-utils :as str]
        [clojure.contrib.fcase :only (case)]))

(declare *cmds*)

(defstruct parser :pos :doc :ldoc :doclen)

(defn make-parser [#^String doc]
  (struct-map parser
    :pos (atom 0)
    :ldoc (.toLowerCase doc)
    :doclen (.length doc)
    :doc doc))

(defn forward-past [parser landmark]
  (let [pos (.indexOf (:ldoc parser) (.toLowerCase landmark) @(:pos parser))]
    (if (= -1 pos)
      false
      (do
        (reset! (:pos parser) (+ pos (count landmark)))
        @(:pos parser)))))

(defn forward-to [parser landmark]
  (let [start (:post parser)
        pos (.indexOf (:ldoc parser) (.toLowerCase landmark) @(:pos parser))]
    (if (= -1 pos)
      false
      (do
        (reset! (:pos parser) pos)
        @(:pos parser)))))

(defn set-pos! [parser pos]
  (if (or (> pos (:doclen parser))
          (< pos 0))
    false
    (do
      (reset! (:pos parser) pos)
      true)))

(defn forward [parser cnt]
  (let [pos (+ cnt @(:pos parser))]
    (if (> pos (:doclen parser))
      false
      (do
        (reset! (:pos parser) pos)
        true))))

(defn rewind [parser cnt]
  (let [pos (- @(:pos parser) cnt)]
    (if (< pos 0)
      false
      (do
        (reset! (:pos parser) pos)
        true))))


(defn rewind-to [p landmark]
  (let [pos (.lastIndexOf (:ldoc p)
                          (.toLowerCase landmark)
                          @(:pos p))]
    (if (= -1 pos)
      false
      (do
        (reset! (:pos p) (+ pos (count landmark)))
        @(:pos p)))))

(defn rewind-past [p landmark]
  (let [pos (.lastIndexOf (:ldoc p)
                          (.toLowerCase landmark)
                          @(:pos p))]
    (if (= -1 pos)
      false
      (do
        (reset! (:pos p) pos)
        @(:pos p)))))

;; support either '((:fp "foo") (:fp "bar"))
;;             or '(:fp "foo" :fp "bar")
(defn parse-cmds [cmds]
  (cond (and (seq? cmds)
             (seq? (first cmds))
             (= 2 (count (first cmds))))
        cmds
        (= 1 (mod (count cmds) 2))
        (kutils/raise (format "parse-cmds: error, odd number of commands (expected even, symbol/landmark): cmds=%s" cmds))
        true
        (partition 2 cmds)))

(defn apply-commands [parser & cmds]
  (loop [[[cmd & args] & cmds] (parse-cmds cmds)]
    (if cmd
      (do
        ;(prn (format  "cmd=%s args=%s" cmd args))
        (if (apply (*cmds* cmd) (cons parser args))
          (do
            (recur cmds))
          false))
      true)))

(defn do-commands [parser cmds]
  ;; (prn "do-commands: cmds=" cmds)
  (loop [[[cmd & args] & cmds] (parse-cmds cmds)]
    (if cmd
      (do
        ;(prn (format  "pos:%d cmd=%s args=%s" @(:pos parser) cmd args))
        (if (apply (*cmds* cmd) (cons parser args))
          (do
            ;(prn (format  "pos:%d cmd=%s args=%s" @(:pos parser) cmd args))
            (recur cmds))
          false))
      true)))

(defn forward-past-regex
  "See also regex-util/*common-regexes*"
  [p regex]
  (kutils/log "forward-past-regex regex=%s" regex)
  (let [pat (if (and (keyword? regex) (regex regex-util/*common-regexes*))
              (regex regex-util/*common-regexes*)
              (Pattern/compile (str regex) (bit-or Pattern/MULTILINE Pattern/CASE_INSENSITIVE)))
        m   (.matcher pat (:doc p))]
    (kutils/log "forward-past-regex: pat=%s m=%s" pat m)
    (if (.find m @(:pos p))
      (do
        (kutils/log "forward-past-regex: found reg:%s at:(%d,%d,)" regex (.start m) (.end m))
        (reset! (:pos p) (.end m))
        @(:pos p))
      false)))

(defn forward-to-regex [p regex]
  "See also regex-util/*common-regexes*"
  (let [pat (if (and (keyword? regex) (regex regex-util/*common-regexes*))
              (regex regex-util/*common-regexes*)
              (Pattern/compile (str regex) (bit-or Pattern/MULTILINE Pattern/CASE_INSENSITIVE)))
        m   (.matcher pat (:doc p))]
    (kutils/log "forward-to-regex: using pat=%s" pat)
    (if (.find m @(:pos p))
      (do
        (reset! (:pos p) (.start m))
        @(:pos p))
      false)))

(def *cmds*
     {:apply-commands        apply-commands
      :a                     apply-commands
      :do-commands           do-commands
      :d                     do-commands
      :forward               forward
      :f                     forward
      :forward-past          forward-past
      :fp                    forward-past
      :forward-past-regex    forward-past-regex
      :fpr                   forward-past-regex
      :forward-to            forward-to
      :ft                    forward-to
      :forward-to-regex      forward-to-regex
      :ftr                   forward-to-regex
      :rewind                rewind
      :r                     rewind
      :rewind-to             rewind-to
      :rt                    rewind-to
      :rewind-past           rewind-past
      :rp                    rewind-past})


(defn doc-substr [parser cnt]
  (.substring (:doc parser)
              @(:pos parser)
              (+ @(:pos parser)
                 cnt)))

(defn extract [p start-cmds end-cmds]
  (let [orig-pos @(:pos p)]
    ;(prn (format "running start-cmds from:%d looking for %s" orig-pos start-cmds))
    (if (do-commands p start-cmds)
      (let [spos @(:pos p)]
        ;(prn (format "found start at:%d looking for end %s" spos end-cmds))
        (if (do-commands p end-cmds)
          (.substring (:doc p)
                      spos
                      @(:pos p))
          (do (set-pos! p orig-pos)
              false)))
      (do (set-pos! p orig-pos)
          false))))

(defn extract-from [html start-cmds end-cmds]
  (extract (make-parser html) start-cmds end-cmds))


(defn extract-all [p start-cmds end-cmds]
  (loop [res []]
    (if (do-commands p start-cmds)
      (let [spos @(:pos p)]
        (if (do-commands p end-cmds)
          (recur (conj res (.substring (:doc p) spos @(:pos p))))
          res))
      res)))

(defn extract-all-from [html start-cmds end-cmds]
  (extract-all (make-parser html) start-cmds end-cmds))

(defn table-rows [html]
  (extract-all-from html
                    '(:ft "<tr")
                    '(:fp "</tr")))

(defn row->cells [html]
  (extract-all-from html
                    '(:fp "<td" :fp ">")
                    '(:ft "</td>")))

(defn html->links [html]
  (extract-all-from html
                    '(:fp "href=\"")
                    '(:ft "\"")))

(defn html->anchors [html]
  (extract-all-from html
                    '(:ft "<a ")
                    '(:fp "</a>")))

(defn anchor->href [html]
  (first (kutils/re-find-first #"href=\"([^\"]+)\"" html)))

(defn anchor->body [html]
  (first (kutils/re-find-first #">(.+?)</a>" html)))

(defn html-find-link-with-body [html text]
  (first
   (kutils/re-find-first
    #"href=\"([^\"]+)\""
    (first
     (filter #(.contains % text)
             (html->anchors html))))))


(defn html->tables [html]
  (extract-all-from html
                    '(:ft "<table")
                    '(:fp "</table>")))

(defn html-table->matrix [html]
  (map row->cells (table-rows html)))

(defn html->form-blocks [html]
  (extract-all-from html
                    '(:ft "<form")
                    '(:fp "</form>")))


;; (def p (make-parser (com.github.kyleburton.sandbox.web/get->string "http://asymmetrical-view.com/")))
;; (forward-past-regex p :num-real)
;; (forward-to-regex p #"\d{4}")

;; (def pat (Pattern/compile (str #"\d{4}") (bit-or Pattern/MULTILINE Pattern/CASE_INSENSITIVE)))
;; (def m (.matcher pat (:doc p)))


;; (html->links (com.github.kyleburton.sandbox.web/get->string "http://asymmetrical-view.com/"))

(defn parse-input-element [html]
  {:tag   :input
   :type  (first (kutils/re-find-first "(?-ims:type=\"([^\"]+)\")" html))
   :name  (first (kutils/re-find-first "(?-ims:name=\"([^\"]+)\")" html))
   :value (first (kutils/re-find-first "(?-ims:value=\"([^\"]+)\")" html))
   })

;; This technique won't work reliably...need to implement :forward-to-first-of '(:ft "<input") '(:ftfo "/>" "</input>"
;; TODO: parse out textarea, button and select
(defn parse-form-elements [html]
  (apply concat [(map parse-input-element (extract-all-from html '(:ft "<input") '(:fp ">")))
;;                  (extract-all-from html '(:ft "<textarea") '(:fp "</textarea>"))
;;                  (extract-all-from html '(:ft "<button") '(:fp ">"))
;;                  (extract-all-from html '(:ft "<select") '(:fp "</select>"))
                 ]))

;;(parse-form-elements (first (html->form-blocks com.github.kyleburton.sandbox.web/html)))

(defn parse-form [html]
  {:method (or (first (kutils/re-find-first "(?-ims:method=\"([^\"]+)\")" html))
               "GET")
   :action (or (first (kutils/re-find-first "(?-ims:action=\"([^\"]+)\")" html))
               nil)
   :params (vec (parse-form-elements html))
   })







