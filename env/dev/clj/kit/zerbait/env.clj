(ns kit.zerbait.env
  (:require
    [clojure.tools.logging :as log]
    [kit.zerbait.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[zerbait starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[zerbait started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[zerbait has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}
  :db-spec {:dbtype "postgresql"
            :host "localhost"
            :port 5432
            :dbname "zerbait_dev"
            :user "zerbait"
            :password "zerbait"} })
