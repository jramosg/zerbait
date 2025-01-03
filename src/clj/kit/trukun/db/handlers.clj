(ns kit.trukun.db.handlers 
  (:require
   [clojure.tools.logging :as log]
   [kit.trukun.config :refer [system]]
   [next.jdbc.sql :as sql]
   [next.jdbc :as jbdc]))

(defn query [sql-statement]
  (try
    (let [result (jbdc/execute!
                  (:trukun/datasource @system)
                  sql-statement)]
      {:success? true
       :result (vec result)})
    (catch Exception e
      (log/error "Error occurred while querying table"  )
      {:success? false
       :error (.getMessage e)})))

(defn insert! [table row]
   (try
    {:success? true 
     :result (sql/insert!
              (:trukun/datasource @system)
              table
              row)}
    (catch java.sql.SQLException e
      (log/error "Database error occurred while inserting into" table) 
      {:success? false
       :error (.getMessage e)})))

(comment 
  
  
  (:trukun/datasource @system)

  ;(:trukun/datasource @config)
  ;(next.jbdc/get-datasource (:trukun/datasource @config))
  (insert! 
   :users {:id (random-uuid)
           :email "as"
           :password "a"})
  )