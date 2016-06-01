(ns tpc.routes.home
  (:require [compojure.core :refer :all]
            [tpc.views.layout :as layout]
            [hiccup.form :refer :all]
            [pdfboxing.text :as text]))


(defn home [& [message]]
  (layout/common
    [:h1 "TPC"]
    [:p "Import the TPC bill here"]
    [:p message]
    [:hr]
    (form-to {:enctype "multipart/form-data"}
             [:post "/"]
             (file-upload :file)
             (submit-button "Upload"))))


(defn handle-upload [params]
  (let [file-data (:file params)
        tempfile (:tempfile file-data)
        file-path (.getPath tempfile)
        txt (text/extract file-path)]
    (home txt)))

(defroutes home-routes
  (GET "/" [] (home))
  (POST "/" {params :params} (handle-upload params)))
