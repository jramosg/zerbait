(ns kit.zerbait.env
  (:require [clojure.tools.logging :as log]))

(def defaults
  {:init       (fn []
                 (log/info "\n-=[zerbait starting]=-"))
   :start      (fn []
                 (log/info "\n-=[zerbait started successfully]=-"))
   :stop       (fn []
                 (log/info "\n-=[zerbait has shut down successfully]=-"))
   :middleware (fn [handler _] handler)
   :opts       {:profile :prod}})
