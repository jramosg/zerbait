(ns kit.trukun.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[trukun starting]=-"))
   :start      (fn []
                 (log/info "\n-=[trukun started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[trukun has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
