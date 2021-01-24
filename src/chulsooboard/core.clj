(ns chulsooboard.core
  (:require [chulsooboard.scrape :as scrape]
            [clojure.tools.namespace.repl :refer [refresh]]
            [clojure.tools.trace :as trace]
            [clojure.repl :refer :all])
  (:gen-class))


(defn -main
  [& args]
  ;; (scrape-upto-number 5380)
  (scrape/scrape-chulsoo-by-number 500))
