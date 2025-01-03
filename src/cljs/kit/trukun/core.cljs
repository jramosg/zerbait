(ns kit.trukun.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]
   [day8.re-frame.http-fx]
   [ajax.core :as ajax]
   [re-frame.core :as rf]))

;; -------------------------
;; Views

(def state (r/atom {}))
(def login (r/atom {}))

(rf/reg-event-fx
 ::create-user
 (fn [{:keys [db]} [_ user]]
   {:fx [[:http-xhrio {:method :post
                       :uri  "/api/user"
                       :params user
                       :format (ajax/json-request-format)
                       :response-format (ajax/json-response-format {:keywords? true})
                       :on-success [::on-create]
                       :on-failure      [::on-create]}]]}))


(rf/reg-event-db
 ::on-create
 (fn [db [_ result]]
   (assoc db :create-result result)))

(rf/reg-event-fx
 ::login
 (fn [{:keys [db]} [_ user]]
   {:db   (assoc db :show-twirly true)
    :fx [[:http-xhrio {:method          :post
                       :uri             "/api/login"
                       :params user
                       :format (ajax/json-request-format)
                       :response-format (ajax/json-response-format {:keywords? true})
                       :on-success [::on-login]
                       :on-failure [::on-login]}]]}))

(rf/reg-event-db
 ::on-login 
 (fn [db [_ result]]
   (assoc db :login-result result)))

(rf/reg-sub
 ::create-result
 :-> :create-result)

(rf/reg-sub
 ::login-result
 :-> :login-result)

(defn home-page []
  [:<>
   [:input
    {:on-change #(swap! state assoc :email (.. % -target -value))}]
   [:input {:on-change #(swap! state assoc :password (.. % -target -value))
            :type "password"}]
   [:button {:on-click #(rf/dispatch [::create-user @state])} "SORTU"]
   [:br]
   [:br]
   [:div (str @(rf/subscribe [::create-result]))]
   [:br]
   [:br]
   [:input
    {:on-change #(swap! login assoc :email (.. % -target -value))}]
   [:input {:on-change #(swap! login assoc :password (.. % -target -value))
            :type "password"}]
   [:button {:on-click #(rf/dispatch [::login @login])}
    "LOGIN"]
    [:br]
   [:br]
   [:div (str @(rf/subscribe [::login-result]))]])

;; -------------------------
;; Initialize app

(defn ^:dev/after-load mount-root []
  (d/render [home-page] (.getElementById js/document "app")))

(defn ^:export ^:dev/once init! []
  (mount-root))
