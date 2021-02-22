(ns chulsooboard.controllers.controller
  (:require [compojure.core :refer [defroutes GET POST]]
            [clojure.string :as str]
            [ring.util.response :as ring]
            [chulsooboard.views.table :as table]
            [chulsooboard.models.db :as db]))

;; Oops gotta come back after I'm done with views


(defn index []
  (table/index (db/find-by-title "Hey Jude")))


(defroutes routes
  (GET "/" [] (index))
  ;; (POST "/" [shout] (create shout))
  )

