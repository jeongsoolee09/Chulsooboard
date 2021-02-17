(ns chulsooboard.core
  (:require [chulsooboard.scrape :as scrape]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
            [clojure.repl :refer :all]
            [compojure.core :refer [defroutes GET]]
            [ring.adapter.jetty :as ring]
            [compojure.route :as route])
  (:gen-class))


(defroutes routes
  (GET "/" [] "<h2>Hello World</h2>"))


(defn -main
  [& args]
  ;; (scrape-upto-number 5380)
  ;; (scrape/scrape-chulsoo-by-number 500)
  (ring/run-jetty #'routes {:port 8080 :join? false}))
