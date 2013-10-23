(ns {{sanitized}}.core
  (:use compojure.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]
            [cemerick.friend :as friend]
            (cemerick.friend [workflows :as workflows]
                             [credentials :as creds])
            [dieter.core :refer :all]
            [{{name}}.thymeleaf :refer :all]
            [{{name}}.db :refer :all]
            [ring.util.response :refer :all]
            [ring.util.request :refer :all]
            [clj-time.core]
            [clj-time.coerce]
            [clojure.walk :refer :all]
            [clojure.data.json]
            [clojure.tools.logging :refer [debug info error]]
            [clojure.java.io :refer [reader]]
            [korma.core :refer [defentity insert select values fields where group join has-many belongs-to with dry-run]]
            [korma.db :refer [defdb mysql]]

            [ring.middleware.keyword-params :refer :all]
            [ring.middleware.stacktrace :refer [wrap-stacktrace-web]]
            [ring.middleware.session :refer [wrap-session]]
            [ring.middleware.session.memory :refer [memory-store]]
            [ring.handler.dump :refer [handle-dump]]           

            [ring.middleware.reload :refer :all]
            [ring.middleware.params :refer :all]
            [ring.util.response :refer [response header content-type]]
            [ring.adapter.jetty :refer [run-jetty]]
            [clojure.java.jdbc :as sql]            
            [conf-er :refer [config]]))

(def PORT (config :port))
(def prod-mode (keyword (config :mode)))
(def config-options {:compress (= prod-mode :production) :cache-mode prod-mode})




(defroutes app-routes

  (GET  "/" []
        (let [context (create-context {})] 
          (.process engine "index" context)))

  (GET "/secret" []
       (friend/authorize #{:user}
                         (let [engine (create-engine)
                               link (link-to-asset "app.js.dieter" config-options)
                               context (create-context {"link" link})] 
                           (.process engine "secret" context))))

  (GET "/login" []
       (let [context (create-context {})] 
         (.process engine "login" context)))
  
  (friend/logout (ANY "/logout" request (ring.util.response/redirect "/")))  
  (route/resources "/")  
  (route/not-found "Not Found"))


(defn post-init!
  []
  (migrate!)
  (info "{{name}} started on port" PORT))


(def session-store (atom {}))


(defentity user)


(defn find-user-by-email
  [email]
  (select user
          (fields :email :password :roles)
          (where {:email email})))


(defn get-user [email]
  (info "Searching for email " email)
  (let [user (first (find-user-by-email email))]
    (info "Username [" (:email user) "] password [" (:password user) "], user " user)
    (if user
      {
       :username (:email user)
       :password (:password user)
       :roles #{ (keyword (:roles user)) }
       }
      nil)))


(defn secure-app [routes]
  (info "Creating secured routes")
  (friend/authenticate
    routes
    {:credential-fn (partial creds/bcrypt-credential-fn get-user)
     :workflows [(workflows/interactive-form)]}))


(defn wrap-app [app-routes] 
  (-> app-routes
      (wrap-reload '{{name}}.core)
      (wrap-session {:store (memory-store session-store)})
      (wrap-params)
      (wrap-stacktrace-web)))


(def handler
  "Represents the wrapped set of configured routes and handlers - WITHOUT starting the server or post-init!"
  (let [secured (secure-app app-routes)
        wrapped (wrap-app secured)
        pipe (asset-pipeline wrapped config-options)]        
    (handler/site pipe)))


(defn start-server []
  "Call this from the REPL to setup the DB and start the server"
  (run-jetty handler {:port PORT :join? false})
  (post-init!))


(defn -main [& args]
  (start-server))
