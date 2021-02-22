(ns chulsooboard.core
  (:require ;; for web logics
            [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route]
            [compojure.handler :as handler]
            ;; chulsooboard things
            [chulsooboard.controllers.controller :as controller]
            [chulsooboard.views.layout :as layout]
            [chulsooboard.scrape.scrape :as scrape]
            ;; repl things
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
            [clojure.repl :refer :all])
  (:gen-class))


(defroutes routes
  controller/routes
  (route/resources "/")
  (route/not-found (layout/four-oh-four)))


(def application (handler/site routes))


(defonce server
  (ring/run-jetty application {:port 8080
                               :join? false}))


(defn -main []
  ;; (scrape-upto-number 5380)
  ;; (scrape/scrape-chulsoo-by-number 500)
  (println "chulsooboard starting!"))
