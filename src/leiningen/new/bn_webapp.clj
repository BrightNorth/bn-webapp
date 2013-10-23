(ns leiningen.new.bn-webapp
  (:use [leiningen.new.templates :only [renderer name-to-path ->files]]))

(def render (renderer "bn-webapp"))

(defn bn-webapp
  "Creates a webapp with Thymeleaf rendering, Flyway DB migrations, Korma DB, Dieter asset pipeline and conf-er configuration"
  [^String name]
  (let [data {:name name
              :sanitized (name-to-path name)}]
    (->files data
             "resources/public"
             "resources/assets/js"
             "resources/asset-cache"
             "resources/public/images"
             "src/migrations"
             ["application.conf" (render "application.conf" data)]
             [".gitignore" (render "gitignore" data)]
             ["project.clj" (render "project.clj" data)]
             ["README.md" (render "README.md" data)]
             ["resources/assets/js/helloworld.js" (render "helloworld.js" data)]
             ["resources/assets/js/app.js.dieter" (render "app.js.dieter")]
             ["resources/assets/css/styles.css" (render "styles.css" data)]
             ["resources/public/index.html" (render "index.html" data)]
             ["resources/public/login.html" (render "login.html" data)]
             ["resources/public/secret.html" (render "secret.html" data)]
             ["test/{{sanitized}}/test/core.clj" (render "core_test.clj" data)]
             ["src/log4j.properties" (render "log4j.properties" data)]
             ["src/migrations/V201310221843__create.sql" (render "V201310221843__create.sql" data)]
             ["src/{{sanitized}}/core.clj" (render "core.clj" data)]
             ["src/{{sanitized}}/db.clj" (render "db.clj" data)]
             ["src/{{sanitized}}/thymeleaf.clj" (render "thymeleaf.clj" data)])))
