(ns clj-etl-utils.crypto.aes
  (:import [javax.crypto KeyGenerator SecretKey Cipher]
           [javax.crypto.spec SecretKeySpec])
  (:use
   [clj-etl-utils.text       :only [decode-base64 encode-base64]]
   [clj-etl-utils.lang-utils :only [raise]]))



(defn genkey [keygen]
  (do (.init keygen  128)
      (.getEncoded (.generateKey keygen) )))

(defn do-encrypt [rawkey plaintext]
  (let [cipher (Cipher/getInstance "AES")]
    (do (.init cipher Cipher/ENCRYPT_MODE (SecretKeySpec. rawkey "AES"))
        (.doFinal cipher (.getBytes plaintext)))))

(defn do-decrypt [rawkey ciphertext]
  (let [cipher (Cipher/getInstance "AES")]
    (do (.init cipher Cipher/DECRYPT_MODE (SecretKeySpec. rawkey "AES"))
        (String. (.doFinal cipher ciphertext)))))

(defn string->key [s]
  (if (< (count s) 16)
    (raise "Error: key must be >= 16 characters to be a valid AES key, you supplied s[%d]='%s'" (count s) s))
  (.getBytes (.substring s 0 16) "UTF-8"))


(comment "Example usage"

         (def key (string->key "some key with some more padding"))

         (String. (encode-base64 (do-encrypt key "this is some plaintext")))

         (do-decrypt key (do-encrypt key "this is some plaintext"))

         )
