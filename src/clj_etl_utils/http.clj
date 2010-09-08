(ns clj-etl-utils.http
  (:use [clj-etl-utils.lang :only [raise assert-allowed-keys! rest-params->map]])
  (:import
   [org.apache.commons.httpclient Credentials Header HttpClient UsernamePasswordCredentials NameValuePair]
   [org.apache.commons.httpclient.auth AuthScope]
   [org.apache.commons.httpclient.methods InputStreamRequestEntity PostMethod GetMethod]
   [org.apache.commons.lang StringUtils]
   [java.io ByteArrayInputStream]))


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
;;         name-value-pairs  (map->name-value-pair-vec (second params))]
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

(defn map->name-value-pair-vec [params]
  (into-array NameValuePair
   (reduce
    (fn [accum key]
      (cons (NameValuePair. (str key) (str (params key))) accum))
    []
    (keys params))))

;;; (map->name-value-pair-vec { :foo "bar" :chicken "turkey" })
;;; (map->name-value-pair-vec {})


(defn do-get [#^HttpClient ua #^String url & params]
  (let [get-method        (GetMethod. url)
        name-value-pairs  (map->name-value-pair-vec (second params))]
    (.setQueryString get-method name-value-pairs)
    (printf "do-get: qs=%s" (.getQueryString get-method))
    (let [return-code   (.executeMethod (:ua ua) get-method )
          response-body (.getResponseBodyAsString get-method)]
      (println (format "Return Code: %s" return-code))
      (println (format "Respone: %s" response-body ))
      {:return-code return-code
       :response-body response-body
       :http-method get-method})))


(defn do-post [#^HttpClient ua #^String url & params]
  (let [post-method (PostMethod. url)
        params      (rest-params->map params)]
    (if (:params params)
      (.setRequestBody post-method (map->name-value-pair-vec (:params params))))
    (if (:body params)
      (do
        (.setRequestHeader post-method "Content-type" (or (:content-type params) "text/plain; charset=ISO-8859-1"))
        (.setRequestEntity
         post-method
         (InputStreamRequestEntity.
          (ByteArrayInputStream. (.getBytes (:body params)))
          (long (.length (:body params)))))))
    (let [return-code   (.executeMethod (:ua ua) post-method)
          response-body (.getResponseBodyAsString post-method)]
      (println (format "Return Code: %s" return-code))
      (println (format "Respone: %s" response-body ))
      {:return-code return-code
       :response-body response-body
       :http-method post-method})))


;;   (do-post (user-agent) "http://localhost:10001" :body "some stuff!")

;;; (defn do-get [user-agent ])

(comment

  @*foo*

  (do-get (user-agent) "http://www.google.com" :params {})
  (do-post (user-agent) "http://localhost:10001" :params { :secret 44 :secret2 49} )

  (do-post (user-agent) "http://localhost:10001" :body "some stuff!")


  ;; http://www.google.com/#hl=en&source=hp&q=yatzee!&aq=f&aqi=g2g-s1g4g-s2g1&aql=&oq=&gs_rfai=CpL9EVhGBTLGjHpaCywTGhYH6BQAAAKoEBU_QNS5J&pbx=1&fp=954aa051d21e9187

  ;; http://www.google.com/#
  (do-get (user-agent) "http://www.google.com/#hl=en&source=hp&q=yatzee!&aq=f&aqi=g2g-s1g4g-s2g1&aql=&oq=&gs_rfai=CpL9EVhGBTLGjHpaCywTGhYH6BQAAAKoEBU_QNS5J&pbx=1&fp=954aa051d21e9187"
          :params)

  (print (do-get (user-agent) "http://www.reddit.com/search" :params { "q" "blarg"}))
  (do-get (user-agent) "http://www.reddit.com/search" :params { "q" "blarg"})



  )


