(ns http-test
  (:require [clj-etl-utils.http :as ua])
  (:import
      [org.apache.commons.httpclient HttpClient])
  (:use [clojure.test]
        [clj-etl-utils.test-helper]))

;; (deftest test-main-dispatcher
;;   (is (= :ua-url-and-params
;;          (ua/http-method-dispatcher
;;           (HttpClient.)
;;           (java.net.URL. "http://localhost:8080/"))))
;;   (is (= :ua-uri-and-params
;;          (ua/http-method-dispatcher
;;           (HttpClient.)
;;           (java.net.URI. "http://localhost:8080/"))))
;;   (is (= :ua-url-str-and-params
;;          (ua/http-method-dispatcher
;;           (HttpClient.)
;;           "http://localhost:8080/")))
;;   (is (= :url-and-params
;;          (ua/http-method-dispatcher
;;           (java.net.URL. "http://localhost:8080/"))))
;;   (is (= :uri-and-params
;;          (ua/http-method-dispatcher
;;           (java.net.URI. "http://localhost:8080/"))))
;;   (is (= :url-str-and-params
;;          (ua/http-method-dispatcher
;;           "http://localhost:8080/")))
;;   (is (= :default
;;          (ua/http-method-dispatcher))))

;; ;; ;; (test-main-dispatcher)















