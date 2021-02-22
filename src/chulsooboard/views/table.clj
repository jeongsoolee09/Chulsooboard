(ns chulsooboard.views.table
  (:require [chulsooboard.views.layout :as layout]
            [hiccup.core :refer [h]]
            [hiccup.form :as form]))


(defn search-form []
  [:div {:id "search-form" :class "sixteen columns alpha omega"}
   (form/form-to [:post "/"]
                 (form/label "shout" "What do you want to SHOUT?")
                 (form/text-area "검색하고 싶은 곡명을 검색해 보세요")
                 (form/submit-button "검색!"))])

(defn display-table [shouts]
  ;; TODO
  )


(defn index [shouts]
  (layout/common "SHOUTER"
                 (search-form)
                 [:div {:class "clear"}]
                 (display-table shouts)))
