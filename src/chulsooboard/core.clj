(ns chulsooboard.core
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [java-time :as time]
            [progrock.core :as pr]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
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


(defn refine-map [filtered-table-map]
  (let [type_ (:class (:attrs filtered-table-map))
        content (:content filtered-table-map)]
    (if (= type_ "singer")
      {:singer (first content)}
      {:title (first content)})))


(defn get-table [html-doc]
  (let [raw (->> html-doc
                 (#(html/select % [:tr :td]))
                 (map :content)
                 (filter (comp not string-singleton?))
                 (filter (comp not nil?))
                 (map first)
                 (map refine-map))
        singers (take-nth 2 raw)
        titles (take-nth 2 (rest raw))
        zipped (map list singers titles)]
    (reduce (fn [acc elem]
              (let [[singer-map title-map] elem]
                (conj acc (merge singer-map title-map)))) zipped)))


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
