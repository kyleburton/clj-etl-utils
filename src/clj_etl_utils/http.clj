(ns clj-etl-utils.http
  (:use [clj-etl-utils.lang :only [rest-params->map assert-allowed-keys!]])
  (:import
   [org.apache.commons.httpclient Credentials Header HttpClient UsernamePasswordCredentials NameValuePair]
   [org.apache.commons.httpclient.auth AuthScope]
   [org.apache.commons.httpclient.methods InputStreamRequestEntity PostMethod GetMethod]
   [org.apache.commons.lang StringUtils]))

;; TODO: support multiple :basic-auth - support this as a vector of
;; maps instead of just a single map, just have this function test
;; the class of the value of :basic-auth and act appropriately
(defn user-agent [& params]
  (let [params (rest-params->map params)
        ua (HttpClient.)]
    (assert-allowed-keys! params [:follow-redirects :basic-auth])
    ;; TODO: this needs to be carried as state somehow, and applied to
    ;; the HttpMethod's we create and invoke...
    ;; (if (:follow-redirects params)
    ;;   (.setAuthenticationPreemptive (.getParams ua) (:follow-redirects params)))
    (if-let [auth-info (:basic-auth params)]
      (do
        (.setAuthenticationPreemptive (.getParams ua) true)
        (assert-allowed-keys! auth-info [:user :pass :auth-host :auth-port :auth-realm])
        (.setCredentials
         (.getState ua)
         (AuthScope.
          (:auth-host  params AuthScope/ANY_HOST)
          (:auth-port  params AuthScope/ANY_PORT)
          (:auth-realm params AuthScope/ANY_REALM))
         (UsernamePasswordCredentials. (:user auth-info) (:pass auth-info)))))
    (assoc params :ua ua)))

;; (user-agent)
;; (user-agent :follow-redirects false)
;; (user-agent :basic-auth {:user "bob" :pass "0xB0B"})


;; (defn http-method-dispatcher [& args]
;;   (cond
;;     (and (isa? (class (first args))
;;                HttpClient)
;;          (isa? (class (second args))
;;                java.net.URL))
;;     :ua-url-and-params


;;     (and (isa? (class (first args))
;;                HttpClient)
;;          (isa? (class (second args))
;;                java.net.URI))
;;     :ua-uri-and-params

;;     (and (isa? (class (first args))
;;                HttpClient)
;;          (isa? (class (second args))
;;                String))
;;     :ua-url-str-and-params

;;     (and (isa? (class (first args))
;;                java.net.URL))
;;     :url-and-params

;;     (and (isa? (class (first args))
;;                java.net.URI))
;;     :uri-and-params

;;     (and (isa? (class (first args))
;;                String))
;;     :url-str-and-params

;;     :else       :default))


;; (defmulti do-get http-method-dispatcher)

;; (defmulti do-post http-method-dispatcher)

;; (defmethod do-get :ua-url-and-params [ua url & params]
;;   (prn "do-get :ua-url-and-params")
;; )

;; (defmethod do-get :ua-uri-and-params [ua uri & params]
;;   (prn "do-get :ua-uri-and-params"))

;; (defmethod do-get :ua-url-str-and-params [ua url & params]
;;   (let [get               (GetMethod. url)
;;         name-value-pairs  (create-name-value-pairs (second params))]
;;     (.setQueryString get (to-array name-value-pairs))
;;     (println (format "Return Code: %s" (.executeMethod ua get )))
;;     (println (format "Respone: %s" (.getResponeBodyAsString get))))
;;   (prn "do-get :ua-url-str-and-params"))



;; (defmethod do-get :url-and-params [url & params]
;;   (prn "do-get :url-and-params")
;;   (apply do-get (user-agent) url))

;; (defmethod do-get :uri-and-params [& args]
;;   (prn "do-get :uri-and-params")
;;   (apply do-get (user-agent) args))

;; (defmethod do-get :url-str-and-params [& args]
;;   (prn "do-get :url-str-and-params")
;;   (apply do-get (user-agent) args))

;; (defmethod do-get :default [& params]
;;   (prn "do-get :default"))

(defn create-name-value-pairs [params]
  (into-array NameValuePair
   (reduce
    (fn [accum key]
      (cons (NameValuePair. (str key) (params key)) accum))
    []
    (keys params))))

;;; (create-name-value-pairs { :foo "bar" :chicken "turkey" })
;;; (create-name-value-pairs {})


(defn do-get [ua url & params]
  (let [get               (GetMethod. url)
        name-value-pairs  (create-name-value-pairs (second params))]
    (.setQueryString get name-value-pairs)
    (printf "do-get: qs=%s" (.getQueryString get))
    (println (format "Return Code: %s" (.executeMethod (:ua ua) get )))
    (println (format "Respone: %s" (.getResponseBodyAsString get))))
  (prn "do-get :ua-url-str-and-params"))


;;; (defn do-get [user-agent ])

(comment

  @*foo*

  (do-get (user-agent) "http://www.google.com" :params {})


;; http://www.google.com/#hl=en&source=hp&q=yatzee!&aq=f&aqi=g2g-s1g4g-s2g1&aql=&oq=&gs_rfai=CpL9EVhGBTLGjHpaCywTGhYH6BQAAAKoEBU_QNS5J&pbx=1&fp=954aa051d21e9187

;; http://www.google.com/#
  (do-get (user-agent) "http://www.google.com/#hl=en&source=hp&q=yatzee!&aq=f&aqi=g2g-s1g4g-s2g1&aql=&oq=&gs_rfai=CpL9EVhGBTLGjHpaCywTGhYH6BQAAAKoEBU_QNS5J&pbx=1&fp=954aa051d21e9187"
          :params)

  (print (do-get (user-agent) "http://www.reddit.com/search" :params { "q" "blarg"}))
  (do-get (user-agent) "http://www.reddit.com/search" :params { "q" "blarg"})



)


