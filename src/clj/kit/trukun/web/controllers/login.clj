(ns kit.trukun.web.controllers.login
  (:require
   [buddy.hashers :as hashers]
   [kit.trukun.db.handlers :as db]
   [ring.util.http-response :as http-response]))

(defn login [{:keys [body-params]}]
  (let [password (-> (db/query [ "SELECT password FROM users WHERE email=?" (:email body-params)])
                    (get-in [:result 0 :password]))]
    (cond
      (not password)
      (http-response/not-found {:reason "User not found"})
      ;;
      (not (:valid (hashers/verify (:password body-params) password)))
      (http-response/unauthorized {:reason "Incorrect password"})
      ;;
      :else (http-response/ok {:success? true}))))