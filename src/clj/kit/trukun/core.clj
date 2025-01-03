(ns kit.trukun.core
  (:require
   [clojure.tools.logging :as log]
   [integrant.core :as ig]
   [kit.trukun.config :as config :refer [system]]
   [kit.trukun.env :refer [defaults]]

    ;; Edges
   [kit.edge.server.undertow]
   [kit.trukun.web.handler]

    ;; Routes
   [kit.trukun.web.routes.api]

   [kit.trukun.web.routes.pages]
   [kit.edge.db.sql.conman]
   [kit.edge.db.sql.migratus]
   [kit.edge.db.postgres]
   [kit.trukun.db.handlers])
  (:gen-class))

;; log uncaught exceptions in threads
(Thread/setDefaultUncaughtExceptionHandler
 (fn [thread ex]
   (log/error {:what :uncaught-exception
               :exception ex
               :where (str "Uncaught exception on" (.getName thread))})))

(defn stop-app []
  ((or (:stop defaults) (fn [])))
  (some-> (deref system) (ig/halt!)))

(defn start-app [& [params]] 
  ((or (:start params) (:start defaults) (fn [])))
  (->> (config/system-config (or (:opts params) (:opts defaults) {}))
       (ig/expand)
       (ig/init)
       (reset! system)))

(defn -main [& _]
  (start-app)
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
  (.addShutdownHook (Runtime/getRuntime) (Thread. shutdown-agents)))

(comment
  @system)