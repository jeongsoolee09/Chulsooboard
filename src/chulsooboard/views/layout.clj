(ns chulsooboard.views.layout
  (:require [hiccup.core :refer [html]]
            [hiccup.page :refer [html5 include-css]]))


(defn common
  "Parameterized common layout to be reused again and again!"
  [title & body]
  (html5
   [:head
    [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge, chrome=1"}]
    [:meta {:name "viewport" :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title title]
    ;; TODO I don't have a CSS yet
    ;; (include-css "stylesheets/base.cass"
    ;;              "/stylesheets/skeleton.css"
    ;;              "/stylesheets/screen.css")
    ;; (include-css "http://fonts.googleapis.com/css?family=Sigmar+One&v1")
    ]
   [:body
    [:div {:id "header"}
     [:h1 {:class "container"} "Chulsooboard!!"]]
    [:div {:id "content" :class "container"} body]]))


(defn four-oh-four []
  (common "Page Not Found"
          [:div {:id "four-oh-four"}
           "The page you requested could not be found"]))
