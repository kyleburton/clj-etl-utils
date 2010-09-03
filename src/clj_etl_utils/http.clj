(ns clj-etl-utils.http
  (:use [clj-etl-utils.lang :only [rest-params->map assert-allowed-keys!]])
  (:import
   [org.apache.commons.httpclient Credentials Header HttpClient UsernamePasswordCredentials]
   [org.apache.commons.httpclient.auth AuthScope]
   [org.apache.commons.httpclient.methods InputStreamRequestEntity PostMethod]
   [org.apache.commons.lang StringUtils]))

(defn user-agent [& params]
  (let [params (rest-params->map params)
        ua (HttpClient.)]
    (assert-allowed-keys! params [:follow-redirects :basic-auth])
    (if (:follow-redirects params)
      (.setAuthenticationPreemptive (.getParams ua) (:follow-redirects params)))
    (if-let [auth-info (:basic-auth params)]
      (do
        (assert-allowed-keys! auth-info [:user :pass :auth-host :auth-port :auth-realm])
        (.setCredentials
         (.getState ua)
         (AuthScope.
          (:auth-host  params AuthScope/ANY_HOST)
          (:auth-port  params AuthScope/ANY_PORT)
          (:auth-realm params AuthScope/ANY_REALM))
         (UsernamePasswordCredentials. (:user auth-info) (:pass auth-info)))))
    ua))

;; (user-agent)
;; (user-agent :follow-redirects false)
;; (user-agent :basic-auth {:user "bob" :pass "0xB0B"})

(defn http-method-dispatcher [& args]
  (cond
    (and (isa? (class (first args))
               HttpClient)
         (isa? (class (second args))
               java.net.URL))
    :ua-url-and-params

    (and (isa? (class (first args))
               HttpClient)
         (isa? (class (second args))
               java.net.URI))
    :ua-uri-and-params

    (and (isa? (class (first args))
               HttpClient)
         (isa? (class (second args))
               String))
    :ua-url-str-and-params

    (and (isa? (class (first args))
               java.net.URL))
    :url-and-params

    (and (isa? (class (first args))
               java.net.URI))
    :uri-and-params

    (and (isa? (class (first args))
               String))
    :url-str-and-params

    :else       :default))

(defmulti do-get http-method-dispatcher)

(defmulti do-post http-method-dispatcher)

(defmethod do-get :ua-url-and-params [ua url & params]
  (prn "do-get :ua-url-and-params"))

(defmethod do-get :ua-uri-and-params [ua uri & params]
  (prn "do-get :ua-uri-and-params"))

(defmethod do-get :ua-url-str-and-params [ua uri & params]
  (prn "do-get :ua-url-and-params")
  (apply do-get ua (java.net.URI. uri) params))

(defmethod do-get :url-and-params [url & params]
  (prn "do-get :url-and-params")
  (apply do-get (user-agent) url))

(defmethod do-get :uri-and-params [& args]
  (prn "do-get :uri-and-params")
  (applly do-get (user-agent) args))

(defmethod do-get :url-str-and-params [& args]
  (prn "do-get :url-str-and-params")
  (applly do-get (user-agent) args))

(defmethod do-get :default [& params]
  (prn "do-get :default"))









