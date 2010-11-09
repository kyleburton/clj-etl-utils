(ns clj-etl-utils.sql
  (:use
   [clj-etl-utils.lang-utils :only (raise aprog1)])
  (:require
   [clojure.contrib.duck-streams :as ds]
   [clj-etl-utils.lang-utils :as lang-utils]
   fleet))

(def *dialects* {:postgresql
                 {:templates
                  {:create-table "/clj_etl_utils/sql/dialects/postgresql/create-table.sql.fleet"}}})

(defn create-table-ddl [dialect column-defs table-name & [params]]
  (if-not (get *dialects* dialect)
    (raise "Error: unknown dialect='%s'" dialect))
  (if (empty? column-defs)
    (raise "Error: can't create table with no columns!"))
  (if-not table-name
    (raise "Error: can't create table with no name!"))
  (if-not (get params :owner)
    (raise "Error: can't create table without an owner!"))
  (let [tmpl-res-url      (or (get-in *dialects* [dialect :templates :create-table])
                              (raise "Error: no [fleet] template for dialect:%s for create table!" dialect))
        tmpl-res          (lang-utils/resource-as-string tmpl-res-url)
        create-table-tmpl (fleet/fleet [params] tmpl-res)]
    (str (create-table-tmpl {:table-name table-name
                             :columns    column-defs
                             :owner      (get params :owner)}))))

(comment

  (println
   (create-table-ddl
    :postgresql
    [{:name "field1", :type "character varying", :length 15} {:name "field2", :type "character varying", :length 16} {:name "field3", :type "character varying", :length 11}]
    "foo"
    {:owner "rails"}))

)