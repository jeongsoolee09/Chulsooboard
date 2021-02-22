;;; namespace for scraping

(ns chulsooboard.scrape.scrape
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [java-time :as time]
            [progrock.core :as pr]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
            [clojure.repl :refer :all])
  (:gen-class))


(defn string-singleton? [entry]
  (and (= (count entry) 1)
       (string? (first entry))))


(defn convert-HTML-special-characters [string]
  (str/escape string {"&lt;" \<
                      "&gt;" \>
                      "&amp;" \&}))

(defn escape-quotes
  [string]
  (str/escape string {\" "\\\""
                      \' "''"}))


(defn normalize-string
  "strip away the extra trailing whitespace,
   and deal with HTML special characters"
  [string]
  (-> string
      (str/trim)
      (convert-HTML-special-characters)
      (escape-quotes)))



(defn refine-map [filtered-table-map]
  (let [type_ (:class (:attrs filtered-table-map))
        content (->> filtered-table-map
                     (:content)
                     (map normalize-string))]
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
                (conj acc (merge singer-map title-map)))) [] zipped)))


(defn parse-only-integer
  "\"2007년\" |-> 2007"
  [string-with-numbers]
  (if-let [matches (take-while
                    #(re-matches #"[0-9]+" (str %))
                    string-with-numbers)]
    (Integer/parseInt (apply str matches))))


(defn ymd-list-to-hashmap
  "(2007 8 27) |->
    {:year 2007 :month 8 :day 27}"
  [ymd-list]
  (let [labels '(:year :month :day)
        zipped (map list labels ymd-list)]
    (apply hash-map (flatten zipped))))


(defn parse-date-string
  "Parse the date string,
   e.g. 2007년 8월 27일 월요일 |->
        {:year 2007 :month 8 :day 27}"
  [date-string]
  (->> date-string
       (#(str/split % #" "))
       (take 3)
       (map parse-only-integer)
       (ymd-list-to-hashmap)))


(defn scrape-by-number [number]
  (let [url (str/join
             ["http://miniweb.imbc.com/Music/View?seqID="
              number
              "&progCode=RAMFM300"])
        html-doc (html/html-resource (java.net.URL. url))
        ymdt-string (-> (html/select html-doc
                                     [:body :div :div :div :div :p])
                        (nth 0)
                        (:content)
                        (first))
        table (get-table html-doc)
        ymdt-map (parse-date-string ymdt-string)]
    (map #(merge % ymdt-map) table)))


(defn scrape-upto-number [number]
  (loop [cnt number acc '()]
    (if (< cnt 0)
      acc
      (let [scraped (try (scrape-by-number cnt)
                         (catch Exception e nil))] ; Maybe the URL is invalid
        (if scraped
          (do
            (println cnt)
            (recur (dec cnt) (concat scraped acc)))
          (recur (dec cnt) acc))))))


(scrape-by-number 5389)
