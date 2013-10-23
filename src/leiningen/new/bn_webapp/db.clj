(ns {{name}}.db
  (:require [clojure.java.jdbc :as jdbc]
            [clojure.tools.logging :refer [debug info error]]
            [clj-time.format]
            [korma.core :refer :all]
            [korma.db :refer :all]
            [clj-time.core]
            [conf-er :refer [config]])
  (:import com.googlecode.flyway.core.Flyway
           com.googlecode.flyway.core.exception.FlywayException
           java.sql.Date))  


(def db-name (config :database :name))
(def db-user (config :database :user))
(def db-pass (config :database :password))
(def db-host (config :database :host))
(def db-port (config :database :port))

(def db-prefix "jdbc:mysql://")
(def db-url (str db-prefix db-host ":" db-port "/" db-name))

(defdb db (mysql {:db db-name
                       :user db-user
                       :password db-pass
                       :port db-port
                       :host db-host}))

(defn migrate! []
  (let [flyway (Flyway.)]
    (.setLocations flyway (into-array ["migrations"]))
    (.setDataSource flyway db-url db-user db-pass)
    (try 
      (.init flyway)
      (catch FlywayException e
        (info "Caught exception init db" (.getMessage e))))
    (.migrate flyway)))
