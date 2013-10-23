(ns {{name}}.thymeleaf
  (:import (java.util Date)
           (org.thymeleaf TemplateEngine)
           (org.thymeleaf.context Context)
           (org.thymeleaf.resourceresolver FileResourceResolver)
           (org.thymeleaf.templateresolver TemplateResolver)))
  
;; ThymeLeaf integration
(defn create-engine []
  (let [tr (TemplateResolver.)]
    (.setResourceResolver tr (FileResourceResolver.))
    (.setTemplateMode tr "XHTML")
    (.setPrefix tr "resources/public/")
    (.setSuffix tr ".html")
    (let [engine (TemplateEngine.)]
      (.setTemplateResolver engine tr)
      engine)))

(def engine (create-engine))

(defn create-context [m]
  (reduce (fn [c [k v]] (.setVariable c k v) c) (Context.) m))
