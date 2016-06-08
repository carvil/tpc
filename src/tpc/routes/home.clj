(ns tpc.routes.home
  (:require [compojure.core :refer :all]
            [hiccup.form :refer :all]
            [tpc.views.layout :as layout]
            [tpc.api :as api]))


(defn home [& [message]]
  (layout/common
    [:div {:class "container"}
       [:div {:class "columns"}
         [:div {:class "column col-4"} ""]
         [:div {:class "column col-4"}
           [:h2 "The Porto Concierge"]
           [:hr]
           (form-to {:enctype "multipart/form-data"}
             [:post "/"]
             (file-upload :file)
             (submit-button "Upload"))]
         [:div {:class "column col-4"} ""]
        ]]
    [:p message]))

(defn temp-file-path [params]
  (->
    params
    (:file)
    (:tempfile)
    (.getPath)))


(defn parse-pdf [params]
  (let [file-path (temp-file-path params)
        rows (api/find-rows file-path)
        amounts {:tpc-cost (api/tpc-cost rows)
                 :booking-cost (api/booking-cost rows)
                 :total-cost (api/total-cost rows)
                 :profit (api/net-profit rows)}
       ]
    (home (str amounts))))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" {params :params} (parse-pdf params)))
