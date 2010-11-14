(ns
    ^{:doc "Collections extensions."
      :author "Kyle Burton"}
  clj-etl-utils.collections)


(defn
  ^{:doc "Given a map and a vector of key/default value pairs, will apply the default if the key is present but the value is nil in the original map.

  (fill-map-defaults {} [])
    => {}
  (fill-map-defaults {:foo 1}                 [[:created-at :foo]])
    => {:foo 1}
  (fill-map-defaults {:foo 1 :created-at 3}   [[:created-at :foo]])
    => {:foo 1, :created-at 3}
  (fill-map-defaults {:foo 1 :created-at nil} [[:created-at :foo]])
    => {:foo 1, :created-at :foo}

"
    :added "1.0.0"}
  fill-map-defaults [m defaults]
  (loop [[[k v] & kvs] defaults
         m m]
    (if-not k
      m
      (recur kvs
             (if (contains? m k)
               (assoc m k (if (nil? (get m k))
                              v
                              (get m k)))
               m)))))

(comment

)

