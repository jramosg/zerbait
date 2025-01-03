(ns kit.trukun.web.routes.api
  (:require
   [integrant.core :as ig]
   [kit.trukun.web.controllers.create-user :as create-user]
   [kit.trukun.web.controllers.health :as health]
   [kit.trukun.web.controllers.login :as login]
   [kit.trukun.web.middleware.exception :as exception]
   [kit.trukun.web.middleware.formats :as formats]
   [reitit.coercion.malli :as malli]
   [reitit.ring.coercion :as coercion]
   [reitit.ring.middleware.muuntaja :as muuntaja]
   [reitit.ring.middleware.parameters :as parameters]
   [reitit.swagger :as swagger]))

(def email-spec
  [:string {:min 5 :max 100 :regex #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$"}])

(def password-spec
  [:string {:min 8 :max 100}])

(def create-user-spec
  [:map
   [:email email-spec]
   [:password password-spec]])

(def login-spec
  [:map
   [:email email-spec]
   [:password password-spec]])

(def route-data
  {:coercion   malli/coercion
   :muuntaja   formats/instance
   :swagger    {:id ::api}
   :middleware [;; query-params & form-params
                parameters/parameters-middleware
                  ;; content-negotiation
                muuntaja/format-negotiate-middleware
                  ;; encoding response body
                muuntaja/format-response-middleware
                  ;; exception handling
                coercion/coerce-exceptions-middleware
                  ;; decoding request body
                muuntaja/format-request-middleware
                  ;; coercing response bodys
                coercion/coerce-response-middleware
                  ;; coercing request parameters
                coercion/coerce-request-middleware
                  ;; exception handling
                exception/wrap-exception]})

;; Routes
(defn api-routes [_opts]
  [["/swagger.json"
    {:get {:no-doc  true
           :swagger {:info {:title "kit.trukun API"}}
           :handler (swagger/create-swagger-handler)}}]
   ["/health"
    {:get health/healthcheck!}]
   ["/user" {:post {:handler create-user/create-user
                    :parameters {:body [:map
                                        [:email string?]
                                        [:password string?]]}}}]
    ["/login"
    {:post {:handler login/login
            :parameters {:body [:map
                                [:email string?]
                                [:password string?]]}}}]])

(derive :reitit.routes/api :reitit/routes)

(defmethod ig/init-key :reitit.routes/api
  [_ {:keys [base-path]
      :or   {base-path ""}
      :as   opts}]
  (fn [] [base-path route-data (api-routes opts)]))
