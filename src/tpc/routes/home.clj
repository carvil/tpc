(ns tpc.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.form :refer :all]
            [tpc.views.layout :as layout]
            [tpc.api :as api]))

(defn amount-to-list [kv]
  (vector [:dt (first kv)] [:dd (second kv)]))

(defn amounts-to-list [amounts]
  (map amounts-to-list amounts))

(defn empty-response []
  [:section { :class "empty"}
   [:p {:class "empty-title"} "No file selected"]
   [:p {:class "empty-meta"} "Upload a TPC bill to see the results"]])

(defn display-amounts [amounts]
  (if (empty? amounts)
    (empty-response)
    (let [list-items (apply concat (map #(vector [:dt (first %)] [:dd (second %)]) amounts))
          dl [:dl list-items]]
      [:section { :class "empty"}
        [:p {:class "empty-title"} "Breakdown of amounts"]
        [:p {:class "empty-meta"} dl]])))

(defn form-component []
  (form-to
    {:enctype "multipart/form-data"}
    [:post "/"]
    (file-upload :file)
    [:input {:type "submit" :text "Upload" :class "btn btn-primary"}]))

(defn form-container []
  [:div {:class "container"}
       [:div {:class "columns"}
         [:div {:class "column col-4"} ""]
         [:div {:class "column col-4"}
           [:h2 "The Porto Concierge"]
           [:hr]
           (form-component)
          ]
         [:div {:class "column col-4"} ""]]])

(defn response-container [amounts]
  [:div {:class "container"}
       [:div {:class "columns"}
         [:div {:class "column col-4"} ""]
         [:div {:class "column col-4"} (display-amounts amounts)]
         [:div {:class "column col-4"} ""]]])

(defn home [& [amounts]]
  (layout/common
    (form-container)
    (response-container amounts)))

(defn temp-file-path [params]
  (->
    params
    (:file)
    (:tempfile)
    (.getPath)))

(defn parse-pdf [params]
  (let [file-path (temp-file-path params)
        rows (api/find-rows file-path)
        amounts {"TPC cost" (api/tpc-cost rows)
                 "Booking.com cost" (api/booking-cost rows)
                 "Total cost" (api/total-cost rows)
                 "Net profit" (api/net-profit rows)}
       ]
    (home amounts)))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" {params :params} (parse-pdf params)))
