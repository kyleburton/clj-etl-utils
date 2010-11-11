(ns clj-etl-utils.sql
  (:use
   [clj-etl-utils.lang-utils :only (raise aprog1)])
  (:require
   clojure.contrib.string
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

  (ds/with-out-writer "/tmp/tables.sql"
    (doseq [[table-name table-def]
            [["campaign_ivr_routing"
              [{:name "campaign_id",      :type "bigint"}
               {:name "ivr_phone_number", :type "character varying", :length 15}
               {:name "dnis_id",          :type "character varying", :length 10}
               {:constraint "campaign_ivr_routing_campaign_id_fk FOREIGN KEY (campaign_id) REFERENCES campaigns (id)"}]]]]
      (println
       (create-table-ddl
        :postgresql
        table-def
        table-name
        {:owner "rails"}))
      (println
       (create-history-table-ddl
        :postgresql
        table-def
        table-name
        {:owner "rails"}))
      (println (apply str (repeat 80 "-"))))

    (doseq [[table-name table-def]
            [["billers"
              [{:name "name"          :type "character varying" :length 255}
               {:name "address1"      :type "character varying" :length 255}
               {:name "address2"      :type "character varying" :length 255}
               {:name "city"          :type "character varying" :length 255}
               {:name "state"         :type "character varying" :length 255}
               {:name "zipcode"       :type "character varying" :length 255}
               {:name "cs_number"     :type "character varying" :length 255}
               {:name "support_email" :type "character varying" :length 255}
               {:name "url"           :type "character varying" :length 255}
               {:name "logo_name"     :type "character varying" :length 255}]]
             ["campaigns"
              [{:name "name"       :type "character varying"    :length 255}
               {:name "biller_id"  :type "integer"}
               {:name "product_id" :type "integer"}]]]]
      (println
       (create-history-table-ddl
        :postgresql
        table-def
        table-name
        {:owner "rails"}))
      (println (apply str (repeat 80 "-")))))



  )