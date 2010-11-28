(ns
    ^{:doc "Wrapper around Jakarta's HTTP Client."
      :author "Kyle Burton"}
  clj-etl-utils.http
  (:use [clj-etl-utils.lang-utils :only [raise assert-allowed-keys! rest-params->map]])
  (:import
   [org.apache.commons.httpclient Credentials Header HttpClient UsernamePasswordCredentials NameValuePair]
   [org.apache.commons.httpclient.auth AuthScope]
   [org.apache.commons.httpclient.methods InputStreamRequestEntity PostMethod GetMethod]
   [org.apache.commons.lang StringUtils]
   [java.io ByteArrayInputStream]))

;; TODO: support follow redirects in the various get/post methods
;; TODO: this needs to be carried as state somehow, and applied to
;; the HttpMethod's we create and invoke...
;; (if (:follow-redirects params)
;;   (.setAuthenticationPreemptive (.getParams ua) (:follow-redirects params)))

;; TODO: :follow-redirects should default to true (as this is most
;; frequently the desired behavior of a user agent).
;; TODO: support specification of host/port/realm in the basic-auth structure
;  TODO: support either a single map for basic auth, or a sequence of maps
(defn ^{:doc "Construct a new user agent (HttpClient).

   :follow-redirects [optional] true/false defaults to true

   :basic-auth [optional] a map of:
     :auth-host  the host for authentication, defaults to AuthScope/ANY_HOST
     :auth-port  the port for authentication, defaults to AuthScope/ANY_PORT
     :auth-realm the authentication realm for authentication, defaults to AuthScope/ANY_REALM
     :user [required] the username
     :pass [required] the password
"
        :added "1.0.0"}
  user-agent [& params]
  (let [params (rest-params->map params)
        ua (HttpClient.)]
    (assert-allowed-keys! params [:follow-redirects :basic-auth])
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

;; TODO: this helper fn belongs in lang.clj
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
    ;;(printf "do-get: qs=%s" (.getQueryString get-method))
    (let [return-code   (.executeMethod (:ua ua) get-method )
          response-body (.getResponseBodyAsString get-method)]
      ;;(println (format "Return Code: %s" return-code))
      ;;(println (format "Respone: %s" response-body ))
      {:return-code   return-code
       :response-body response-body
       :http-method   get-method})))

;; If a :body is passed, it will be the body of the post, with a
;; default content type of "text/plain; charset=ISO-8859-1".  If
;; :params is passed (as a map) then it will be assigned as a map of
;; form parameters and used as
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
      ;;(println (format "Return Code: %s" return-code))
      ;;(println (format "Respone: %s" response-body ))
      {:return-code   return-code
       :response-body response-body
       :http-method   post-method})))


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


