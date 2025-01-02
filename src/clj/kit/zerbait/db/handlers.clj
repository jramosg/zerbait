(ns kit.zerbait.db.handlers 
  (:require
   [clojure.java.jdbc :as jbdc]
   [clojure.tools.logging :as log]
   [kit.zerbait.env :as env]))

(defn query [sql-statement]
  (try
    (let [result (jbdc/query 
                  (:db-spec env/defaults)
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
     :result (jbdc/insert!
              (:db-spec env/defaults)
              table
              row)}
    (catch java.sql.SQLException e
      (log/error "Database error occurred while inserting into" table) 
      {:success? false
       :error (.getMessage e)})))