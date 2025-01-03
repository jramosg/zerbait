(ns kit.trukun.env
  (:require
    [clojure.tools.logging :as log]
    [kit.trukun.dev-middleware :refer [wrap-dev]]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[trukun starting using the development or test profile]=-"))
   :start      (fn []
                 (log/info "\n-=[trukun started successfully using the development or test profile]=-"))
   :stop       (fn []
                 (log/info "\n-=[trukun has shut down successfully]=-"))
   :middleware wrap-dev
   :opts       {:profile       :dev
                :persist-data? true}})

(comment 
  defaults)
