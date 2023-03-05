(ns gb-rentals.server.core
  (:require [camel-snake-kebab.core :as csk]
            [gb-rentals.server.routes :as routes]
            [ring.middleware.cors :refer [wrap-cors]]
            [ring.middleware.defaults :refer [wrap-defaults api-defaults]]
            [ring.middleware.json :refer [wrap-json-body wrap-json-response]]))

(def app
  (-> (wrap-json-body routes/app-routes {:keywords? true})
      (wrap-json-response {:key-fn csk/->camelCaseString})
      (wrap-defaults api-defaults)
      (wrap-cors :access-control-allow-origin [#".*"]
                 :access-control-allow-methods [:get])))
