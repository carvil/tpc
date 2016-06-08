(defproject tpc "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [compojure "1.1.6"]
                 [hiccup "1.0.5"]
                 [ring-server "0.3.1"]
                 [org.apache.pdfbox/pdfbox "1.8.10"]
                 [technology.tabula/tabula "0.9.0"]]
  :plugins [[lein-ring "0.8.12"]]
  :ring {:handler tpc.handler/app
         :init tpc.handler/init
         :destroy tpc.handler/destroy}
  :min-lein-version "2.6.1"
  :profiles
  {:uberjar {:aot :all}
   :production
   {:ring
    {:open-browser? false, :stacktraces? false, :auto-reload? false}}
   :dev
   {:dependencies [[ring-mock "0.1.5"] [ring/ring-devel "1.3.1"]]}})
