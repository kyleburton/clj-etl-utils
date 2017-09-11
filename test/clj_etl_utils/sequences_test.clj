(ns clj-etl-utils.sequences-test
  (:require
   [clj-etl-utils.sequences :as sequences])
  (:use
   [clojure.test]))


(deftest test-make-stream-sampler
  (let [sampler (sequences/make-stream-sampler (fn [_] 0))]
    (is (= [:a] (sampler [:a :b :c :d] 1 1))))
  (let [rand-int-fn (fn [_] 0)
        sampler     (sequences/make-stream-sampler rand-int-fn)
        population   [:a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z]]
    (is (= [:a :b :c :d :e]) (sampler population (count population) 5)))
  (let [data (atom 0)
        rand-int-fn (fn [_] (if (zero? @data)
                              (do
                                (reset! data 1)
                                0)
                              (do
                                (reset! data 0)
                                9999)))
        sampler     (sequences/make-stream-sampler rand-int-fn)
        population   [:a :b :c :d :e :f :g :h :i :j :k :l :m :n :o :p :q :r :s :t :u :v :w :x :y :z]]
    (is (= [:a :c :e :g :i]) (sampler population (count population) 5))))


(deftest test-make-resevior-sampler
  (is (= [] ((sequences/make-resevior-sampler 6) [])))
  (is (= [1] ((sequences/make-resevior-sampler 6) [1])))
  (is (= [0 1 2 3 4 5] ((sequences/make-resevior-sampler 6) [0 1 2 3 4 5])))
  (let [sampler (sequences/make-resevior-sampler 2 (fn [_] 0))]
    (is (= [:d :b] (sampler [:a :b :c :d])))))
