(ns chulsooboard.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [java-time :as time]
            [progrock.core :as pr]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.repl :refer :all])
  (:gen-class))


(defn string-singleton? [entry]
  (string? (first entry)))


(defn scrape-chulsoo-by-number [number]
  (let [url (str/join ["http://miniweb.imbc.com/Music/View?seqID=" number "&progCode=RAMFM300"])
        html-doc (html/html-resource (java.net.URL. url))
        ymdt-string (first (:content (nth (html/select sample [:body :div :div :div :div :p]) 0)))
        table (get-table html-doc)]
    [ymdt-string table]))


;; http://miniweb.imbc.com/Music/View?seqID=300&progCode=RAMFM300 가 이상하다


(defn scrape-upto-number [number]
  (loop [cnt 1 acc []]
      (let [scraped (try (scrape-chulsoo-by-number cnt)
                         (catch Exception e nil))] ; In this case, the URL invalid
        (if (= cnt number)
          acc
          (if scraped
            (do
              (println cnt)
              (recur (inc cnt) (conj acc scraped)))
            (do
              (println cnt)
              (recur (inc cnt) acc)))))))


(defn day->seqID
  "Get today's seqID for the URL."
  []
  (let [first-day-of-2021 5390
        now (time/local-date)
        ]))


(defn -main
  [& args]
  (scrape-upto-number 5380))
