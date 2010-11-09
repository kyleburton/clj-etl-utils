(ns clj-etl-utils.sql
  (:use
   [clj-etl-utils.lang-utils :only (raise aprog1)])
  (:require
   [clojure.contrib.duck-streams :as ds]
   [clj-etl-utils.lang-utils :as lang-utils]
   fleet))

(def *dialects* {:postgresql
                 {:templates
                  {:create-table         "/clj_etl_utils/sql/dialects/postgresql/create-table.sql.fleet"
                   :create-history-table "/clj_etl_utils/sql/dialects/postgresql/create-history-table.sql.fleet"}}})

(defn render-create-ddl [dialect template column-defs table-name & [params]]
  (if-not (get *dialects* dialect)
    (raise "Error: unknown dialect='%s'" dialect))
  (if (empty? column-defs)
    (raise "Error: can't %s with no columns!" template))
  (if-not table-name
    (raise "Error: can't %s with no name!" template))
  (if-not (get params :owner)
    (raise "Error: can't %s without an owner!" template))
  (let [tmpl-res-url      (or (get-in *dialects* [dialect :templates template])
                              (raise "Error: no [fleet] template for dialect:%s for %s!" dialect template))
        tmpl-res          (lang-utils/resource-as-string tmpl-res-url)
        create-table-tmpl (fleet/fleet [params] tmpl-res)]
    (str (create-table-tmpl {:table-name table-name
                             :columns    column-defs
                             :owner      (get params :owner)}))))

(defn create-table-ddl [dialect column-defs table-name & [params]]
  (render-create-ddl dialect :create-table column-defs table-name params))

(defn create-history-table-ddl [dialect column-defs table-name & [params]]
  (render-create-ddl dialect :create-history-table column-defs table-name params))

(comment

  (println
   (create-table-ddl
    :postgresql
    [{:name "name",      :type "character varying", :length 15}
     {:name "client_id", :type "character varying", :length 16}]
    "campaigns"
    {:owner "rails"}))

  (println
   (create-history-table-ddl
    :postgresql
    [{:name "name",      :type "character varying", :length 15}
     {:name "client_id", :type "character varying", :length 16}]
    "campaigns"
    {:owner "rails"}))


)