(ns kit.zerbait.web.middleware.core
  (:require
   [kit.zerbait.env :as env]
   [ring.middleware.defaults :as defaults]
   [ring.middleware.session.cookie :as cookie]))

(defn wrap-base
  [{:keys [metrics site-defaults-config cookie-secret] :as opts}]
  (let [cookie-store (cookie/cookie-store {:key (.getBytes ^String cookie-secret)})]
    (fn [handler]
      (cond-> ((:middleware env/defaults) handler opts)
        true (defaults/wrap-defaults
              (assoc-in site-defaults-config [:session :store] cookie-store))))))

(comment
  (require '[next.jdbc :as jdbc]
           '[clojure.java.jdbc :as clj.jdbc]
           )
  (def db-spec (:db-spec env/defaults))
  (def datasource (jdbc/get-datasource (:db-spec env/defaults)))
   (jdbc/execute! datasource ["SELECT * FROM guestbook"]) 

  (defn insert-guestbook-entry [name message]
  (clj.jdbc/with-db-transaction [t-conn db-spec]
    (clj.jdbc/insert! t-conn :guestbook {:name name 
                                     :message message})))
(insert-guestbook-entry "John Doe" "is This a test message!")

  env/defaults
)

