;;; namepsace for working with Postgres

(ns chulsooboard.db
  (:require [next.jdbc :as jdbc]
            [chulsooboard.scrape :as scrape]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
            [clojure.repl :refer :all])
  (:gen-class))


(def chulsooboard {:dbtype "postgres" :dbname "chulsooboard"})


(def datasource (jdbc/get-datasource chulsooboard))


(defn insert-song [datasource year month day singer title]
  (let [insert-command (str "insert into songboard"
                            " (year, month, day, singer, title) "
                            "values ("
                            year ", "
                            month ", "
                            day ", "
                            "'" singer "'" ", "
                            "'" title "'" ")")]
    (jdbc/execute! datasource [insert-command])))


(defn batch-insert-song [datasource record-vector]
  (loop [remaining record-vector]
    (if (= remaining [])
      datasource
      (let [record (first remaining)
            year (:year record)
            month (:month record)
            day (:day record)
            singer (:singer record)
            title (:title record)]
        (insert-song datasource
                     year month day singer title)
        (recur (rest remaining))))))


;; chulsoo never plays the same song twice
(defn create-songboard-if-needed []
  (jdbc/execute! datasource
                 ["create table if not exists songboard
                   (year int,
                    month int,
                    day int,
                    singer varchar(255),
                    title varchar(255))"]))


(defn save-all-songs []
  (let [first-of-2021 5390
        scraped (scrape/scrape-upto-number first-of-2021)]
    (create-songboard-if-needed)
    (println "inserting into db...")
    (batch-insert-song datasource scraped)
    (println "inserting into db...done")))


(save-all-songs)
