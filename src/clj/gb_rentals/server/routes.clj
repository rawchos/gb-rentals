(ns gb-rentals.server.routes
  (:require [camel-snake-kebab.core :as csk]
            [camel-snake-kebab.extras :refer [transform-keys]]
            [compojure.core :refer [context defroutes GET]]
            [compojure.route :as route]
            [gb-rentals.server.handler :as handler]))

(defroutes app-routes
  (context "/api" []
    (GET "/games/search" {:keys [query-params]}
      (-> (transform-keys csk/->kebab-case-keyword query-params)
          (handler/search-games))))
  (route/not-found "Not Found"))
