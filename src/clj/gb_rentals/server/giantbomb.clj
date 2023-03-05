(ns gb-rentals.server.giantbomb
  (:require [clj-http.client :as http]
            [clojure.data.json :as json]
            [camel-snake-kebab.core :as csk]
            [gb-rentals.config.env :as env]))

(def base-url "https://www.giantbomb.com/api/")

(def fields [:apiDetailUrl :name :deck :siteDetailUrl :resourceType :imageTags :image :guid])

(defn search-params [{:keys [query resources]}]
  {:api_key   (env/get-env :api-key)
   :query     query
   :resources resources
   :format    "json"})

(defn prune-details [results]
  (if (seq results)
    (map #(select-keys % fields) results)
    []))

(defn search [params]
  (-> (http/get (str base-url "search/")
                {:query-params (search-params params)
                 :accept :json
                 :headers {"Content-Type" "application/json"
                           "User-Agent" "my backend app"}})
      :body
      (json/read-str :key-fn csk/->camelCaseKeyword)
      (update :results prune-details)))