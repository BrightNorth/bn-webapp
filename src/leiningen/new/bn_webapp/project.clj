(defproject {{name}} "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[ch.qos.logback/logback-core "1.1.3"]
                 [ch.qos.logback/logback-classic "1.1.3"]
                 [org.clojure/clojure "1.5.1"]
                 [ring/ring-core "1.2.0"]
                 [com.cemerick/friend "0.2.0" :exclusions [ring/ring-core]]
                 [compojure "1.1.5" :exclusions [ring/ring-core]]
                 [ring/ring-jetty-adapter "1.2.0"]
                 [dieter "0.4.1" :exclusions [ring/ring-core]]
                 [org.clojure/data.json "0.1.1"]
                 [org.thymeleaf/thymeleaf "2.0.19"]
                 [clj-time "0.5.1"]
                 [conf-er "1.0.1"]
                 [org.slf4j/slf4j-api "1.7.12"]
                 [mysql/mysql-connector-java "5.1.6"]
                 [korma "0.3.0-RC5"]
                 [com.googlecode.flyway/flyway-core "2.2"]
                 [org.clojure/tools.logging "0.3.1"]]
  :ring {:handler {{name}}.core/handler
         :init {{name}}.core/post-init!
         :port 4000}
  :jvm-opts ["-Dconfig=application.conf"]
  :main {{name}}.core
  :plugins [[lein-ring "0.8.5"] [lein-midje "3.1.1"]]
  :aot [{{name}}.core]
  :profiles {
             :dev {
                   :dependencies [[ring-mock "0.1.5"]
                                  [midje "1.5.1"]
                                  [ring/ring-devel "1.2.0"]]
                   }
             }
  :repositories [["snapshots" "http://nexus.brightnorth.co.uk/content/repositories/snapshots"]
                 ["releases" "http://nexus.brightnorth.co.uk/content/repositories/releases"]]
  )
