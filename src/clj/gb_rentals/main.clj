(ns gb-rentals.main
  (:require [gb-rentals.server.core :as server]
            [ring.adapter.jetty :as jetty]))

(defn start-server [app]
  (jetty/run-jetty app {:port 3000}))

(defn -main []
  (start-server server/app))
