(defproject gb-rentals "0.1.0"
  :description "Backend API used to interact with the GiantBomb API."
  :min-lein-version "2.0.0"
  :dependencies [[aero "1.1.6"]
                 [camel-snake-kebab "0.4.3"]
                 [clj-http "3.12.3"]
                 [compojure "1.7.0"]
                 [org.clojure/clojure "1.10.0"]
                 [org.clojure/data.json "2.4.0"]
                 [ring-cors "0.1.13"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-jetty-adapter "1.9.5"]
                 [ring/ring-json "0.5.1"]]
  :plugins [[lein-ring "0.12.5"]]
  :source-paths ["src/clj"]
  :main gb-rentals.main
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
