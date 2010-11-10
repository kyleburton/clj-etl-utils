(ns clj-etl-utils.collections)


(defn fill-map-defaults [m defaults]
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
  (fill-map-defaults {} [])
  (fill-map-defaults {:foo 1}                 [[:created-at :foo]])
  (fill-map-defaults {:foo 1 :created-at 3}   [[:created-at :foo]])
  (fill-map-defaults {:foo 1 :created-at nil} [[:created-at :foo]])

)

